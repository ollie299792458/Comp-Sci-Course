package uk.ac.cam.cl.fjava.messages;

import java.io.Serializable;
import java.util.Map;

public class NewMessageType extends Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private String name;
  private byte[] classData;

  public NewMessageType(
      String name, byte[] classData, String uid, Map<String, Integer> vectorClock) {
    super(uid, vectorClock);
    this.name = name;
    this.classData = classData;
  }

  public NewMessageType(String name, byte[] classData) {
    this(name,classData,null,null);
  }

  public String getName() {
    return name;
  }

  public byte[] getClassData() {
    return classData;
  }
}
