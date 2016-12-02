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
	private String gname;

	ChatGroupMessage(int m_id, String timestamp, String sender, String body, String gname){
    super(m_id, timestamp, sender, body);
    this.gname = gname;
  }
  ChatGroupMessage(String timestamp, String sender, String body, String gname){
    super(timestamp, sender, body);
    this.gname = gname;
  }

  @Override
	public String toString(){
		String o = sender + ": " + body + "\n Time: " + timestamp;
		return o;
	}
}
