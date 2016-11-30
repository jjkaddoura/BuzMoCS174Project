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
	private ArrayList<TopicWord> topics;

	public MyCircleMessage(int m_id, String timestamp, String sender, String body, List<TopicWord> topics){
    super(m_id, timestamp, sender, body);
    this.topics = new ArrayList<TopicWord>();
    this.topics.addAll(topics);
  }
}