source beeterdb-schema.sql;

insert into users values('admin', MD5('admin'), 'Admin', 'admin@probook.com');
insert into user_roles values ('admin', 'administrator');

insert into users values('alex', MD5('alex'), 'Alex', 'alex@probook.com');
insert into user_roles values ('alex', 'registered');

insert into users values('ferni', MD5('ferni'), 'Ferni', 'ferni@probook.com');
insert into user_roles values ('ferni', 'registered');

insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) values ('Titulolibro1', 'Author1', 'Castellano', 43, '1998-1-1', '2000-1-1','Editorial1');
select sleep(1);insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) values ('Titulolibro2', 'Author2', 'Castellano', 43, '1998-1-1', '2000-1-1','Editorial2');
select sleep(1);insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) values ('Titulolibro3', 'Author3', 'Castellano', 43, '1998-1-1', '2000-1-1','Editorial3');
select sleep(1);insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) values ('Titulolibro4', 'Author4', 'Castellano', 43, '1998-1-1', '2000-1-1','Editorial4');
select sleep(1);insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) values ('Titulolibro5', 'Author5', 'Castellano', 43, '1998-1-1', '2000-1-1','Editorial5');
select sleep(1);insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) values ('Titulolibro6', 'Author6', 'Castellano', 43, '1998-1-1', '2000-1-1','Editorial6');

select sleep(1);insert into reviews (username, idbook, text) values ('alex', '1', 'Me apetece queso');
select sleep(1);insert into reviews (username, idbook, text) values ('ferni', '1', 'A mi tambien');
select sleep(1);insert into reviews (username, idbook, text) values ('alex', '2', 'Tienes tu mi queso');
select sleep(1);insert into reviews (username, idbook, text) values ('ferni', '2', 'Me lo he comido');
select sleep(1);insert into reviews (username, idbook, text) values ('alex', '3', 'Queso quesito queso');
select sleep(1);insert into reviews (username, idbook, text) values ('ferni', '3', 'Tralala');
select sleep(1);insert into reviews (username, idbook, text) values ('alex', '4', 'Tralali');
