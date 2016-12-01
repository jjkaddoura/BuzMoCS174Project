DROP TRIGGER broadcastmessage_insert;
DROP TRIGGER chatgroupmessage_insert;
DROP TRIGGER privatemessage_insert;
DROP TRIGGER custommessage_insert;
DROP TRIGGER message_viewer_insert;
DROP TRIGGER broadcastmessage_topic_insert;
DROP TRIGGER custommessage_topic_insert;


DROP TABLE BroadcastMessage_Topic CASCADE CONSTRAINTS;
DROP TABLE CustomMessage_Topic CASCADE CONSTRAINTS;
DROP TABLE Message_Viewer CASCADE CONSTRAINTS;
DROP TABLE CustomMessage CASCADE CONSTRAINTS;
DROP TABLE PrivateMessage CASCADE CONSTRAINTS;
DROP TABLE BroadcastMessage CASCADE CONSTRAINTS;
DROP TABLE ChatGroupMessage CASCADE CONSTRAINTS;
DROP TABLE ChatGroupInvite CASCADE CONSTRAINTS;
DROP TABLE Group_Member CASCADE CONSTRAINTS;
DROP TABLE ChatGroup CASCADE CONSTRAINTS;
DROP TABLE UserProfile_Topic CASCADE CONSTRAINTS;
DROP TABLE TopicWord CASCADE CONSTRAINTS;
DROP TABLE Friendship CASCADE CONSTRAINTS;
DROP TABLE UserProfile CASCADE CONSTRAINTS;

DROP SEQUENCE message_id_seq;


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

CREATE TABLE Friendship(
	initiator varchar(20),
	receiver varchar(20),
	is_pending number(1,0),
	PRIMARY KEY(initiator, receiver),
	FOREIGN KEY(initiator) REFERENCES UserProfile(email) ON DELETE CASCADE,
	FOREIGN KEY(receiver) REFERENCES UserProfile(email) ON DELETE CASCADE,
	CHECK(initiator != receiver)
);

CREATE TABLE TopicWord(
	keyword varchar(4000),
	PRIMARY KEY(keyword)
);

CREATE TABLE UserProfile_Topic(
	email varchar(20),
	keyword varchar(4000),
	PRIMARY KEY(email, keyword),
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE,
	FOREIGN KEY(keyword) REFERENCES TopicWord
); 


CREATE TABLE ChatGroup(
	gname varchar(20),
	duration integer DEFAULT 7,
	owner varchar(20) NOT NULL,
	PRIMARY KEY(gname),
	FOREIGN KEY(owner) REFERENCES UserProfile(email) ON DELETE CASCADE
);

CREATE TABLE Group_Member(
	email varchar(20),
	gname varchar(20),
	PRIMARY KEY(email, gname),
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE,
	FOREIGN KEY(gname) REFERENCES ChatGroup ON DELETE CASCADE
); 

CREATE TABLE ChatGroupInvite(
	email varchar(20),
	gname varchar(20),
	PRIMARY KEY(email, gname),
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE,
	FOREIGN KEY(gname) REFERENCES ChatGroup ON DELETE CASCADE
);

CREATE SEQUENCE message_id_seq start with 1 increment by 1;

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

create or replace trigger chatgroupmessage_insert
before insert on ChatGroupMessage
for each row
begin
	select message_id_seq.nextval into :new.m_id from dual;
end;
/

CREATE TABLE BroadcastMessage(
	m_id integer,
	time timestamp NOT NULL,
	sent_by varchar(20) NOT NULL,
	body varchar(1400),
	is_public number(1,0),
	read_count integer DEFAULT 0,
	PRIMARY KEY(m_id),
	FOREIGN KEY(sent_by) REFERENCES UserProfile(email) ON DELETE CASCADE
);

create or replace trigger broadcastmessage_insert
before insert on BroadcastMessage
for each row
begin
	select message_id_seq.nextval into :new.m_id from dual;
end;
/

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

create or replace trigger privatemessage_insert
before insert on PrivateMessage
for each row
begin
	select message_id_seq.nextval into :new.m_id from dual;
end;
/

CREATE TABLE CustomMessage(
	m_id integer,
	time timestamp NOT NULL,
	sent_by varchar(20) NOT NULL,
	body varchar(1400),
	PRIMARY KEY(m_id),
	FOREIGN KEY(sent_by) REFERENCES UserProfile(email) ON DELETE CASCADE
);

create or replace trigger custommessage_insert
before insert on CustomMessage
for each row
begin
	select message_id_seq.nextval into :new.m_id from dual;
end;
/

CREATE TABLE CustomMessage_Topic(
	m_id integer,
	keyword varchar(4000),
	PRIMARY KEY(m_id, keyword),
	FOREIGN KEY(m_id) REFERENCES CustomMessage ON DELETE CASCADE,
	FOREIGN KEY(keyword) REFERENCES TopicWord
);

create or replace trigger custommessage_topic_insert
before insert on CustomMessage_Topic
for each row
begin
	select message_id_seq.currval into :new.m_id from dual;
end;
/

CREATE TABLE Message_Viewer(
	m_id integer,
	email varchar(20),
	PRIMARY KEY(m_id, email),
	FOREIGN KEY(m_id) REFERENCES CustomMessage ON DELETE CASCADE,
	FOREIGN KEY(email) REFERENCES UserProfile ON DELETE CASCADE
);

create or replace trigger message_viewer_insert
before insert on Message_Viewer
for each row
begin
	select message_id_seq.currval into :new.m_id from dual;
end;
/

CREATE TABLE BroadcastMessage_Topic(
	m_id integer,
	keyword varchar(4000),
	PRIMARY KEY(m_id, keyword),
	FOREIGN KEY(m_id) REFERENCES BroadcastMessage ON DELETE CASCADE,
	FOREIGN KEY(keyword) REFERENCES TopicWord
);

create or replace trigger broadcastmessage_topic_insert
before insert on BroadcastMessage_Topic
for each row
begin
	select message_id_seq.currval into :new.m_id from dual;
end;
/
