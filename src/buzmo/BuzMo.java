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

		currentUser = new User("Kevin Durant", "DurantKev@gmail.com", "password", false, null);

		// TODO code application logic here

		promptLoginOrRegister();
		boolean isManager = false; // DO A QUERY HERE TO SEE IF CUR_USER IS A MANAGER
		int action  = -1;
		while(action != 0){
			if(isManager){
			// MANAGER INTERFACE
				System.out.println("What would you like to do?\n    (1) Post a message\n    "+
				                   "(2) Delete a message\n    (3) Create a ChatGroup\n    "+
				                   "(4) Modify ChatGroup properties\n    (5) Invite a friend to a ChatGroup\n    "+
				                   "(6) Accept a ChatGroup invite\n    (7) Search recent messages\n   "+
				                   "(8) Search for users\n    (9) Request to join friend circle\n    "+
				                   "(10) Find active users\n    (11) Show number of inactive users\n    "+
				                   "(12) Find top messages\n    (13) Display complete summary report\n"+
				                   "(0) EXIT BuzMo");
			}
			else{
			// USER INTERFACE
				System.out.println("What would you like to do?\n    (1) Post a message\n    "+
				                   "(2) Delete a message\n    (3) Create a ChatGroup\n    "+
				                   "(4) Modify ChatGroup properties\n    (5) Invite a friend to a ChatGroup\n    "+
				                   "(6) Accept a ChatGroup invite\n    (7) Search recent messages\n    "+
				                   "(8) Search for users\n    (9) Request to join friend circle\n    "+
				                   "(0) EXIT BuzMo");
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
		String promptMessage = "What type of message do you want to post?\n    (1) for a private message\n    " + 
		                       "(2) for a message to a ChatGroup\n    (3) for a message to certain friends\n    " + 
		                       "(4) for a message to all friends\n    (5) for a public post";
		int messageType = -1;

		while (messageType < 0 || messageType > 5){
			try {
				System.out.println(promptMessage);
				System.out.println("Enter 0 to return to main menu.");
				messageType = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e){
				promptMessage = "Not valid input, please enter:\n(1) for a private message\n" + 
				                "(2) for a message to a ChatGroup\n(3) for a message to certain friends\n" + 
				                "(4) for a message to all friends\n(5) for a public post";
			}
		}
		switch(messageType){
			case 1:
				postPrivateMessage();
				break;
			case 2:
				postChatGroupMessage();
				break;
			case 3:
				postCustomMessage();
				break;
			case 4:
				postBroadcastMessage(false);
				break;
			case 5:
				postBroadcastMessage(true);
				break;
		}
	}

	private static void postPrivateMessage(){
		System.out.println("Enter recipient's email (or 0 to return to main menu):");
		String email = "";
		while (email.equals("")) email = scanner.nextLine();
		if (email.equals("0")) return;

		ArrayList<String> friends = getFriends();
		while (!friends.contains(email)){
			System.out.println("This user is not in your MyCircle\nEnter another email (or 0 to return to main menu):");
			email = scanner.nextLine();
			if (email.equals("0")) return;
		}

		System.out.println("Enter message body (or 0 to return to main menu):");
		String body = scanner.nextLine();
		if (body.equals("0")) return;

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//TODO get timestamp

		//PrivateMessage message = new PrivateMessage(timestamp.toString(), currentUser.getEmail(), email, body);

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			int result = updateDatabase("INSERT INTO PrivateMessage (time, sent_by, received_by, body) VALUES (" + 
			                            "TIMESTAMP '" + timestamp + "','" + currentUser.getEmail() + "','" + email + "','" + body + "')");
			if (result == 1) System.out.println("PrivateMessage sent to " + email);
			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	private static void postChatGroupMessage(){
		System.out.println("Enter ChatGroup name (or 0 to return to main menu):");
		String gname = scanner.nextLine();
		if (gname.equals("0")) return;

		ArrayList<String> myChatGroups = getMyChatGroups();
		while (!myChatGroups.contains(gname)){
			System.out.println("You are not a member of this ChatGroup\n Enter another ChatGroup name (or 0 to return to main menu):");
			gname = scanner.nextLine();
			if (gname.equals("0")) return;
		}

		System.out.println("Enter message body (or 0 to return to main menu):");
		String body = scanner.nextLine();
		if (body.equals("0")) return;

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			int result = updateDatabase("INSERT INTO ChatGroupMessage (time, sent_by, gname, body) VALUES (" + 
			                            "TIMESTAMP '" + timestamp + "','" + currentUser.getEmail() + "','" + gname + "','" + body + "')");
			if (result == 1) System.out.println("ChatGroupMessage sent to " + gname);
			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	private static void postCustomMessage(){
		System.out.println("Enter recipient's email (or 0 to return to main menu):");
		String email = scanner.nextLine();
		if (email.equals("0")) return;

		ArrayList<String> recipients = new ArrayList<String>();
		ArrayList<String> friends = getFriends();

		// Ask for recipients
		while (recipients.isEmpty() || !email.toLowerCase().equals("done")){
			if (recipients.isEmpty() && email.toLowerCase().equals("done")){
				System.out.println("Enter at least one email (or 0 to return to main menu):");
			}
			else if (friends.contains(email)){
				recipients.add(email);
				System.out.println("Enter more emails, or 'done' if you're done (enter 0 to return to main menu):");
			} 
			else {
				System.out.println("This user is not in your MyCircle\n Enter another email (or 0 to return to main menu):");
			}

			email = scanner.nextLine();
			if (email.equals("0")) return;
		}

		// Ask for body
		System.out.println("Enter message body (or 0 to return to main menu):");
		String body = scanner.nextLine();
		if (body.equals("0")) return;

		// Ask for TopicWords
		ArrayList<String> topics = new ArrayList<String>();
		System.out.println("Enter a TopicWord for this message (or 0 to return to main menu):");
		String topic = scanner.nextLine();
		if (topic.equals("0")) return;
		while (topics.isEmpty() || !topic.toLowerCase().equals("done")){
			if (topics.isEmpty() && topic.toLowerCase().equals("done")){
				System.out.println("Please enter at least one TopicWord (or 0 to return to main menu):");
			}
			else {
				topics.add(topic);
				System.out.println("Enter more TopicWords, or 'done' if you're done (enter 0 to return to main menu):");
			} 

			topic = scanner.nextLine();
			if (topic.equals("0")) return;
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//TODO get timestamp

		//PrivateMessage message = new PrivateMessage(timestamp.toString(), currentUser.getEmail(), email, body);

		ArrayList<String> existingTopicWords = getTopicWords();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = insertAndGetM_id("INSERT INTO CustomMessage (time, sent_by, body) VALUES (" + 
			                                "TIMESTAMP '" + timestamp + "','" + currentUser.getEmail() + "','" + body + "')"); //+

			String m_id = "";
			while(rs != null && rs.next()){
				m_id = rs.getString(1);
			}

			int result = 0;
			String printString = "Custom Message sent to ";
			String topicString = "    topics: ";

			for (String keyword: topics){
				if (!existingTopicWords.contains(keyword)) result = updateDatabase("INSERT INTO TopicWord (keyword) VALUES ('" + keyword + "')");
				result = updateDatabase("INSERT INTO CustomMessage_Topic (m_id, keyword) VALUES (" + m_id + ",'" + keyword + "')");
				topicString += keyword + ", ";
			}

			for (String recipient: recipients){
				result = updateDatabase("INSERT INTO Message_Viewer (m_id, email) VALUES (" + m_id + ",'" + recipient + "')");
				printString += recipient + ", ";
			}
			printString = printString.substring(0, printString.length()-2);
			topicString = topicString.substring(0, topicString.length()-2);
			if (result == 1) {
				System.out.println(printString);
				System.out.println(topicString);
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 
	}

	private static void postBroadcastMessage(boolean isPublic){
		System.out.println("Enter message body (or 0 to return to main menu):");
		String body = "";
		while (body.equals("")) body = scanner.nextLine();
		if (body.equals("0")) return;

		// Ask for TopicWords
		ArrayList<String> topics = new ArrayList<String>();
		System.out.println("Enter a TopicWord for this message (or 0 to return to main menu):");
		String topic = scanner.nextLine();
		if (topic.equals("0")) return;
		while (topics.isEmpty() || !topic.toLowerCase().equals("done")){
			if (topics.isEmpty() && topic.toLowerCase().equals("done")){
				System.out.println("Please enter at least one TopicWord (or 0 to return to main menu):");
			}
			else {
				topics.add(topic);
				System.out.println("Enter more TopicWords, or 'done' if you're done (enter 0 to return to main menu):");
			} 

			topic = scanner.nextLine();
			if (topic.equals("0")) return;
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//TODO get timestamp

		ArrayList<String> existingTopicWords = getTopicWords();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			/*int result = updateDatabase("INSERT INTO BroadcastMessage (time, sent_by, body, is_public) VALUES (" + 
			                            "TIMESTAMP '" + timestamp + "','" + currentUser.getEmail() + "','" + body + "','" + (isPublic ? "1" : "0") + "')");*/

			ResultSet rs = insertAndGetM_id("INSERT INTO BroadcastMessage (time, sent_by, body, is_public) VALUES (" + 
			                                "TIMESTAMP '" + timestamp + "','" + currentUser.getEmail() + "','" + body + "','" + (isPublic ? "1" : "0") + "')");

			String m_id = "";
			while(rs != null && rs.next()){
				m_id = rs.getString(1);
			}

			int result = 0;

			String topicString = "    topics: ";
			for (String keyword: topics){
				if (!existingTopicWords.contains(keyword)) result = updateDatabase("INSERT INTO TopicWord (keyword) VALUES ('" + keyword + "')");
				result = updateDatabase("INSERT INTO BroadcastMessage_Topic (m_id, keyword) VALUES (" + m_id + ",'" + keyword + "')");
				topicString += keyword + ", ";
			}
			topicString = topicString.substring(0, topicString.length()-2);

			if (result == 1) {
				System.out.println((!isPublic ? "Message posted to friends" : "Public message posted"));
				System.out.println(topicString);
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	private static void deleteMessage(){
		String promptMessage = "What type of message do you want to delete?\n    (1) for a private message\n    " + 
		                       "(2) for a message to a ChatGroup\n    (3) for a message to certain friends\n    " + 
		                       "(4) for a message to all friends\n    (5) for a public post";
		int messageType = -1;

		while (messageType < 0 || messageType > 5){
			try {
				System.out.println(promptMessage);
				System.out.println("Enter 0 to return to main menu.");
				messageType = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e){
				promptMessage = "Not valid input, please enter:\n(1) for a private message\n" + 
				                "(2) for a message to a ChatGroup\n(3) for a message to certain friends\n" + 
				                "(4) for a message to all friends\n(5) for a public post";
			}
		}
		switch(messageType){
			case 1:
				deletePrivateMessage();
				break;
			case 2:
				deleteChatGroupMessage();
				break;
			case 3:
				deleteCustomMessage();
				break;
			case 4:
				deleteBroadcastMessage(false);
				break;
			case 5:
				deleteBroadcastMessage(true);
				break;
		}

		//TODO remove from database
	}

	private static void deletePrivateMessage(){
		ArrayList<PrivateMessage> privateMessages = getMySentPrivateMessages();
		if (privateMessages.isEmpty()){
			System.out.println("You do not have any privateMessages");
			return;
		}
		System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");
		int i = 1;
		for (PrivateMessage pm: privateMessages){
			System.out.println("    (" + i + ") " + pm.toString());
			i++;
		}

		int indexInput = Integer.parseInt(scanner.nextLine());
		if (indexInput == 0) return;
		int indexToDelete = indexInput - 1;
		int m_idToDelete = privateMessages.get(indexToDelete).getM_id();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);
			int result = updateDatabase("DELETE FROM PrivateMessage WHERE m_id=" + m_idToDelete);
			if (result == 1) System.out.println("Message successfully deleted");
			con.close();
		} catch (Exception e){
			System.out.println("deletePrivateMessages ERROR");
		}
	}

	private static void deleteChatGroupMessage(){
		ArrayList<ChatGroupMessage> chatGroupMessages = getMySentChatGroupMessages();
		if (chatGroupMessages.isEmpty()){
			System.out.println("You do not have any chatGroupMessages");
			return;
		}
		System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");
		int i = 1;
		for (ChatGroupMessage gm: chatGroupMessages){
			System.out.println("    (" + i + ") " + gm.toString());
			i++;
		}

		int indexInput = Integer.parseInt(scanner.nextLine());
		if (indexInput == 0) return;
		int indexToDelete = indexInput - 1;
		int m_idToDelete = chatGroupMessages.get(indexToDelete).getM_id();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);
			int result = updateDatabase("DELETE FROM ChatGroupMessage WHERE m_id=" + m_idToDelete);
			if (result == 1) System.out.println("Message successfully deleted");
			con.close();
		} catch (Exception e){
			System.out.println("deleteChatGroupMessages ERROR");
		}

	}

	private static void deleteCustomMessage(){
		ArrayList<CustomMessage> customMessages = getMySentCustomMessages();
		if (customMessages.isEmpty()){
			System.out.println("You do not have any customMessages");
			return;
		}
		System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");
		int i = 1;
		for (CustomMessage cm: customMessages){
			System.out.println("    (" + i + ") " + cm.toString());
			i++;
		}

		int indexInput = Integer.parseInt(scanner.nextLine());
		if (indexInput == 0) return;
		int indexToDelete = indexInput - 1;
		int m_idToDelete = customMessages.get(indexToDelete).getM_id();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);
			int result = updateDatabase("DELETE FROM CustomMessage WHERE m_id=" + m_idToDelete);
			if (result == 1) System.out.println("Message successfully deleted");
			con.close();
		} catch (Exception e){
			System.out.println("deleteCustomMessages ERROR");
		}
	}

	private static void deleteBroadcastMessage(boolean isPublic){
		ArrayList<BroadcastMessage> broadcastMessages = getMySentBroadcastMessages(isPublic);
		if (broadcastMessages.isEmpty()){
			System.out.println("You do not have any broadcastMessages");
			return;
		}
		System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");
		int i = 1;
		for (BroadcastMessage bm: broadcastMessages){
			System.out.println("    (" + i + ") " + bm.toString());
			i++;
		}

		int indexInput = Integer.parseInt(scanner.nextLine());
		if (indexInput == 0) return;
		int indexToDelete = indexInput - 1;
		int m_idToDelete = broadcastMessages.get(indexToDelete).getM_id();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);
			int result = updateDatabase("DELETE FROM BroadcastMessage WHERE m_id=" + m_idToDelete);
			if (result == 1) System.out.println("Message successfully deleted");
			con.close();
		} catch (Exception e){
			System.out.println("deleteBroadcastMessages ERROR");
		}
	}


	private static ChatGroup createChatGroup(){
		System.out.println("Name your chatgroup:");
		String groupName = scanner.nextLine();

		System.out.println("How many days would you like messages in this chatGroup to last? (Hit Enter to use default 7)");
		int duration;
		try {
			duration = scanner.nextInt();
			scanner.nextLine();
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
				scanner.nextLine();
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
	
	// Returns a list of friends' emails
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

	// Returns a list of topic words
	private static ArrayList<String> getTopicWords(){
		ArrayList<String> topicWords = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT keyword FROM TopicWord");
			while(rs.next()){
				topicWords.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return topicWords;
	}

	// Returns a list of my sent PrivateMessages
	private static ArrayList<PrivateMessage> getMySentPrivateMessages(){
		ArrayList<PrivateMessage> messages = new ArrayList<PrivateMessage>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT m_id, time, received_by, body FROM PrivateMessage WHERE sent_by='" + currentUser.getEmail() + "' AND sender_copy_delete=0");
			while(rs.next()){
				messages.add(new PrivateMessage(Integer.parseInt(rs.getString(1)), rs.getString(2), currentUser.getEmail(), rs.getString(3), rs.getString(4)));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return messages;
	}

	// Returns a list of my sent ChatGroupMessages
	private static ArrayList<ChatGroupMessage> getMySentChatGroupMessages(){
		ArrayList<ChatGroupMessage> messages = new ArrayList<ChatGroupMessage>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT m_id, time, body, gname FROM ChatGroupMessage WHERE sent_by='" + currentUser.getEmail() + "'");
			while(rs.next()){
				messages.add(new ChatGroupMessage(Integer.parseInt(rs.getString(1)), rs.getString(2), currentUser.getEmail(), rs.getString(3), rs.getString(4)));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return messages;
	}

	// Returns a list of my sent BroadCastMessages
	private static ArrayList<BroadcastMessage> getMySentBroadcastMessages(boolean isPublic){
		ArrayList<BroadcastMessage> messages = new ArrayList<BroadcastMessage>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT m_id, time, body, is_public FROM BroadCastMessage WHERE sent_by='" + currentUser.getEmail() + "' AND is_public=" + (isPublic ? 1 : 0));
			while(rs.next()){
				String m_id = rs.getString(1);

				ResultSet topicRs = queryDatabase("SELECT keyword FROM BroadcastMessage_Topic WHERE m_id=" + m_id);
				ArrayList<String> topics = new ArrayList<String>();
				while (topicRs.next()){
					topics.add(topicRs.getString(1));
				}

				messages.add(new BroadcastMessage(Integer.parseInt(m_id), rs.getString(2), currentUser.getEmail(), rs.getString(3), topics, (rs.getInt(4) == 1 ? true : false) ));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return messages;
	}

	// Returns a list of my sent CustomMessages
	private static ArrayList<CustomMessage> getMySentCustomMessages(){
		ArrayList<CustomMessage> messages = new ArrayList<CustomMessage>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT m_id, time, body FROM CustomMessage WHERE sent_by='" + currentUser.getEmail() + "'");
			while(rs.next()){
				String m_id = rs.getString(1);

				ResultSet topicRs = queryDatabase("SELECT keyword FROM CustomMessage_Topic WHERE m_id=" + m_id);
				ArrayList<String> topics = new ArrayList<String>();
				while (topicRs.next()){
					topics.add(topicRs.getString(1));
				}

				ResultSet recipientRs = queryDatabase("SELECT email FROM Message_Viewer WHERE m_id=" + m_id);
				ArrayList<String> recipients = new ArrayList<String>();
				while (recipientRs.next()){
					recipients.add(recipientRs.getString(1));
				}

				CustomMessage message = new CustomMessage(Integer.parseInt(m_id), rs.getString(2), currentUser.getEmail(), rs.getString(3), topics, recipients);
				messages.add(message);
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return messages;
	}

	// Returns a list of ChatGroups' gnames currentUser is a member of
	private static ArrayList<String> getMyChatGroups(){
		if (currentUser == null) return null;
		ArrayList<String> chatGroups = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT gname FROM Group_Member" +
			                             " WHERE email='" + currentUser.getEmail() + "'");
			while(rs.next()){
				chatGroups.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return chatGroups;
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
	private static int updateDatabase(String sqlString){
		// System.out.println(sqlString);
		try{
			Statement st = con.createStatement();
			
			return st.executeUpdate(sqlString);
		}
		catch(Exception e){
			System.out.println("updateDatabase ERROR: " + e);
		}

		return -1;
	}

	private static ResultSet insertAndGetM_id(String sqlString){
		try{
			PreparedStatement pstm = con.prepareStatement(sqlString ,new String[]{"m_id"});
			int i = pstm.executeUpdate();

			if (i > 0) return pstm.getGeneratedKeys();
		}
		catch(Exception e){
			System.out.println("insertAndGetM_id ERROR: " + e);
		}

		return null;
	}
	private static ResultSet queryDatabase(String queryString){
		//System.out.println(queryString);
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
