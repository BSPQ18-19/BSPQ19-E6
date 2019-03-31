/* DELETE 'biddingdb' database*/
DROP SCHEMA biddingdb;
/* DELETE USER 'bidadmin' AT LOCAL SERVER*/
DROP USER 'bidadmin'@'localhost';

/* CREATE 'biddingdb' DATABASE */
CREATE SCHEMA biddingdb;
/* CREATE THE USER 'bidadmin' AT LOCAL SERVER WITH PASSWORD 'bidpassword' */
CREATE USER 'bidadmin'@'localhost' IDENTIFIED WITH mysql_native_password BY 'bidpassword';
/* GRANT FULL ACCESS TO THE DATABASE FOR THE USER 'bidadmin' AT LOCAL SERVER*/
GRANT ALL ON biddingDB.* TO 'bidadmin'@'localhost';;
