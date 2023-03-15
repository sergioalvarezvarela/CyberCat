


alter table courses
drop
foreign key FK51k53m6m5gi9n91fnlxkxgpmv;


alter table inscriptions
drop
foreign key FKpn93uheuheyahivnb70qvebpl;


alter table inscriptions
drop
foreign key FK1vjrmgbufa849ni4pgcjqnl5d;


drop table if exists courses;


drop table if exists inscriptions;


drop table if exists users;


create table courses (
                         course_id bigint not null auto_increment,
                         course_category integer not null,
                         course_description varchar(255) not null,
                         course_name varchar(255) not null,
                         course_photo varchar(255) not null,
                         course_price float not null,
                         creation_date date not null,
                         user_id bigint,
                         primary key (course_id)
) engine=InnoDB;


create table inscriptions (
                              inscriptionid bigint not null auto_increment,
                              completed bit not null,
                              course_id bigint not null,
                              user_id bigint not null,
                              primary key (inscriptionid)
) engine=InnoDB;


create table users (
                       user_id bigint not null auto_increment,
                       email varchar(255) not null,
                       fecha_creacion datetime not null,
                       imagen_perfil varchar(255),
                       password varchar(255) not null,
                       tipo integer not null,
                       username varchar(255) not null,
                       primary key (user_id)
) engine=InnoDB;


alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);


alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);


alter table courses
    add constraint FK51k53m6m5gi9n91fnlxkxgpmv
        foreign key (user_id)
            references users (user_id);


alter table inscriptions
    add constraint FKpn93uheuheyahivnb70qvebpl
        foreign key (course_id)
            references courses (course_id);


alter table inscriptions
    add constraint FK1vjrmgbufa849ni4pgcjqnl5d
        foreign key (user_id)
            references users (user_id);