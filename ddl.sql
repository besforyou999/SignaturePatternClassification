CREATE TABLE tsign
(
    id BIGINT NOT NULL AUTO_INCREMENT,
    data JSON,
    image VARCHAR(2083),
    label INT,
    created DATE,
    PRIMARY KEY (id)
);
