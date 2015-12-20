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
loginName varchar(32),
password varchar(16),
tel varchar(14),
email varchar(32),
create_date datetime
);
insert into user(id,name, loginName, password, tel, email, create_date) values(1,'吾同树','jin', '123' ,'15102751852', 'jins321@gmail.com','2014-04-26 22:07:37');
insert into user(id,name, loginName, password, tel, email, create_date) values(2,'天仙姐姐','jie', '123' ,'15105201314', '5201314@qq.com','2014-04-26 22:07:37');

-- 菜单表
create table menu(
id int primary key auto_increment,
name varchar(32) not null,
price float not null,
state int not null
);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (1,'鱼香肉丝',12,1);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (2,'粉蒸排骨',15,2);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (3,'土豆回锅肉',15,3);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (4,'红烧肉',20,3);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (5,'韭菜炒鸡蛋',12,3);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (6,'青椒肉丝',12,1);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (7,'麻辣豆腐',15,1);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (8,'辣子鸡丁',15,1);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (9,'地三鲜',10,3);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (10,'剁椒鱼头',30,2);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (11,'毛豆牛肉',18,1);
INSERT INTO `menu` (`id`,`name`,`price`,`state`) VALUES (12,'千张肉丝',15,3);

-- 订单表
create table user_menu(
id int primary key auto_increment,
userid int not null,
menuid int not null,
state int not null,
order_date datetime not null,
create_date datetime not null
);
INSERT INTO `user_menu` (`userid`,`menuid`,`state`,`order_date`,`create_date`) VALUES (1,1,1,now(),'2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`,`menuid`,`state`,`order_date`,`create_date`) VALUES (1,3,1,SUBDATE(now(),INTERVAL 1 DAY),'2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`,`menuid`,`state`,`order_date`,`create_date`) VALUES (1,2,1,ADDDATE(now(),INTERVAL 1 DAY),'2014-04-29 00:09:20');
INSERT INTO `user_menu` (`userid`,`menuid`,`state`,`order_date`,`create_date`) VALUES (1,4,2,now(),'2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`,`menuid`,`state`,`order_date`,`create_date`) VALUES (1,5,2,SUBDATE(now(),INTERVAL 1 DAY),'2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`,`menuid`,`state`,`order_date`,`create_date`) VALUES (1,6,2,ADDDATE(now(),INTERVAL 1 DAY),'2014-04-29 00:09:20');

-- Room list
create table room(
id int primary key auto_increment,
name varchar(32) not null
);
insert into room(id,name) values(1,'5F Pantry');
insert into room(id,name) values(2,'5F Interview');
insert into room(id,name) values(3,'3F Pantry');
insert into room(id,name) values(4,'3F Big Room');

create table room_schedule(
id int primary key auto_increment,
subject varchar(256),
roomid int not null,
username varchar(50) not null,
password varchar(50) not null,
start datetime not null,
end datetime not null,
create_date datetime not null
);
INSERT INTO `room_schedule` (`subject`,`roomid`,`username`, `password`,`start`,`end`,`create_date`) 
VALUES ('subject db',1,'username db','password', '2015-12-22 10:38:23','2015-12-22 11:38:23','2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`,`roomid`,`username`, `password`,`start`,`end`,`create_date`) 
VALUES ('subject db2',1,'username db','password', ADDDATE(now(),INTERVAL "1 4" DAY_HOUR),ADDDATE(now(),INTERVAL "1 5" DAY_HOUR),'2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`,`roomid`,`username`, `password`,`start`,`end`,`create_date`) 
VALUES ('subject db3',1,'username db','password', ADDDATE(now(),INTERVAL "1 4" DAY_HOUR),ADDDATE(now(),INTERVAL "1 5" DAY_HOUR),'2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`,`roomid`,`username`, `password`,`start`,`end`,`create_date`) 
VALUES ('subject db',2,'username db','password', ADDDATE(now(),INTERVAL "1 4" DAY_HOUR),ADDDATE(now(),INTERVAL "1 5" DAY_HOUR),'2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`,`roomid`,`username`, `password`,`start`,`end`,`create_date`) 
VALUES ('subject db2',2,'username db','password', ADDDATE(now(),INTERVAL "1 4" DAY_HOUR),ADDDATE(now(),INTERVAL "1 5" DAY_HOUR),'2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`,`roomid`,`username`, `password`,`start`,`end`,`create_date`) 
VALUES ('subject db3',2,'username db','password', ADDDATE(now(),INTERVAL "1 4" DAY_HOUR),ADDDATE(now(),INTERVAL "1 5" DAY_HOUR),'2014-04-29 00:09:20');
