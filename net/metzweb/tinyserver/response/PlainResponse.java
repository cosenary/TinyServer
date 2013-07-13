package net.metzweb.tinyserver.response;

/**
 * Plain text response.
 * Default response format.
 * 
 * @package TinyServer
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 15.06.2013
 * @version 1.0
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class PlainResponse extends ResponseFormat<String> {

  /**
   * Custom constructor that sets MIME type.
   */
  public PlainResponse() {
    super("text/plain");
  }

  /**
   * Test success message.
   */
  public void success() {
    success("Hello world. I'm your new Java server.");
  }

  /**
   * 200 OK.
   * 
   * @param data Text string.
   */
  @Override
  public void success(String data) {
    write(STATUS_CODE.SUCCESS.getHeader(), data);
  }
  
}
