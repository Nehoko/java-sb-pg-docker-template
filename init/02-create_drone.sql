CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE drone
(
    id               BIGINT       NOT NULL,
    serial_number    VARCHAR(100) NOT NULL,
    model            INTEGER      NOT NULL,
    weight_limit     INTEGER      NOT NULL,
    battery_capacity INTEGER      NOT NULL,
    state            INTEGER      NOT NULL,
    CONSTRAINT pk_drone PRIMARY KEY (id)
);

ALTER TABLE drone
    ADD CONSTRAINT uc_drone_serialnumber UNIQUE (serial_number);