drop user 'library'@'localhost';
create user 'library'@'localhost' identified by 'library';
grant all privileges on librarydb.* to 'library'@'localhost';
flush privileges;
