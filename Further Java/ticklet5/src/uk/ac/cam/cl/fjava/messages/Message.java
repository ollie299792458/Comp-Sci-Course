package uk.ac.cam.cl.fjava.messages;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private Date creationTime;
  private String uid;
  private Map<String, Integer> vectorClock;

  public Message() {
    this(new Date());
  }

  public Message(String uid, Map<String, Integer> vectorClock) {
    this(new Date(), uid, vectorClock);
  }

  public Message(Date date) {
    this(date, null, null);
  }

  public Message(Message copy) {
    this(copy.creationTime, copy.uid, copy.vectorClock);
  }

  protected Message(Date time, String uid, Map<String, Integer> vectorClock) {
    this.creationTime = time;
    this.uid = uid;
    this.vectorClock =
        vectorClock != null ? Collections.unmodifiableMap(new HashMap<>(vectorClock)) : null;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public Map<String, Integer> getVectorClock() {
    return vectorClock;
  }

  public String getUid() {
    return uid;
  }
}
