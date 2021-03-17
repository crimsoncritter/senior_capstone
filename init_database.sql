create database if not exists cart_assist;

create table if not exists Customer(id serial primary key, first_name text, last_name text, email text, car_id int);
create table if not exists Employee(id serial primary key, first_name text, last_name text, email text,store_id int, working boolean);
create table if not exists Car(id serial primary key, make text, model text, location text);
create table if not exists Store(id serial primary key, carts_total int, carts_available int);
create table if not exists CartRequest(id serial primary key, user_email text, store_id int, car_id int, request_time timestamp);
