
CREATE DATABASE IF NOT EXISTS `rhythm`;

USE rhythm;

SET NAMES utf8;

DROP TABLE IF EXISTS tab_user;
DROP TABLE IF EXISTS tab_team;
DROP TABLE IF EXISTS tab_members;
-- 创建用户信息表
CREATE TABLE tab_user(
	uid		INT NOT NULL AUTO_INCREMENT,
	email		VARCHAR(100) NOT NULL,
	PASSWORD 	VARCHAR(32) NOT NULL DEFAULT 123456,
	nickName	VARCHAR(100),
	sex		VARCHAR(10),
	birthday	VARCHAR(20),
	address		VARCHAR(200),
	school		VARCHAR(100),
	workUnit	VARCHAR(100),
	PRIMARY KEY (uid)
);
-- 创建队伍表
CREATE TABLE tab_team(
	tid		INT NOT NULL AUTO_INCREMENT,
	uid		INT NOT NULL,
	teamName	VARCHAR(100),
	TYPE 		VARCHAR(10),
	frequency	VARCHAR(10),
	beginDate	VARCHAR(30),
	endDate		VARCHAR(30),
	startTime	VARCHAR(30),
	endTime		VARCHAR(30),
	synopsis	VARCHAR(200),
	PRIMARY KEY (tid),
	CONSTRAINT team_uid FOREIGN KEY (uid) REFERENCES tab_user(uid)
);
-- 创建队伍成员表
CREATE TABLE tab_members(
	MID 		INT NOT NULL AUTO_INCREMENT,
	uid		INT NOT NULL,
	tid		INT NOT NULL,
	member		VARCHAR(32),
	punchTime	DATETIME,
	punch 		INT NOT NULL DEFAULT 0,
	PRIMARY KEY (MID),
	CONSTRAINT members_uid FOREIGN KEY (uid) REFERENCES tab_user(uid),
	CONSTRAINT members_tid FOREIGN KEY (tid) REFERENCES tab_team(tid)
);
