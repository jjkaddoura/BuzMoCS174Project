/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;
import java.lang.*;
/**
 *
 * @author jacob
 */
public abstract class Message {
    String timestamp;
    int m_id;
    String sent_by;
    String received_by;
    String body;
    Message(int m_id, String timestamp, String sent_by, String received_by, String body){
        this.m_id = m_id;
        this.timestamp = timestamp;
        this.sent_by = sent_by;
        this.received_by = received_by;
        this.body = body;
    }
}
