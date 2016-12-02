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
public class MyCircleMessage extends Message{
	protected ArrayList<String> topics;
  protected ArrayList<String> recipients;
  protected boolean isPublic;

	public MyCircleMessage(int m_id, String timestamp, String sender, String body, List<String> topics){
    super(m_id, timestamp, sender, body);
    this.topics = new ArrayList<String>();
    this.topics.addAll(topics);

    this.recipients = new ArrayList<String>();
    isPublic = false;
  }
  public MyCircleMessage(String timestamp, String sender, String body, List<String> topics){
    super(timestamp, sender, body);
    this.topics = new ArrayList<String>();
    this.topics.addAll(topics);

    this.recipients = new ArrayList<String>();
    isPublic = false;
  }
   
   
}
