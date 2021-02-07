package net.metzweb.tinyserver.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON response.
 * Dependency: http://code.google.com/p/json-simple/
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 20.06.2013
 * @version 1.3
 * license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class JsonResponse extends ResponseFormat<Object> {

  private boolean wrapResponse;
  private ObjectMapper objectMapper;

  public JsonResponse(boolean wrapResponse) {
    this();
    this.wrapResponse = wrapResponse;
  }

  /**
   * Custom constructor that sets MIME type.
   */
  public JsonResponse() {
    super("application/json");
    this.wrapResponse = true;
    this.objectMapper = new ObjectMapper();
  }

  /**
   * 200 OK.
   * 
   * @param data List holding JSON entries.
   */
  public void success(List data) {
    writeJson(StatusCode.SUCCESS, "OK", data);
  }

  /**
   * 200 OK.
   * 
   * @param data Map holding JSON key - value pairs.
   */
  @Override
  public void success(Object data) {
    writeJson(StatusCode.SUCCESS, "OK", data);
  }

  /**
   * 403 forbidden.
   */
  @Override
  public void forbidden() {
    forbidden("Forbidden");
  }

  @Override
  public void forbidden(String message) {
    writeJson(StatusCode.FORBIDDEN, message);
  }

  /**
   * 404 not found.
   */
  @Override
  public void notFound() {
    notFound("Not Found");
  }

  @Override
  public void notFound(String message) {
    writeJson(StatusCode.NOT_FOUND, message);
  }

  /**
   * 500 internal server error.
   */
  @Override
  public void error() {
    error("Internal Server Error");
  }

  @Override
  public void error(String message) {
    writeJson(StatusCode.ERROR, message);
  }

  /**
   * Wrapper method for responses without data.
   * 
   * @param code    The request status code.
   * @param message The request message.
   */
  private void writeJson(StatusCode code, String message) {
    writeJson(code, message, null);
  }

  /**
   * Convert Map to write JSON
   * 
   * @param code    The request status code.
   * @param message The request message.
   * @param data    The request data map (key => value).
   */
  private void writeJson(StatusCode code, String message, Object data) {

    if(isWrapResponse()){
      Map json = new LinkedHashMap();
      json.put("status", Integer.toString(code.getCode()));
      json.put("message", message);

      if (data != null) {
        json.put("data", data);
      }
      data = json;
    }
    try {
      write(code.getHeader(), data != null ? objectMapper.writeValueAsString(data) : null);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean isWrapResponse() {
    return wrapResponse;
  }
}
