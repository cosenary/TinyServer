package example;
import net.metzweb.tinyserver.Request;
import net.metzweb.tinyserver.Response;
import net.metzweb.tinyserver.TinyServer;
import net.metzweb.tinyserver.response.JsonResponse;

import java.util.HashMap;

/**
 * TinyServer JSON test class.
 *
 * @since 15.06.2013
 * @author Christian Metz | christian@metzweb.net
 * @version 1.0
 * license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class JsonTest {

  public static void main(String[] args) {
    // init server at port 8200
    TinyServer server = new TinyServer(8200);

    // setup a GET route
    server.get("/weather/[location]", new MyCallback());
    
    // set JSON response class
    server.setResponseFormat(new JsonResponse(false));
    
    // start server
    server.start();
  }

}

/**
 * Custom callback class.
 * Implements the callback interface Response and its callback method.
 *
 * @since 18.06.2013
 * @author Christian Metz | christian@metzweb.net
 * @version 1.0
 */
class MyCallback implements Response {

  /**
   * Callback method.
   * > http://127.0.0.1:8200/weather/Munich
   *
   * @param request Request details.
   */
  @Override
  public void callback(Request request) {
    HashMap<String, String> map = new HashMap<>();
    map.put("location", request.param("location"));
    map.put("condition", "fair");
    map.put("temprature", "32 °C");
    request.write(map);
  }

}
