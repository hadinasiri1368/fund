
BEGIN
EXECUTE IMMEDIATE 'update  aha_customer set  f_CUSTOMER_BANK_ACCOUNt_id =null';
EXECUTE IMMEDIATE 'ALTER TABLE AHA_CUSTOMER_BANK_ACCOUNT DROP CONSTRAINT FK_CBA_2_CUSTOMER';
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Failed to process table');
END;
/

BEGIN
EXECUTE IMMEDIATE 'ALTER TABLE AHA_CUSTOMER_BANK_ACCOUNT MODIFY(F_CUSTOMER_ID  NULL)';
EXECUTE IMMEDIATE 'update  AHA_CUSTOMER_BANK_ACCOUNT set  f_CUSTOMER_id =null';
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Failed to process table: ');
END;
/


DECLARE
    -- Cursor to fetch table names starting with 'AHA'
CURSOR table_cursor IS
SELECT TABLE_NAME
FROM USER_TABLES
WHERE TABLE_NAME LIKE 'AHA_%';

v_table_name VARCHAR2(128); -- Variable to hold each table name
BEGIN
    -- Open the cursor and iterate through each table
FOR table_rec IN table_cursor LOOP
        v_table_name := table_rec.TABLE_NAME;

BEGIN
            -- Delete all rows from the table
EXECUTE IMMEDIATE 'DELETE FROM ' || v_table_name;
DBMS_OUTPUT.PUT_LINE('Deleted rows from table: ' || v_table_name);

            -- Drop the table after deleting rows
EXECUTE IMMEDIATE 'DROP TABLE ' || v_table_name || ' CASCADE CONSTRAINTS';
DBMS_OUTPUT.PUT_LINE('Dropped table: ' || v_table_name);
EXCEPTION
            WHEN OTHERS THEN
                -- Log the error for the specific table
                DBMS_OUTPUT.PUT_LINE('Failed to process table: ' || v_table_name || ' - ' || SQLERRM);
END; -- End of inner BEGIN...END for each table
END LOOP;
END;
/




