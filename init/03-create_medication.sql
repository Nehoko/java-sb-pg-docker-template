CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE medication
(
    id       BIGINT       NOT NULL,
    name     VARCHAR(255) NOT NULL,
    weight   INTEGER      NOT NULL,
    code     VARCHAR(255) NOT NULL,
    picture  VARCHAR(255) NOT NULL,
    drone_id BIGINT,
    CONSTRAINT pk_medication PRIMARY KEY (id)
);

ALTER TABLE medication
    ADD CONSTRAINT FK_MEDICATION_ON_DRONE FOREIGN KEY (drone_id) REFERENCES drone (id);