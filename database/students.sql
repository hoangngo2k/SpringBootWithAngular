USE demoapp;

DROP TABLE IF EXISTS students;

CREATE TABLE student(
    id bigint primary key not null auto_increment,
    code varchar(25) not null unique,
    full_name varchar(60) not null,
    gender varchar(10) not null,
    grade varchar(15) not null,
    point double not null
)

insert into students (code, full_name, gender, grade, point) values ('16590-071', 'Anallise Maidstone', 'Female', 'A6', 9.4);
insert into students (code, full_name, gender, grade, point) values ('68428-150', 'Clement Bearblock', 'Male', 'A10', 6.0);
insert into students (code, full_name, gender, grade, point) values ('46672-228', 'Colas Krzyzowski', 'Male', 'A3', 2.4);
insert into students (code, full_name, gender, grade, point) values ('0363-0944', 'Elysee McFfaden', 'Female', 'A2', 7.0);
insert into students (code, full_name, gender, grade, point) values ('48951-2071', 'Evyn Enoksson', 'Male', 'A6', 8.1);
insert into students (code, full_name, gender, grade, point) values ('50593-003', 'Sansone Prine', 'Male', 'A8', 8.3);
insert into students (code, full_name, gender, grade, point) values ('53852-0007', 'Dorie Gorringe', 'Male', 'A2', 2.7);
insert into students (code, full_name, gender, grade, point) values ('51706-302', 'Chrissie Jurgensen', 'Male', 'A10', 1.0);
insert into students (code, full_name, gender, grade, point) values ('67510-0507', 'Tim Kleinholz', 'Female', 'A10', 6.1);
insert into students (code, full_name, gender, grade, point) values ('41250-018', 'Janeta Geekin', 'Female', 'A7', 9.9);
insert into students (code, full_name, gender, grade, point) values ('31722-730', 'Heidie Grumell', 'Female', 'A7', 5.7);
insert into students (code, full_name, gender, grade, point) values ('67544-237', 'Jillie McCaghan', 'Female', 'A9', 3.2);
insert into students (code, full_name, gender, grade, point) values ('21695-220', 'Dyan Stilliard', 'Female', 'A11', 3.8);
insert into students (code, full_name, gender, grade, point) values ('49852-017', 'Drucy Keay', 'Female', 'A8', 8.1);
insert into students (code, full_name, gender, grade, point) values ('49288-0062', 'Lena Toffolini', 'Female', 'A11', 2.9);
insert into students (code, full_name, gender, grade, point) values ('51345-052', 'Cobbie Gornal', 'Male', 'A4', 9.0);
insert into students (code, full_name, gender, grade, point) values ('53208-491', 'Ellsworth Snelman', 'Male', 'A3', 2.7);
insert into students (code, full_name, gender, grade, point) values ('43857-0057', 'Ezechiel Kindleside', 'Male', 'A9', 5.5);
insert into students (code, full_name, gender, grade, point) values ('64578-0089', 'Craggy Tring', 'Male', 'A12', 8.9);
insert into students (code, full_name, gender, grade, point) values ('10544-847', 'Darill Raatz', 'Male', 'A5', 5.9);
insert into students (code, full_name, gender, grade, point) values ('52544-496', 'Hieronymus Josefer', 'Male', 'A10', 9.1);
insert into students (code, full_name, gender, grade, point) values ('63629-5131', 'Jerrome Underwood', 'Male', 'A12', 9.7);
insert into students (code, full_name, gender, grade, point) values ('54868-0617', 'Charles Dillistone', 'Male', 'A7', 6.6);
insert into students (code, full_name, gender, grade, point) values ('0268-1169', 'Chrissie Heighton', 'Female', 'A11', 7.3);
insert into students (code, full_name, gender, grade, point) values ('54569-0505', 'Kelby Morefield', 'Male', 'A3', 3.0);
insert into students (code, full_name, gender, grade, point) values ('67691-769', 'Ardella Gruszka', 'Female', 'A4', 8.2);
insert into students (code, full_name, gender, grade, point) values ('68703-072', 'Giraldo Dunsford', 'Male', 'A1', 6.9);
insert into students (code, full_name, gender, grade, point) values ('52584-262', 'Cornell Schirok', 'Male', 'A5', 3.1);
insert into students (code, full_name, gender, grade, point) values ('54868-4984', 'Cchaddie Vittori', 'Male', 'A11', 9.0);
insert into students (code, full_name, gender, grade, point) values ('67692-241', 'Nicol Pund', 'Female', 'A7', 9.2);
insert into students (code, full_name, gender, grade, point) values ('10631-098', 'Oliver Weigh', 'Male', 'A5', 4.0);
insert into students (code, full_name, gender, grade, point) values ('52125-214', 'Julienne Meltetal', 'Female', 'A10', 8.2);
insert into students (code, full_name, gender, grade, point) values ('54868-4539', 'Bee Anear', 'Female', 'A11', 9.1);
insert into students (code, full_name, gender, grade, point) values ('49035-427', 'Dorolisa Paprotny', 'Female', 'A11', 2.8);
insert into students (code, full_name, gender, grade, point) values ('52731-7043', 'Cosmo Hobson', 'Male', 'A12', 6.6);
insert into students (code, full_name, gender, grade, point) values ('63739-671', 'Hy Jindrich', 'Male', 'A7', 3.4);
insert into students (code, full_name, gender, grade, point) values ('42254-048', 'Elly Meads', 'Female', 'A2', 4.7);
insert into students (code, full_name, gender, grade, point) values ('44924-114', 'Giffer Gatchell', 'Male', 'A10', 9.3);
insert into students (code, full_name, gender, grade, point) values ('58118-0169', 'Salomon Sackett', 'Male', 'A4', 6.5);
insert into students (code, full_name, gender, grade, point) values ('68382-536', 'Saunders Teare', 'Male', 'A4', 3.6);
insert into students (code, full_name, gender, grade, point) values ('50988-181', 'Yuma Smitherman', 'Male', 'A7', 8.8);
insert into students (code, full_name, gender, grade, point) values ('52125-912', 'Hirsch Youson', 'Male', 'A10', 2.3);
insert into students (code, full_name, gender, grade, point) values ('43269-828', 'Betta Mallaby', 'Female', 'A3', 3.3);
insert into students (code, full_name, gender, grade, point) values ('66993-410', 'Annalee Tocknell', 'Female', 'A10', 5.2);
insert into students (code, full_name, gender, grade, point) values ('76015-400', 'Michele Simonnet', 'Male', 'A2', 3.6);
insert into students (code, full_name, gender, grade, point) values ('59746-311', 'Layton Challice', 'Male', 'A6', 1.5);
insert into students (code, full_name, gender, grade, point) values ('41190-124', 'Lesley Aldham', 'Female', 'A11', 4.5);
insert into students (code, full_name, gender, grade, point) values ('10237-727', 'Kirby Niezen', 'Male', 'A9', 4.7);
