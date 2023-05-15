
INSERT INTO users (email, fecha_creacion, imagen_perfil, password, tipo, username)
VALUES ('sergio.alvarez.varela@udc.es', NOW(),'78798378.png' , '$2a$10$07ZB.3zlertxqi/H3o25VeB8mfCVt01qjQv/kLbj6mhdL/86A8DEC', 'ROLE_ADMIN', 'sergioalvarezvarela');


insert
into
    courses
(course_category, course_description, course_name, course_photo, course_price, creation_date, user_id)
values
    ('FORENSE', 'Curso de introducción a los conceptos básicos de forense', 'Introducción a Forense', 'image(49).png', 100.49, NOW(), 1);

insert
into
    modules
(course_id, module_date, module_name, module_position)
values
    (1, NOW(), '¿Qué es la informática Forense?', 1);

insert
into
    modules
(course_id, module_date, module_name, module_position)
values
    (1, NOW(), 'Tipos de informática Forense', 2);

insert
into
    teststring
(content_name, content_position, content_category, module_id, html)
values
    ('Contenido 1', 1, 'TEORIC', 1, 'Lore Ipsum', 1);

insert
into
    testquestion
(content_name, content_position, content_category, module_id, correct, option1, option2, option3, option4, question, content_id)
values
    ('Contenido 2', 2, 'SELECT', 1, 1, 'Opcion 1', 'Opcion 2', 'Opcion 3', 'Opcion 4', 'Pregunta para test', 10);

insert
into
    string_completes
(enunciado, sentence , correct_sentence , content, content_id, content_name,content_position, content_category, module_id)
values
    ('Enunciado', 'Frase _ para rellenar', 'Frase correcta para rellenar', 'correcta\nincorrecta', 33, 'Contenido 3',3,'PUZZLE',1);