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
		String o =  m_id  + ", " + timestamp + ", " + sender + ", " + body;
		for(String topic : topics){
			o += ", topics: " + topic + ", ";
		}
		for(String rec : recipients){
			o += "recipients: " + rec + ", ";
		}
			 
		return o;
	} 
}
