# --- !Ups
ALTER TABLE TRANSFORMER
ADD COLUMN version integer default 0;

# --- !Downs
ALTER TABLE TRANSFORMER
DROP COLUMN version;