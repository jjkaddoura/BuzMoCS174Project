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
public class ChatGroupInvite{
  private User invitee;
  private ChatGroup chatGroup;

  ChatGroupInvite(User invitee, ChatGroup chatGroup){
    this.invitee = invitee;
    this.chatGroup = chatGroup;
  }

  public User getInvitee(){
  	return invitee;
  }

  public ChatGroup getChatGroup(){
  	return chatGroup;
  }

  public void acceptInvite(){
  	//TODO
  }
}
