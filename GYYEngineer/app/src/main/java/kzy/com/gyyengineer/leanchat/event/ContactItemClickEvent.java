package kzy.com.gyyengineer.leanchat.event;

/**
 * Created by wli on 15/11/26.
 */
public class ContactItemClickEvent {

  public String memberId;

  public ContactItemClickEvent(String id) {
    memberId = id;
  }
}
