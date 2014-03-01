# --- !Ups

alter table transformer
add column category varchar(255);



# --- !Downs
alter table transformer
drop column category;
