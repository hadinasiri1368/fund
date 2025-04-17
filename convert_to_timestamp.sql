CREATE OR REPLACE FUNCTION convert_to_timestamp(
    p_date IN VARCHAR2,
    p_time IN VARCHAR2
) RETURN TIMESTAMP
IS
    v_timestamp TIMESTAMP;
BEGIN
    -- بررسی صحت فرمت تاریخ و زمان
    IF REGEXP_LIKE(p_date, '^(\d{4})/(0[1-9]|1[0-2])/(0[1-9]|[12]\d|3[01])$')
       AND REGEXP_LIKE(p_time, '^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$') THEN
BEGIN
            v_timestamp := TO_TIMESTAMP(p_date || ' ' || p_time, 'YYYY/MM/DD HH24:MI');
RETURN v_timestamp;
EXCEPTION
            WHEN OTHERS THEN
                RETURN NULL; -- در صورت وقوع خطا، مقدار NULL برمی‌گردد
END;
ELSE
        RETURN NULL; -- در صورت نامعتبر بودن فرمت
END IF;
END;
/