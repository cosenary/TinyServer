package net.metzweb.tinyserver.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * File response.
 *
 * @author Christian Metz | christian@metzweb.net
 * @since 13.07.2013
 * @version 1.3
 * license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public class FileResponse extends ResponseFormat<String> {

  /**
   * Mapping file extension -> MIME type
   */
  private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>() {{
    put("css", "text/css");
    put("html", "text/html");
    put("txt", "text/plain");
    put("gif", "image/gif");
    put("jpg", "image/jpeg");
    put("jpeg", "image/jpeg");
    put("png", "image/png");
    put("mp3", "audio/mpeg");
    put("mp4", "video/mp4");
    put("flv", "video/x-flv");
    put("mov", "video/quicktime");
    put("swf", "application/x-shockwave-flash");
    put("js", "application/javascript");
    put("pdf", "application/pdf");
    put("doc", "application/msword");
    put("zip", "application/octet-stream");
    put("binary", "application/octet-stream");
  }};

  /**
   * Custom constructor.
   * Defines no MIME type, since it depends on the file extension.
   */
  public FileResponse() {
    super(null);
  }

  /**
   * 200 OK.
   * 
   * @param filePath File path, with proper file extension.
   */
  @Override
  public void success(String filePath) {
    String extension = "binary";
    int dot = filePath.lastIndexOf(".");
    if (dot > 0) {
      extension = filePath.substring(dot + 1);
    }
    
    setMimeType(MIME_TYPES.get(extension));
    write(StatusCode.SUCCESS.getHeader(), filePath);
  }

  /**
   * Write file content to the open socket.
   * 
   * @param code The response STATUS_CODE.
   * @param path The file path.
   */
  @Override
  protected void write(String code, String path) {
    File file = new File(path);
    if (file.exists() && file.canRead()) {
      try (FileInputStream fileInput = new FileInputStream(file);
           DataOutputStream out = new DataOutputStream(output)) {
        
        // write header with status code
        writeHeader(code);
        
        int length = (int) file.length();
        byte[] array = new byte[length];
        int offset = 0;
        while (offset < length) {
          int count = fileInput.read(array, offset, (length - offset));
          offset += count;
        }
        
        out.write(array);
        out.flush();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      System.err.println("Couldn't load file, since it's damaged.");
    }
  }

}
