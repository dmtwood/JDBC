set names utf8mb4;
set charset utf8mb4;

drop database if exists familie;
create database familie charset utf8mb4;

use familie;

create table personen (
  id int unsigned not null auto_increment primary key,
  voornaam varchar(50) NOT NULL,
  papaid int unsigned,
  mamaid int unsigned,
  vermogen decimal(10,2) default 0,
  constraint personenPersonenPapa FOREIGN KEY (papaid) REFERENCES personen(id),
  constraint personenPersonenMama FOREIGN KEY (mamaid) REFERENCES personen(id)  
);

insert into personen(voornaam, vermogen) 
values ('Hans', 900),
('Alexandra', 600);

insert into personen(voornaam, papaid, mamaid) 
values ('Aeneas',1,2),
('Alissia',1,2),
('Anaïs',1,2);






create user if not exists cursist identified by 'cursist';
grant select, insert, update, delete on personen to cursist;