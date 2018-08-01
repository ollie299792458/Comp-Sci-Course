package uk.ac.cam.cl.fjava.messages;

import java.io.Serializable;
import java.util.Map;

public class ChangeNickMessage extends Message implements Serializable {
  private static final long serialVersionUID = 1L;

  public String name;

  public ChangeNickMessage(String name, String uid, Map<String, Integer> vectorClock) {
    super(uid, vectorClock);
    this.name = name;
  }

  public ChangeNickMessage(String name) {
    this(name, null, null);
  }
}
