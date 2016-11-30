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
public class BroadCastMessage extends Message{
    int read_count;
    BroadCastMessage(int m_id, String timestamp, String sent_by, String received_by, String body){
        super(m_id, timestamp, sent_by, received_by, body);
        read_count = 0;
    }
    int getReadCount(){ return this.read_count; }
    
}
