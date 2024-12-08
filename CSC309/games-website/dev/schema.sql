drop table appuser cascade;
drop table guessgame cascade;
drop table pegsolitaire cascade;
drop table puzzlegame cascade;
drop table mastermind cascade;

create table appuser (
	userid varchar(20) primary key,
	password varchar(80),
	birthday varchar(10),
	favcolor varchar(10),
	year varchar(10),

	csc108 boolean,
	csc236 boolean,
	csc301 boolean,
	csc309 boolean,
	csc384 boolean,
	csc411 boolean,

	lecture varchar(10),

	signature varchar(100)
);

create table guessgame (
	userid varchar(20) primary key,
	attempts integer,
	time integer
);

create table pegsolitaire (
    userid varchar(20) primary key,
	pegsleft integer,
    time integer
);

create table puzzlegame (
    userid varchar(20) primary key,
    moves integer,
    time integer
);

create table mastermind (
    userid varchar(20) primary key,
    attempts integer,
    time integer
);

insert into appuser values (
	'auser',
	'apassword',
	'2000-01-01',
	'#ff0000',
	'1st Year',
	true, true, true, true, true, true,
	'101',
	'This is a signature'
);
