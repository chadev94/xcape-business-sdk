-- hanseong 2023-05-18
alter table merchant
    drop foreign key FKcopwnv5whb6i951mxkent7nx0

drop table account;

CREATE TABLE account
(
    account_id   BIGINT       NOT NULL,
    username     VARCHAR(255) NULL,
    password     VARCHAR(255) NULL,
    account_role VARCHAR(255) NULL,
    CONSTRAINT pk_account PRIMARY KEY (account_id)
);

