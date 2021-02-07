package net.metzweb.tinyserver;

import net.metzweb.tinyserver.response.PlainResponse;
import net.metzweb.tinyserver.response.ResponseFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;

/**
 * Request handler.
 * Parses incoming requests.
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 18.05.2013
 * @version 1.3
 * license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class Request {

  private final String[] request;
  private final OutputStream outputStream;
  private final TinyServer server;

  /**
   * Holds GET request parameters.
   */
  private final HashMap<String, String> params = new HashMap<>();
  private final LinkedList<String> wildcardParams = new LinkedList<>();

  /**
   * Response format class.
   * e.g. HtmlResponse
   */
  private ResponseFormat response;

  /**
   * POST data.
   */
  private String requestData;

  /**
   * Custom constructor.
   * 
   * @param request The request header.
   * @param server  The server.
   * @param outputStream  The connection writer.
   */
  public Request(String[] request, TinyServer server, OutputStream outputStream) {
    this.request = request;
    this.server = server;
    this.outputStream = outputStream;
    processResponseFormat(null);
  }

  /**
   * Parse GET request.
   */
  protected void parseGET() {    
    String requestURL = request[1];
    
    // are parameters attatched?
    // example: /hello.json?name=world&foo=bar
    if (requestURL.indexOf("?") > 0) {
      // [0] => "/hello.json", [1] => "name=world&foo=bar"
      String[] requestParams = requestURL.split("\\?");
      
      // set parameter cleaned request URL
      requestURL = requestParams[0];
      
      // get params pairs as an array
      String[] paramPairs = requestParams[1].split("\\&");
      
      for (String parameter : paramPairs) {
        // [0] => key, [1] => value
        String[] keyValue = parameter.split("\\=");
        
        // URL decode parameter key and value
        try {
          String decodedKey = URLDecoder.decode(keyValue[0], "UTF-8");
          String decodedValue = URLDecoder.decode(keyValue[1], "UTF-8");
          
          if (!params.containsKey(decodedKey)) {
            addParam(decodedKey, decodedValue);
          } else {
            // error: parameter already exists
            System.out.println("Couldn't add parameter '" + decodedKey + "', since it already exists.");
          }
        } catch (Exception e) {
          System.out.println("Couldn't decode request params: " + e.getMessage());
        }
      }
    }
    
    triggerRoute(requestURL, false);
  }

  /**
   * Parse POST request.
   * 
   * @param data Sent POST data.
   */
  protected void parsePOST(String data) {
    requestData = data;
    triggerRoute(request[1], true);
  }

  /**
   * Returns POST data.
   * 
   * @return The POST data.
   */
  public String getData() {
    return requestData;
  }

  /**
   * Get all parameter values.
   * Used to receive all wildcard values.
   * 
   * @return All wildcard `*` parameters.
   */
  public LinkedList<String> params() {
    return wildcardParams;
  }

  /**
   * Get a request parameter by its key.
   * 
   * @param  key Parameter key (URL syntax: ?key=value)
   * @return The parameter's value or null if it doesn't exist.
   */
  public String param(String key) {
    // check whether requested key exists
    if ((key != null && key.length() > 0) && params.containsKey(key)) {
      return params.get(key);
    }
    return null;
  }

  /**
   * Alias for request success method.
   * 
   * @param data The data to write. 
   */
  public void write(Object data) {
    response.success(data);
  }

  /**
   * Get response methods.
   * 
   * @return Format response object.
   */
  public ResponseFormat write() {
    return response;
  }

  /**
   * Add a parameter.
   * 
   * @param key   Parameter key (URL syntax: ?key=value)
   * @param value Parameter value.
   * @return      The parameter's value or null if it doesn't exist.
   */
  private void addParam(String key, String value) {
    // check whether key already exists
    if ((key != null && key.length() > 0) && !params.containsKey(key)) {
      params.put(key, value);
    } else {
      System.err.println("Invalid or dublicate parameter: " + key);
    }
  }

  /**
   * Trigger route.
   * 
   * @param requestURL  The requested URL.
   * @param isPostRoute Whether it's a POST route.
   */
  private void triggerRoute(String requestURL, boolean isPostRoute) {
    if (server.isRouteRegistered(requestURL, isPostRoute)) {
      Route route = server.getRoute(requestURL, isPostRoute);
      
      if (route.isParamRoute()) {
        Matcher matcher = route.getPattern().matcher(requestURL);
        
        // check if route params are available
        if (matcher.find()) {
          for (int i = 0; i < matcher.groupCount(); i++) {
            // add route parameter
            String paramKey = route.getParam(i);
            if (paramKey.startsWith("*")) {
              wildcardParams.add(matcher.group(i+1));
            } else {
              addParam(paramKey, matcher.group(i+1));
            }
          }
        }
      }
      
      // trigger callback
      processResponseFormat(route.getResponseFormat());
      route.getCallback().callback(this);
      
      try {
        outputStream.close();
      } catch (IOException ex) {
        System.err.println("I/O Exception while closing writer.");
      }
    } else {
      // route not found
      response.notFound();
    }
  }

  /**
   * Process response format.
   * 
   * @param format Response class, defined in a Route.
   */
  private void processResponseFormat(ResponseFormat format) {
    if (format != null) {
      response = format;
    } else if (server.getResponseFormat() != null) {
      response = server.getResponseFormat();
    } else {
      response = new PlainResponse();
    }  
    response.setOutputStream(outputStream);
  }

}
