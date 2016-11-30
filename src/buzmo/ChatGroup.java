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
public class ChatGroup {
    String gname;
    int duration;
    String owner; // email of person who made group
    
    // Default duration is 7 days
    ChatGroup(String gname, String owner){
        this.gname = gname;
        this.owner = owner;
        this.duration = 7;
    }
    ChatGroup(String gname, String owner, int duration){
        this.gname = gname;
        this.owner = owner;
        this.duration = duration;
    }
    int getDuration(){ return this.duration; }
    
}
