package uk.ac.cam.cl.fjava.messages;

import java.io.Serializable;
import java.util.Map;

public class StatusMessage extends Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private String message;

  public StatusMessage(String message, String uid, Map<String, Integer> vectorClock) {
    super(uid, vectorClock);
    this.message = message;
  }

  public StatusMessage(String message) {
    this(message, null, null);
  }

  public String getMessage() {
    return message;
  }
}
