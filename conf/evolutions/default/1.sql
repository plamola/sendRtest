# --- !Ups

create table transformer (
  id                        bigint not null,
  name                      varchar(255) not null,
  webservice_char_set       varchar(255),
  webservice_url            varchar(255),
  webservice_user           varchar(255),
  webservice_password       varchar(255),
  nr_of_requests            long,
  webservice_timeout        integer,
  version                   integer default 0,
  category                  varchar(255),
  webservice_template       clob,
  constraint uq_transformer_name unique (name),
  constraint pk_transformer primary key (id))
;


create sequence transformer_seq;



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists transformer;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists transformer_seq;


