--liquibase formatted sql

--changeset IlyaShchegolev:1
--comment first migration

create table test
(
    id integer,
    title varchar(50)
);

insert into test
values (1,'test'),
        (2,'test2');
--rollback truncate table test;