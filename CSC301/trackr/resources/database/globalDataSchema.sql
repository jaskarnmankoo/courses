CREATE TABLE IF NOT EXISTS UserAccount (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(40),
    firstname VARCHAR(50),
    lastname VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS GlobalMedia (
    mediatitle VARCHAR(70),
    mediatype VARCHAR(8),
    mediagenre VARCHAR(30),
    mediaduration VARCHAR(20),
    PRIMARY KEY(mediatitle),
    CHECK (mediatype IN('Movie', 'TV Show'))
);

CREATE TABLE IF NOT EXISTS Ratings (
    mediatitle VARCHAR(70),
    username VARCHAR(50),
    review VARCHAR(200),
    rating CHAR(1),
    PRIMARY KEY(username, mediatitle)
);
