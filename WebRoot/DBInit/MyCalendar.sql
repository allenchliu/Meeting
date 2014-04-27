create database MyCalendar;

use MyCalendar;
drop table user;
drop table menu;
drop table user_menu;
drop table room;
drop table room_schedule;
-- 用户表
create table user(
id int primary key auto_increment,
name varchar(8) not null,
loginName varchar(32) not null,
password varchar(16) not null,
tel varchar(14) not null,
email varchar(32) not null,
create_date datetime not null
);
insert into user(name, loginName, password, tel, email, create_date) values('吾同树','jin', '123' ,'15102751852', 'jins321@gmail.com','2014-04-26 22:07:37');

-- 菜单表
create table menu(
id int primary key auto_increment,
name varchar(32) not null,
price float not null,
state int not null
);
insert into menu(name, price, state) values('鱼香肉丝', 12, 1);
insert into menu(name, price, state) values('粉蒸排骨', 15, 2);
insert into menu(name, price, state) values('土豆回锅肉', 15, 3);

-- 订单表
create table user_menu(
id int primary key auto_increment,
userid int not null,
menuid int not null,
state int not null,
order_date datetime not null,
create_date datetime not null
);

-- 会议室表
create table room(
id int primary key auto_increment,
name varchar(32) not null
);
insert into room(name) values('会议室一');
insert into room(name) values('会议室二');

create table room_schedule(
id int primary key auto_increment,
subject varchar(256) not null,
roomid int not null,
userid int not null,
start datetime not null,
end datetime not null,
create_date datetime not null
);