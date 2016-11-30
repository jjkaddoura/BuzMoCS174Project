/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;
import java.lang.String;
/**
 *
 * @author jacob
 */
public class PrivateMessage extends Message {
    boolean sender_copy_delete;
    boolean receiver_copy_delete;
    PrivateMessage(int m_id, String timestamp, String sent_by, String received_by, String body){
        super(m_id, timestamp, sent_by, received_by, body);
        this.sender_copy_delete = false;
        this.receiver_copy_delete = false;
    }
    void deleteSenderCopy() { this.sender_copy_delete = true; }
    void deleteReceiverCopy() { this.receiver_copy_delete = true; }
}
