package net.metzweb.tinyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.regex.Pattern;
import net.metzweb.tinyserver.response.ResponseFormat;

/**
 * TinyServer application logic.
 * Manages connections, routes and its callacks.
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 15.06.2013
 * @version 1.1
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class TinyServer {

  private final int port;
  private LinkedList<Connection> connections = new LinkedList<>();

  // collected GET and POST routes
  private LinkedList<Route> getRoutes = new LinkedList<>();
  private LinkedList<Route> postRoutes = new LinkedList<>();

  /**
   * Route specific response format.
   */
  private ResponseFormat responseFormat = null;

  /**
   * Custom constructor.
   */
  public TinyServer() {
    this(8000);
  }

  /**
   * Constructor with custom port.
   * 
   * @param port  The server port.
   */
  public TinyServer(int port) {
    this.port = port;
  }

  /**
   * Register a new GET route.
   * 
   * @param  String   Route path (has to start with a backslash).
   * @param  Response Callback object.
   * @return Route    Route object.
   */
  public Route get(String route, Response callback) {
    Route routeObj = new Route(route, callback);
    getRoutes.add(routeObj);
    return routeObj;
  }

  /**
   * Register a new POST route.
   * 
   * @param  String   Route path (has to start with a backslash).
   * @param  Response Callback object.
   * @return Route    Route object.
   */
  public Route post(String route, Response callback) {
    Route routeObj = new Route(route, callback);
    postRoutes.add(routeObj);
    return routeObj;
  }

  /**
   * Start server and listen for connections.
   */
  public void start() {
    // open Server stream
    try (ServerSocket serverSocket = new ServerSocket(port)) {      
      System.out.println("Server started: http://127.0.0.1:" + port);
      
      // listen for connections
      while (true) {
        // accept connection and receive socket
        try (Socket socket = serverSocket.accept()) {
          System.out.println("Client connected: " + socket);
          // establish connection
          Connection connection = new Connection(socket, this);
          // start connection thread
          connection.start();
          // store connection
          connections.add(connection);
        } catch (IOException e) {
          System.err.println("Request couldn't be accepted.");
        }
      }
    } catch (IOException e) {
      System.err.println("Port already in use: " + port);
    }
  }

  /**
   * Server port Getter.
   * 
   * @return The port.
   */
  public int getPort() {
    return port;
  }

  /**
   * Check whether a route path is registered.
   * 
   * @param String  The route path, starting with a backslash.
   * @return        Wherher route is registered.
   */
  public boolean isRouteRegistered(String route, boolean isPostRoute) {
    LinkedList<Route> routes = (isPostRoute) ? postRoutes : getRoutes;
    for (Route entry : routes) {
      if (Pattern.matches(entry.getRegex(), route)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check whether a route path is registered
   * 
   * @param String  The route path.
   * @return        Response callback object, implementing the Response interface.
   */
  public Route getRoute(String route, boolean isPostRoute) {
    LinkedList<Route> routes = (isPostRoute) ? postRoutes : getRoutes;
    for (Route entry : routes) {
      if (Pattern.matches(entry.getRegex(), route)) {
        return entry;
      }
    }
    return null;
  }

  /**
   * GET route Getter.
   * 
   * @return Map that holds registered routes and their callbacks.
   */
  public LinkedList<Route> getRoutes() {
    return getRoutes;
  }

  /**
   * Response format Setter.
   * 
   * @param format Response format object e.g. HtmlResponse
   */
  public void setResponseFormat(ResponseFormat format) {
    responseFormat = format;
  }

  /**
   * Response format Getter.
   * 
   * @return Response format object e.g. HtmlResponse
   */
  public ResponseFormat getResponseFormat() {
    return responseFormat;
  }

}
