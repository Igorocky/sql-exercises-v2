drop table IF EXISTS Employee;

create table Employee (
    ID INT NOT NULL,
       FIRST_NAME VARCHAR (50)     NOT NULL,
       LAST_NAME VARCHAR (50)     NOT NULL,
       SALARY   DECIMAL (18,2) NOT NULL,
       PRIMARY KEY (ID)
);
