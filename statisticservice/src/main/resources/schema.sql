CREATE TABLE IF NOT EXISTS statistics
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    app
    VARCHAR
(
    250
) NOT NULL,
    uri VARCHAR
(
    500
) NOT NULL,
    ip VARCHAR
(
    100
) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT statistics_pk PRIMARY KEY
(
    id
)
    );