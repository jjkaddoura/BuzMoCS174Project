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
public abstract class Message {
    private String timestamp;
    private int m_id;
    private String sender;
    private String body;

    Message(int m_id, String timestamp, String sender, String body){
      this.m_id = m_id;
      this.timestamp = timestamp;
      this.sender = sender;
      this.body = body;
    }
}
