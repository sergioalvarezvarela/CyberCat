drop table if exists users;

create table users (
                       user_id bigint not null auto_increment,
                       email varchar(255) not null,
                       fecha_creacion datetime not null,
                       imagen_perfil varchar(255) not null,
                       password varchar(255) not null,
                       tipo bit not null,
                       username varchar(255) not null,
                       primary key (user_id)
) engine=InnoDB;

alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);