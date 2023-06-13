create table if not exists persons (
    id serial primary key,
    login varchar(2000) NOT NULL unique,
    password varchar(2000) NOT NULL
);