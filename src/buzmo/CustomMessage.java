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
	private ArrayList<String> recipients;

	CustomMessage(int m_id, String timestamp, String sender, String body, ArrayList<String> topics, ArrayList<String> recipients){
		super(m_id, timestamp, sender, body, topics);
		this.recipients = recipients;
	}
	CustomMessage(String timestamp, String sender, String body, ArrayList<String> topics, ArrayList<String> recipients){
		super(timestamp, sender, body, topics);
		this.recipients = recipients;
	}
	
	@Override
	public String toString(){
			String o = " 		" +sender + ":  " + body + "\n 		Time: " + timestamp + "\n 		";
		for(String topic : topics){
			o += "Topics:  " + topic + ", ";

		}
		if(!topics.isEmpty()) o = o.substring(0,o.length()-2);
		o += "\n 		";
		for(String rec : recipients){
			o += "recipients: " + rec + ", ";
		}
		if(!recipients.isEmpty()) o = o.substring(0,o.length()-2);
		return o;
	} 
}
