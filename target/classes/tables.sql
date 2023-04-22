alter table courses
drop
foreign key FK51k53m6m5gi9n91fnlxkxgpm;

alter table inscriptions
drop
foreign key FKido0vx1jd93clgn7y09j9ehk;

alter table inscriptions
drop
foreign key FK1g65v8e022dbfmug8plgi2ba;

alter table modules
drop
foreign key FK8qnnp812q1jd38fx7mxrhpw;

alter table teststring
drop
foreign key FK_3gh0y5qbtkg0jpiuj4lxo1fh;

drop table if exists courses;

drop table if exists hibernate_sequence;

drop table if exists inscriptions;

drop table if exists modules;

drop table if exists teststring;

drop table if exists users;

create table courses (
                         course_id bigint not null auto_increment,
                         course_category varchar(255) not null,
                         course_description varchar(255) not null,
                         course_name varchar(255) not null,
                         course_photo varchar(255) not null,
                         course_price float not null,
                         creation_date date not null,
                         user_id bigint not null,
                         primary key (course_id)
) engine=InnoD;

create table hibernate_sequence (
    next_val bigint
) engine=InnoD;
    
    insert into hibernate_sequence values ( 1 ;

create table inscriptions (
                              completed bit not null,
                              users_user_id bigint not null,
                              courses_course_id bigint not null,
                              primary key (courses_course_id, users_user_id)
) engine=InnoD;

create table modules (
                         module_id bigint not null auto_increment,
                         module_date date not null,
                         module_name varchar(255) not null,
                         module_position integer not null,
                         course_id bigint not null,
                         primary key (module_id)
) engine=InnoD;

create table teststring (
                            content_id bigint not null,
                            content_name varchar(255) not null,
                            content_position integer not null,
                            module_id bigint not null,
                            enunciado varchar(255) not null,
                            markdown bit not null,
                            primary key (content_id)
) engine=InnoD;

create table users (
                       user_id bigint not null auto_increment,
                       email varchar(255) not null,
                       fecha_creacion datetime not null,
                       imagen_perfil varchar(255),
                       password varchar(255) not null,
                       tipo varchar(255) not null,
                       username varchar(255) not null,
                       primary key (user_id)
) engine=InnoD;

alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table users
    add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

alter table courses
    add constraint FK51k53m6m5gi9n91fnlxkxgpmv
        foreign key (user_id)
            references users (user_id);

alter table inscriptions
    add constraint FKido0vx1jd93clgn7y09j9ehkd
        foreign key (courses_course_id)
            references courses (course_id);

alter table inscriptions
    add constraint FK1g65v8e022dbfmug8plgi2baq
        foreign key (users_user_id)
            references users (user_id);

alter table modules
    add constraint FK8qnnp812q1jd38fx7mxrhpw9
        foreign key (course_id)
            references courses (course_id);

alter table teststring
    add constraint FK_3gh0y5qbtkg0jpiuj4lxo1fhc
        foreign key (module_id)
            references modules (module_id)