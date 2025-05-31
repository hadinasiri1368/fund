BEGIN
FOR rec IN (
      SELECT
         ac.table_name,
         ac.constraint_name
      FROM
         all_constraints ac
      WHERE  ac.constraint_type = 'R' -- Foreign Key
         AND ac.table_name LIKE 'AHA_%'
   ) LOOP
      EXECUTE IMMEDIATE 'ALTER TABLE ' || rec.table_name ||
                        ' DROP CONSTRAINT ' || rec.constraint_name;
END LOOP;
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
