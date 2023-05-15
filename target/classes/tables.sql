

alter table content_users
drop
foreign key FKim0183q15eol9wyg2k1ff7je;

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

alter table module_users
drop
foreign key FKtolq176hl36x59htuc65wa4k;

alter table module_users
drop
foreign key FKdhl62qlp3jqqdmdccf87qjww;

alter table string_completes
drop
foreign key FK_87usiie886moctid7dg38gku;

alter table testquestion
drop
foreign key FK_hjqegrth1xjjanoci4vqxqn4;

alter table teststring
drop
foreign key FK_3gh0y5qbtkg0jpiuj4lxo1fh;

drop table if exists content_users;

drop table if exists courses;

drop table if exists hibernate_sequence;

drop table if exists inscriptions;

drop table if exists modules;

drop table if exists module_users;

drop table if exists string_completes;

drop table if exists testquestion;

drop table if exists teststring;

drop table if exists users;

create table content_users (
                               completed bit,
                               users_user_id bigint not null,
                               content_content_id bigint not null,
                               primary key (content_content_id, users_user_id)
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

create table module_users (
                              completed bit,
                              module_module_id bigint not null,
                              users_user_id bigint not null,
                              primary key (module_module_id, users_user_id)
) engine=InnoD;

create table string_completes (
                                  content_id bigint not null,
                                  content_name varchar(255) not null,
                                  content_position integer not null,
                                  content_category varchar(255) not null,
                                  module_id bigint not null,
                                  content varchar(255) not null,
                                  correct_sentence longtext not null,
                                  enunciado varchar(255) not null,
                                  sentence longtext not null,
                                  primary key (content_id)
) engine=InnoD;

create table testquestion (
                              content_id bigint not null,
                              content_name varchar(255) not null,
                              content_position integer not null,
                              content_category varchar(255) not null,
                              module_id bigint not null,
                              correct integer not null,
                              option1 varchar(255) not null,
                              option2 varchar(255) not null,
                              option3 varchar(255) not null,
                              option4 varchar(255) not null,
                              question varchar(255) not null,
                              primary key (content_id)
) engine=InnoD;

create table teststring (
                            content_id bigint not null,
                            content_name varchar(255) not null,
                            content_position integer not null,
                            content_category varchar(255) not null,
                            module_id bigint not null,
                            html longtext not null,
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

alter table content_users
    add constraint FKim0183q15eol9wyg2k1ff7je5
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

alter table module_users
    add constraint FKtolq176hl36x59htuc65wa4k5
        foreign key (module_module_id)
            references modules (module_id);

alter table module_users
    add constraint FKdhl62qlp3jqqdmdccf87qjwwd
        foreign key (users_user_id)
            references users (user_id);

alter table string_completes
    add constraint FK_87usiie886moctid7dg38gkut
        foreign key (module_id)
            references modules (module_id);

alter table testquestion
    add constraint FK_hjqegrth1xjjanoci4vqxqn49
        foreign key (module_id)
            references modules (module_id);

alter table teststring
    add constraint FK_3gh0y5qbtkg0jpiuj4lxo1fhc
        foreign key (module_id)
            references modules (module_id);