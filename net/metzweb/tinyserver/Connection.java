package net.metzweb.tinyserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Server connection.
 * Handels incoming connections.
 * 
 * @package TinyServer
 * 
 * @author Christian Metz | christian@metzweb.net
 * @since 15.10.2012
 * @version 1.1
 * @license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class Connection extends Thread {

  private final Socket socket;
  private final TinyServer server;

  /**
   * Custom constructor.
   * 
   * @param socket Connection socket.
   * @param server TinyServer instance.
   */
  public Connection(Socket socket, TinyServer server) {
    this.socket = socket;
    this.server = server;
  }

  /**
   * Server multithreading.
   */
  @Override
  public void run() {
    try {
      // create reader and writer
      OutputStream output = socket.getOutputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
      // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
      
      // read first header line
      String line = reader.readLine();
      
      // check request type (GET/POST)
      // example: [GET, /hello/world.json, HTTP/1.1]
      String[] requestHeader = line.split("(\\s{1,})");
      String requestType = requestHeader[0];
      
      Request request = new Request(requestHeader, server, output);
      
      // check request type: GET / POST
      if (requestType.equals("GET")) {
        request.parseGET();
      } else if (requestType.equals("POST")) {
        int counter = 0;
        int contentLength = 0;
        for (line = reader.readLine(); line != null; line = reader.readLine()) {
          if (line.isEmpty()) {
            counter++;
          }
          if (counter > 0) {
            break;
          }
          if (line.startsWith("Content-Length:")) {
            contentLength = Integer.parseInt(line.replace("Content-Length: ", ""));
          }
        }
        
        StringBuilder requestContent = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
          requestContent.append((char) reader.read());
        }
        
        request.parsePOST(requestContent.toString());
      } else {
        System.err.println("Invalid request type.");
      }
      
    } catch (IOException ex) {
      System.out.println("I/O Exception while serving client.");
    } finally {
      try {
        socket.close();
      } catch (IOException ex) {
        System.err.println("I/O Exception while closing socket.");
      }
    }
  }

}
