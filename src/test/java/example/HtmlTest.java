package example;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;
import net.metzweb.tinyserver.response.HtmlResponse;

/**
 * TinyServer HTML test class.
 *
 * @since 15.06.2013
 * @author Christian Metz | christian@metzweb.net
 * @version 1.0
 * license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class HtmlTest {

  public static void main(String[] args) {
    // init server at port 8200
    TinyServer server = new TinyServer(8200);

    // setup a GET route
    server.get("/hello/[name]", new Response() {
      @Override
      public void callback(Request request) {
        String htmlString = "<!DOCTYPE html>"
          + "<html>"
          + "  <head><title>31 October</title></head>"
          + "  <body>"
          + "    <h2>Howdy " + request.param("name") + "! Do you like kittens?</h2><br>"
          + "    <img src=\"http://placekitten.com/g/404/280\">"
          + "  </body>"
          + "</html>";
        request.write(htmlString);
      }
    });

    // set HTML response class
    server.setResponseFormat(new HtmlResponse());

    // start server
    server.start();
  }

}
