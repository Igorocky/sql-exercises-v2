drop table IF EXISTS Shape;

create table Shape (
    ID INT NOT NULL,
    KIND VARCHAR (50) NOT NULL,
    WIDTH DECIMAL(18,2),
    HEIGHT DECIMAL(18,2),
    RADIUS DECIMAL(18,2),
    PRIMARY KEY (ID)
);
