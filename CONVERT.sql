insert into AHA_CALENDAR
select ROWNUM,TBL.* from  (select cal_day,is_off,is_vacation,null INSERTED_DATE_TIME,null INSERTED_USER_ID,null UPDATED_DATE_TIME,null UPDATED_USER_ID from  BUSINESS_CALENDAR ORDER BY CAL_DAY) TBL
    /

-----------------------------------------------------------------------------------------------------

INSERT INTO AHA_BANK
SELECT b.bank_id,
       b.BANK_NAME,
       b.is_Valid,
       NULL,
       NULL,
       NULL,
       NULL
FROM bank b
    /

-----------------------------------------------------------------------------------------------------

INSERT INTO AHA_FILE_TYPE
select 1,'PDF',null,null,null,null from  dual
union
select 2,'IMAGE',null,null,null,null from  dual
    /

-----------------------------------------------------------------------------------------------------

INSERT INTO AHA_FUND
select nf.FUND_ID,nf.FUND_NAME,IS_BASE_FUND,IS_ACTIVE,f.is_etf IS_ETF,NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID from  N_FUND nf,fund f where nf.fund_id=f.fund_id
    /

-----------------------------------------------------------------------------------------------------

INSERT INTO AHA_ACCOUNT_NATURE
SELECT dln.DL_NATURE_ID,
       dln.DL_NATURE_NAME,
       NULL,
       NULL,
       NULL,
       NULL
FROM DETAIL_LEDGER_NATURE dln
    /

-----------------------------------------------------------------------------------------------------

INSERT INTO AHA_DETAIL_LEDGER_TYPE
SELECT dlc.DL_CLASS_ID,
       dlc.DL_CLASS_NAME,
       dlc.DL_NATURE_ID,
       NULL,
       NULL,
       NULL,
       NULL
FROM detail_ledger_class dlc
    /

-----------------------------------------------------------------------------------------------------

insert into AHA_FUND_BRANCH
select branch_id,is_active,branch_code,branch_name,manager,phone,fax,cell_phone,postal_code,address,null,null,null,null from  branch
    /

----------------------------------------------------------------------------------------------------

insert into  AHA_FUND_FILE
SELECT rownum,tbl.*
FROM (SELECT f.fund_id,
             fmi.FUND_MOB_INFO_FILE_NAME,
             DECODE (fmit.is_image, 1, 2, 1)     F_FILE_TYPE_ID,
             NULL                                file_content,
             description,
             NULL                                INSERTED_DATE_TIME,
             NULL                                INSERTED_USER_ID,
             NULL                                UPDATED_DATE_TIME,
             NULL                                UPDATED_USER_ID
      FROM n_fund f, FUND_MOB_INFO fmi, FUND_MOB_INFO_TYPE fmit
      WHERE fmit.FUND_MOB_INFO_TYPE_ID = fmi.FUND_MOB_INFO_TYPE_ID) tbl
    /

----------------------------------------------------------------------------------------------------

INSERT INTO AHA_DETAIL_LEDGER
SELECT dl.dl_id,
       dl.dl_name,
       dl.DL_NUMBER,
       dl.DL_CLASS_ID,
       dl.DL_NATURE_ID,
       dl.IS_ACTIVE,
       NULL,
       NULL,
       NULL,
       NULL
FROM DETAIL_LEDGER dl
    /

----------------------------------------------------------------------------------------------------

Insert into AHA_FUND_TYPE
(ID, NAME)
Values
    (6, 'بازنشستگی')
    /

Insert into AHA_FUND_TYPE
(ID, NAME)
Values
    (5, 'تضمین')
    /

Insert into AHA_FUND_TYPE
(ID, NAME)
Values
    (4, 'بخشی')
    /
Insert into AHA_FUND_TYPE
(ID, NAME)
Values
    (3, 'بازاگردانی')
    /
Insert into AHA_FUND_TYPE
(ID, NAME)
Values
    (2, 'قابل معامله')
    /
Insert into AHA_FUND_TYPE
(ID, NAME)
Values
    (1, 'صدور و ابطال')
    /

----------------------------------------------------------------------------------------------------

INSERT INTO AHA_GENERAL_LEDGER_TYPE
select GL_GROUP_ID,GL_GROUP_NAME,NULL,NULL,NULL,NULL from  GENERAL_LEDGER_GROUP
    /

----------------------------------------------------------------------------------------------------

