DROP TABLE IF EXISTS emp;
create table emp(
	id int NOT NULL auto_increment,
	age bigint(50),
	name varchar(50),
	PRIMARY KEY  (id)
)DEFAULT CHARSET=utf8;
 insert into emp(id,age,name) values(29,'2b');
 insert into emp(age,name) values(25,'sb');
 
 select *from emp;
 