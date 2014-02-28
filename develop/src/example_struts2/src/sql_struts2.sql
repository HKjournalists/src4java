drop table if exists t_project;

create table t_project(
	id int not null auto_increment primary key,
	name varchar(50) not null,
	start_date date not null,
	end_date date not null
)ENGINE=InnoDB;

INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000001','2008-01-02','2008-01-15'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000002','2008-02-01','2008-02-20'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000003','2008-03-01','2008-03-08'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000004','2008-03-08','2008-03-10');
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000005','2008-01-02','2008-01-15'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000006','2008-02-01','2008-02-20'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000007','2008-03-01','2008-03-08'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000008','2008-03-08','2008-03-10');
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000009','2008-01-02','2008-01-15'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000010','2008-02-01','2008-02-20');
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000012','2008-03-08','2008-03-10'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000013','2008-01-02','2008-01-15'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000014','2008-02-01','2008-02-20'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000015','2008-03-01','2008-03-08'); 
INSERT INTO t_project (name,start_date,end_date) VALUES ('pm000016','2008-03-08','2008-03-10');

select * from t_project;

