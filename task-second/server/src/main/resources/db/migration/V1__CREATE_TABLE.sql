CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    middle_name VARCHAR(40) NOT NULL,
    date_of_birth DATE NOT NULL,
    group_name VARCHAR(30) NOT NULL,
    student_number VARCHAR(30) UNIQUE
);