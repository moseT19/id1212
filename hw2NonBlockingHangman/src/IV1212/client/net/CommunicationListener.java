package IV1212.client.net;

import IV1212.common.Message;

public interface CommunicationListener {

   void print(String msg);

   void sendMsg(Message msg);
}
