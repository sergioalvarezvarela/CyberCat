

alter table courses
drop
foreign key FK51k53m6m5gi9n91fnlxkxgpm;

alter table inscriptions
drop
foreign key FKpn93uheuheyahivnb70qvebp;

alter table inscriptions
drop
foreign key FK1vjrmgbufa849ni4pgcjqnl5;

alter table modules
drop
foreign key FK8qnnp812q1jd38fx7mxrhpw;

alter table moduleusers
drop
foreign key FK6mbr29euhr942j9a5immd6et;

alter table moduleusers
drop
foreign key FKkscmo15sak617txsww1v3gm7;

drop table if exists courses;

drop table if exists inscriptions;

drop table if exists modules;

drop table if exists moduleusers;

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

create table inscriptions (
                              inscriptionid bigint not null auto_increment,
                              completed bit not null,
                              course_id bigint not null,
                              user_id bigint not null,
                              primary key (inscriptionid)
) engine=InnoD;

create table modules (
                         module_id bigint not null auto_increment,
                         module_date date not null,
                         module_name varchar(255) not null,
                         course_id bigint not null,
                         primary key (module_id)
) engine=InnoD;

create table moduleusers (
                             id bigint not null auto_increment,
                             completed bit not null,
                             module_id bigint,
                             user_id bigint,
                             primary key (id)
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
    add constraint FKpn93uheuheyahivnb70qvebpl
        foreign key (course_id)
            references courses (course_id);

alter table inscriptions
    add constraint FK1vjrmgbufa849ni4pgcjqnl5d
        foreign key (user_id)
            references users (user_id);

alter table modules
    add constraint FK8qnnp812q1jd38fx7mxrhpw9
        foreign key (course_id)
            references courses (course_id);

alter table moduleusers
    add constraint FK6mbr29euhr942j9a5immd6etd
        foreign key (module_id)
            references modules (module_id);

alter table moduleusers
    add constraint FKkscmo15sak617txsww1v3gm73
        foreign key (user_id)
            references users (user_id);