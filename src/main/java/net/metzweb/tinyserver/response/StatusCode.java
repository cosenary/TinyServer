package net.metzweb.tinyserver.response;

/**
 * HTTP status codes.
 */
public enum StatusCode {

  SUCCESS(200, "OK"),
  BAD_REQUEST(400, "Bad Request"),
  FORBIDDEN(403, "Forbidden"),
  NOT_FOUND(404, "Not Found"),
  ERROR(500, "Internal Server Error");

  private int code;
  private final String message;

  StatusCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getHeader() {
    return String.format("%s %s", this.code, this.message);
  }

  public String getDesc() {
    switch (this) {
      case FORBIDDEN:
        return "You don't have enough rights to access this resource.";
      case NOT_FOUND:
        return "The requested resource wasn't found.";
      case ERROR:
        return "An error occured.";
      default:
      case SUCCESS:
        return "Success";
    }
  }
}
