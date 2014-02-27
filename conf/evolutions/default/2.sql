# --- !Ups

alter table transformer
add column tags varchar(255);



# --- !Downs
alter table transformer
drop column tags;
