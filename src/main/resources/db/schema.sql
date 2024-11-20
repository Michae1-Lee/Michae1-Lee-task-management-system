CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    author      VARCHAR(255) NOT NULL,
    assignee    VARCHAR(255) NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    status      VARCHAR(50)  NOT NULL
);

CREATE TABLE comments
(
    id      SERIAL PRIMARY KEY,
    text    TEXT NOT NULL,
    task_id INT  NOT NULL,
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);
create table users
(
    id       bigserial primary key,
    username varchar(255) not null
        unique,
    password varchar(255) not null,
    role     varchar(255) not null
);