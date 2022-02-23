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

CREATE INDEX birthdate ON nba_player(birthdate);

GO

CREATE FUNCTION forced_timeout(@seconds int) returns int as
BEGIN
    DECLARE @endTime datetime2(0) = DATEADD(SECOND, @seconds, GETDATE());
    WHILE (GETDATE() < @endTime ) BEGIN SET @endTime = @endTime; END
    RETURN @seconds
END