INSERT INTO AHA_GENERAL_LEDGER
select GL_ID,GL_NAME,GL_NUMBER,IS_ACTIVE,GL_GROUP_ID,GL_NATURE_ID,NULL,NULL,NULL,NULL from  GENERAL_LEDGER
    /
----------------------------------------------------------------------------------------------------

INSERT INTO AHA_FUND_INFORMATION
SELECT ROWNUM,HEADER_NAME,CASE WHEN ALL_RECORDS = 1 AND COUNT_NON_ETF = 1 THEN 1
                               WHEN ALL_RECORDS = 1 AND COUNT_ETF = 1 THEN 2
                               WHEN ALL_RECORDS > 1 AND COUNT_ETF = 0 THEN 3
                               WHEN ALL_RECORDS > 1 AND COUNT_ETF = ALL_RECORDS THEN 4
                               WHEN ALL_RECORDS > 1 AND COUNT_ETF = COUNT_NON_ETF THEN 5
    END     DD   ,NULL,NULL,NULL,NULL
FROM (SELECT (SELECT COUNT (1) FROM N_FUND)    ALL_RECORDS,
             (SELECT COUNT (1)
              FROM FUND
              WHERE IS_ETF = 1)               COUNT_ETF,
             (SELECT COUNT (1)
              FROM FUND
              WHERE IS_ETF = 0)               COUNT_NON_ETF
      FROM DUAL) TBL ,FUND
    /
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_PARAMS_TYPE
SELECT a.AP_CATEGORY_ID,
       a.AP_CATEGORY_NAME,
       NULL,
       NULL,
       NULL,
       NULL
FROM APP_PARAM_CATEGORY a
UNION
SELECT 7,
       'تنظیم مشخصات',
       NULL,
       NULL,
       NULL,
       NULL
FROM DUAL a
UNION
SELECT 8,
       'تنظیم حساب',
       NULL,
       NULL,
       NULL,
       NULL
FROM DUAL a
    /
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_SUBSIDIARY_LEDGER
select SL_ID,SL_NAME,SL_NUMBER,IS_ACTIVE,GL_ID,NULL,NULL,NULL,NULL from  SUBSIDIARY_LEDGER
    /
----------------------------------------------------------------------------------------------------

INSERT INTO AHA_PARAMS_VALUE_TYPE (ID,NAME) VALUES (1,'NUMBER')
    /

INSERT INTO AHA_PARAMS_VALUE_TYPE (ID,NAME) VALUES (2,'STRING')
    /

INSERT INTO AHA_PARAMS_VALUE_TYPE (ID,NAME) VALUES (3,'BOOLEAN')
    /

INSERT INTO AHA_PARAMS_VALUE_TYPE (ID,NAME) VALUES (4,'DATE')
    /

INSERT INTO AHA_PARAMS_VALUE_TYPE (ID,NAME) VALUES (5,'DROPDOWN')
    /

INSERT INTO AHA_PARAMS_VALUE_TYPE (ID,NAME) VALUES (6,'TIME')
    /
----------------------------------------------------------------------------------------------------
INSERT INTO  AHA_PARAMS
select ROWNUM,TBL.* from  (
                              SELECT PARAM_CODE,
                                     PARAM_NAME,
                                     NVL (
                                             PARAM_VALUE_STRING,
                                             DECODE (
                                                     INSTR (PARAM_VALUE, '.'),
                                                     0, TO_CHAR (PARAM_VALUE),
                                                     DECODE (SUBSTR (PARAM_VALUE, 0, 1),
                                                             '.', TO_CHAR (PARAM_VALUE, 'FM0.999999'),
                                                             TO_CHAR (PARAM_VALUE, 'FM99999.999999'))))    VALUE,
       1 IS_ACTIVE,is_Mutable, AP_CATEGORY_ID,FUND_ID,DL_ID,SL_ID,
       CASE WHEN PARAM_VALUE_TYPE=1 AND PARAM_VALUE_STRING IS NULL THEN 1
            WHEN PARAM_VALUE_TYPE=1 AND PARAM_VALUE_STRING IS NOT NULL THEN 2
            WHEN PARAM_VALUE_TYPE=2 THEN 2
            WHEN PARAM_VALUE_TYPE=3 THEN 3
            WHEN PARAM_VALUE_TYPE=4 THEN 4
            WHEN PARAM_VALUE_TYPE=5 THEN 5
            WHEN PARAM_VALUE_TYPE>=6 THEN 2
       END  F_PARAMS_VALUE_TYPE_ID,NULL DATA_QUERY,0 IS_GLOBAL,
       NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID
                              FROM APP_PARAM ORDER BY PARAM_ID) TBL
    /

