/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;

import java.util.*;
import java.lang.*;
import java.sql.*;

/**
 *
 * @author jacob
 */
public class BuzMo {

	private static User currentUser;
	private static Scanner scanner;
	 
	private static String url = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	//private static String username = "jmangel";
	private static String username = "jjkaddoura";
	//private static String password = "514";
	private static String password = "432";
	private static Connection con;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		//set up database connection
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);
		} catch (Exception e){
			System.out.println("ERROR: " + e);
		}

		
		System.out.println("Welcome to BuzMo!");
		// Login or register
		promptLoginORRegister();
		
		
		int action  = -1;
		while(action != 0){
			System.out.println("What would you like to do?\n   (1) Post a message\n   "+
			                   "(2) Delete a message\n   (3) Create a ChatGroup\n   "+
			                   "(4) Modify ChatGroup properties\n   (5) Invite a friend to a ChatGroup\n   "+
			                   "(6) Accept a ChatGroup invite\n   (7) Search recent messages\n   "+
			                   "(8) Search for users\n   (9) Request to join friend circle\n   "+
			                   "(10) Get summary report\n   (0) EXIT BuzMO");
			try{
				String input  = scanner.nextLine();
				action = Integer.parseInt(input);
			}
			catch(Exception e){
				System.out.println("ERROR: " + e);
			}
			
			switch(action){
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			}
		}
		ResultSet rs = queryDatabase("SELECT * FROM UserProfile");
		try {
			while(rs.next()){
				System.out.println(rs.getString(1) + " " +
				                   rs.getString(2) + " " +
				                   rs.getString(3) + " " +
				                   rs.getString(4) + " " +
				                   rs.getString(5) + " " +
				                   (rs.getInt(6)==1 ? "Y" : "N"));
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}

	
		// TODO code application logic here


		//close connection
		try{
			con.close();
		} catch (Exception e){
			System.out.println("ERROR closing connection: " + e);
		}
		
	}


	// LOGIN OR REGISTER TO BUZMO 
	private static void promptLoginORRegister(){
		String answer = " ";
		System.out.println("Already have an account? (y/n)");
		while(!answer.toLowerCase().equals("y") && !answer.toLowerCase().equals("n")){
			System.out.println("Please enter 'y' to login or 'n' to register.");
			answer = scanner.nextLine();
		}
		System.out.println("Email:");
		String username = scanner.nextLine();
		System.out.println("Password:");
		String password = scanner.nextLine();
		if(answer.equals("y")){
			//TODO validate
			System.out.println("Logged in.");
		}
		else{
			// INSERT NEW USER TO DATABASE

			System.out.println("Congratulations! You have been successfully registered to BuzMo!");
		}
	}

	private static boolean setScreenname(String screenname){
		if (screenname.length() <= 20){
			currentUser.setScreenname(screenname);
			return true;
		}
		else {
			System.out.println("Screenname cannot be more than 20 characters.");
			return false;
		}
	}

	private static void PostMessage(){
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

	private static void PostPrivateMessage(){
		System.out.println("Enter recipient's email (or 0 to return to main menu):");
		String email = scanner.nextLine();
		if (email == "0") return;
		//TODO the rest

	}

	private static void PostChatGroupMessage(){
		System.out.println("Enter ChatGroup name (or 0 to return to main menu):");
		String groupName = scanner.nextLine();
		if (groupName.equals("0")) return;

		String queryString = "SELECT * FROM GroupMember WHERE gname=" + groupName + " AND email=" + currentUser.getEmail() + ";";
		//TODO check if GroupMember relationship is found for this (user,group) tuple
		//TODO otherwise print error message
	}

	private static void PostCustomMessage(){
		//TODO
	}

	private static void PostBroadcastMessage(boolean isPublic){
		//TODO
	}

	private static void deleteMessage(Message message){
		//TODO remove from database
	}

	private static ChatGroup createChatGroup(){
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

	private static ChatGroup modifyChatGroup(){
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

		while (!changeGroup.toLowerCase().equals("y") && !changeGroup.toLowerCase().equals("n")){
			System.out.println("Could not understand input. Please input 'Y' if you want to change the name or 'N' otherwise.");
			changeGroup = scanner.nextLine();
		}
		if (changeGroup.toLowerCase().equals("y")){
			System.out.println("Enter new ChatGroup name:");
			String newName = scanner.nextLine();
			chatGroup.setName(newName);
		}


		System.out.println("Would you like to change the duration of messages posted in this ChatGroup?(Y/N)");
		String changeDuration = scanner.nextLine();

		while (!changeDuration.toLowerCase().equals("y") && !changeDuration.toLowerCase().equals("n")){
			System.out.println("Could not understand input. Please input 'Y' if you want to change the duration or 'N' otherwise.");
			changeDuration = scanner.nextLine();
		}
		if (changeDuration.toLowerCase().equals( "y")){
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
	
	private static Friendship sendFriendRequest(){
		//TODO make sure friend request doesn't exist either direction
		return null;
	}

	private static ResultSet queryDatabase(String queryString){
		try{
			Statement st = con.createStatement();
			
			ResultSet rt = st.executeQuery(queryString);

			return rt;
			}
		catch(Exception e){
			System.out.println(e);
		}

		return null;
	}
}
