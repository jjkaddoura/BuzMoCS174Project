/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buzmo;

import java.util.*;
import java.lang.*;
import java.sql.*;

//import org.skife.jdbi.*;

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

		System.out.println("Welcome to BuzMo!");

		// Print all users REMOVE THIS CODE
		ArrayList<Map<String, Object>> allUsers = getAllUsers();
		for (Map user: allUsers){
			// Print all column values
			for (Object key: user.keySet()){
				System.out.print(user.get(key) + " ");
			}
			System.out.println();

			// Print only email
			//System.out.println(user.get("EMAIL"));
		}

		// Print Kevin Durant's friends REMOVE THIS CODE
		currentUser = new User("Kevin Durant", "DurantKev@gmail.com", "password", false, null);
		ArrayList<String> kevsFriends = getFriends();
		for (String email: kevsFriends){
			System.out.println(email);
		}

		// TODO code application logic here

		promptLoginOrRegister();
		boolean isManager = false; // DO A QUERY HERE TO SEE IF CUR_USER IS A MANAGER
		int action  = -1;
		while(action != 0){
		    if(isManager){
			// MANAGER INTERFACE
			System.out.println("What would you like to do?\n   (1) Post a message\n   "+
					   "(2) Delete a message\n   (3) Create a ChatGroup\n   "+
					   "(4) Modify ChatGroup properties\n   (5) Invite a friend to a ChatGroup\n   "+
					   "(6) Accept a ChatGroup invite\n   (7) Search recent messages\n   "+
					   "(8) Search for users\n   (9) Request to join friend circle\n   "+
					   "(10) Find active users\n   (11) Show number of inactive users\n   "+
					   "(12) Find top messages\n   (13) Display complete summary report\n"+
					   "(0) EXIT BuzMO");
		    }
		    else{
			// USER INTERFACE
			System.out.println("What would you like to do?\n   (1) Post a message\n   "+
					   "(2) Delete a message\n   (3) Create a ChatGroup\n   "+
					   "(4) Modify ChatGroup properties\n   (5) Invite a friend to a ChatGroup\n   "+
					   "(6) Accept a ChatGroup invite\n   (7) Search recent messages\n   "+
					   "(8) Search for users\n   (9) Request to join friend circle\n   "+
					   "(0) EXIT BuzMO");
		    }
		    
		    try{
			String input  = scanner.nextLine();
			action = Integer.parseInt(input);
			}
		    catch(Exception e){
			System.out.println("ERROR: " + e);
		    }
		    
		   
		    switch(action){
		    case 1:
			PostMessage();
			break;
		    case 2:
			deleteMessage();
			break;
		    case 3:
			createChatGroup();
			break;
		    case 4:
			modifyChatGroup();
			break;
		    case 5:
			sendChatGroupInvite();
			break;
		    case 6:
			acceptChatGroupInvite();
			break;
		    case 7:
			searchRecentMessages();
		    break;
		    case 8:
			searchUsers();
			break;
		    case 9:
			sendFriendRequest();
			break;
		    case 10:
			if(!isManager) break;
			findActiveUsers();
			break;
		    case 11:
			if(!isManager) break;
			getNumberOfInactiveUsers();
			break;
		    case 12:
		    if(!isManager) break;
		    findTopMessages();
		    break;
		    case 13:
			if(!isManager) break;
			getSummaryReport();
			break;
		    }
		}
	}
    // LOGIN OR REGISTER TO BUZMO 
    private static void promptLoginOrRegister(){
		String answer = "";
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
			//TODO validate and set currentUser
			System.out.println("Logged in.");
		}
		else{
			// TODO INSERT NEW USER TO DATABASE and set currentUser
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

	private static void deleteMessage(){
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
	
	private static ArrayList<String> getFriends(){
		if (currentUser == null) return null;
		ArrayList<String> friends = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT UserProfile.email, UserProfile.name FROM UserProfile" +
			                             " JOIN Friendship ON UserProfile.email=Friendship.initiator" + 
			                             " WHERE is_pending=0 AND receiver='" + currentUser.getEmail() + "'" +
			                             
			                             " UNION SELECT UserProfile.email, UserProfile.name FROM UserProfile" +
			                             " JOIN Friendship ON UserProfile.email=Friendship.receiver" + 
			                             " WHERE is_pending=0 AND initiator='" + currentUser.getEmail() + "'");
			while(rs.next()){
				friends.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return friends;
	}

	private static ArrayList<Map<String, Object>> getAllUsers(){
		ArrayList<Map<String, Object>> users = new ArrayList<Map<String, Object>>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT * FROM UserProfile");

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

				for (int i = 1; i <= columnCount; i++) {
					columns.put(metaData.getColumnLabel(i), rs.getObject(i));
				}

				users.add(columns);
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return users;
	}
  private static ChatGroup acceptChatGroupInvite(){
      // TODO pull all pending chatgroup invites for particular user
      return null;
  }
  private static ChatGroup sendChatGroupInvite(){
      // TODO Make sure user is in ChatGroup
      return null;
  }

  private static void searchRecentMessages(){
      // TODO ask user for set of topicwords to search
      // default is topicwords associated with that user
      return;
  }
  private static void searchUsers(){
      // TODO ask user for emails/topicwords
      // the email provided must have posted a message <= 7 days ago
      return;
  }
    // MANAGER FUNCTIONS
  private static void getSummaryReport(){
      findActiveUsers();
      findTopMessages();
      getNumberOfInactiveUsers();
	
  }
  private static void findActiveUsers(){
	// TODO top 3 in message counts in the last 7 days
      return;
  }
  private static Message findTopMessages(){
      //TODO top read counts in the last 7 days
      return null;

  }
  private static int getNumberOfInactiveUsers(){
      //TODO all users with < 3 messages sent
      return -1;
  }
	private static ResultSet queryDatabase(String queryString){
		System.out.println(queryString);
		try{
			Statement st = con.createStatement();
			
			ResultSet rt = st.executeQuery(queryString);

			return rt;
			}
		catch(Exception e){
			System.out.println("executeQuery ERROR: " + e);
		}

		return null;
	}
}