INSERT INTO AHA_PARAMS_HISTORY
select ROWNUM,TBL.* from  (select ID,VALUE,'0000/00/00' EFFECTIVE_DATE,NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID from   AHA_PARAMS ORDER BY ID) TBL
    /

update aha_params set F_PARAMS_VALUE_TYPE_ID=2 where CODE='WEB_SERVICE_API_WSDL_NEWURL'
    /
update aha_params set VALUE=null where code='EFFECTIVE_DATE'
    /
update aha_params set F_PARAMS_VALUE_TYPE_ID=1 where code='MAX_CHARITY_PROFIT_PERCENT'
    /
update  aha_params set F_PARAMS_VALUE_TYPE_ID=6  where F_PARAMS_VALUE_TYPE_ID=5  and DATA_QUERY is null
    /
update  aha_params
set F_PARAMS_VALUE_TYPE_ID=3
where F_PARAMS_VALUE_TYPE_ID=2
  and value is not null
  and F_PARAMS_TYPE_ID in (4,6)
  and (value='0' or value='1')
/
----------------------------------------------------------------------------------------------------
INSERT INTO  AHA_PARAMS
select (select MAX(ID) from  AHA_PARAMS)+ROWNUM ID,TBL.* from  (
                                                                   SELECT PARAM_CODE,
                                                                          PARAM_NAME,
                                                                          DECODE (
                                                                                  INSTR (PARAM_VALUE, '.'),
                                                                                  0, TO_CHAR (PARAM_VALUE),
                                                                                  DECODE (SUBSTR (PARAM_VALUE, 0, 1),
                                                                                          '.', TO_CHAR (PARAM_VALUE, 'FM0.999999'),
                                                                                          TO_CHAR (PARAM_VALUE, 'FM99999.999999'))) VALUE,
       1 IS_ACTIVE,1 IS_EDITABLE,6 AP_CATEGORY_ID,FUND_ID,NULL DL_ID,NULL SL_ID,
       1 F_PARAMS_VALUE_TYPE_ID,NULL DATA_QUERY,0 IS_GLOBAL,       
       NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID
                                                                   from  APP_PARAM_HISTORICAL  WHERE (PARAM_CODE,PARAM_TIME_STAMP) IN(
                                                                       select PARAM_CODE,MAX(PARAM_TIME_STAMP) PARAM_TIME_STAMP from  APP_PARAM_HISTORICAL  GROUP BY PARAM_CODE)) TBL
    /

INSERT INTO AHA_PARAMS_HISTORY
select ROWNUM+(select MAX(ID) from  AHA_PARAMS_HISTORY),TBL.* from  (
                                                                        SELECT (select ID from  AHA_PARAMS AP WHERE AP.CODE=PARAM_CODE) F_PARAMS_ID,PARAM_VALUE,PARAM_START_EFFECTIVE_DATE,NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID
                                                                        from  APP_PARAM_HISTORICAL ) TBL
    /

----------------------------------------------------------------------------------------------------
MERGE INTO AHA_PARAMS P
    USING (select PARAM_CODE,FUND_ID,PARAM_VALUE_TYPE from  APP_PARAM WHERE PARAM_VALUE_TYPE>=6) AP
ON (P.CODE = AP.PARAM_CODE AND P.F_FUND_ID=AP.FUND_ID )
    WHEN MATCHED THEN
UPDATE SET P.F_PARAMS_TYPE_ID = 8
    /
----------------------------------------------------------------------------------------------------

INSERT INTO AHA_PARAMS
SELECT (SELECT MAX (ID) FROM AHA_PARAMS) + ROWNUM ID, TBL.*
FROM (SELECT FUND_COLOUMN_NAME,
             NAME,
             VALUE,
             1       IS_ACTIVE,
             IS_USER_EDIT,
             7       F_PARAMS_TYPE_ID,
             FUND_ID,
             NULL    F_DETAIL_LEDGER_ID,
             NULL    F_SUBSIDIARY_LEDGER_ID,
             CASE
                 WHEN TYPE = 1 AND LOV IS NULL THEN 1
                 WHEN TYPE = 1 AND LOV IS NOT NULL THEN 5
                 WHEN TYPE = 7 THEN 1
                 ELSE TYPE
                 END     F_PARAMS_VALUE_TYPE_ID,
             LOV,
             0       IS_GLOBAL,
             NULL    INSERTED_DATE_TIME,
             NULL    INSERTED_USER_ID,
             NULL    UPDATED_DATE_TIME,
             NULL    UPDATED_USER_ID
      FROM FUND_INFO F
      WHERE EXISTS
                (SELECT *
                 FROM FUND_INFO ff3
                 WHERE     ff3.FUND_COLOUMN_NAME =
                           f.FUND_COLOUMN_NAME
                   AND F.INSERT_TIMESTAMP =
                       (SELECT MAX (FF4.INSERT_TIMESTAMP)
                        FROM FUND_INFO FF4
                        WHERE FF4.FUND_COLOUMN_NAME =F.FUND_COLOUMN_NAME))
     ) TBL
    /

