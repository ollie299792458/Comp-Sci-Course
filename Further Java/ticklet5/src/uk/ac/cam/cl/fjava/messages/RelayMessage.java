package uk.ac.cam.cl.fjava.messages;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class RelayMessage extends Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private String from;
  private String message;

  public RelayMessage(
      String from, String message, Date time, String uid, Map<String, Integer> vectorClock) {
    super(time, uid, vectorClock);
    this.from = from;
    this.message = message;
  }

  public RelayMessage(String from, String message, Date time) {
    this(from, message, time, null, null);
  }

  public String getFrom() {
    return from;
  }

  public String getMessage() {
    return message;
  }
}
