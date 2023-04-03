
alter table contentusers
drop
foreign key FK3doiqpycj3ag5dp7l5lil43d;

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

alter table moduleusers
drop
foreign key FK10exki5c4nlg1fwmiw9phldb;

alter table moduleusers
drop
foreign key FKe5i03tgg1vgt3kejm1g5r2mh;

alter table testquestion
drop
foreign key FK_5v7rn7y10e97p4x2dnw4hnwm;

alter table teststring
drop
foreign key FK_jrdwsrdu9a0vnrv8oauv5f7u;

drop table if exists contentusers;

drop table if exists courses;

drop table if exists inscriptions;

drop table if exists modules;

drop table if exists moduleusers;

drop table if exists testoptions;

drop table if exists testquestion;

drop table if exists teststring;

drop table if exists users;

create table contentusers (
                              completed bit not null,
                              users_user_id bigint not null,
                              content_content_name varchar(255) not null,
                              content_course_id bigint not null,
                              content_module_name varchar(255) not null,
                              primary key (content_content_name, content_course_id, content_module_name, users_user_id)
) engine=InnoD;

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
                              completed bit not null,
                              users_user_id bigint not null,
                              courses_course_id bigint not null,
                              primary key (courses_course_id, users_user_id)
) engine=InnoD;

create table modules (
                         course_id bigint not null,
                         module_name varchar(255) not null,
                         module_date date not null,
                         module_position integer not null,
                         primary key (course_id, module_name)
) engine=InnoD;

create table moduleusers (
                             completed bit not null,
                             module_course_id bigint not null,
                             module_module_name varchar(255) not null,
                             users_user_id bigint not null,
                             primary key (module_course_id, module_module_name, users_user_id)
) engine=InnoD;

create table testoptions (
                             content_name varchar(255) not null,
                             course_id bigint not null,
                             module_name varchar(255) not null,
                             option_string varchar(255) not null,
                             primary key (content_name, course_id, module_name, option_string)
) engine=InnoD;

create table testquestion (
                              content_name varchar(255) not null,
                              course_id bigint not null,
                              module_name varchar(255) not null,
                              question varchar(255) not null,
                              primary key (content_name, course_id, module_name)
) engine=InnoD;

create table teststring (
                            content_name varchar(255) not null,
                            course_id bigint not null,
                            module_name varchar(255) not null,
                            enunciado varchar(255) not null,
                            markdown bit not null,
                            primary key (content_name, course_id, module_name)
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

alter table contentusers
    add constraint FK3doiqpycj3ag5dp7l5lil43dp
        foreign key (users_user_id)
            references users (user_id);

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

alter table moduleusers
    add constraint FK10exki5c4nlg1fwmiw9phldbo
        foreign key (module_course_id, module_module_name)
            references modules (course_id, module_name);

alter table moduleusers
    add constraint FKe5i03tgg1vgt3kejm1g5r2mhn
        foreign key (users_user_id)
            references users (user_id);

alter table testquestion
    add constraint FK_5v7rn7y10e97p4x2dnw4hnwmy
        foreign key (course_id, module_name)
            references modules (course_id, module_name);

alter table teststring
    add constraint FK_jrdwsrdu9a0vnrv8oauv5f7uy
        foreign key (course_id, module_name)
            references modules (course_id, module_name);