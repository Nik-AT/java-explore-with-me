CREATE TABLE IF NOT EXISTS users
(
    user_id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    email
    VARCHAR
(
    100
) NOT NULL,
    name VARCHAR
(
    100
) NOT NULL,
    CONSTRAINT USER_PK PRIMARY KEY
(
    user_id
),
    CONSTRAINT EMAIL_UNIQUE UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS categories
(
    category_id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    category_name
    VARCHAR
(
    100
) NOT NULL,
    CONSTRAINT CAT_PK PRIMARY KEY
(
    category_id
),
    CONSTRAINT NAME_UNIQUE UNIQUE
(
    category_name
)
    );

CREATE TABLE IF NOT EXISTS events
(
    event_id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY,
    title
    VARCHAR
(
    500
) NOT NULL,
    annotation VARCHAR
(
    1000
) NOT NULL,
    description VARCHAR
(
    2000
) NOT NULL,
    category_id BIGINT NOT NULL,
    initiator_id BIGINT NOT NULL,
    created_on TIMESTAMP NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INT NOT NULL,
    event_date TIMESTAMP NOT NULL,
    lat NUMERIC NOT NULL,
    lon NUMERIC NOT NULL,
    published TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR
(
    50
),
    CONSTRAINT EVENT_PK PRIMARY KEY
(
    event_id
),
    CONSTRAINT CATEGORY_FK FOREIGN KEY
(
    category_id
) REFERENCES categories
(
    category_id
),
    CONSTRAINT INITIATOR_FK FOREIGN KEY
(
    initiator_id
) REFERENCES users
(
    user_id
)
    );

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    title
    VARCHAR
(
    500
) NOT NULL,
    pinned BOOLEAN,
    CONSTRAINT COMPILATION_PK PRIMARY KEY
(
    compilation_id
)
    );

CREATE TABLE IF NOT EXISTS event_compilations
(
    compilation_Id
    BIGINT
    NOT
    NULL,
    event_Id
    BIGINT
    NOT
    NULL,
    CONSTRAINT
    COMPILATIONS_FK
    FOREIGN
    KEY
(
    compilation_Id
) REFERENCES compilations
(
    compilation_id
),
    CONSTRAINT EVENT_FK FOREIGN KEY
(
    event_Id
) REFERENCES events
(
    event_id
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    request_id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    requestor_id
    BIGINT
    NOT
    NULL,
    event_id
    BIGINT
    NOT
    NULL,
    state
    VARCHAR
(
    50
),
    created_on TIMESTAMP,
    CONSTRAINT REQUEST_PK PRIMARY KEY
(
    request_id
),
    CONSTRAINT REQUESTOR_EVENT_UNIQUE UNIQUE
(
    requestor_id,
    event_id
),
    CONSTRAINT REQUESTOR_FK FOREIGN KEY
(
    requestor_id
) REFERENCES users
(
    user_id
),
    CONSTRAINT EVENTS_FK FOREIGN KEY
(
    event_id
) REFERENCES events
(
    event_id
)
    );