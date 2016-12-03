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

		//TODO REMOVE
		//currentUser = new User("Kevin Durant", "DurantKev@gmail.com", "password", false, null);
		// currentUser = new User("Kit", "KHarin@gmail.com", "KHarin1", false, null);
		// currentUser = new User("Ariana", "AGrande@yahoo.com", "oass", true, null);

		while (currentUser == null){
			promptLoginOrRegister();
		}
		boolean isManager = false;
		isManager = currentUser.is_manager;
		int action  = -1;
		while(action != 0){
			if(isManager){
				// MANAGER INTERFACE
				/*System.out.println("What would you like to do?\n    (1) Post a message\n    "+
				                   "(2) Delete a message\n    (3) Create a ChatGroup\n    "+
				                   "(4) Modify ChatGroup properties\n    (5) Invite a friend to a ChatGroup\n    "+
				                   "(6) Accept a ChatGroup invite\n    (7) Search recent messages\n   "+
				                   "(8) Search for users\n    (9) Request to join friend circle\n    "+
				                   "(10) Find active users\n    (11) Show number of inactive users\n    "+
				                   "(12) Find top messages\n    (13) Display complete summary report\n"+
				                   "(0) EXIT BuzMo");*/
				System.out.println("What would you like to do?\n    (1) Enter an existing private conversation\n    "+
				                   "(2) Start a new private conversation\n    (3) Enter MyCircle \n    "+
				                   "(4) Enter ChatGroup\n    (5) Create new ChatGroup\n    "+
				                   "(6) View ChatGroup invites\n    (7) Enter BuzMo Feed\n    " +
				                   "(8) Send friend request\n    (9) Find active users\n    "+
				                   "(10) Show number of inactive users\n    (11) Find top messages\n    "+
				                   "(12) Display complete summary report\n    (0) EXIT BuzMo");
			}
			else{
				// USER INTERFACE
				/*System.out.println("What would you like to do?\n    (1) Post a message\n    "+
				                   "(2) Delete a message\n    (3) Create a ChatGroup\n    "+
				                   "(4) Modify ChatGroup properties\n    (5) Invite a friend to a ChatGroup\n    "+
				                   "(6) Accept a ChatGroup invite\n    (7) Search recent messages\n    "+
				                   "(8) Search for users\n    (9) Request to join friend circle\n    "+
				                   "(0) EXIT BuzMo");*/
				System.out.println("What would you like to do?\n    (1) Enter an existing private conversation\n    "+
				                   "(2) Start a new private conversation\n    (3) Enter MyCircle \n    "+
				                   "(4) Enter ChatGroup\n    (5) Create new ChatGroup\n    "+
				                   "(6) View ChatGroup invites\n    (7) Enter BuzMo Feed\n    " +
				                   "(8) Send friend request\n    (0) EXIT BuzMo");
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
					enterPrivateConversation();
					break;
				case 2:
					postPrivateMessage();
					break;
				case 3:
					enterMyCircle();
					break;
				case 4:
					enterChatGroup();
					break;
				case 5:
					createChatGroup();
					break;
				case 6:
					viewChatGroupInvites();
					break;
				case 7:
					enterBuzMoFeed();
					break;
				case 8:
					sendFriendRequest();
					break;
				case 9:
					if(!isManager) break;
					findActiveUsers();
					break;
				case 10:
					if(!isManager) break;
					getNumberOfInactiveUsers();
					break;
				case 11:
					if(!isManager) break;
						findTopMessages();
					break;
				case 12:
					if(!isManager) break;
					getSummaryReport();
					break;
			}
		    
			/*switch(action){
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
			}*/
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

		if(answer.equals("y")){

			System.out.println("Email:");
			String username = scanner.nextLine();
			System.out.println("Password:");
			String userPassword = scanner.nextLine();

			currentUser = validate(username, userPassword);
			if (currentUser != null) System.out.println("Logged in.");
			else System.out.println("Invalid login");
		}
		else{
			currentUser = register();			
		}
	}

	private static boolean setScreenname(String screenname){
		if (screenname.length() <= 20){
			currentUser.setScreenname(screenname);
			//TODO update database
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

	private static void postMyCircleMessage(){
		String promptMessage = "Who do you want to post to?\n    (1) for all friends\n    " + 
		                       "(2) for certain friends\n    (3) for all BuzMo users\n    " +
		                       "(0) to return to main menu";
		int messageType = -1;

		while (messageType < 0 || messageType > 3){
			try {
				System.out.println(promptMessage);
				System.out.println("Enter 0 to return to main menu.");
				messageType = scanner.nextInt();
				scanner.nextLine();
			} catch (InputMismatchException e){
				promptMessage = "Not valid input, please enter:\n(1) for all friends\n" + 
				                "(2) for certain friends\n(3) for all BuzMo users\n" +
				                "(0) to return to main menu";
			}
		}
		switch(messageType){
			case 1:
				postBroadcastMessage(false);
				break;
			case 2:
				postCustomMessage();
				break;
			case 3:
				postBroadcastMessage(true);
				break;
		}
	}

	// create new private conversation
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

	// overload if email already selected
	private static void postPrivateMessage(String email){
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

	private static void postChatGroupMessage(String gname){
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
				topics.add(topic.toLowerCase());
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
				System.out.println();
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
				topics.add(topic.toLowerCase());
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
				System.out.println();
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
	}

	/*private static void deleteMessage(){
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
	}*/

	private static void enterPrivateConversation(){
		System.out.println("Enter friend's email:");
		String email = scanner.nextLine();

		ArrayList<String> friends = getFriends();
		ArrayList<String> sentRequests = getSentRequests();
		ArrayList<String> receivedRequests = getReceivedRequests();

		// make sure email is of a friend
		while (!friends.contains(email)){
			if (receivedRequests.contains(email)){
				System.out.println("This user has sent you a friend request. Would you like to accept at this time? (y/n)");
				String r = scanner.nextLine();

				while (!r.equals("0") && !r.toLowerCase().equals("n") && !r.toLowerCase().equals("y")){
					System.out.println("Please enter 'y' or 'n' or 0 to return to main menu");
					r = scanner.nextLine();
				}

				if (r.equals("0") || r.toLowerCase().equals("n")) return;

				if (r.toLowerCase().equals("y")){
					try {
						Class.forName("oracle.jdbc.driver.OracleDriver");
						con = DriverManager.getConnection(url, username, password);

						int result = updateDatabase("UPDATE Friendship SET is_pending=0 WHERE initiator='" + email + "' AND receiver='" + currentUser.getEmail() +"'");
					
						con.close();
					} catch (Exception e){
						System.out.println("modify ERROR: " + e);
					}
				}

				friends = getFriends();
			} else if (sentRequests.contains(email)){
				System.out.println("You have already sent this user a friend request");
				return;
			} else {
				System.out.println("Could not find friend. Please enter a different email:");
				email = scanner.nextLine();
			}

		}

		// get private conversation with friend
		ArrayList<PrivateMessage> privateMessages = getPrivateConversation(email);
		if (privateMessages.isEmpty()){
			System.out.println("This conversation is empty. To start a new conversation, enter 2 from the main menu.");
			return;
		}

		while (true) {
		
			// print recent messages 7 at a time
			int i = 1;
			int viewingMessagesUpTo = 7;
			int length = privateMessages.size();
			for (PrivateMessage pm: privateMessages){
				if (i <= viewingMessagesUpTo) {
					System.out.println("    (" + i + ") " + pm.toString());
					i++;
				} else if (length > viewingMessagesUpTo){
					System.out.println("Type 'more' to view more or 'done' to continue.");
					String response = scanner.nextLine();
					while (!response.toLowerCase().equals("more") && !response.toLowerCase().equals("done") && !response.equals("0")){
						System.out.println("Please enter 'more' to view more, 'done' to continue or 0 to return to the main menu.");
						response = scanner.nextLine();
					}
					boolean isDone = false;
					switch (response.toLowerCase()) {
						case "0":
							return;
						case "more":
							viewingMessagesUpTo += 7;
							break;
						case "done":
							isDone = false;
							break;
					}
					if (isDone) break;
				}
			}

			// prompt user for action
			System.out.println("To reply, enter 'reply'.");
			System.out.println("To delete a message, enter 'delete'. (Enter 0 to return to the main menu.)");

			String response = scanner.nextLine();
			while (!response.toLowerCase().equals("reply") && !response.toLowerCase().equals("delete") && !response.equals("0")){
				System.out.println("Invalid input. Please enter 'reply' to reply, 'delete' to delete a message, or 0 to return to the main menu.");
				response = scanner.nextLine();
			}

			if (response.toLowerCase().equals("reply")){
				postPrivateMessage(email);
			}
			else if (response.toLowerCase().equals("delete")){
				System.out.println("Please enter the corresponding number of the message you want to delete. (Enter 0 to return to the main menu.)");

				int indexInput = Integer.parseInt(scanner.nextLine());
				if (indexInput == 0) return;
				int indexToDelete = indexInput - 1;
				PrivateMessage messageToDelete = privateMessages.get(indexToDelete);
				int m_idToDelete = messageToDelete.getM_id();

				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection(url, username, password);

					int result;
					if ((messageToDelete.sender.equals(email) && messageToDelete.deletedBySender()) ||
					    (messageToDelete.receiver.equals(email) && messageToDelete.deletedByReceiver())){
						result = updateDatabase("DELETE FROM PrivateMessage WHERE m_id=" + m_idToDelete);
					} else {
						result = updateDatabase("UPDATE PrivateMessage SET sender_copy_delete=1 WHERE m_id=" + m_idToDelete);
					}

					if (result == 1) System.out.println("Message successfully deleted");
					con.close();
				} catch (Exception e){
					System.out.println("deletePrivateMessages(" + email + ") ERROR");
				}

			}
			else if (response.equals("0")){
				return;
			}

			privateMessages = getPrivateConversation(email);
		}
	}

	// overload to delete from specific conversation
	private static void deletePrivateMessage(String friendEmail){
		ArrayList<PrivateMessage> privateMessages = getPrivateConversation(friendEmail);
		if (privateMessages.isEmpty()){
			System.out.println("This conversation is empty.");
			return;
		}
		
		int i = 1;
		int viewingMessagesUpTo = 7;
		int length = privateMessages.size();
		for (PrivateMessage pm: privateMessages){
			if (i <= viewingMessagesUpTo) {
				System.out.println("    (" + i + ") " + pm.toString());
				i++;
			} else if (length > viewingMessagesUpTo){
				System.out.println("Type 'more' to view more or 'done' to continue.");
				String response = scanner.nextLine();
				while (!response.toLowerCase().equals("more") && !response.toLowerCase().equals("done") && !response.equals("0")){
					System.out.println("Please enter 'more' to view more, 'done' to continue or 0 to return to the main menu.");
					response = scanner.nextLine();
				}
				boolean isDone = false;
				switch (response.toLowerCase()) {
					case "0":
						return;
					case "more":
						viewingMessagesUpTo += 7;
						break;
					case "done":
						isDone = false;
						break;
				}
				if (isDone) break;
			}
		}

		System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");

		int indexInput = Integer.parseInt(scanner.nextLine());
		if (indexInput == 0) return;
		int indexToDelete = indexInput - 1;
		PrivateMessage messageToDelete = privateMessages.get(indexToDelete);
		int m_idToDelete = messageToDelete.getM_id();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);

			int result;
			if ((messageToDelete.sender.equals(friendEmail) && messageToDelete.deletedBySender()) ||
			    (messageToDelete.receiver.equals(friendEmail) && messageToDelete.deletedByReceiver())){
				result = updateDatabase("DELETE FROM PrivateMessage WHERE m_id=" + m_idToDelete);
			} else {
				result = updateDatabase("UPDATE PrivateMessage SET sender_copy_delete=1 WHERE m_id=" + m_idToDelete);
			}

			if (result == 1) System.out.println("Message successfully deleted");
			con.close();
		} catch (Exception e){
			System.out.println("deletePrivateMessages(" + friendEmail + ") ERROR");
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

		//TODO MAYBE get only 7 most recent and scroll
	}

	private static void enterChatGroup(){
		System.out.println("Enter ChatGroup name:");
		String gname = scanner.nextLine();

		ArrayList<String> chatGroups = getMyChatGroups();

		// make sure user in CGroup
		while (!chatGroups.contains(gname)){
			System.out.println("Could not find ChatGroup or you are not a member. Please enter another group name:");
			gname = scanner.nextLine();
			if (gname.equals("0")) return;
		}

		ArrayList<ChatGroupMessage> chatGroupMessages = getChatGroupConversation(gname);

		while (true) {
		
			// print recent messages 7 at a time
			System.out.println(gname.toUpperCase());
			int i = 1;
			int viewingMessagesUpTo = 7;
			int length = chatGroupMessages.size();
			for (ChatGroupMessage gm: chatGroupMessages){
				if (i <= viewingMessagesUpTo) {
					System.out.println("    (" + i + ") " + gm.toString());
					i++;
				} else if (length > viewingMessagesUpTo){
					System.out.println("Type 'more' to view more or 'done' to continue.");
					String response = scanner.nextLine();
					while (!response.toLowerCase().equals("more") && !response.toLowerCase().equals("done") && !response.equals("0")){
						System.out.println("Please enter 'more' to view more, 'done' to continue or 0 to return to the main menu.");
						response = scanner.nextLine();
					}
					boolean isDone = false;
					switch (response.toLowerCase()) {
						case "0":
							return;
						case "more":
							viewingMessagesUpTo += 7;
							break;
						case "done":
							isDone = false;
							break;
					}
					if (isDone) break;
				}
			}

			// prompt user for action
			System.out.println("To post a message, enter 'post'.");
			System.out.println("To delete a message, enter 'delete'.");
			System.out.println("To modify chatGroup properties, enter 'modify'.");
			System.out.println("To invite a friend, enter 'invite'. (Enter 0 to return to the main menu.)");

			String response = scanner.nextLine();
			while (!response.toLowerCase().equals("post") && !response.toLowerCase().equals("invite") && !response.toLowerCase().equals("delete") && !response.toLowerCase().equals("modify") && !response.equals("0")){
				System.out.println("Invalid input. Please enter 'post' to post, 'invite' to invite a friend, 'modify' to modify ChatGroup properties, 'delete' to delete a message, or 0 to return to the main menu.");
				response = scanner.nextLine();
			}

			if (response.toLowerCase().equals("post")){
				postChatGroupMessage(gname);
			}
			else if (response.toLowerCase().equals("delete")){
				deleteChatGroupMessage(gname);
			}
			else if (response.toLowerCase().equals("modify")){
				modifyChatGroup(gname);
			}
			else if (response.toLowerCase().equals("invite")){
				System.out.println("Please enter the email of the friend you want to invite. (Enter 0 to return to the main menu.)");

				String email =(scanner.nextLine());
				if (email.equals("0")) return;
				ArrayList<String> friends = getFriends();
				while (!friends.contains(email)){
					System.out.println("User not found in friends, enter another email or 0 to return to main menu");
					email = scanner.nextLine();
					if (email.equals("0")) return;
				}

				if (getChatGroupMembers(gname).contains(email)){
					System.out.println("User already in ChatGroup\n");
					return;
				}


				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection(url, username, password);

					int result = updateDatabase("INSERT INTO ChatGroupInvite (email, gname) VALUES ('" + email + "','" + gname + "')");

					if (result == 1) System.out.println("User invited");
					con.close();
				} catch (Exception e){
					System.out.println("invite to chat group ERROR");
				}

			}
			else if (response.equals("0")){
				return;
			}

			chatGroupMessages = getChatGroupConversation(gname);
		}

	}

	// overload to delete from specific conversation
	private static void deleteChatGroupMessage(String gname){
		ArrayList<ChatGroupMessage> chatGroupMessages = getChatGroupConversation(gname);
		if (chatGroupMessages.isEmpty()){
			return;
		}

		System.out.println(gname.toUpperCase());
		int i = 1;
		int viewingMessagesUpTo = 7;
		int length = chatGroupMessages.size();
		for (ChatGroupMessage gm: chatGroupMessages){
			if (i <= viewingMessagesUpTo) {
				System.out.println("    (" + i + ") " + gm.toString());
				i++;
			} else if (length > viewingMessagesUpTo){
				System.out.println("Type 'more' to view more or 'done' to continue.");
				String response = scanner.nextLine();
				while (!response.toLowerCase().equals("more") && !response.toLowerCase().equals("done") && !response.equals("0")){
					System.out.println("Please enter 'more' to view more, 'done' to continue or 0 to return to the main menu.");
					response = scanner.nextLine();
				}
				boolean isDone = false;
				switch (response.toLowerCase()) {
					case "0":
						return;
					case "more":
						viewingMessagesUpTo += 7;
						break;
					case "done":
						isDone = false;
						break;
				}
				if (isDone) break;
			}
		}

		System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");

		int indexInput = Integer.parseInt(scanner.nextLine());
		if (indexInput == 0) return;
		int indexToDelete = indexInput - 1;
		ChatGroupMessage messageToDelete = chatGroupMessages.get(indexToDelete);
		if (!messageToDelete.sender.equals(currentUser.getEmail())){
			System.out.println("Only the sender can delete a message.");
			return;
		}
		int m_idToDelete = messageToDelete.getM_id();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);
			int result = updateDatabase("DELETE FROM ChatGroupMessage WHERE m_id=" + m_idToDelete);
			if (result == 1) System.out.println("Message successfully deleted");
			con.close();
		} catch (Exception e){
			System.out.println("deleteChatGroupMessages(" + gname + ") ERROR");
		}

	}

	// view chat group invites
	private static void viewChatGroupInvites(){

		ArrayList<String> invites = getMyChatGroupInvites();
		if (invites.isEmpty()){
			System.out.println("you have no invites");
			return;
		}
		int i = 1;
		for (String gname: invites){
			System.out.println("("+ i +") invited to " + gname);
			i++;
		}
		System.out.println("enter a number to accept or decline invite (0 to return to main menu)");
		String indexString = scanner.nextLine();
		while (indexString.equals("")){
			indexString = scanner.nextLine();
		}
		int inputIndex = -1;
		try{
			inputIndex = Integer.parseInt(indexString);
		} catch (Exception e){

		}
		int indexToAcceptorDecline = inputIndex -1;
		if(inputIndex == 0) return;
		System.out.println("Enter 'decline' to decline request, enter 'accept' to accept");
		String response = scanner.nextLine();
		while (response.equals("")){
			response = scanner.nextLine();
		}
		if(!response.equals("decline")&& !response.equals("accept")) return;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);
			int result = updateDatabase("DELETE FROM ChatGroupInvite WHERE gname='" + invites.get(indexToAcceptorDecline) + "' AND email='" + currentUser.getEmail() + "'" );
			if (result == 1) System.out.println("ChatGroup invite declined.");
			con.close();
		} catch (Exception e){
			System.out.println("deleteCustomMessages ERROR");
		}
		if(response.equals("accept")){
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection(url, username, password);
				int result = updateDatabase("INSERT INTO Group_Member (gname, email) VALUES ('" + invites.get(indexToAcceptorDecline) + "','" + currentUser.getEmail() + "')" );

				con.close();
			} catch (Exception e){
				System.out.println("deleteCustomMessages ERROR");
			}

		}
		System.out.println("Successfully " + response + "ed invite");

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

		//TODO MAYBE get only 7 most recent and scroll
	}

	private static void enterMyCircle(){
		ArrayList<MyCircleMessage> myCircleMessages;

		while (true) {

			myCircleMessages = getMyCircleMessages();
		
			// print recent messages 7 at a time
			int i = 1;
			int viewingMessagesUpTo = 7;
			int length = myCircleMessages.size();
			for (MyCircleMessage pm: myCircleMessages){
				if (i <= viewingMessagesUpTo) {
					System.out.println("    (" + i + ") " + pm.toString());
					i++;
				} else if (length > viewingMessagesUpTo){
					System.out.println("Type 'more' to view more or 'done' to continue.");
					String response = scanner.nextLine();
					while (!response.toLowerCase().equals("more") && !response.toLowerCase().equals("done") && !response.equals("0")){
						System.out.println("Please enter 'more' to view more, 'done' to continue or 0 to return to the main menu.");
						response = scanner.nextLine();
					}
					boolean isDone = false;
					switch (response.toLowerCase()) {
						case "0":
							return;
						case "more":
							viewingMessagesUpTo += 7;
							break;
						case "done":
							isDone = false;
							break;
					}
					if (isDone) break;
				}
			}

			// prompt user for action
			System.out.println("To post a new MyCircleMessage, enter 'post'.");
			if (!myCircleMessages.isEmpty()) System.out.println("To delete a message, enter 'delete'. (Enter 0 to return to the main menu.)");
			System.out.println("To return to the main menu, enter 0");

			String response = scanner.nextLine();

			while (!response.toLowerCase().equals("post") && !response.toLowerCase().equals("delete") && !response.equals("0")){
				String invalidInputPrompt = "Invalid input. Please enter 'reply' to reply, ";
					if (!myCircleMessages.isEmpty()) invalidInputPrompt += "'delete' to delete a message, ";
					invalidInputPrompt += "or 0 to return to the main menu.";
					System.out.println(invalidInputPrompt);
					response = scanner.nextLine();
			}

			if (response.toLowerCase().equals("post")){
				postMyCircleMessage();
			}
			else if (response.toLowerCase().equals("delete") && !myCircleMessages.isEmpty()){

				System.out.println("Enter the corresponding number to the message you want to delete. (Enter 0 to return to the main menu.)");
			
				int indexInput = Integer.parseInt(scanner.nextLine());
				if (indexInput == 0) return;
				int indexToDelete = indexInput - 1;
				MyCircleMessage messageToDelete = myCircleMessages.get(indexToDelete);
				int m_idToDelete = messageToDelete.getM_id();

				if (!messageToDelete.sender.equals(currentUser.getEmail())){
					System.out.println("Only the sender can delete a message.");
					return;
				}
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection(url, username, password);

					int result = updateDatabase("DELETE FROM CustomMessage WHERE m_id=" + m_idToDelete);
					result += updateDatabase("DELETE FROM BroadcastMessage WHERE m_id=" + m_idToDelete);

					if (result >= 1) System.out.println("Message successfully deleted");
					con.close();
				} catch (Exception e){
					System.out.println("deleteMyCircleMessage ERROR");
				}
			}
			else if (response.equals("0")){
				return;
			}
			else {
				String invalidInputPrompt = "Invalid input. Please enter 'reply' to reply, ";
				if (!myCircleMessages.isEmpty()) invalidInputPrompt += "'delete' to delete a message, ";
				invalidInputPrompt += "or 0 to return to the main menu.";
				System.out.println(invalidInputPrompt);
				response = scanner.nextLine();
			}

		}
	}

	private static void enterBuzMoFeed(){
		ArrayList<BroadcastMessage> publicMessages;

		publicMessages = getPublicFeed();
	
		// print recent messages 7 at a time
		int i = 1;
		int viewingMessagesUpTo = 7;
		int length = publicMessages.size();
		for (BroadcastMessage pm: publicMessages){
			if (i <= viewingMessagesUpTo) {
				System.out.println("    (" + i + ") " + pm.toString());
				i++;
			} else if (length > viewingMessagesUpTo){
				System.out.println("Type 'more' to view more or 'done' to continue.");
				String response = scanner.nextLine();
				while (!response.toLowerCase().equals("more") && !response.toLowerCase().equals("done") && !response.equals("0")){
					System.out.println("Please enter 'more' to view more, 'done' to continue or 0 to return to the main menu.");
					response = scanner.nextLine();
				}
				boolean isDone = false;
				switch (response.toLowerCase()) {
					case "0":
						return;
					case "more":
						viewingMessagesUpTo += 7;
						break;
					case "done":
						isDone = false;
						break;
				}
				if (isDone) break;
			}
		}

		System.out.println("What would you like to do? Enter\n    (1) to search feed by topic words\n    (2) to send friend request to a message author\n    (3) to search users by topic word\n    (0) to return to main menu");
		String response;
		int action = -1;
		while (action < 0 || action > 3){
			response = scanner.nextLine();
			try{
				action = Integer.parseInt(response);
			} catch (Exception e){
				System.out.println("Enter a number between 0 and 3");
			}
			
		}
		switch (action){
			case 1:
				searchFeedByTopic();
				break;
			case 2:
				System.out.println("Enter a corresponding number to send a friend request to the author  (or 0 to exit)");
				int indexInput = Integer.parseInt(scanner.nextLine());
				
				int indexToAdd = indexInput - 1;
				if (indexInput <= 0 || indexToAdd > length) return;
				String friendtoAdd = publicMessages.get(indexToAdd).sender;
				sendFriendRequest(friendtoAdd);
				break;
			case 3:
				searchUsers();
				break;
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

		//TODO MAYBE get only 7 most recent and scroll
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

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			int result = updateDatabase("INSERT INTO ChatGroup (gname, duration, owner) VALUES ('" + groupName + "'," + duration + ",'" + currentUser.getEmail() + "')");
			if (result == 1) {
				System.out.println("ChatGroup " + groupName + " created.");
				System.out.println();
			}
			con.close();
		} catch (Exception e) {
			System.out.println("createDatabase ERROR: " + e);
		}

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

	private static void modifyChatGroup(String gname){

		String owner = getChatGroupOwner(gname);

		if (!owner.equals(currentUser.getEmail())){
			System.out.println("Only the owner can modify a ChatGroup.");
			return;
		}

		String newName = "";
		int newDuration = -1;

		System.out.println("Would you like to change the name of this ChatGroup?(Y/N)");
		String changeGroup = scanner.nextLine();

		while (!changeGroup.toLowerCase().equals("y") && !changeGroup.toLowerCase().equals("n")){
			System.out.println("Could not understand input. Please input 'Y' if you want to change the name or 'N' otherwise.");
			changeGroup = scanner.nextLine();
		}
		if (changeGroup.toLowerCase().equals("y")){
			System.out.println("Enter new ChatGroup name:");
			newName = scanner.nextLine();
			
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
			newDuration = duration;
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);

			int result;
			if (newDuration > -1){
				result = updateDatabase("UPDATE ChatGroup SET duration=" + newDuration + " WHERE gname='" + gname + "'");
			} 
			if (!newName.equals("")){
				//result = updateDatabase("UPDATE ChatGroup SET gname='" + newName + "' WHERE gname='" + gname + "'");
			}

			con.close();
		} catch (Exception e){
			System.out.println("modify ERROR: " + e);
		}
		return;
		
	}
	
	private static void sendFriendRequest(){
		System.out.println("enter an email to send a friend request to");
		String email = scanner.nextLine();

		ArrayList<String> allUsers = getUserEmails();
		ArrayList<String> myFriends = getFriends();
		ArrayList<String> sentRequests = getSentRequests();
		ArrayList<String> receivedRequests = getReceivedRequests();

		if (!allUsers.contains(email)){
			System.out.println("This user does not exist");
			return;
		}
		else if (myFriends.contains(email)){
			System.out.println("You are already friends with this user");
			return;
		}
		else if (sentRequests.contains(email)){
			System.out.println("You have already sent this user a friend request");
			return;
		}
		else if (receivedRequests.contains(email)){
			System.out.println("This user has sent you a friend request. Would you like to accept at this time? (y/n)");
			String r = scanner.nextLine();

			while (!r.equals("0") && !r.toLowerCase().equals("n") && !r.toLowerCase().equals("y")){
				System.out.println("Please enter 'y' or 'n' or 0 to return to main menu");
				r = scanner.nextLine();
			}

			if (r.equals("0") || r.toLowerCase().equals("n")) return;

			if (r.toLowerCase().equals("y")){
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection(url, username, password);

					int result = updateDatabase("UPDATE Friendship SET is_pending=0 WHERE initiator='" + email + "' AND receiver='" + currentUser.getEmail() +"'");
				
					if (result == 1) {
						System.out.println("friend request accepted!");
					}
					con.close();
				} catch (Exception e){
					System.out.println("accept frequest ERROR: " + e);
				}
			}
			return;
		} else {
			// send request
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection(url, username, password);

				int result = updateDatabase("INSERT INTO Friendship (initiator, receiver, is_pending) VALUES ('" + currentUser.getEmail() + "','" + email +"', 1)");
			
				if (result == 1) {
					System.out.println("friend request sent");
				}
				con.close();
			} catch (Exception e){
				System.out.println("sent fRequest ERROR: " + e);
			}
		}

		return;

	}

	private static void sendFriendRequest(String email){
		ArrayList<String> allUsers = getUserEmails();
		ArrayList<String> myFriends = getFriends();
		ArrayList<String> sentRequests = getSentRequests();
		ArrayList<String> receivedRequests = getReceivedRequests();


		if (myFriends.contains(email)){
			System.out.println("You are already friends with this user");
			return;
		}
		else if (sentRequests.contains(email)){
			System.out.println("You have already sent this user a friend request");
			return;
		}
		else if (receivedRequests.contains(email)){
			System.out.println("This user has sent you a friend request. Would you like to accept at this time? (y/n)");
			String r = scanner.nextLine();

			while (!r.equals("0") && !r.toLowerCase().equals("n") && !r.toLowerCase().equals("y")){
				System.out.println("Please enter 'y' or 'n' or 0 to return to main menu");
				r = scanner.nextLine();
			}

			if (r.equals("0") || r.toLowerCase().equals("n")) return;

			if (r.toLowerCase().equals("y")){
				try {
					Class.forName("oracle.jdbc.driver.OracleDriver");
					con = DriverManager.getConnection(url, username, password);

					int result = updateDatabase("UPDATE Friendship SET is_pending=0 WHERE initiator='" + email + "' AND receiver='" + currentUser.getEmail() +"'");
				
					if (result == 1) {
						System.out.println("friend request accepted!");
					}
					con.close();
				} catch (Exception e){
					System.out.println("accept frequest ERROR: " + e);
				}
			}
			return;
		} else {
			// send request
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				con = DriverManager.getConnection(url, username, password);

				int result = updateDatabase("INSERT INTO Friendship (initiator, receiver, is_pending) VALUES ('" + currentUser.getEmail() + "','" + email +"', 1)");
			
				if (result == 1) {
					System.out.println("friend request sent");
				}
				con.close();
			} catch (Exception e){
				System.out.println("sent fRequest ERROR: " + e);
			}
		}

		return;

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

	// Returns a list of friends' emails
	private static ArrayList<String> getSentRequests(){
		if (currentUser == null) return null;
		ArrayList<String> friends = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT UserProfile.email, UserProfile.name FROM UserProfile" +
			                             " JOIN Friendship ON UserProfile.email=Friendship.receiver" + 
			                             " WHERE is_pending=1 AND initiator='" + currentUser.getEmail() + "'");
			while(rs.next()){
				friends.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return friends;
	}

	// Returns a list of friends' emails
	private static ArrayList<String> getReceivedRequests(){
		if (currentUser == null) return null;
		ArrayList<String> friends = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT UserProfile.email, UserProfile.name FROM UserProfile" +
			                             " JOIN Friendship ON UserProfile.email=Friendship.initiator" + 
			                             " WHERE is_pending=1 AND receiver='" + currentUser.getEmail() + "'");
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

			ResultSet rs = queryDatabase("SELECT m_id, time, received_by, body, receiver_copy_delete FROM PrivateMessage WHERE sent_by='" + currentUser.getEmail() + "' AND sender_copy_delete=0");
			while(rs.next()){
				messages.add(new PrivateMessage(Integer.parseInt(rs.getString(1)), rs.getString(2), currentUser.getEmail(), rs.getString(3), rs.getString(4), false, (rs.getString(5) == "1" ? true : false)));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return messages;
	}

	// Returns a list of PrivateMessages in a conversation with friend
	private static ArrayList<PrivateMessage> getPrivateConversation(String friendEmail){
		ArrayList<PrivateMessage> messages = new ArrayList<PrivateMessage>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT m_id, time, sent_by, body, receiver_copy_delete AS deletedByFriend FROM PrivateMessage WHERE sent_by='" + currentUser.getEmail() + 
			                             "' AND received_by='" + friendEmail + "' AND sender_copy_delete!=1" +
			                             " UNION SELECT m_id, time, sent_by, body, sender_copy_delete AS deletedByFriend FROM PrivateMessage WHERE received_by='" + currentUser.getEmail() + 
			                             "' AND sent_by='" + friendEmail + "' AND receiver_copy_delete!=1" +
			                             " ORDER BY time DESC");
			while(rs.next()){
				boolean sentByMe = (rs.getString(3).equals(currentUser.getEmail()));
				boolean deletedByFriend = (rs.getString(5).equals("1"));
				messages.add(new PrivateMessage(Integer.parseInt(rs.getString(1)), rs.getString(2), rs.getString(3), 
				                                (sentByMe ? friendEmail : currentUser.getEmail()), rs.getString(4), 
				                                (sentByMe ? false : deletedByFriend), (sentByMe ? deletedByFriend : false)));
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

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			//TODO get timestamp

			ResultSet rs = queryDatabase("SELECT m_id, time, body, G.gname, G.duration FROM ChatGroupMessage M JOIN ChatGroup G ON M.gname=G.gname" +
			                             " WHERE sent_by='" + currentUser.getEmail() + "' AND time + G.duration >= TIMESTAMP '" + timestamp + "'");
			while(rs.next()){
				messages.add(new ChatGroupMessage(Integer.parseInt(rs.getString(1)), rs.getString(2), currentUser.getEmail(), rs.getString(3), rs.getString(4)));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return messages;
	}

	// Returns a list of my sent ChatGroupMessages
	private static ArrayList<ChatGroupMessage> getChatGroupConversation(String gname){
		ArrayList<ChatGroupMessage> messages = new ArrayList<ChatGroupMessage>();

		ArrayList<String> myChatGroups = getMyChatGroups();
		if (!myChatGroups.contains(gname)){
			System.out.println("You are not a member of this ChatGroup or it does not exist.");
			return messages;
		}

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			//TODO get timestamp

			ResultSet rs = queryDatabase("SELECT m_id, time, body, G.gname, G.duration FROM ChatGroupMessage M JOIN ChatGroup G ON M.gname=G.gname" + 
			                             " WHERE G.gname='" + gname + "' AND time + G.duration >= TIMESTAMP '" + timestamp + 
			                             "' ORDER BY time DESC");
			while(rs.next()){
				messages.add(new ChatGroupMessage(Integer.parseInt(rs.getString(1)), rs.getString(2), currentUser.getEmail(), rs.getString(3), rs.getString(4)));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		if (messages.isEmpty()){
			System.out.println("There are no recent messages in this ChatGroup.");
		}

		return messages;
	}

	// Return a list of messages in MyCircle
	private static ArrayList<MyCircleMessage> getMyCircleMessages(){
		ArrayList<MyCircleMessage> messages = new ArrayList<MyCircleMessage>();

		ArrayList<String> myFriends = getFriends();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			//TODO get timestamp

			// get my posted broadcast messages
			String sqlQuery = "SELECT m_id, sent_by, time, body FROM BroadcastMessage WHERE sent_by='" + currentUser.getEmail() + "'";
			// get my posted custom messages
			sqlQuery += " UNION SELECT m_id, sent_by, time, body FROM CustomMessage WHERE sent_by='" + currentUser.getEmail() + "'";
			for (String friend: myFriends) {
				// get my friends' broadcast messages
				sqlQuery += " UNION SELECT m_id, sent_by, time, body FROM BroadcastMessage WHERE sent_by='" + friend + "'";
				// get my friends' custom messages that I can see
				sqlQuery += " UNION SELECT V.m_id, M.sent_by, M.time, M.body FROM CustomMessage M JOIN Message_Viewer V ON V.m_id=M.m_id WHERE V.email='" + currentUser.getEmail() + 
				            "' AND M.sent_by='" + friend + "'";
			}

			//TODO get topics?

			// sort by time descending
			sqlQuery += " ORDER BY time DESC";

			ResultSet rs = queryDatabase(sqlQuery);

			while (rs.next()){
				int m_id = Integer.parseInt(rs.getString(1));

				ResultSet topicRs = queryDatabase("SELECT keyword FROM BroadcastMessage_Topic WHERE m_id=" + m_id + " UNION SELECT keyword FROM CustomMessage_Topic WHERE m_id=" + m_id);
				ArrayList<String> topics = new ArrayList<String>();
				while (topicRs.next()){
					topics.add(topicRs.getString(1));
				}
				messages.add(new MyCircleMessage(m_id, rs.getString(3), rs.getString(2), rs.getString(4), topics));
			}

			con.close();
		} catch (Exception e){
			System.out.println("getMyCircleMessages ERROR: " + e);
		}

		return messages;
		
	}

	// Return a list of public messages to browse
	private static ArrayList<BroadcastMessage> getPublicFeed(){
		ArrayList<BroadcastMessage> messages = new ArrayList<BroadcastMessage>();


		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			//TODO get timestamp

			// get my posted broadcast messages
			String sqlQuery = "SELECT m_id, sent_by, time, body FROM BroadcastMessage WHERE is_public='1' ORDER BY time DESC";
			ResultSet rs = queryDatabase(sqlQuery);

			while (rs.next()){
				int m_id = Integer.parseInt(rs.getString(1));

				// get all topics for each message
				ResultSet topicRs = queryDatabase("SELECT keyword FROM BroadcastMessage_Topic WHERE m_id=" + m_id);
				ArrayList<String> topics = new ArrayList<String>();
				while (topicRs.next()){
					topics.add(topicRs.getString(1));
				}

				// add this post to the list
				messages.add(new BroadcastMessage(m_id, rs.getString(3), rs.getString(2), rs.getString(4), topics, true));
			}

			con.close();
		} catch (Exception e){
			System.out.println("getPublicFeed ERROR: " + e);
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

	// Returns a list of ChatGroups' gnames currentUser is a member of
	private static ArrayList<String> getMyChatGroupInvites(){
		if (currentUser == null) return null;
		ArrayList<String> chatGroups = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT gname FROM ChatGroupInvite" +
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

	// Returns a list of members of ChatGroup with gname
	private static ArrayList<String> getChatGroupMembers(String gname){
		ArrayList<String> members = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT email FROM Group_Member" +
			                             " WHERE gname='" + gname + "'");
			while(rs.next()){
				members.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return members;
	}

	// Returns a list of members of ChatGroup with gname
	private static String getChatGroupOwner(String gname){
		String members = "";

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT owner FROM ChatGroup" +
			                             " WHERE gname='" + gname + "'");
			while(rs.next()){
				members = rs.getString(1);
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return members;
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

	private static ArrayList<String> getUserEmails(){
		ArrayList<String> users = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT email FROM UserProfile");

			while (rs.next()) {
				users.add(rs.getString(1));
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

	public static ArrayList<String> getMyTopics(){
		ArrayList<String> topicWords = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT keyword FROM UserProfile_Topic WHERE email='" + currentUser.getEmail()+ "'");
			while(rs.next()){
				topicWords.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return topicWords;
	}

	private static ArrayList<BroadcastMessage> searchFeedByTopic(){
		ArrayList<BroadcastMessage> result = new ArrayList<BroadcastMessage>();

		ArrayList<String> topics = new ArrayList<String>();
		System.out.println("Enter topic words to search by. Enter 'done' if done.");
		String response = scanner.nextLine();
		while (!response.toLowerCase().equals("done")){
			topics.add(response);
			System.out.println("Enter more topic words or enter 'done' if done");
			response = scanner.nextLine();
		}

		if (topics.isEmpty()){
			topics = getMyTopics();
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			//TODO get timestamp

			
			ArrayList<String> m_ids = new ArrayList<String>();

			for (String topic: topics){

				ResultSet messageRs = queryDatabase("SELECT m_id FROM BroadcastMessage_Topic WHERE keyword='" + topic + "'");
				while (messageRs.next()){
					m_ids.add(messageRs.getString(1));
				}

			}

			// get my posted broadcast messages
			for (String m_id: m_ids){
				String sqlQuery = "SELECT m_id, sent_by, time, body FROM BroadcastMessage WHERE is_public='1' AND m_id=" + m_id + " ORDER BY time DESC";
				ResultSet rs = queryDatabase(sqlQuery);

				while (rs.next()){
					int mid = Integer.parseInt(rs.getString(1));

					// get all topics for each message
					ResultSet topicRs = queryDatabase("SELECT keyword FROM BroadcastMessage_Topic WHERE m_id=" + mid);
					ArrayList<String> messageTopics = new ArrayList<String>();
					while (topicRs.next()){
						messageTopics.add(topicRs.getString(1));
					}

					// add this post to the list
					result.add(new BroadcastMessage(mid, rs.getString(3), rs.getString(2), rs.getString(4), messageTopics, true));
				}
			}
			

			con.close();
		} catch (Exception e){
			System.out.println("getPublicFeed ERROR: " + e);
		}

		int i = 1;
		for (BroadcastMessage message: result){
			System.out.println(" (" + i + ") " +message.toString());
			i++;
		}

		return result;
	}

	private static ArrayList<String> searchUsers(){
		ArrayList<String> result = new ArrayList<String>();

		ArrayList<String> topics = new ArrayList<String>();
		System.out.println("Enter topic words to search by. Enter 'done' if done.");
		String response = scanner.nextLine();
		while (!response.toLowerCase().equals("done")){
			topics.add(response);
			System.out.println("Enter more topic words or enter 'done' if done");
			response = scanner.nextLine();
		}

		if (topics.isEmpty()){
			topics = getMyTopics();
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);
			
			for (String topic: topics){

				ResultSet messageRs = queryDatabase("SELECT DISTINCT T.email, P.screenname, P.name FROM UserProfile_Topic T JOIN UserProfile P ON T.email=P.email WHERE T.keyword='" + topic + "'");
				while (messageRs.next()){
					String message = messageRs.getString(1) + ", " + messageRs.getString(2) + ", " + messageRs.getString(3);
					if (!result.contains(message)) result.add(message);
				}

			}

			// get my posted broadcast messages
			/*for (String m_id: m_ids){
				String sqlQuery = "SELECT email FROM UserProfile WHERE email='" + m_id + "'";
				ResultSet rs = queryDatabase(sqlQuery);

				while (rs.next()){
					int mid = Integer.parseInt(rs.getString(1));



					// add this post to the list
					result.add(new BroadcastMessage(mid, rs.getString(3), rs.getString(2), rs.getString(4), messageTopics, true));
				}
			}*/
			

			con.close();
		} catch (Exception e){
			System.out.println("searchUsers ERROR: " + e);
		}

		int i = 1;
		for (String email: result){
			System.out.println(email);
			i++;
		}

		return result;
	}

	private static User validate(String email, String userPassword){
		User user = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase("SELECT name, password, is_manager FROM UserProfile WHERE email='" + email + "'");
			while(rs.next()){
				if (userPassword.equals(rs.getString(2))){
					user = new User(rs.getString(1), email, userPassword, (rs.getString(3) == "1" ? true : false), new ArrayList<String>());
				}
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 
		
		return user;
	}

	private static User register(){
		System.out.println("What's your name?");
		String name = scanner.nextLine();
		while (name.length() > 20 || name.length() < 1){
			System.out.println("Name can be up to 20 characters:");
			name = scanner.nextLine();
		}

		System.out.println("Hello " + name + "! Please enter a 10 digit phone number:");
		String phone_num = scanner.nextLine();
		while (!phone_num.matches("\\A[0-9]{10}\\z")){
			if (phone_num.equals("0")) return null;
			System.out.println("Phone number must be 10 digits. (Enter 0 to return to main menu.)");
			phone_num = scanner.nextLine();
		}

		System.out.println("Enter your email (you will use this email to login with).");
		String email = scanner.nextLine();
		while (email.length() > 20 || email.length() < 2){
			System.out.println("Email can be no more than 20 characters:");
			email = scanner.nextLine();
		}

		System.out.println("Would you like to select an optional screenname? (y/n)");
		String response = scanner.nextLine();
		while(!response.toLowerCase().equals("y") && !response.toLowerCase().equals("n")){
			System.out.println("Please enter 'y' to choose a screenname or 'n' to skip (You can set later).");
			response = scanner.nextLine();
		}

		String screenname = "";
		while (response.toLowerCase().equals("y") && (screenname.length() <= 20 || screenname.length() > 0)){
			System.out.println("Enter a screenname of max 20 characters:");
			screenname = scanner.nextLine();
		}

		System.out.println("Choose a password between 2 and 10 characters:");
		String userPassword = scanner.nextLine();
		while (userPassword.length() < 2 || password.length() > 10){
			System.out.println("Password must be between 2 and 10 characters:");
			userPassword = scanner.nextLine();
		}

		// Ask for TopicWords
		ArrayList<String> topics = new ArrayList<String>();
		System.out.println("Enter some TopicWords to associate with yourself (Hit enter to skip, enter 0 to return to the main menu):");
		String topic = scanner.nextLine();
		if (topic.equals("0")) return null;
		while (!topic.toLowerCase().equals("done") && !topic.toLowerCase().equals("")){
			topics.add(topic.toLowerCase());
			System.out.println("Enter more TopicWords, or 'done' if you're done (enter 0 to return to main menu):");

			topic = scanner.nextLine();
			if (topic.equals("0")) return null;
		}

		User user = null;

		if (!screenname.equals("")) user = new User(name, email, userPassword, false, topics, screenname);
		else user = new User(name, email, userPassword, false, topics);

		ArrayList<String> existingTopicWords = getTopicWords();

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);

			int result;
			if (!screenname.equals("")) result = updateDatabase("INSERT INTO UserProfile (email, name, phone_num, password, screenname) VALUES ('" + email + "','" + name + "','" + phone_num + "','" + userPassword + "','" + screenname + "')");
			else result = updateDatabase("INSERT INTO UserProfile (email, name, phone_num, password) VALUES ('" + email + "','" + name + "','" + phone_num + "','" + userPassword + "')");

			if (result == 1) System.out.println("Congratulations! You have been successfully registered to BuzMo!");
			con.close();
		} catch (Exception e){
			System.out.println("register ERROR");
		}

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, username, password);

			int result;
			for (String keyword: topics){
				if (!existingTopicWords.contains(keyword)) result = updateDatabase("INSERT INTO TopicWord (keyword) VALUES ('" + keyword + "')");
				result = updateDatabase("INSERT INTO UserProfile_Topic (email, keyword) VALUES ('" + email + "','" + keyword + "')");
			}

			con.close();
		} catch (Exception e){
			System.out.println("register ERROR");
		}

		return user;
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

		/*String sqlQuery = "SELECT email FROM UserProfile MINUS SELECT DISTINCT email FROM PrivateMessage";
		sqlQuery += " MINUS SELECT DISTINCT email FROM BroadcastMessage";
		sqlQuery += " MINUS SELECT DISTINCT email FROM CustomMessage";
		sqlQuery += " MINUS SELECT DISTINCT email FROM ChatGroupMessage";*/

		ArrayList<String> allUsers = getUserEmails();

		/*for (String user: users){
			String userQuery = "SELECT COUNT(*) FROM PrivateMessage P,BroadcastMessage B,CustomMessage C,ChatGroupMessage G WHERE sent_by='" + user + "'";
			String userQuery = "SELECT (SELECT COUNT(*) FROM PrivateMessage WHERE sent_by='" + user + "') + (SELECT COUNT(*) FROM PrivateMessage WHERE sent_by='" + user + "') + (SELECT COUNT(*) FROM PrivateMessage WHERE sent_by='" + user + "') + (SELECT COUNT(*) FROM PrivateMessage WHERE sent_by='" + user + "') FROM DUAL;";
		}

		String sqlQuery = "SELECT COUNT(*) FROM PrivateMessage,BroadcastMessage,CustomMessage,ChatGroupMessage  GROUP BY email FROM UserProfile MINUS SELECT DISTINCT email FROM PrivateMessage";
		sqlQuery += " MINUS SELECT DISTINCT email FROM BroadcastMessage";
		sqlQuery += " MINUS SELECT DISTINCT email FROM CustomMessage";
		sqlQuery += " MINUS SELECT DISTINCT email FROM ChatGroupMessage";

		ArrayList<String> users = new ArrayList<String>();

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			con = DriverManager.getConnection(url,username, password);

			ResultSet rs = queryDatabase(sqlQuery);

			while (rs.next()) {
				users.add(rs.getString(1));
			}

			con.close();
		} catch (Exception e) {
			System.out.println("ERROR: " + e);
		} 

		return users.size();*/
		// ArrayList<
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