insert into AHA_PARAMS_HISTORY
select * from  (
                   select NVL((select MAX(ID) from  AHA_PARAMS_HISTORY),0)+rownum ID,TBL.* from  (
                                                                                                     SELECT (select ID from  AHA_PARAMS AP WHERE AP.CODE=F.FUND_COLOUMN_NAME) F_PARAMS_ID,VALUE,FUND_DATE,NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID
                                                                                                     FROM FUND_INFO F
                                                                                                     WHERE EXISTS( select * from  FUND_INFO FF5 WHERE FF5.FUND_COLOUMN_NAME=F.FUND_COLOUMN_NAME AND FF5.FUND_DATE=F.FUND_DATE AND F.INSERT_TIMESTAMP=(select MAX(FF6.INSERT_TIMESTAMP) from  FUND_INFO FF6 WHERE FF6.FUND_COLOUMN_NAME=F.FUND_COLOUMN_NAME AND FF6.FUND_DATE=F.FUND_DATE ))
                                                                                                 )TBL
               )
    /
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_PAYMENT_REASON
select RP_REASON_ID,RP_REASON_NAME,SYSTEM_DEFINED,SL_ID,SECOND_SL_ID,NULL FROM_DETAIL_LEDGER_ID,NULL TO_DETAIL_LEDGER_ID, NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID from  RP_REASON
    /
