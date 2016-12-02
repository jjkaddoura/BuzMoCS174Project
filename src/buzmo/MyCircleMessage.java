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

    this.receipients = new ArrayList<String>();
    isPublic = false;
  }
  public MyCircleMessage(String timestamp, String sender, String body, List<String> topics){
    super(timestamp, sender, body);
    this.topics = new ArrayList<String>();
    this.topics.addAll(topics);

    this.receipients = new ArrayList<String>();
    isPublic = false;
  }
   @Override
   public String toString(){
    String o = isPublic ? "BuzMo Public Feed\n" : "MyCircle\n";
    o = sender + ":  " + body + "\n Time: " + timestamp + "\n";
    for(String topic : topics){
      o += "Topics:  " + topic + ", ";
    }
    if(!topics.isEmpty()) o = o.subsstring(0,o.length()-2);
    
         
    return o;

   }
}
