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
public class ChatGroup {
  private String gname;
  private int duration;
  private String owner; // email of person who made group
  private ArrayList<User> members;

  ChatGroup(){}
  
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

  public String getOwner(){
    return this.owner;
  }

  public int getDuration(){ 
    return this.duration; 
  }

  public void setDuration(int duration){
    this.duration = duration;
  }

  public void setName(String name){
    gname = name;
  }
}
