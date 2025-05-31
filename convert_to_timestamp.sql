CREATE OR REPLACE FUNCTION aha_shamsi_to_gregorian (
    shamsi_date VARCHAR2  -- مثال: '1399/12/30'
) RETURN DATE IS
    jy NUMBER;
    jm NUMBER;
    jd NUMBER;

    gy NUMBER;
    gm NUMBER;
    gd NUMBER;

    j_day_no  NUMBER;
    g_day_no  NUMBER;
    i NUMBER;

    leap NUMBER;
    g_days_in_month  PLS_INTEGER;
    g_dm             SYS.ODCINumberList := SYS.ODCINumberList(31,28,31,30,31,30,31,31,30,31,30,31);
    j_dm             SYS.ODCINumberList := SYS.ODCINumberList(31,31,31,31,31,31,30,30,30,30,30,29);

    cycle_year NUMBER;
    is_leap BOOLEAN := FALSE;
BEGIN
    -- پارس کردن رشته ورودی
    jy := TO_NUMBER(SUBSTR(shamsi_date, 1, 4));
    jm := TO_NUMBER(SUBSTR(shamsi_date, 6, 2));
    jd := TO_NUMBER(SUBSTR(shamsi_date, 9, 2));

    -- اعتبارسنجی ماه
    IF jm < 1 OR jm > 12 THEN
        RETURN NULL;
END IF;

    -- بررسی سال کبیسه شمسی بر اساس سیکل 33 ساله
    cycle_year := MOD(jy - 1309, 33);  -- فرض: 1309 اولین سال سیکل جدید
    IF cycle_year IN (1, 5, 9, 13, 17, 22, 26, 30) THEN
        is_leap := TRUE;
END IF;

    -- اعتبارسنجی روز
    IF (jm = 12 AND jd = 30 AND NOT is_leap) OR jd > j_dm(jm) OR jd < 1 THEN
        RETURN NULL;
END IF;

    -- محاسبه روز جلالی از مبدأ
    j_day_no := 365 * (jy - 979) + FLOOR((jy - 979)/33) * 8 + FLOOR((MOD((jy - 979),33) + 3) / 4);

FOR i IN 1..jm-1 LOOP
        j_day_no := j_day_no + j_dm(i);
END LOOP;

    j_day_no := j_day_no + jd - 1;

    -- محاسبه تاریخ میلادی معادل
    g_day_no := j_day_no + 79;
    gy := 1600 + 400 * FLOOR(g_day_no / 146097);
    g_day_no := MOD(g_day_no, 146097);

    leap := 1;
    IF g_day_no >= 36525 THEN
        g_day_no := g_day_no - 1;
        gy := gy + 100 * FLOOR(g_day_no / 36524);
        g_day_no := MOD(g_day_no, 36524);

        IF g_day_no >= 365 THEN
            g_day_no := g_day_no + 1;
ELSE
            leap := 0;
END IF;
END IF;

    gy := gy + 4 * FLOOR(g_day_no / 1461);
    g_day_no := MOD(g_day_no, 1461);

    IF g_day_no >= 366 THEN
        leap := 0;
        g_day_no := g_day_no - 1;
        gy := gy + FLOOR(g_day_no / 365);
        g_day_no := MOD(g_day_no, 365);
END IF;

    -- سال کبیسه میلادی
    IF MOD(gy, 4) = 0 AND (MOD(gy, 100) <> 0 OR MOD(gy, 400) = 0) THEN
        g_days_in_month := 29;
ELSE
        g_days_in_month := 28;
END IF;
    g_dm(2) := g_days_in_month;

    gm := 1;
    WHILE g_day_no >= g_dm(gm) LOOP
        g_day_no := g_day_no - g_dm(gm);
        gm := gm + 1;
END LOOP;

    gd := g_day_no + 1;

RETURN TO_DATE(gy || '/' || LPAD(gm,2,'0') || '/' || LPAD(gd,2,'0'), 'YYYY/MM/DD');
EXCEPTION
    WHEN OTHERS THEN
        RETURN NULL;
END;
/



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
            v_timestamp := TO_TIMESTAMP(aha_shamsi_to_gregorian(p_date) || ' ' || p_time, 'YYYY/MM/DD HH24:MI');
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