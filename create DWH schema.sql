use `electronica-dw`;
select* from sales;
use transaction;
show tables;
select* from transactions;

-- STAR SCHEMA
drop database if exists `electronica-dw`;
CREATE DATABASE `ELECTRONICA-DW`;
USE `ELECTRONICA-DW`;

DROP TABLE IF EXISTS `Sales`;
DROP TABLE IF EXISTS `Customer`;
DROP TABLE IF EXISTS `Store`;
DROP TABLE IF EXISTS `Supplier`;
DROP TABLE IF EXISTS `Product`;
DROP TABLE IF EXISTS `Date`;

-- Create `Date` dimension table
CREATE TABLE `Date` (
	dateID INT NOT NULL AUTO_INCREMENT,
  `day` INT NOT NULL,
  `week` INT NOT NULL,
  `month` INT NOT NULL,
  `quarter` INT NOT NULL,
  `year` INT NOT NULL,
	`full_date` DATETIME NOT NULL,
  PRIMARY KEY (`dateID`)
) ENGINE=InnoDB;

-- Create `Product` dimension table
CREATE TABLE `Product` (
  `productID` INT NOT NULL,
  `productName` VARCHAR(255) NOT NULL,
  `productPrice` DECIMAL(10, 2) NOT NULL,
  PRIMARY KEY (`productID`)
) ENGINE=InnoDB;

-- Create `Supplier` dimension table
CREATE TABLE `Supplier` (
  `supplierID` INT NOT NULL,
  `supplierName` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`supplierID`)
) ENGINE=InnoDB;

-- Create `Store` dimension table
CREATE TABLE `Store` (
  `storeID` INT  NOT NULL,
  `storeName` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`storeID`)
) ENGINE=InnoDB;

-- Create `Customer` dimension table
CREATE TABLE `Customer` (
  `customerID` INT NOT NULL,
  `customerName` VARCHAR(255) NOT NULL,
  `gender` CHAR(6),
  PRIMARY KEY (`customerID`)
) ENGINE=InnoDB;

-- Create `Sales` fact table
CREATE TABLE `Sales` (
  `saleID` INT AUTO_INCREMENT NOT NULL,
  `orderID` INT NOT NULL,
  `dateID` INT NOT NULL,
  `productID` INT,
  `customerID` INT,
  `storeID` INT,
  `supplierID` INT,
  `quantityOrdered` INT NOT NULL,
  `totalSale` DECIMAL(10, 2), -- This can be calculated using a trigger or during data insertion
  PRIMARY KEY (`saleID`),
    CONSTRAINT `fk_sales_date` FOREIGN KEY (`dateID`) REFERENCES `Date` (`dateID`),
  CONSTRAINT `fk_sales_product` FOREIGN KEY (`productID`) REFERENCES `Product` (`productID`),
  CONSTRAINT `fk_sales_customer` FOREIGN KEY (`customerID`) REFERENCES `Customer` (`customerID`),
  CONSTRAINT `fk_sales_store` FOREIGN KEY (`storeID`) REFERENCES `Store` (`storeID`),
  CONSTRAINT `fk_sales_supplier` FOREIGN KEY (`supplierID`) REFERENCES `Supplier` (`supplierID`)
) ENGINE=InnoDB;

select* from sales;
SELECT COUNT(*) FROM sales;
select* from customer;
SELECT COUNT(*) FROM customer;
select* from Date;
SELECT COUNT(*) FROM date;
select* from Product;
SELECT COUNT(*) FROM product;
select* from store;
SELECT COUNT(*) FROM store;
select* from supplier;
SELECT COUNT(*) FROM supplier;


-- ALL DATA
-- SELECT 
--     s.*, 
--     c.customerName, c.gender, 
--     p.productName, p.productPrice,
--     st.storeName,
--     sp.supplierName
-- FROM 
--     sales s
-- LEFT JOIN 
--     customer c ON s.customerID = c.customerID
-- LEFT JOIN 
--     Product p ON s.productID = p.productID
-- LEFT JOIN 
--     store st ON s.storeID = st.storeID
-- LEFT JOIN 
--     supplier sp ON s.supplierID = sp.supplierID;


-- I put salesID as autoincrement
-- All foreign keys (dateID, productID, customerID, storeID, supplierID) are
-- to be linked to the primary key of their respective dimension tables.