----------------------------------------------------------------------------------------------------
Insert into AHA_MENU
(ID, NAME, MENU_LEVEL)
Values
    (1, 'راهبری سیستم', 0)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (11, 'صندوق ها', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (12, 'شعب', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (13, 'کاربران', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (14, 'گروه کاربری', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (15, 'نقش ها', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (16, 'پارامترها', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (17, 'مدیریت OTP', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (18, 'علت های پرداخت', 1, 1)
    /
Insert into AHA_MENU
(ID, NAME, F_MENU_ID, MENU_LEVEL)
Values
    (19, 'فایل ها', 1, 1)
    /
----------------------------------------------------------------------------------------------------

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (1, 'ثبت صندوق ها', '/basicData/fund/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (2, 'ثبت شعب', '/basicData/branch/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (3, 'ثبت کاربران', '/basicData/user/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (4, 'ثبت گروه کاربری', '/basicData/groupUser/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (5, 'ثبت نقش ها', '/basicData/role/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (6, 'ثبت پارامترها', '/basicData/param/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (7, 'ثبت مدیریت OTP', '/basicData/OTP/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (8, 'ثبت علت های پرداخت', '/basicData/reason/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (9, 'ثبت فایل ها', '/basicData/file/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (10, 'ویرایش صندوق ها', '/basicData/fund/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (11, 'ویرایش شعب', '/basicData/branch/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (12, 'ویرایش کاربران', '/basicData/user/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (13, 'ویرایش گروه کاربری', '/basicData/groupUser/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (14, 'ویرایش نقش ها', '/basicData/role/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (15, 'ویرایش پارامترها', '/basicData/param/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (16, 'ویرایش مدیریت OTP', '/basicData/OTP/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (17, 'ویرایش علت های پرداخت', '/basicData/reason/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (18, 'ویرایش فایل ها', '/basicData/file/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (19, 'حذف صندوق ها', '/basicData/fund/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (20, 'حذف شعب', '/basicData/branch/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (21, 'حذف کاربران', '/basicData/user/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (22, 'حذف گروه کاربری', '/basicData/groupUser/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (23, 'حذف نقش ها', '/basicData/role/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (24, 'حذف پارامترها', '/basicData/param/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (25, 'حذف مدیریت OTP', '/basicData/OTP/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (26, 'حذف علت های پرداخت', '/basicData/reason/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (27, 'حذف فایل ها', '/basicData/file/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (28, 'نمایش صندوق ها', '/basicData/fund', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (29, 'نمایش شعب', '/basicData/branch', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (30, 'نمایش کاربران', '/basicData/user', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (31, 'نمایش گروه کاربری', '/basicData/groupUser', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (32, 'نمایش نقش ها', '/basicData/role', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (33, 'نمایش پارامترها', '/basicData/param', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (34, 'نمایش مدیریت OTP', '/basicData/OTP', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (35, 'نمایش علت های پرداخت', '/basicData/reason', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (36, 'نمایش فایل ها', '/basicData/file', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (37, 'انواع ورود دوعاملی', '/basicData/getOtpStrategies', 0)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (38, 'دریافت مقدار عددی پارامترها', '/basicData/param/getLongValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (39, 'دریافت مقدار رشته ای پارامترها', '/basicData/param/getStringValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (40, 'دریافت مقدار اعشاری با دقت بالا پارامترها', '/basicData/param/getDoubleValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (41, 'دریافت مقدار اعشاری با دقت پایین پارامترها', '/basicData/param/getFloatValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (42, 'دریافت مقدار منطقی پارامترها', '/basicData/param/getBooleanValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (43, 'دریافت معین تنظیم حساب', '/basicData/param/getSubsidiaryLedger', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (44, 'دریافت تفصیل تنظیم حساب', '/basicData/param/getDetailLedger', 1)
    /

UPDATE AHA_PERMISSION SET URL = '/api/v1' || URL
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (45, 'ورود', '/login/**', 0)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (46, 'swagger', '/v3/api-docs/**', 0)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (47, 'swagger', '/swagger-ui.html', 0)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (48, 'کد دوعاملی برای ورود کاربر', '/sendOtpForLogin', 0)
/

----------------------------------------------------------------------------------------------------
Insert into AHA_ROLE
(ID, NAME)
Values
    (1, 'مدیر صندوق')
    /
Insert into AHA_ROLE
(ID, NAME)
Values
    (2, 'متولی صندوق')
    /
Insert into AHA_ROLE
(ID, NAME)
Values
    (3, 'پشتیبانی ارشد')
    /
Insert into AHA_ROLE
(ID, NAME)
Values
    (4, 'پشتیبان')
    /
Insert into AHA_ROLE
(ID, NAME)
Values
    (5, 'سرمایه گذار')
    /
Insert into AHA_ROLE
(ID, NAME)
Values
    (6, 'کاربر صندوق')
    /
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_ROLE_PERMISSION
select ROWNUM ID,TBL.* from  (
                                 select AR.ID F_ROLE_ID,AP.ID F_PERMISSION_ID,AR.INSERTED_DATE_TIME,AR.INSERTED_USER_ID,AR.UPDATED_DATE_TIME,AR.UPDATED_USER_ID from  AHA_ROLE AR,AHA_PERMISSION AP
                                 WHERE AR.ID IN (3,4)) TBL
    /
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_PERSON
select ROWNUM ID, TBL.* from  (
                                  select FIRST_NAME,LAST_NAME,NULL PARENT,
                                         NULL BIRTH_DATE,NULL ISSUING_CITY,
                                         NULL BIRTH_CERTIFICATION_NUMBER,
                                         NULL BIRTH_CERTIFICATION_ID,PHONE,
                                         NULL NATIONAL_CODE,FAX,CELL_PHONE,POSTAL_CODE,ADDRESS,E_MAIL,
                                         0 IS_COMPANY,NULL COMPANY_NAME,NULL REGISTERATION_NUMBER,APPUSER_ID,
                                         NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID from  APPUSER
                                  WHERE APPUSER_ID>0
                                    AND USERNAME NOT IN ('vn','rh1','rayan')) TBL
    /

INSERT INTO AHA_PERSON
SELECT (select max(id) from  AHA_PERSON)+ROWNUM ID, TBL.*
FROM (SELECT ao.username FIRST_NAME,
             ao.username LAST_NAME,
             NULL     PARENT,
             NULL     BIRTH_DATE,
             NULL     ISSUING_CITY,
             NULL     BIRTH_CERTIFICATION_NUMBER,
             NULL     BIRTH_CERTIFICATION_ID,
             NULL     PHONE,
             NULL     NATIONAL_CODE,
             NULL     FAX,
             ao.MOBILE_NUMBER,
             NULL     POSTAL_CODE,
             NULL     ADDRESS,
             ao.EMAIL,
             0        IS_COMPANY,
             NULL     COMPANY_NAME,
             NULL     REGISTERATION_NUMBER,
             ao.APPUSER_OTP_ID,
             NULL     INSERTED_DATE_TIME,
             NULL     INSERTED_USER_ID,
             NULL     UPDATED_DATE_TIME,
             NULL     UPDATED_USER_ID
      FROM appuser_otp  ao
               INNER JOIN appuser a ON a.APPUSER_ID = ao.APPUSER_ID
      WHERE ao.otp_id > 0) TBL
    /

----------------------------------------------------------------------------------------------------
Insert into AHA_USER_GROUP
(ID, NAME)
Values
    (1, 'گروه کاربران صندوق')
    /
Insert into AHA_USER_GROUP
(ID, NAME)
Values
    (2, 'گروه سرمایه گذاران')
    /
Insert into AHA_USER_GROUP
(ID, NAME)
Values
    (3, 'گروه مدیران')
    /
Insert into AHA_USER_GROUP
(ID, NAME)
Values
    (4, 'گروه متولیان')
    /
Insert into AHA_USER_GROUP
(ID, NAME)
Values
    (5, 'گروه کاربران پشتیبانی')
    /

----------------------------------------------------------------------------------------------------
insert into AHA_VERIFICATION_CODE
select OTP_ID,SERIAL,SEED,COUNTER,ENABLED,null,null,null,null from otp
where OTP_ID>0
    /
----------------------------------------------------------------------------------------------------
INSERT INTO aha_users
select ROWNUM ID,TBL.* from  (
                                 select USERNAME,PASSWORD,IS_ACTIVE,0 IS_ADMIN
                                      ,(select MAX(ID) from  AHA_PERSON WHERE REF_ID=AU.APPUSER_ID) F_PERSON_ID
                                      ,OTP_ID
                                      ,NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID ,NULL UPDATED_DATE_TIME , NULL UPDATED_USER_ID
                                 from  appuser  AU where not exists(select 1 from  appuser_otp ao where AU.username = ao.username)) TBL WHERE F_PERSON_ID IS NOT NULL
    /

INSERT INTO aha_users

SELECT (select max(id) from  aha_users)+ROWNUM ID, TBL.*
FROM (SELECT ao.USERNAME,
             a.PASSWORD,
             IS_ACTIVE,
             0                                  IS_ADMIN,
             (SELECT MAX (ID)
              FROM AHA_PERSON
              WHERE REF_ID = ao.APPUSER_OTP_ID)    F_PERSON_ID,
             ao.OTP_ID,
             NULL                               INSERTED_DATE_TIME,
             NULL                               INSERTED_USER_ID,
             NULL                               UPDATED_DATE_TIME,
             NULL                               UPDATED_USER_ID
      FROM appuser_otp  ao
               INNER JOIN appuser a ON a.APPUSER_ID = ao.APPUSER_ID
      WHERE ao.otp_id > 0) TBL
WHERE F_PERSON_ID IS NOT NULL
    /

INSERT INTO AHA_USER_GROUP_DETAIL
select ROWNUM ID,TBL.* from  (
                                 select 1 F_USER_GROUP_ID, ID F_USER_ID,INSERTED_DATE_TIME,INSERTED_USER_ID ,UPDATED_DATE_TIME , UPDATED_USER_ID  from  AHA_USERS
                             ) TBL
    /
----------------------------------------------------------------------------------------------------
Insert into AHA_USER_GROUP_ROLE
(ID, F_USER_GROUP_ID, F_ROLE_ID)
Values
    (1, 3, 1)
    /
Insert into AHA_USER_GROUP_ROLE
(ID, F_USER_GROUP_ID, F_ROLE_ID)
Values
    (2, 4, 2)
    /
Insert into AHA_USER_GROUP_ROLE
(ID, F_USER_GROUP_ID, F_ROLE_ID)
Values
    (3, 5, 3)
    /
Insert into AHA_USER_GROUP_ROLE
(ID, F_USER_GROUP_ID, F_ROLE_ID)
Values
    (4, 5, 4)
    /
Insert into AHA_USER_GROUP_ROLE
(ID, F_USER_GROUP_ID, F_ROLE_ID)
Values
    (5, 2, 5)
    /
Insert into AHA_USER_GROUP_ROLE
(ID, F_USER_GROUP_ID, F_ROLE_ID)
Values
    (6, 1, 6)
    /

----------------------------------------------------------------------------------------------------
INSERT INTO AHA_COMPANY
select 1 id ,tbl.*,null,null,null,null  from  (select SMS_NUMBER,company_name from  company where fund_id=1) tbl
/
----------------------------------------------------------------------------------------------------