DROP TABLE UserProfile;
CREATE TABLE UserProfile(
	email varchar(20),
	name varchar(20),
	phone_num char(10),
	password varchar(10) NOT NULL,
	screenname varchar(20),
	is_manager number(1,0),
	PRIMARY KEY(email),
	UNIQUE(screenname)
);

DROP TABLE Friendship;
CREATE TABLE Friendship(
	initiator varchar(20),
	receiver varchar(20),
	is_pending number(1,0),
	PRIMARY KEY(initiator, receiver),
	FOREIGN KEY(initiator) REFERENCES UserProfile(email) ON DELETE CASCADE,
	FOREIGN KEY(receiver) REFERENCES UserProfile(email) ON DELETE CASCADE,
	CHECK(initiator != receiver)
);

DROP TABLE TopicWord;
CREATE TABLE TopicWord(
	keyword varchar(4000),
	PRIMARY KEY(keyword)
);

DROP TABLE UserProfile_Topic;
CREATE TABLE UserProfile_Topic(
	email varchar(20),
	keyword varchar(4000),
	PRIMARY KEY(email, keyword),
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE,
	FOREIGN KEY(keyword) REFERENCES TopicWord
); 


DROP TABLE ChatGroup;
CREATE TABLE ChatGroup(
	gname varchar(20),
	duration integer DEFAULT 7,
	owner varchar(20) NOT NULL,
	PRIMARY KEY(gname),
	FOREIGN KEY(owner) REFERENCES UserProfile(email) ON DELETE CASCADE
);

DROP TABLE GroupMember;
CREATE TABLE GroupMember(
	email varchar(20),
	gname varchar(20),
	PRIMARY KEY(email, gname),
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE,
	FOREIGN KEY(gname) REFERENCES ChatGroup ON DELETE CASCADE
); 

DROP TABLE ChatGroupInvite;
CREATE TABLE ChatGroupInvite(
	email varchar(20),
	gname varchar(20),
	PRIMARY KEY(email, gname),
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE,
	FOREIGN KEY(gname) REFERENCES ChatGroup ON DELETE CASCADE
);

DROP TABLE ChatGroupMessage;
CREATE TABLE ChatGroupMessage(
	m_id integer,
	time timestamp NOT NULL,
	sent_by varchar(20) NOT NULL,
	body varchar(1400),
	gname varchar(20),
	PRIMARY KEY(m_id),
	FOREIGN KEY(sent_by) REFERENCES UserProfile(email) ON DELETE CASCADE,
	FOREIGN KEY(gname) REFERENCES ChatGroup ON DELETE CASCADE
);

DROP TABLE BroadcastMessage;
CREATE TABLE BroadcastMessage(
	m_id integer,
	time timestamp NOT NULL,
	sent_by varchar(20) NOT NULL,
	body varchar(1400),
	is_public number(1,0),
	read_count integer,
	PRIMARY KEY(m_id),
	FOREIGN KEY(sent_by) REFERENCES UserProfile(email) ON DELETE CASCADE
);

DROP TABLE PrivateMessage;
CREATE TABLE PrivateMessage(
	m_id integer,
	time timestamp NOT NULL,
	sent_by varchar(20) NOT NULL,
	received_by varchar(20) NOT NULL,
	body varchar(1400),
	sender_copy_delete number(1,0),
	receiver_copy_delete number(1,0),
	PRIMARY KEY(m_id),
	FOREIGN KEY(sent_by) REFERENCES UserProfile(email) ON DELETE CASCADE,
	FOREIGN KEY(received_by) REFERENCES UserProfile(email) ON DELETE CASCADE
);

DROP TABLE CustomMessage;
CREATE TABLE CustomMessage(
	m_id integer,
	time timestamp NOT NULL,
	sent_by varchar(20) NOT NULL,
	body varchar(1400),
	PRIMARY KEY(m_id),
	FOREIGN KEY(sent_by) REFERENCES UserProfile(email) ON DELETE CASCADE
);

DROP TABLE Message_Viewer;
CREATE TABLE Message_Viewer(
	m_id integer,
	email varchar(20),
	PRIMARY KEY(m_id, email),
	FOREIGN KEY(m_id) REFERENCES CustomMessage ON DELETE CASCADE,
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE
);

DROP TABLE Message_Topic;
CREATE TABLE Message_Topic(
	m_id integer,
	keyword varchar(4000),
	PRIMARY KEY(m_id, keyword),
	FOREIGN KEY(m_id) REFERENCES BroadcastMessage ON DELETE CASCADE,
	FOREIGN KEY(keyword) REFERENCES TopicWord
);