create table users
(
    id       bigserial primary key,
    username varchar(255) not null
        unique,
    password varchar(255) not null,
    role     varchar(255) not null
);
CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY, -- Автоматическая генерация уникального идентификатора
    title       VARCHAR(255) NOT NULL, -- Заголовок задачи
    description TEXT,                  -- Описание задачи
    status      VARCHAR(50)  NOT NULL, -- Статус задачи
    priority    VARCHAR(50)  NOT NULL, -- Приоритет задачи
    author_id   BIGINT       NOT NULL, -- Внешний ключ для автора
    executor_id BIGINT,                -- Внешний ключ для исполнителя
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_executor FOREIGN KEY (executor_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE comments
(
    id      SERIAL PRIMARY KEY,
    text    TEXT NOT NULL,
    task_id INT  NOT NULL,
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);
