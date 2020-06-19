package net.metzweb.tinyserver.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Response format marker interface.
 * 
 * @author Christian Metz
 * @since 20.06.2013
 * @version 1.3
 * license BSD http://www.opensource.org/licenses/bsd-license.php
 */
public abstract class ResponseFormat<T> {

  /**
   * MIME type.
   * e.g. text/html
   */
  private String mimeType;

  /**
   * String writer.
   */
  protected Writer writer;

  /**
   * Socket output stream.
   */
  protected OutputStream output;

  /**
   * Whether response header already written.
   */
  private boolean headerWritten;

  /**
   * Custom constructor.
   * 
   * @param mimeType The response MIME type.
   */
  public ResponseFormat(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * 200 OK.
   * 
   * @param data The data.
   */
  public abstract void success(T data);

  /**
   * 403 forbidden.
   */
  public void forbidden() {
    forbidden(StatusCode.FORBIDDEN.getDesc());
  }

  public void forbidden(String message) {
    write(StatusCode.FORBIDDEN.getHeader(), message);
  }

  /**
   * 404 not found.
   */
  public void notFound() {
    notFound(StatusCode.NOT_FOUND.getDesc());
  }

  public void notFound(String message) {
    write(StatusCode.NOT_FOUND.getHeader(), message);
  }

  /**
   * 500 internal server error.
   */
  public void error() {
    error(StatusCode.ERROR.getDesc());
  }

  public void error(String message) {
    write(StatusCode.ERROR.getHeader(), message);
  }

  /**
   * MIME type Setter.
   * 
   * @return MIME type.
   */
  public String getMimeType() {
    return mimeType;
  }

  /**
   * MIME type Setter.
   * 
   * @param mimeType The response MIME type.
   */
  protected void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * Set output stream and initialize its writer.
   * 
   * @param output
   */
  public void setOutputStream(OutputStream output) {
    try {
      this.output = output;
      this.writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
      this.headerWritten = false;
    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
  }

  public void write(StatusCode code, String data) {
    this.write(code.getHeader(), data);
  }

  /**
   * Write data string to the open socket.
   * 
   * @param code The response STATUS_CODE.
   * @param data The data.
   */
  protected void write(String code, String data) {
    try {
      writeHeader(code);
      if(data != null){
        writer.write(data);
      }
      writer.flush();
    } catch (IOException ex) {
      System.out.println("Response format, writer error.");
      ex.printStackTrace();
    }
  }

  /**
   * Write response header.
   * 
   * @throws     IOException
   * @param code The response STATUS_CODE.
   */
  protected void writeHeader(String code) throws IOException {
    if (!headerWritten) {
      writer.write("HTTP/1.1 " + code + "\r\n");
      writer.write("Cache-Control: private, max-age=0\r\n");
      writer.write("Content-type: " + mimeType + "; charset=utf-8\r\n");
      writer.write("Connection: keep-alive\r\n");
      writer.write("Server: TinyServer MetzWeb\r\n\n");
      writer.flush();
      headerWritten = true;
    }
  }

}
