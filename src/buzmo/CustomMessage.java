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
 * @author jacob
 */
public class CustomMessage extends MyCircleMessage{
	private ArrayList<User> recipients;

  CustomMessage(int m_id, String timestamp, String sender, String body, ArrayList<TopicWord> topics, ArrayList<User> recipients){
    super(m_id, timestamp, sender, body, topics);
    this.recipients = recipients;
  }
}
