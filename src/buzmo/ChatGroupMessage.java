/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;

import java.lang.*;
import java.util.*;
/**
 *
 * @author John Mangel
 */
public class ChatGroupMessage extends Message{
	private ChatGroup chatGroup;

	ChatGroupMessage(int m_id, String timestamp, String sender, String body, ChatGroup chatGroup){
    super(m_id, timestamp, sender, body);
    this.chatGroup = chatGroup;
  }
  ChatGroupMessage(String timestamp, String sender, String body, ChatGroup chatGroup){
    super(timestamp, sender, body);
    this.chatGroup = chatGroup;
  }
}
