CREATE TABLE users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    userId     VARCHAR(45) NOT NULL,
    password   VARCHAR(64) NOT NULL,
    nickname   VARCHAR(45) NOT NULL,
    status     VARCHAR(45) NOT NULL,
    isAdmin    BOOLEAN DEFAULT FALSE,
    isWithDraw BOOLEAN DEFAULT FALSE,
    createTime TIMESTAMP,
    updateTime TIMESTAMP
);
