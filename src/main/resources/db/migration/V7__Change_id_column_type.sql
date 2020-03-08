alter table QUESTION alter column ID bigint default not null;
alter table QUESTION alter column CREATOR bigint default not null;
alter table USER alter column ID bigint default not null;
alter table COMMENT alter column COMMENTTATOR bigint default not null;