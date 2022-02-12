CREATE TABLE tcp_port
(
    ordinal_id          BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    port_number         INT             NOT NULL UNIQUE,
    description         VARCHAR(100)    NOT NULL
);

INSERT INTO tcp_port(port_number, description) VALUES (3306, 'mysql');