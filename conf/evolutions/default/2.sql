# --- !Ups

ALTER TABLE TRANSFORMER
ADD COLUMN version integer default 1;

# --- !Downs
ALTER TABLE TRANSFORMER
DROP COLUMN version;