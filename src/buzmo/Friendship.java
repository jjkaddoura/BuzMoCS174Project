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
public class Friendship{
  private String initiator;
  private String receiver;
  private boolean isPending;

  public String getInitiator(){
  	return initiator;
  }

  public String getReceiver(){
  	return receiver;
  }

  public List<String> getFriends(){
    ArrayList<String> friends = new ArrayList<String>();
    friends.add(initiator);
    friends.add(receiver);
  	return friends;

  }

  public boolean isPending(){
  	return isPending;
  }
}
