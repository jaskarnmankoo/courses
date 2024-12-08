CREATE TABLE IF NOT EXISTS PublicList (
    mediatitle VARCHAR(70),
    mediatype VARCHAR(8),
    mediagenre VARCHAR(20),
    mediaduration VARCHAR(20),
    status VARCHAR(20) DEFAULT 'N/A',
    episodecount VARCHAR(4),
    startdate CHAR(8),
    enddate CHAR(8),
    userrating CHAR(1) DEFAULT '0',
    PRIMARY KEY(mediatitle),
    CHECK (mediatype IN('Movie', 'TV Show'))
);

CREATE TABLE IF NOT EXISTS PrivateList (
    mediatitle VARCHAR(70),
    mediatype VARCHAR(8),
    mediagenre VARCHAR(20),
    mediaduration VARCHAR(20),
    status VARCHAR(20) DEFAULT 'N/A',
    episodecount VARCHAR(4),
    startdate CHAR(8),
    enddate CHAR(8),
    userrating CHAR(1) DEFAULT '0',
    PRIMARY KEY(mediatitle),
    CHECK (mediatype IN('Movie', 'TV Show'))
);
