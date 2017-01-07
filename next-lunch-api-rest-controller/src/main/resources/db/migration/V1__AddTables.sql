
create table RESTAURANTS (
	ID int generated by default as identity, 
	NAME varchar(60) not null,
	primary key (ID)
);

create table USERS(
	ID int generated by default as identity, 
	NAME varchar(60) not null,
	primary key (ID)
);

create table VOTE(
	ID int generated by default as identity,
	RESTAURANT_ID int,
	USER_ID int,
	DAY date,
	primary key (ID),
	constraint VOTE_UNIQUE unique (RESTAURANT_ID, USER_ID)
);

alter table VOTE add constraint FK_RESTAURANT foreign key (RESTAURANT_ID) references RESTAURANTS;
alter table VOTE add constraint FK_USER foreign key (USER_ID) references USERS;

insert into RESTAURANTS (NAME) values('A Day Latte');
insert into RESTAURANTS (NAME) values('Espressia');
insert into RESTAURANTS (NAME) values('Far Away Feast');
insert into RESTAURANTS (NAME) values('Fringe Factory');
insert into RESTAURANTS (NAME) values('Hat & Goiter');
insert into RESTAURANTS (NAME) values('Hot Dogma');
insert into RESTAURANTS (NAME) values('Kettlepop');
insert into RESTAURANTS (NAME) values('Kinetica');
insert into RESTAURANTS (NAME) values('King Chard');

insert into USERS (NAME) values ('Lidiane da Conceição');
insert into USERS (NAME) values ('Leandro Cunha');
insert into USERS (NAME) values ('Luana Lima Oliveira');
insert into USERS (NAME) values ('Antonio Amaro da Silva');
insert into USERS (NAME) values ('Antonio Ferreira Santana');
insert into USERS (NAME) values ('Adriana Marques Fernandes');
insert into USERS (NAME) values ('Alice Avelar Gonçalves');
insert into USERS (NAME) values ('Graziela Araujo');
insert into USERS (NAME) values ('Inês Assis dos Anjos');