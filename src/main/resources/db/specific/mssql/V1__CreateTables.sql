CREATE TABLE tcp_port
(
    ordinal_id          BIGINT          NOT NULL IDENTITY PRIMARY KEY,
    port_number         INT             NOT NULL UNIQUE,
    description         VARCHAR(100)    NOT NULL
);

INSERT INTO tcp_port(port_number, description) VALUES (1433, 'mssql');

CREATE TABLE nba_player
(
    ordinal_id          BIGINT          NOT NULL IDENTITY PRIMARY KEY,
    name                VARCHAR(100)    NOT NULL,
    birthdate           datetime        NOT NULL,
    mentions            BIGINT          NOT NULL DEFAULT 0
);
