-- hanseong 2023-05-16
alter table account
    add account_type varchar(10) not null

alter table account
    change account_id account_seq bigint auto_increment;

alter table account
    add account_id varchar(100) not null after account_seq;

alter table account
    modify password varchar(255) not null;

alter table account
    modify username varchar(255) not null;

alter table account
    change username user_name varchar(255) not null;