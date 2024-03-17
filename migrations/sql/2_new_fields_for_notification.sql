--liquibase formatted sql
--changeset D-Muscipula:init_2

CREATE TABLE IF NOT EXISTS tg_user
(
    user_chat_id BIGINT NOT NULL,
    user_state   TEXT   NOT NULL,

    PRIMARY KEY (user_chat_id),
    UNIQUE (user_chat_id)
);

CREATE TABLE IF NOT EXISTS link
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY,
    url             VARCHAR                  NOT NULL,

    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_checked_at TIMESTAMP WITH TIME ZONE,

    last_commit_sha VARCHAR,
    answers_count   BIGINT,

    PRIMARY KEY (id),
    UNIQUE (url)
);

CREATE TABLE IF NOT EXISTS users_links
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    tg_user BIGINT,
    link    BIGINT NOT NULL,

    FOREIGN KEY (tg_user) REFERENCES tg_user (user_chat_id) ON DELETE CASCADE,
    FOREIGN KEY (link) REFERENCES link (id) ON DELETE CASCADE,
    PRIMARY KEY (tg_user, link)
);
