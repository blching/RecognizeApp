--source /Users/brandonching/Desktop/School/CS157a/RecognizeApp/RecognizeApp/lib/create_and_populate.sql;
CREATE TABLE RECIPIENTS (
    fName varchar(20),
    lName varchar(20),
    id int,
    honorLevel int
);

CREATE TABLE RECENTRECIPIENTS (
    fName varchar(20),
    lName varchar(20),
    id int
);

CREATE TABLE GIFTS (
    gifter varChar(40),
    reciever varChar(40),
    description varChar(100),
    price int
);

INSERT INTO RECIPIENTS(fName, lName, id, honorLevel)
    VALUES
    ("Kevin", "Ngyuen", 1, 0),
    ("Daniel", "Kim", 2, 0),
    ("Pedro", "Pascal", 10, 0),
    ("Faker", "Lee", 7, 0),
    ("Stephen", "Curry", 30, 0),
    ("Daniel", "White", 11, 0),
    ("Robert", "Smith", 77, 0),
    ("Karina", "Yoo", 65, 0);

INSERT INTO RECENTRECIPIENTS(fName, lName, id)
    VALUES
    ("Kevin", "Ngyuen", 1),
    ("Daniel", "Kim", 2),
    ("Pedro", "Pascal", 10),
    ("Faker", "Lee", 7),
    ("Stephen", "Curry", 30);

CREATE TRIGGER honorLevelUpdate AFTER UPDATE ON RECIPIENTS
    FOR EACH ROW
    INSERT INTO RECENTRECIPIENTS (fName, lName, id)
    VALUES (NEW.fName, NEW.lName, NEW.id);
    DELETE FROM RECENTRECIPIENTS where id IN (select * FROM RECENTRECIPIENTS LIMIT 1);

