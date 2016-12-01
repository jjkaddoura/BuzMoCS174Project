/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;

import java.util.*;
import java.lang.*;

/**
 *
 * @author jacob
 */
public class BuzMo {

	private static User currentUser;
	private static Scanner scanner;

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  	scanner = new Scanner(System.in);

  	System.out.println("Welcome to BuzMo!");
    // TODO code application logic here
  }

  private void promptLogin(){
  	System.out.println("Email:");
  	String username = scanner.nextLine();

  	System.out.println("Password:");
  	String password = scanner.nextLine();

  	//TODO validate
  }

  private boolean setScreenname(String screenname){
  	if (screenname.length() <= 20){
  		currentUser.setScreenname(screenname);
  		return true;
  	}
  	else {
  		System.out.println("Screenname cannot be more than 20 characters.");
  		return false;
  	}
  }

  private void PostMessage(){
  	String promptMessage = "What type of message do you want to post? Enter 1 for a private message, 2 for a message to a ChatGroup, 3 for a message to certain friends, 4 for a message to all friends, 5 for a public post.";
  	int messageType = -1;

  	while (messageType < 0 || messageType > 5){
	  	try {
	  		System.out.println(promptMessage);
	  		System.out.println("Enter 0 to return to main menu.");
	  		messageType = scanner.nextInt();
	  	} catch (InputMismatchException e){
	  		promptMessage = "Not valid input, please input 1 for a private message, 2 for a message to a ChatGroup, 3 for a message to certain friends, 4 for a message to all friends, 5 for a public post.";

	  	}
	  }
	  switch(messageType){
		  case 1:
		  	PostPrivateMessage();
		  	break;
		  case 2:
		  	PostChatGroupMessage();
		  	break;
		  case 3:
		  	PostCustomMessage();
		  	break;
		  case 4:
		  	PostBroadcastMessage(false);
		  	break;
		  case 5:
		  	PostBroadcastMessage(true);
		  	break;
		}
  }

  private void PostPrivateMessage(){
  	System.out.println("Enter recipient's email (or 0 to return to main menu):");
  	String email = scanner.nextLine();
  	if (email == "0") return;
  	//TODO the rest

  }

  private void PostChatGroupMessage(){
  	System.out.println("Enter ChatGroup name (or 0 to return to main menu):");
  	String groupName = scanner.nextLine();
  	if (groupName == "0") return;

  	String queryString = "SELECT * FROM GroupMember WHERE gname=" + groupName + " AND email=" + currentUser.getEmail() + ";";
  	//TODO check if GroupMember relationship is found for this (user,group) tuple
  	//TODO otherwise print error message
  }

  private void PostCustomMessage(){
  	//TODO
  }

  private void PostBroadcastMessage(boolean isPublic){
  	//TODO
  }

  private void deleteMessage(Message message){
  	//TODO remove from database
  }

  private ChatGroup createChatGroup(){
  	System.out.println("Name your chatgroup:");
  	String groupName = scanner.nextLine();

  	System.out.println("How many days would you like messages in this chatGroup to last? (Hit Enter to use default 7)");
  	int duration;
  	try {
  		duration = scanner.nextInt();
  	} catch (InputMismatchException e){
  		duration = 7;
  	}

  	ChatGroup chatGroup = new ChatGroup(currentUser.getEmail(), groupName, duration);
  	//TODO save to database


  	return chatGroup;
  }

  private ChatGroup modifyChatGroup(){
  	System.out.println("Which ChatGroup would you like to modify?");
  	String groupName = scanner.nextLine();

  	//TODO find chatGroup by name
  	String queryString = "SELECT * FROM ChatGroups WHERE gname=" + groupName + ";";
  	//TODO initialize chatGroup object from SQL Query
  	ChatGroup chatGroup = new ChatGroup();
  	//ChatGroup chatGroup = new ChatGroup(FILL IN);

  	if (!chatGroup.getOwner().equals(currentUser.getEmail())){
  		System.out.println("Only the owner can modify a ChatGroup.");
  		return null;
  	}

  	System.out.println("Would you like to change the name of this ChatGroup?(Y/N)");
  	String changeGroup = scanner.nextLine();

  	while (changeGroup.toLowerCase() != "y" && changeGroup.toLowerCase() != "n"){
  		System.out.println("Could not understand input. Please input 'Y' if you want to change the name or 'N' otherwise.");
  		changeGroup = scanner.nextLine();
  	}
  	if (changeGroup.toLowerCase() == "y"){
  		System.out.println("Enter new ChatGroup name:");
  		String newName = scanner.nextLine();
  		chatGroup.setName(newName);
  	}


  	System.out.println("Would you like to change the duration of messages posted in this ChatGroup?(Y/N)");
  	String changeDuration = scanner.nextLine();

  	while (changeDuration.toLowerCase() != "y" && changeDuration.toLowerCase() != "n"){
  		System.out.println("Could not understand input. Please input 'Y' if you want to change the duration or 'N' otherwise.");
  		changeDuration = scanner.nextLine();
  	}
  	if (changeDuration.toLowerCase() == "y"){
  		System.out.println("How many days would you like messages in this chatGroup to last? (Hit Enter to use default 7)");
	  	int duration;
	  	try {
	  		duration = scanner.nextInt();
	  	} catch (InputMismatchException e){
	  		duration = 7;
	  	}
	  	chatGroup.setDuration(duration);
  	}

  	//TODO save to database
  	return chatGroup;
  	
  }
  
  private Friendship sendFriendRequest(){
    //TODO make sure friend request doesn't exist either direction
  }
}
