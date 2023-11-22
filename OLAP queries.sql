-- queries
USE `ELECTRONICA-DW`;


-- drill down on sales of all products by each supplier with respect to quarter and month
-- query1
SELECT Supplier.supplierID, Supplier.supplierName,
    Date.year AS Year, Date.quarter AS Quarter, Date.month AS Month,    
    SUM(Sales.totalSale) AS TotalSales
FROM    Sales
JOIN    Product ON Sales.productID = Product.productID
JOIN    Supplier ON Sales.supplierID = Supplier.supplierID
JOIN    Date ON Sales.dateID = Date.dateID
WHERE Date.year = 2019
GROUP BY 
    Supplier.supplierID, Supplier.supplierName, 
    Date.year, Date.quarter, Date.month
ORDER BY Supplier.supplierID, 
    Year, Quarter, Month;

    
-- total sales of product with respect to month    
-- query2
SELECT p.productID, d.month, SUM(s.totalSale) AS totalSales
FROM Sales s
JOIN Product p ON s.productID = p.productID
JOIN Date d ON s.dateID = d.dateID
JOIN Supplier sup ON s.supplierID = sup.supplierID
WHERE sup.supplierName = 'DJI' AND d.year = 2019
GROUP BY p.productID, d.month WITH ROLLUP;



-- most popular products sold over the weekends.
-- query3
SELECT   P.productName, COUNT(*) AS NumberOfSales
FROM     Sales S
JOIN     Date D ON S.dateID = D.dateID
JOIN 
    Product P ON S.productID = P.productID
-- Assuming you have a function to calculate day of week from day, month, year
WHERE 
    DAYOFWEEK(CONCAT_WS('-', D.year, D.month, D.day)) IN (6, 7) -- Placeholder function
GROUP BY     P.productName
ORDER BY     NumberOfSales DESC LIMIT 5;


-- quarterly sales of each product for 2019 along with its total yearly sales.
-- query 4
SELECT 
    P.productName,
    SUM(CASE WHEN D.quarter = 1 THEN S.totalSale ELSE 0 END) AS Q1_Sales,
    SUM(CASE WHEN D.quarter = 2 THEN S.totalSale ELSE 0 END) AS Q2_Sales,
    SUM(CASE WHEN D.quarter = 3 THEN S.totalSale ELSE 0 END) AS Q3_Sales,
    SUM(CASE WHEN D.quarter = 4 THEN S.totalSale ELSE 0 END) AS Q4_Sales,
    SUM(S.totalSale) AS Total_Yearly_Sales
FROM     Sales S
JOIN     Date D ON S.dateID = D.dateID
JOIN     Product P ON S.productID = P.productID
WHERE     D.year = 2019
GROUP BY     P.productName
ORDER BY     P.productName;


-- find anomaly
-- query 5
-- 1st anomaly is pakistan is in supplier
select* from supplier;
-- 2nd anomaly
use master_data;
SELECT * FROM master_data;
SELECT supplierID, supplierName 
FROM master_data
WHERE supplierID = 19;

-- materialised view to store product-wise sales analysis for each store.
-- query6
CREATE VIEW STOREANALYSIS_VIEW AS
SELECT s.storeID, p.productID, SUM(s.totalSale) AS storeTotal
FROM Sales s
JOIN Product p ON s.productID = p.productID
GROUP BY s.storeID, p.productID;
SELECT * FROM STOREANALYSIS_VIEW;



 --  total sales for the Tech Haven store's products over months 
 -- query7 
 SELECT 
    P.productName, D.month, SUM(S.totalSale) AS TotalSales
FROM     Sales S
JOIN     Store St ON S.storeID = St.storeID
JOIN     Date D ON S.dateID = D.dateID
JOIN     Product P ON S.productID = P.productID
WHERE     St.storeName = 'Tech Haven'
GROUP BY     
	P.productName, D.month
ORDER BY     
	P.productName, D.month;
 
 
-- materialised view to store monthly performance of each supplier.
 -- query8
drop table if exists SUPPLIER_PERFORMANCE_MV;
CREATE TABLE SUPPLIER_PERFORMANCE_MV (
    supplierID INT,
    supplierName VARCHAR(255),
    month INT,
    monthlyPerformance DECIMAL(10, 2)
);
INSERT INTO SUPPLIER_PERFORMANCE_MV
SELECT 
    S.supplierID, Sp.supplierName,  
    D.month, SUM(S.totalSale) AS monthlyPerformance
FROM Sales S
JOIN Date D ON S.dateID = D.dateID
JOIN Supplier Sp ON S.supplierID = Sp.supplierID  
GROUP BY S.supplierID, Sp.supplierName, D.month;
select* from SUPPLIER_PERFORMANCE_MV;

 
 -- top 5 customers with the highest total sales in 2019
 -- query9
 SELECT  C.customerID, C.customerName,
    SUM(S.totalSale) AS TotalSales,
    COUNT(DISTINCT S.productID) AS UniqueProductsPurchased
FROM     Sales S
JOIN     Customer C ON S.customerID = C.customerID
JOIN     Date D ON S.dateID = D.dateID
WHERE     D.year = 2019
GROUP BY     C.customerID, C.customerName
ORDER BY     TotalSales DESC LIMIT 5;
 
 
 -- materialised view to store monthly sales analysis store & customer wise.
-- query10
CREATE TABLE CUSTOMER_STORE_SALES_MV (
    storeID INT,
    customerID INT,
    customerName VARCHAR(255),
    month INT,
    monthlySales DECIMAL(10, 2)
);
INSERT INTO CUSTOMER_STORE_SALES_MV
SELECT s.storeID, c.customerID, c.customerName,
    d.month, SUM(s.totalSale) AS monthlySales
FROM Sales s
JOIN Store st ON s.storeID = st.storeID
JOIN Customer c ON s.customerID = c.customerID
JOIN Date d ON s.dateID = d.dateID
GROUP BY s.storeID, c.customerID, c.customerName, d.month;
select * from CUSTOMER_STORE_SALES_MV;