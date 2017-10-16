package kzy.com.gyyengineer.leanchat.event;


import kzy.com.gyyengineer.leanchat.model.LCIMRedPacketAckMessage;

/**
 * Created by wli on 16/7/14.
 */
public class RedPacketAckEvent {
  public LCIMRedPacketAckMessage ackMessage;

  public RedPacketAckEvent(LCIMRedPacketAckMessage ackMessage) {
    this.ackMessage = ackMessage;
  }
}
