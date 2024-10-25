create schema institute;
use institute;

CREATE TABLE IF NOT EXISTS Courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS Instructors (
    instructor_id INT PRIMARY KEY,
    instructor_name VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS Students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS Enrollments (
    student_id INT,
    course_id INT,
    enrollment_date DATE,
    PRIMARY KEY (student_id, course_idCourse_Instructors),
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);


CREATE TABLE IF NOT EXISTS Course_Instructors (
    course_id INT,
    instructor_id INT,
    PRIMARY KEY (course_id, instructor_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id),
    FOREIGN KEY (instructor_id) REFERENCES Instructors(instructor_id)
);

-- Sample Data Insertion (optional)

INSERT INTO Courses (course_id, course_name) VALUES (101, 'Introduction to SQL');
INSERT INTO Courses (course_id, course_name) VALUES (102, 'Data Structures');

INSERT INTO Instructors (instructor_id, instructor_name) VALUES (1, 'Dr. Smith');
INSERT INTO Instructors (instructor_id, instructor_name) VALUES (2, 'Dr. Ramirez');
INSERT INTO Instructors (instructor_id, instructor_name) VALUES (3, 'Dr. White');
INSERT INTO Instructors (instructor_id, instructor_name) VALUES (4, 'Lic Andres');

INSERT INTO Students (student_id, student_name) VALUES (1, 'John Doe');
INSERT INTO Students (student_id, student_name) VALUES (2, 'Jane Doe');
INSERT INTO Students (student_id, student_name) VALUES (3, 'Paul Doe');

INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (1, 101, '2024-01-10');
INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (2, 102, '2024-02-12');
INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (3, 102, '2024-03-15');

INSERT INTO Course_Instructors (course_id, instructor_id) VALUES (101, 1);
INSERT INTO Course_Instructors (course_id, instructor_id) VALUES (101, 2);
INSERT INTO Course_Instructors (course_id, instructor_id) VALUES (102, 3);
INSERT INTO Course_Instructors (course_id, instructor_id) VALUES (102, 4);
