package uk.ac.cam.cl.fjava.messages;

import java.io.Serializable;
import java.util.Map;

/** Message sent from the client to the server. */
public class ChatMessage extends Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private String message;

  public ChatMessage(String message, String uid, Map<String, Integer> vectorClock) {
    super(uid, vectorClock);
    this.message = message;
  }

  public ChatMessage(String message) {
    this(message, null, null);
  }

  public String getMessage() {
    return message;
  }
}
