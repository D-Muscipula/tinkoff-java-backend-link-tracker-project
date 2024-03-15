--liquibase formatted sql
--changeset D-Muscipula:init_1

CREATE TABLE IF NOT EXISTS users
(
    user_chat_id BIGINT NOT NULL,
    user_state   TEXT   NOT NULL,

    PRIMARY KEY (user_chat_id),
    UNIQUE (user_chat_id)
);

CREATE TABLE IF NOT EXISTS links
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    url        VARCHAR                  NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_checked_at TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (id),
    UNIQUE (url)
);

CREATE TABLE IF NOT EXISTS users_links
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT,
    link_id BIGINT NOT NULL,

    FOREIGN KEY (user_id) REFERENCES users (user_chat_id),
    FOREIGN KEY (link_id) REFERENCES links (id),
    PRIMARY KEY (user_id, link_id)
);
