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
public class PrivateMessage extends Message {
	private boolean sender_copy_delete;
	private boolean receiver_copy_delete;
	protected String receiver;

	PrivateMessage(int m_id, String timestamp, String sender, String receiver, String body){
		super(m_id, timestamp, sender, body);
		this.receiver = receiver;
		this.sender_copy_delete = false;
		this.receiver_copy_delete = false;
	}
	PrivateMessage(int m_id, String timestamp, String sender, String receiver, String body, boolean sender_copy_delete, boolean receiver_copy_delete){
		super(m_id, timestamp, sender, body);
		this.receiver = receiver;
		this.sender_copy_delete = sender_copy_delete;
		this.receiver_copy_delete = receiver_copy_delete;
	}
	PrivateMessage(String timestamp, String sender, String receiver, String body){
		super(timestamp, sender, body);
		this.receiver = receiver;
		this.sender_copy_delete = false;
		this.receiver_copy_delete = false;
	}

	void deleteSenderCopy() { this.sender_copy_delete = true; }
	void deleteReceiverCopy() { this.receiver_copy_delete = true; }

	public boolean deletedBySender(){
		return sender_copy_delete;
	}
	public boolean deletedByReceiver(){
		return receiver_copy_delete;
	}

	@Override
	public String toString(){
		String o = sender + "------>" + receiver + "\nText: " + body + "\n" + "Time: " + timestamp;
		return o;
	}
}
