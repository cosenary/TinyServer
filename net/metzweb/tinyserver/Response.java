package net.metzweb.tinyserver;

/**
 * Response callback.
 * Has to be implemented by every callback class.
 *
 * @package TinyServer
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 15.06.2013
 * @version 0.1
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public interface Response {

  /**
   * Callback method.
   * 
   * @param request Request object, containing route and request params.
   */
  public void callback(Request request);

}
