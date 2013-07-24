package net.metzweb.tinyserver.response;

/**
 * HTML response.
 * 
 * @package TinyServer
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 20.06.2013
 * @version 1.2
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class HtmlResponse extends ResponseFormat<String> {

  /**
   * Basic HTML wrapper.
   */
  private final String HTML_WRAPPER = "<!DOCTYPE html><html><head><title>%s</title></head><body><h2>%s</h2><br><h3>%s</h3></body></html>";

  /**
   * Custom constructor that sets MIME type.
   */
  public HtmlResponse() {
    super("text/html");
  }

  /**
   * Test success message.
   */
  public void success() {
    String data = String.format(HTML_WRAPPER, "Hello world", "Hello world :)", "I'm your new Java server.");
    success(data);
  }

  /**
   * 200 OK.
   * 
   * @param data HTML string.
   */
  @Override
  public void success(String data) {
    write(STATUS_CODE.SUCCESS.getHeader(), data);
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
    String data = String.format(HTML_WRAPPER, "Forbidden", message, STATUS_CODE.FORBIDDEN.getDesc());
    write(STATUS_CODE.FORBIDDEN.getHeader(), data);
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
    String data = String.format(HTML_WRAPPER, "Not Found", message, STATUS_CODE.NOT_FOUND.getDesc());
    write(STATUS_CODE.NOT_FOUND.getHeader(), data);
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
    String data = String.format(HTML_WRAPPER, "Internal Server Error", message, STATUS_CODE.ERROR.getDesc());
    write(STATUS_CODE.ERROR.getHeader(), data);
  }

}
