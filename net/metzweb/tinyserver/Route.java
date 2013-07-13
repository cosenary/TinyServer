package net.metzweb.tinyserver;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a route.
 * Holds the route path and its callback.
 * 
 * @package TinyServer
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 16.06.2013
 * @version 1.0
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class Route {

  private final String route;
  private final String routeRegex;
  private final Pattern routePattern;
  private final Response callback;

  /**
   * Precompile route param pattern.
   */
  private static final Pattern pattern = Pattern.compile("\\[(.*?)\\]");

  /**
   * Holds route parameters.
   */
  private LinkedList<String> params = new LinkedList<>();

  /**
   * Custom constructor.
   * "\\:\\w+"
   * 
   * @param route     The route path.
   * @param callback  The callback object.
   */
  public Route(String route, Response callback) {
    // make sure route starts with a backslash
    if (!route.startsWith("/")) {
      route = route + "/";
    }
    
    this.route = route;
    this.callback = callback;
    
    // replace route param placeholder by regex.
    this.routeRegex = route.replaceAll("\\[(.*?)\\]", "([^\\/]+)").replaceAll("\\*\\w+", "(.*?)");
    this.routePattern = Pattern.compile(routeRegex);
    matchParams();
  }

  /**
   * Get route parameter.
   * A route param is defined between square brackets.
   * 
   * @param index Position of the param in the route path.
   * @return      The param name or null.
   */
  public String getParam(int index) {
    if (params.size() < index) {
      return null;
    }
    return params.get(index);
  }

  /**
   * Get route path.
   * 
   * @return 
   */
  public String getPath() {
    return route;
  }

  /**
   * Get route path with params replaced by regex.
   * 
   * @return 
   */
  public String getRegex() {
    return routeRegex;
  }

  /**
   * Get precompiled regex pattern.
   * 
   * @return 
   */
  public Pattern getPattern() {
    return routePattern;
  }

  /**
   * Get the callack object.
   * 
   * @return 
   */
  public Response getCallback() {
    return callback;
  }

  /**
   * Indicates whether the route path contains parameters.
   * 
   * @return 
   */
  public boolean isParamRoute() {
    return (params != null);
  }

  /**
   * Indicates whether a param matches the route.
   */
  private void matchParams() {
    Matcher matcher = pattern.matcher(route);
    while (matcher.find()) {
      for (int i=0; i < matcher.groupCount(); i++) {
        params.add(matcher.group(i+1));
      }
    }
  }

}
