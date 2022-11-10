drop schema if exists iss_food;

create schema iss_food;

use iss_food;

create table user_info(
    user_id int not null auto_increment,
    username varchar(64) not null,
    password varchar(500) not null,
    user_name varchar(64) not null,
    vegetarian boolean,
    start_date date,
    end_date date,
    user_type char(10) not null,

    primary key(user_id)
);

create table meals(
    meal_id char(8) not null,
    meal_date date not null,
    meal_time char(1) not null,
    vegetarian boolean not null,
    main varchar(500),
    drinks varchar(50),
    dessert varchar(100),
    remarks varchar(256),

    primary key(meal_id)
);

create table reviews(
    user_id int not null,
    post_id char(8) not null,
    meal_id char(8) not null,
    post_date date not null,
    rating tinyint,
    caption varchar(1000),
    pic varchar(500),

    primary key(post_id),
    constraint fk_reviews_user_id foreign key(user_id) references user_info(user_id),
    constraint fk_reviews_meal_id foreign key(meal_id) references meals(meal_id)
);

create table attendance(
    attendance_id int auto_increment,
    user_id int not null,
    meal_id char(8) not null,

    primary key(attendance_id),
    constraint fk_attendance_user_id foreign key(user_id) references user_info(user_id),
    constraint fk_attendance_meal_id foreign key(meal_id) references meals(meal_id)
);
