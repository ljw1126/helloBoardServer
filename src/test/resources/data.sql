CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId     VARCHAR(45) NOT NULL,
    password   VARCHAR(64) NOT NULL,
    nickname   VARCHAR(45) NOT NULL,
    status     VARCHAR(45) NOT NULL,
    isAdmin    BOOLEAN DEFAULT FALSE,
    isWithDraw BOOLEAN DEFAULT FALSE,
    createTime TIMESTAMP,
    updateTime TIMESTAMP
);

CREATE TABLE category
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(45) NOT NULL
);

CREATE TABLE post
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(45) NOT NULL,
    contents   VARCHAR(512) NOT NULL,
    userId     INT NOT NULL,
    categoryId INT NOT NULL,
    createTime TIMESTAMP,
    updateTime TIMESTAMP,
    views      INT NOT NULL
);


