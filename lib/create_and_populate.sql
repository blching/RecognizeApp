-- /usr/local/mysql/bin/mysql -u root -p
-- source /Users/brandonching/Desktop/School/CS157a/RecognizeApp/RecognizeApp/lib/create_and_populate.sql;
-- UPDATE RECIPIENTS SET honorLevel = 10 where fName = 'Karina';

CREATE TABLE RECIPIENTS (
    fName varchar(20),
    lName varchar(20),
    id int,
    honorLevel int
);

CREATE TABLE RECENTRECIPIENTS (
    fName varchar(20),
    lName varchar(20),
    id int,
    stackId int
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

INSERT INTO RECENTRECIPIENTS(fName, lName, id, stackId)
    VALUES
    ("Kevin", "Ngyuen", 1, 1),
    ("Daniel", "Kim", 2, 2),
    ("Pedro", "Pascal", 10, 3),
    ("Faker", "Lee", 7, 4),
    ("Stephen", "Curry", 30, 5);

DELIMITER //
CREATE TRIGGER honorLevelUpdate AFTER UPDATE ON RECIPIENTS
    FOR EACH ROW
    BEGIN
    INSERT INTO RECENTRECIPIENTS (fName, lName, id, stackId)
    VALUES (NEW.fName, NEW.lName, NEW.id, 6);

    UPDATE RECENTRECIPIENTS SET stackId = stackId - 1;
    DELETE FROM RECENTRECIPIENTS where stackId = 0; 
    END //
DELIMITER ;
