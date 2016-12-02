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
public class BroadcastMessage extends MyCircleMessage{
	private int read_count;
	private boolean isPublic;

	BroadcastMessage(int m_id, String timestamp, String sender, String body, List<String> topics, boolean isPublic){
		super(m_id, timestamp, sender, body, topics);
		read_count = 0;
		this.isPublic = isPublic;
	}
	BroadcastMessage(String timestamp, String sender, String body, List<String> topics, boolean isPublic){
		super(timestamp, sender, body, topics);
		read_count = 0;
		this.isPublic = isPublic;
	}
	
	public int getReadCount(){ return this.read_count; }

	@Override
		public String toString(){
			String o = sender + ":  " + body + "\n Time: " + timestamp + "\n";
		for(String topic : topics){
			o += "Topics:  " + topic + ", ";
		}
		if(!topics.isEmpty()) o = o.subsstring(0,o.length()-2);
		o += isPublic ? "BuzMo public feed" : "MyCircle";
		
				 
		return o;
	}
		
}
