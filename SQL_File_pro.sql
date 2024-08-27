----Creating The DB
--Tables
CREATE TABLE Product (
    ProductID SERIAL   NOT NULL,
    ProductName varchar(50) NOT NULL,
    Category varchar(20) NOT NULL,
	PRIMARY KEY (ProductID)
);

ALTER SEQUENCE product_productid_seq MINVALUE 0;
ALTER SEQUENCE product_productid_seq RESTART WITH 0;

CREATE TABLE Stock (
    ProductID int   NOT NULL,
    Quantity int,
	CONSTRAINT fk_pid FOREIGN KEY (ProductID)
            REFERENCES product(ProductID)
);

CREATE TABLE Cart (
    ProductID int   NOT NULL,
    Quantity int,
    TimeDate TIMESTAMP,
	CONSTRAINT fk_pid FOREIGN KEY (ProductID)
            REFERENCES product(ProductID)
);

CREATE TABLE Books (
    ProductID int   NOT NULL,
    Pages int,
    Author varchar(50),
	CONSTRAINT fk_pid FOREIGN KEY (ProductID)
            REFERENCES product(ProductID)
);

CREATE TABLE Electronic(
    ProductID int   NOT NULL,
    Model varchar(50),
    Company varchar(50),
	CONSTRAINT fk_pid FOREIGN KEY (ProductID)
            REFERENCES product(ProductID)
);

CREATE TABLE Clothing (
    ProductID int   NOT NULL,
    Size  varchar(10),
    Color varchar(50),
	sex  varchar(10),
	CONSTRAINT fk_pid FOREIGN KEY (ProductID)
            REFERENCES product(ProductID)
);
---------------------------------------------
--Data
INSERT INTO product (productname, category)
VALUES ('T-shirt OF BOSS ', 'Clothing'), ('T-shirt OF Castro ', 'Clothing'),
	('To Kill a Mockingbird', 'Books'), ('Harry Potter and the Sorcerers Stone', 'Books'),
	('ThinkPad', 'Electronics'),('Camera', 'Electronics'), ('TV', 'Electronics'), 
	('To the Lighthouse', 'Books'), ('Jeans OF ManiaJeans ', 'Clothing');

INSERT INTO clothing (productid, size, color, Sex)
VALUES (0,'L', 'White','M'),(1,'XL', 'Black','F'), (8,'S', 'Red', 'M');

INSERT INTO books (productid,pages,author)
VALUES (2,281, 'Harper Lee'), (3,320, 'J.K. Rowling'), (7,209, 'Virginia Woolf');

INSERT INTO electronic (productid,model, company)
VALUES (4,'X1 Carbon', 'Lenovo'), (5,'a7 III', 'Sony Alpha'), (6,'QLED 65-inch', 'Samsung');

INSERT INTO stock (productid,quantity)
VALUES (0,15), (1,20), (2,12), (3,10), (4,50), (5,14), (6,19), (7,3), (8,17);

INSERT INTO cart (productid, quantity, TimeDate)
VALUES (0,0, NULL), (1,0, NULL), (2,0, NULL), (3,0, NULL), (4,0, NULL), (5,0, NULL), (6,0, NULL), (7,0, NULL), (8,0, NULL);
---------------------------------------------
--Functions and Triggers
--Count how many products in the table:
CREATE OR REPLACE FUNCTION count_rows_in_table(table_name text)
RETURNS integer 
LANGUAGE 'plpgsql'
AS 
$count_rows_in_table$
DECLARE 
    row_count integer;
BEGIN
    EXECUTE format('SELECT COUNT(*) FROM %I', table_name) INTO row_count;
    RETURN row_count;
END;
$count_rows_in_table$;

--Reduce the amount of products and update the timedate:
CREATE OR REPLACE FUNCTION Reduce_the_amount_of_products_stock()
RETURNS TRIGGER 
LANGUAGE 'plpgsql'
AS 
$Reduce_the_amount_of_products$
BEGIN
	
    UPDATE stock
    SET quantity = quantity - (NEW.quantity - OLD.quantity)
    WHERE productid = NEW.productid;

	NEW.timedate = NOW();

    RETURN NEW;
END
$Reduce_the_amount_of_products$;


CREATE TRIGGER trigger_update_quantities_stock
BEFORE UPDATE ON cart
FOR EACH ROW
EXECUTE FUNCTION Reduce_the_amount_of_products_stock();
