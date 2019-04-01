/* DELETE 'biddingdb' database*/
DROP SCHEMA IF EXISTS biddingdb;
/* DELETE USER 'bidadmin' AT LOCAL SERVER*/
DROP USER IF EXISTS 'bidadmin'@'localhost';

/* CREATE 'biddingdb' DATABASE */
CREATE SCHEMA IF NOT EXISTS biddingdb;
/* CREATE THE USER 'bidadmin' AT LOCAL SERVER WITH PASSWORD 'bidpassword' */
CREATE USER IF NOT EXISTS 'bidadmin'@'localhost' IDENTIFIED WITH mysql_native_password BY 'bidpassword';
/* GRANT FULL ACCESS TO THE DATABASE FOR THE USER 'bidadmin' AT LOCAL SERVER*/
GRANT ALL ON biddingDB.* TO 'bidadmin'@'localhost';;
