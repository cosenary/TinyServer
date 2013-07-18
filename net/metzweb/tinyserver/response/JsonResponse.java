package net.metzweb.tinyserver.response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONValue;

/**
 * JSON response.
 * Dependency: http://code.google.com/p/json-simple/
 * 
 * @package TinyServer
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 20.06.2013
 * @version 1.1
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class JsonResponse extends ResponseFormat<Map> {

  /**
   * Custom constructor that sets MIME type.
   */
  public JsonResponse() {
    super("application/json");
  }

  /**
   * 200 OK.
   * 
   * @param data List holding JSON entries.
   */
  public void success(List data) {
    writeJson(STATUS_CODE.SUCCESS, "OK", data);
  }

  /**
   * 200 OK.
   * 
   * @param data Map holding JSON key => value pairs.
   */
  @Override
  public void success(Map data) {
    writeJson(STATUS_CODE.SUCCESS, "OK", data);
  }

  /**
   * 403 forbidden.
   */
  @Override
  public void forbidden() {
    writeJson(STATUS_CODE.FORBIDDEN, "Forbidden");
  }

  /**
   * 404 not found.
   */
  @Override
  public void notFound() {
    writeJson(STATUS_CODE.NOT_FOUND, "Not Found");
  }

  /**
   * 500 internal server error.
   */
  @Override
  public void error() {
    writeJson(STATUS_CODE.ERROR, "Internal Server Error");
  }

  /**
   * Wrapper method for responses without data.
   * 
   * @param code    The request status code.
   * @param message The request message.
   */
  private void writeJson(STATUS_CODE code, String message) {
    writeJson(code, message, null);
  }

  /**
   * Convert Map to write JSON
   * 
   * @param code    The request status code.
   * @param message The request message.
   * @param data    The request data map (key => value).
   */
  private void writeJson(STATUS_CODE code, String message, Object data) {
    Map json = new LinkedHashMap();
    json.put("status", Integer.toString(code.getCode()));
    json.put("message", message);
    
    if (data != null) {
      json.put("data", data);
    }
    
    String jsonData = JSONValue.toJSONString(json);
    write(code.getHeader(), jsonData);
  }

}
