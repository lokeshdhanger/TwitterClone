create database if not exists twitter;

use twitter;

create table users (
	id int not null auto_increment primary key,
	username varchar(255) not null,
	email varchar(255) not null,
	password varchar(255) not null,
	terms_and_condition_acccept boolean not null default false,
	created_by int not null default 1,
	created_at timestamp not null default now(),
	updated_by int not null default 1,
	updated_at timestamp not null default now()
);

create index users_index
	on users (username); 

create table posts (
	id int not null auto_increment primary key,
	post varchar(255) not null,
	user_id int not null,
	created_by int not null default 1,
	created_at timestamp not null default now(),
	updated_by int not null default 1,
	updated_at timestamp not null default now()
);

ALTER TABLE posts
ADD FOREIGN KEY (user_id) REFERENCES users(id);


create table post_likes (
	id int not null auto_increment primary key,
	post_id int not null,
	user_id int not null,
	is_liked boolean not null default false,
	created_by int not null default 1,
	created_at timestamp not null default now(),
	updated_by int not null default 1,
	updated_at timestamp not null default now()
);

ALTER TABLE post_likes
ADD FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE post_likes
ADD FOREIGN KEY (post_id) REFERENCES posts(id);

create table follows (
	id int not null auto_increment primary key,
	follower_user_id int not null,
	user_id int not null,
	is_followed boolean not null default false,
	created_by int not null default 1,
	created_at timestamp not null default now(),
	updated_by int not null default 1,
	updated_at timestamp not null default now()
);

ALTER TABLE follows
ADD FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE follows
ADD FOREIGN KEY (follower_user_id) REFERENCES users(id);

