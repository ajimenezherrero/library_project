drop database if exists librarydb;
create database librarydb;
 
use librarydb;

create table book (
	idbook		int not null auto_increment primary key,
	title		varchar(255) not null,
	author		varchar(70) not null,
	lenguage	varchar(50) not null,
	edition		int not null,
	edition_date	date,
	print_date	date,
	editorial	varchar(50) not null
); 

create table users (
	username	varchar(20) not null primary key,
	userpass	char(32) not null,
	name		varchar(70) not null,
	email		varchar(255) not null
);

create table user_roles (
	username			varchar(20) not null,
	rolename 			varchar(20) not null,
	foreign key(username) references users(username) on delete cascade,
	primary key (username, rolename)
);
 
create table reviews (
	idreview		 int not null auto_increment,
	username	 	 varchar(20) not null,
	idbook		 	 int not null,
	text		 	 varchar(500) not null,
	date_review		 timestamp,
	constraint rel_username foreign key(username) references users(username),
	constraint rel_idbook foreign key(idbook) references book(idbook),
	primary key (username, idbook),
	index(idreview)
);
