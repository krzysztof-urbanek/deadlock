CREATE TABLE tcp_port
(
    ordinal_id          BIGINT          NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    port_number         INT             NOT NULL UNIQUE,
    description         VARCHAR(100)    NOT NULL
);

INSERT INTO tcp_port(port_number, description) VALUES (5432, 'postgresql');

CREATE TABLE nba_player
(
    ordinal_id          BIGINT          NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name                VARCHAR(100)    NOT NULL,
    birthdate           TIMESTAMP       NOT NULL,
    mentions            BIGINT          NOT NULL DEFAULT 0
);

CREATE INDEX birthdate ON nba_player(birthdate);
