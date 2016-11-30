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
public class User {
  private String email;
  private String name;
  private String phone_num;
  private String password;
  private String screenname;
  public boolean is_manager;
  private ArrayList<String> topicWords;

  User(String name, String email, String password, boolean is_manager, ArrayList<String> topicWords){
  	this.name = name;
    this.email = email;
    this.password = password;
    this.is_manager = is_manager;
    this.topicWords = topicWords;
  }
  User(String name, String email, String password, boolean is_manager, ArrayList<String> topicWords, String screenname){
  	this.name = name;
    this.email = email;
    this.password = password;
    this.screenname = screenname;
    this.topicWords = topicWords;
    this.is_manager = is_manager;
  }

  public String getName(){
  	return this.name;
  }

  public void setName(String name){
  	this.name = name;
  }

  public String getScreenname(){
  	return this.screenname;
  }

  public void setScreenname(String screenname){
  	this.screenname = screenname;
  }

  public String getEmail(){
  	return this.screenname;
  }

  public void setEmail(String email){
  	this.email = email;
  }

  public String getPassword(){
  	return this.password;
  }

  public void setPassword(String password){
  	this.password = password;
  }

  public String getPhone_num(){
  	return this.phone_num;
  }

  public void setPhone_num(String phone_num){
  	this.phone_num = phone_num;
  }

  public ArrayList<String> getTopicWords(){
  	return this.topicWords;
  }

  public void removeTopicWord(String topicWord){
  	topicWords.remove(topicWord);
  }

  public void addTopicWord(String topicWord){
  	if (!topicWords.contains(topicWord)){
	  	topicWords.add(topicWord);
	  }
  }
}
