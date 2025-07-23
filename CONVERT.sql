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
select nf.FUND_ID,nf.FUND_NAME,f.is_etf IS_ETF,NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID from  N_FUND nf,fund f where nf.fund_id=f.fund_id
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

update  aha_params set f_fund_id=null,IS_GLOBAL=1
where code in ('TWO_FACTOR_LOGIN_WITH_SMS_APPUSERS','TWO_FACTOR_LOGIN_WITH_PHYSICAL_APPUSERS','TWO_FACTOR_LOGIN_WITH_EMAIL_APPUSERS')
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

update  aha_params
set code='IS_NEW_FOREIGN_CUSTOMER_VALID'
where code='ّIS_NEW_FOREIGN_CUSTOMER_VALID'
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
    (1, 'ثبت صندوق ها', '/administration/fund/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (2, 'ثبت شعب', '/administration/branch/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (3, 'ثبت کاربران', '/authentication/user/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (4, 'ثبت گروه کاربری', '/basicData/groupUser/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (5, 'ثبت نقش ها', '/authentication/role/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (6, 'ثبت پارامترها', '/administration/param/add', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (7, 'ثبت مدیریت OTP', '/administration/verificationCode/add', 1)
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
    (10, 'ویرایش صندوق ها', '/administration/fund/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (11, 'ویرایش شعب', '/administration/branch/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (12, 'ویرایش کاربران', '/authentication/user/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (13, 'ویرایش گروه کاربری', '/basicData/groupUser/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (14, 'ویرایش نقش ها', '/authentication/role/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (15, 'ویرایش پارامترها', '/administration/param/edit', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (16, 'ویرایش مدیریت OTP', '/administration/verificationCode/edit', 1)
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
    (19, 'حذف صندوق ها', '/administration/fund/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (20, 'حذف شعب', '/administration/branch/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (21, 'حذف کاربران', '/authentication/user/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (22, 'حذف گروه کاربری', '/basicData/groupUser/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (23, 'حذف نقش ها', '/authentication/role/remove/{id}', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (24, 'حذف پارامترها', '/administration/param/remove', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (25, 'حذف مدیریت OTP', '/administration/verificationCode/remove', 1)
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
    (28, 'نمایش صندوق ها', '/administration/fund', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (29, 'نمایش شعب', '/administration/branch', 1)
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
    (32, 'نمایش نقش ها', '/authentication/role', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (33, 'نمایش پارامترها', '/administration/param', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (34, 'نمایش مدیریت OTP', '/administration/verificationCode', 1)
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
    (37, 'انواع ورود دوعاملی', '/getOtpStrategies', 0)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (38, 'دریافت مقدار عددی پارامترها', '/administration/param/getLongValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (39, 'دریافت مقدار رشته ای پارامترها', '/administration/param/getStringValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (40, 'دریافت مقدار اعشاری با دقت بالا پارامترها', '/administration/param/getDoubleValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (41, 'دریافت مقدار اعشاری با دقت پایین پارامترها', '/administration/param/getFloatValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (42, 'دریافت مقدار منطقی پارامترها', '/administration/param/getBooleanValue', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (43, 'دریافت معین تنظیم حساب', '/administration/param/getSubsidiaryLedger', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (44, 'دریافت تفصیل تنظیم حساب', '/administration/param/getDetailLedger', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (45, 'ثبت مجوز', '/authentication/permission/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (46, 'ویرایش مجوز', '/authentication/permission/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (47, 'حذف مجوز', '/authentication/permission/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (48, 'ثبت گروه کاربری', '/authentication/userGroup/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (49, 'ویرایش گروه کاربری', '/authentication/userGroup/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (50, 'حذف گروه کاربری', '/authentication/userGroup/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (51, 'تخصیص مجوزها به نقش', '/authentication/permission/assignPermissionToRole', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (52, 'تخصیص مجوزها به نقش', '/authentication/permission/assignRoleToUserGroup', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (53, 'تخصیص کاربرها به گروه کاربری', '/authentication/user/assignUserToGroup', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (54, 'تخصیص نقش ها به کاربر', '/authentication/user/assignRoleToUser', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (55, 'تخصیص مجوزها به کاربر', '/authentication/user/assignPermissionToUser', 1)
    /


Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (56, 'ثبت تقویم کاری', '/administration/calendar/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (57, 'ویرایش تقویم کاری', '/administration/calendar/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (58, 'حذف تقویم کاری', '/administration/calendar/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (59, 'لیست تقویم کاری', '/administration/calendar', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (60, 'ثبت تفصیلی', '/accounting/detailLedger/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (70, 'ویرایش تفصیلی', '/accounting/detailLedger/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (80, 'حذف تفصیلی', '/accounting/detailLedger/remove', 1)
    /


Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (90, 'لیست تفصیلی', '/accounting/detailLedger', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (91, 'ثبت سرمایه گذار', '/baseInformation/customer/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (92, 'ثبت سرمایه گذار به صورت گروهی', '/baseInformation/customer/batch/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (93, 'ویرایش سرمایه گذار', '/baseInformation/customer/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (94, 'ویرایش سرمایه گذار به صورت گروهی', '/baseInformation/customer/batch/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (95, 'حذف سرمایه گذار', '/baseInformation/customer/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (96, 'لیست سرمایه گذار', '/baseInformation/customer', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (97, 'ثبت حساب بانکی سرمایه گذار', '/baseInformation/customer/bankAccount/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (98, 'ویرایش حساب بانکی سرمایه گذار', '/baseInformation/customer/bankAccount/edit', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (99, 'حذف حساب بانکی سرمایه گذار', '/baseInformation/customer/bankAccount/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'حذف حساب بانکی سرمایه گذار', '/baseInformation/customer/bankAccount/default', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (101, 'ثبت حساب بانکی نهاد های مالی', '/baseInformation/financialInstitution/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (103, 'حذف حساب بانکی نهاد های مالی', '/baseInformation/financialInstitution/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (104, 'نمایش حساب بانکی نهاد های مالی', '/baseInformation/financialInstitution', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (105, 'تخصیص تفصیلی به آیتم های معاملاتی', '/baseInformation/tradableItem/detailLedger/add', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (106, 'حذف تفصیلی آیتم های معاملاتی', '/baseInformation/tradableItem/detailLedger/remove', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (107, 'لیست آیتم های معاملاتی', '/baseInformation/tradableItem', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (108, 'به روز رسانی توکن', '/refreshToken', 1)
    /

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (109, 'ثبت صندوق تحت تملک', '/baseInformation/fundOwnership/add', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (110, 'حذف صندوق تحت تملک', '/baseInformation/fundOwnership/remove', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (111, 'نمایش صندوق تحت تملک', '/baseInformation/fundOwnership', 1)
    /
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (112, 'نمایش مجوز های دسترسی', '/authentication/permission', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (113, 'ثبت دلایل پرداخت', '/paymentModule/paymentReason/add', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (114, 'ویرایش دلایل پرداخت', '/paymentModule/paymentReason/edit', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (115, 'نمایش دلایل پرداخت', '/paymentModule/paymentReason', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (116, 'نمایش دلایل پرداخت', '/paymentModule/paymentReason', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (117, 'حذف دلایل پرداخت', '/paymentModule/paymentReason/remove', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (118, 'ثبت پرداخت', '/paymentModule/payment/add', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (119, 'ویرایش پرداخت', '/paymentModule/payment/edit', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (120, 'حذف پرداخت', '/paymentModule/payment/remove', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (121, 'نمایش پرداخت', '/paymentModule/payment', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (122, 'تغییر وضعیت پرداخت', '/paymentModule/payment/changeStatus', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (123, 'ثبت سند حسابداری', '/accounting/voucher/add', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (124, 'ویرایش سند حسابداری', '/accounting/voucher/edit', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (125, 'حذف سند حسابداری', '/accounting/voucher/remove', 1)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (126, 'نمایش سند حسابداری', '/accounting/voucher', 1)
/





UPDATE AHA_PERMISSION SET URL = '/api/v1' || URL
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    ((select max(id)+1 from AHA_PERMISSION), 'ورود', '/login/**', 0)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    ((select max(id)+1 from AHA_PERMISSION), 'swagger', '/v3/api-docs/**', 0)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    ((select max(id)+1 from AHA_PERMISSION), 'swagger', '/swagger-ui.html', 0)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    ((select max(id)+1 from AHA_PERMISSION), 'کد دوعاملی برای ورود کاربر', '/sendOtpForLogin', 0)
/

Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت لیست حساب های بانکی سرمایه گذار', '/baseInformation/customer/bankAccount/{customerId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت لیست گروه های کاربری کاربر', '/authentication/user/userGroupPerUserId/{userId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت مجوزهای یک کاربر', '/authentication/user/userPermissionPerUserId/{userId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت مجوزهای یک نقش', '/authentication/role/rolePermissionPerRoleId/{roleId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده گروه کاربری', '/authentication/userGroup/{id}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت گروه های کاربری', '/authentication/userGroup', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت نقش های  گروه کاربری', '/authentication/userGroup/userGroupRolePerUserGroup/{userGroupId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده اطلاعات کاربر', '/authentication/user/{id}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده اطلاعات کاربران', '/authentication/user', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده نقش های کاربر', '/authentication/user/userRolePerUser/{userId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت نقش ها', '/authentication/role', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده نقش', '/authentication/role/{id}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت بانک ها', '/baseInformation/customer/bank', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'دریافت نوع حساب های بانکی', '/baseInformation/customer/bankAccountType', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده نوع حساب', '/baseInformation/customer/bankAccountType/{bankAccountTypeId}', 1)
/
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده نوع حساب', '/baseInformation/customer/bankAccountType/{bankAccountTypeId}', 1)
Insert into AHA_PERMISSION
(ID, NAME, URL, IS_SENSITIVE)
Values
    (100, 'مشاهده بانک', '/baseInformation/customer/bank/{bankId}', 1)
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
                                         0 IS_COMPANY,NULL COMPANY_NAME,NULL REGISTERATION_NUMBER,NULL LATIN_FIRST_NAME,NULL LATIN_LAST_NAME,0 IS_IRANIAN,APPUSER_ID,
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
             NULL LATIN_FIRST_NAME,
             NULL LATIN_LAST_NAME,
             0    IS_IRANIAN,
             ao.APPUSER_OTP_ID,
             NULL     INSERTED_DATE_TIME,
             NULL     INSERTED_USER_ID,
             NULL     UPDATED_DATE_TIME,
             NULL     UPDATED_USER_ID
      FROM appuser_otp  ao
               INNER JOIN appuser a ON a.APPUSER_ID = ao.APPUSER_ID
      WHERE ao.otp_id > 0) TBL
    /

insert into aha_person
select (select max(id) from  aha_person)+ROWNUM ID, TBL.* from  (
                                                                    select FIRST_NAME,LAST_NAME,PARENT,
                                                                           BIRTH_DATE,NULL ISSUING_CITY,
                                                                           BIRTH_CERTIFICATION_NUMBER,
                                                                           BIRTH_CERTIFICATION_ID,PHONE,
                                                                           NATIONAL_CODE,FAX,CELL_PHONE,POSTAL_CODE,ADDRESS,E_MAIL,
                                                                           IS_COMPANY,COMPANY_NAME,REGISTERATION_NUMBER,LATIN_FIRST_NAME,LATIN_LAST_NAME,IS_IRANIAN,CUSTOMER_ID,
                                                                           NULL INSERTED_DATE_TIME,NULL INSERTED_USER_ID,NULL UPDATED_DATE_TIME,NULL UPDATED_USER_ID
                                                                    from  t_customer
                                                                    WHERE CUSTOMER_ID>0) TBL
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
select OTP_ID,SEED,COUNTER,ENABLED,null,null,null,null from otp
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
select 1 id ,tbl.*,tbl2.BOURSE_ACCOUNT_NUMBER,tbl2.BOURSE_ACCOUNT_NAME,null,null,null,null  from  (select SMS_NUMBER,company_name from  company where fund_id=1) tbl,(select MAX(BOURSE_ACCOUNT_NAME) BOURSE_ACCOUNT_NAME,MAX(BOURSE_ACCOUNT_NUMBER) BOURSE_ACCOUNT_NUMBER from  bourse_account)tbl2
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_MMTP_CONFIG
select ROWNUM,TBL.*,NULL,NULL,NULL,NULL from  (select MMTP_BROKER_ID,MMTP_APP_ID,MMTP_TRADER_ID,INS_MAX_LCODE_FUND2,FUND_ACCOUNT_NUMBER,INS_MAX_LCODE_FUND,INS_MNEMONIC_CODE_FUND,RESERVE_ORDER_ORIGIN,FUND_ID from  MMTP_CONFIG) TBL
/
----------------------------------------------------------------------------------------------------
insert into aha_wage_rate
select rownum id, tbl.*,null,null,null,null from  (
                                                      select FUND_ID,INSTRUMENT_TYPE_ID,INST_TYPE_DERIVATIVES_ID,IS_OTC,IS_PURCHASE
                                                      from  fund_wage_rate
                                                      group by FUND_ID,INSTRUMENT_TYPE_ID,INST_TYPE_DERIVATIVES_ID,IS_OTC,IS_PURCHASE
                                                  ) tbl
/
----------------------------------------------------------------------------------------------------
insert into aha_wage_rate_detail
select rownum id,tbl.*,null,null,null,null from  (
                                                     select awr.id,INDUSTRY_ID,INSTRUMENT_ID,BROKERAGE_ID,fwr.change_date
                                                          ,fwr.BOURSE_CO,fwr.DEPOSIT_CO,fwr.BOURSE_ORG,fwr.IT_MANAGEMENT,fwr.INTEREST,fwr.TAX,fwr.MAX_BOURSE_CO,fwr.MAX_DEPOSIT_CO,fwr.MAX_BOURSE_ORG,fwr.MAX_IT_MANAGEMENT,fwr.MAX_INTEREST,fwr.RAYAN_BOURSE,fwr.MAX_RAYAN_BOURSE
                                                     from  aha_wage_rate awr
                                                               inner join  fund_wage_rate fwr on fwr.fund_id= awr.f_FUND_ID
                                                         and (fwr.INSTRUMENT_TYPE_ID= awr.f_INSTRUMENT_TYPE_ID or (fwr.INSTRUMENT_TYPE_ID is null and awr.f_INSTRUMENT_TYPE_ID is null))
                                                         and fwr.INST_TYPE_DERIVATIVES_ID= awr.f_INST_TYPE_DERIVATIVES_ID
                                                         and fwr.IS_OTC= awr.IS_OTC
                                                         and fwr.IS_PURCHASE= awr.IS_PURCHASE) tbl order by change_date
/
----------------------------------------------------------------------------------------------------
insert into AHA_CUSTOMER_STATUS
select CUSTOMER_STATUS_ID id,CUSTOMER_STATUS_NAME name ,null,null,null,null from  customer_status
/
----------------------------------------------------------------------------------------------------
insert into aha_customer
select customer_id id,dl_id ,CUSTOMER_STATUS_ID,p.id,null,COMMENTS,IS_SMS_SEND,decode(SEJAM_STATUS_TYPE_ID,7,1,0)
     ,PROFIT_RATE
     ,IS_PROFIT_ISSUE
     ,IS_VAT
     ,IS_EPAYMENT_CUSTOMER
     ,convert_to_timestamp(CREATION_DATE, CREATION_TIME)
     ,INSERT_USER_ID
     ,convert_to_timestamp(MODIFICATION_DATE, MODIFICATION_TIME)
     ,UPDATE_USER_ID
from  t_customer c
          inner join aha_person p on p.REF_ID=c.CUSTOMER_ID
where customer_id >0
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_BANK_ACCOUNT_TYPE
select BA_TYPE_ID ID , BA_TYPE_NAME NAME , NULL, NULL, NULL, NULL from  BANK_ACCOUNT_TYPE
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_BANK_ACCOUNT
select ba.bank_account_id,ba.is_active,ba.ba_type_id,bb.bank_id,ba.account_number,ba.annual_interest,NULL,NULL,NULL,NULL,NULL
from  bank_account ba
          inner join bank_branch bb on bb.bank_branch_id=ba.bank_branch_id
/

INSERT INTO AHA_BANK_ACCOUNT
select ba.bank_account_id,ba.is_active,ba.ba_type_id,ba.bank_id,ba.account_number,0,ba.SHABA_NUMBER,NULL,NULL,NULL,NULL
from  customer_bank_account ba
/

insert into aha_bank_account
select DL_BANK_ACCOUNT_ID id,is_active,null,bank_id,ACCOUNT_NUMBER,0,SHEBA_NUMBER,null,null,null,null
from  detail_ledger_bank_account
/
----------------------------------------------------------------------------------------------------
insert into AHA_FUND_BANK_ACCOUNT
select rownum,ba.fund_id,ba.bank_account_id,ba.dl_id,null,null,null,null
from  bank_account ba
/
----------------------------------------------------------------------------------------------------
insert into AHA_CUSTOMER_BANK_ACCOUNT
select rownum,cba.customer_id,cba.bank_account_id,null,null,null,null
from customer_bank_account cba
/
----------------------------------------------------------------------------------------------------
MERGE INTO aha_customer ac
    USING (select customer_id,cba.id bank_account_id from  t_customer tc left join aha_customer_bank_account cba on cba.f_customer_id=tc.customer_id and cba.f_bank_account_id=tc.bank_account_id) tc
ON (tc.customer_id = ac.id)
    WHEN MATCHED THEN
UPDATE SET ac.f_customer_bank_account_id = tc.bank_account_id
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_FINANCIAL_INSTITUTION
select dlba.dl_bank_account_id,dlt.name || ' - ' || dl.name ,dlba.dl_id,c.f_person_id,NULL,NULL,NULL,NULL
from  detail_ledger_bank_account dlba
          inner join aha_detail_ledger dl on dl.id=dlba.dl_id
          inner join aha_detail_ledger_type dlt on dlt.id=dl.f_detail_ledger_type_id
          left  join financial_company fc on fc.dl_bank_account_id=dlba.dl_bank_account_id
          left  join aha_customer c on c.id=fc.customer_id
/
----------------------------------------------------------------------------------------------------
insert into AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT
select rownum,dlba.dl_bank_account_id,dlba.dl_bank_account_id,dlba.is_active,null,null,null,null from  detail_ledger_bank_account dlba
/
----------------------------------------------------------------------------------------------------
insert into AHA_TRADABLE_ITEM_DETAIL_LEDGER
select rownum,vw.id,vw.type_id,vw.TRADABLE_ITEM_GROUP,dl_id,null,null,null,null
from  instrument_dl idl
          inner join vw_tradable_item vw on vw.ID=idl.instrument_id and  TRADABLE_ITEM_GROUP=1
/

insert into AHA_TRADABLE_ITEM_DETAIL_LEDGER
select (select max(id) from  AHA_TRADABLE_ITEM_DETAIL_LEDGER)+rownum id,vw.id,vw.type_id,vw.TRADABLE_ITEM_GROUP,dl_id,null,null,null,null
from  ime_contract_dl idl
          inner join vw_tradable_item vw on vw.ID=idl.IME_CONTRACT_ID and  TRADABLE_ITEM_GROUP=2
/

insert into AHA_TRADABLE_ITEM_DETAIL_LEDGER
select (select max(id) from  AHA_TRADABLE_ITEM_DETAIL_LEDGER)+rownum id,vw.id,vw.type_id,vw.TRADABLE_ITEM_GROUP,dl_id,null,null,null,null
from  bourse_fund_dl idl
          inner join vw_tradable_item vw on vw.ID=idl.BOURSE_FUND_ID and  TRADABLE_ITEM_GROUP=3
/

insert into AHA_TRADABLE_ITEM_DETAIL_LEDGER
select (select max(id) from AHA_TRADABLE_ITEM_DETAIL_LEDGER)+rownum id, ti.id,ti.type_id,ti.TRADABLE_ITEM_GROUP,bd.dl_id,null,null,null,null
from  BROKERAGE_DL bd
          inner join VW_TRADABLE_ITEM ti on ti.id=bd.BROKERAGE_id and ti.TRADABLE_ITEM_GROUP=4
/
----------------------------------------------------------------------------------------------------
insert into aha_fund_ownership
select rownum,bourse_fund_id,instrument_id,null,null,null,null from  fund_ownership
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_PAYMENT_TYPE
select RP_TYPE_ID,RP_TYPE_NAME,NULL,NULL,NULL,NULL from  RP_TYPE
/
----------------------------------------------------------------------------------------------------
insert into AHA_PAYMENT_STATUS
select PAYMENT_OGS_ID,PAYMENT_OGS_NAME,'PAYMENT_DETAIL',null,null,null,null from  payment_og_status
/

insert into AHA_PAYMENT_STATUS
select (select MAX(ID) from  AHA_PAYMENT_STATUS)+PAYMENT_OGM_STATUS_ID ID,PAYMENT_OGM_STATUS_NAME,'PAYMENT',null,null,null,null from  payment_ogM_status
/
----------------------------------------------------------------------------------------------------
insert into AHA_PAYMENT_ORIGIN
select PAYMENT_OG_ORIGIN_ID,PAYMENT_OG_ORIGIN_NAME,null,null,null,null from  PAYMENT_og_ORIGIN
/
----------------------------------------------------------------------------------------------------
insert into AHA_PAYMENT
select NR.RECEIPT_ID id,RECEIPT_NUMBER,NR.branch_id,NR.RP_REASON_ID,nr.from_sl_id,null,nvl(tc.dl_id,FROM_DL_ID) dl_id ,NR.fund_id,nr.RP_TYPE_ID,NULL,NULL,null,nr.RECEIPT_DATE,nr.COMMENTS,nr.IS_MANUAL,0,NULL
     ,convert_to_timestamp(nr.CREATION_DATE,nr.CREATION_TIME) INSERTED_DATE_TIME,nr.APPUSER_ID,convert_to_timestamp(nr.MODIFICATION_DATE,nr.MODIFICATION_TIME) UPDATED_DATE_TIME,nr.APPUSER_ID
from  N_RECEIPT NR
          left join t_customer tc on tc.customer_id=NR.FROM_CUSTOMER_ID
/

insert into AHA_PAYMENT_DETAIL
select NW.wire_id,NR.RECEIPT_ID id,ba.dl_id,NULL,fo.fund_order_id,ba.bank_account_id,NW.AMOUNT,NW.COMMENTS,null,p.INSERTED_DATE_TIME,INSERTED_USER_ID,UPDATED_DATE_TIME,UPDATED_USER_ID
from  N_RECEIPT NR
          INNER JOIN n_wire NW ON NW.RECEIPT_ID=NR.RECEIPT_ID
          left  join fund_order fo on fo.RECEIPT_id=nr.RECEIPT_id
          inner join bank_account ba on ba.bank_account_id= NW.TO_BANK_ACCOUNT_ID
          inner join aha_payment p on p.id=NR.RECEIPT_ID
/

insert into AHA_PAYMENT
select p.PAYMENT_ID,p.PAYMENT_NUMBER,p.BRANCH_ID,p.RP_REASON_ID,p.FROM_SL_ID,p.TO_SL_ID,nvl(ba.dl_id,nvl(tc.dl_id,p.from_dl_id)) dl_id,p.fund_id,p.RP_TYPE_ID,NULL,NULL,nvl(ba.bank_account_id,tc.bank_account_id),p.payment_date,p.comments,p.is_manual,0,NULL
     ,convert_to_timestamp(p.CREATION_DATE,p.CREATION_TIME) INSERTED_DATE_TIME,p.APPUSER_ID,convert_to_timestamp(p.MODIFICATION_DATE,p.MODIFICATION_TIME) UPDATED_DATE_TIME,p.APPUSER_ID
from  payment p
          left join bank_account ba on ba.bank_account_id=p.form_bank_account_id
          left join bank_branch bb on bb.bank_branch_id=ba.bank_branch_id
          left join t_customer tc on tc.customer_id=p.from_customer_id
where p.rp_type_id not in (7,8,9)
/

insert into aha_payment_detail
select (select max(id) from aha_payment_detail )+rownum id,p.PAYMENT_ID,nvl(ba.dl_id,nvl(tc.dl_id,p.to_dl_id)) dl_id,NULL,fo.fund_order_id,nvl(ba.bank_account_id,tc.bank_account_id),p.amount,p.COMMENTS,null
     ,ap.INSERTED_DATE_TIME,ap.INSERTED_USER_ID,ap.UPDATED_DATE_TIME,ap.UPDATED_USER_ID
from  payment p
          left join bank_account ba on ba.bank_account_id=p.to_bank_account_id
          left join bank_branch bb on bb.bank_branch_id=ba.bank_branch_id
          left join t_customer tc on tc.customer_id=p.to_customer_id
          left join fund_order fo on fo.PAYMENT_ID=p.payment_id
          inner join aha_payment ap on ap.id=p.payment_id
where p.rp_type_id not in (7,8,9)
/

insert into aha_payment
select p.payment_id,p.PAYMENT_NUMBER,p.BRANCH_ID,p.rp_reason_id,p.from_sl_id,p.to_sl_id,nvl(ba.dl_id,nvl(ba.dl_id,tc.dl_id)) dl_id,p.fund_id,p.rp_type_id,NULL,NULL,nvl(ba.bank_account_id,tc.bank_account_id),p.payment_date,p.comments,p.is_manual,0,NULL
     ,convert_to_timestamp(p.CREATION_DATE,p.CREATION_TIME) INSERTED_DATE_TIME,p.APPUSER_ID,convert_to_timestamp(p.MODIFICATION_DATE,p.MODIFICATION_TIME) UPDATED_DATE_TIME,p.APPUSER_ID
from  payment p
          left join bank_account ba on ba.bank_account_id=p.form_bank_account_id
          left join t_customer tc on tc.customer_id=p.from_customer_id
where p.rp_type_id=7
/

insert into aha_payment_detail
select pt.pay_to_id,p.payment_id,nvl(tc.dl_id,pt.to_dl_id) dl_id,NULL,NULL,tc.bank_account_id,pt.amount,pt.comments,null
     ,ap.inserted_date_time,ap.inserted_user_id,ap.updated_date_time,ap.updated_user_id
from  payment p
          inner join pay_to pt on pt.payment_id=p.payment_id
          left  join t_customer tc on tc.customer_id=pt.to_customer_id
          inner join aha_payment ap on ap.id=p.payment_id
where p.rp_type_id=7
/

INSERT INTO AHA_PAYMENT
select DISTINCT POM.PAYMENT_OGM_ID,POM.PAYMENT_NUMBER,POM.BRANCH_ID,P.RP_REASON_ID,P.FROM_SL_ID,P.TO_SL_ID,BA.DL_ID,POM.FUND_ID,P.RP_TYPE_ID,POM.POGM_STATUS_ID+7 POGM_STATUS_ID,POM.PAYMENT_ORIGIN_ID,PCT.BANK_ACCOUNT_ID,POM.PAYMENT_DATE,POM.COMMENTS,P.IS_MANUAL,POM.SENT_TO_BANK,POM.MASTER_UUID
              ,convert_to_timestamp(p.CREATION_DATE,p.CREATION_TIME) INSERTED_DATE_TIME,p.APPUSER_ID,convert_to_timestamp(p.MODIFICATION_DATE,p.MODIFICATION_TIME) UPDATED_DATE_TIME,p.APPUSER_ID
from  PAYMENT P
          INNER JOIN PAYMENT_OG_LINE POL ON POL.PAYMENT_ID=P.PAYMENT_ID
          INNER JOIN PAYMENT_OG_MASTER POM ON POM.PAYMENT_OGM_ID=POL.PAYMENT_OGM_ID
          INNER JOIN PAYABLE_CHECK_TEMPLATE PCT ON PCT.PC_TEMPLATE_ID=POM.PC_TEMPLATE_ID
          INNER JOIN BANK_ACCOUNT BA ON BA.BANK_ACCOUNT_ID=P.FORM_BANK_ACCOUNT_ID
          INNER JOIN BANK_BRANCH BB ON BB.BANK_BRANCH_ID=BA.BANK_BRANCH_ID
WHERE P.RP_TYPE_ID=8
/

INSERT INTO AHA_PAYMENT_DETAIL
select P.PAYMENT_ID,POM.PAYMENT_OGM_ID,NVL(TC.DL_ID,POL.TO_DL_ID),POL.PAYMENT_OGS_ID,POL.FUND_ORDER_ID,NVL(POL.BANK_ACCOUNT_ID,ABA.ID),POL.AMOUNT,POL.COMMENTS,POL.LINE_UUID
     ,AP.INSERTED_DATE_TIME,AP.INSERTED_USER_ID,AP.UPDATED_DATE_TIME,AP.UPDATED_USER_ID
from  PAYMENT P
          INNER JOIN PAYMENT_OG_LINE POL ON POL.PAYMENT_ID=P.PAYMENT_ID
          INNER JOIN PAYMENT_OG_MASTER POM ON POM.PAYMENT_OGM_ID=POL.PAYMENT_OGM_ID
          LEFT  JOIN T_CUSTOMER TC ON TC.CUSTOMER_ID=POL.TO_CUSTOMER_ID
          LEFT  JOIN AHA_BANK_ACCOUNT ABA  ON ABA.ID= POL.TO_DL_BANK_ACCOUNT_ID
          INNER JOIN AHA_PAYMENT AP ON AP.ID=POM.PAYMENT_OGM_ID
WHERE P.RP_TYPE_ID=8
/

INSERT INTO AHA_PAYMENT
select distinct POM.PAYMENT_OGM_ID,POM.PAYMENT_NUMBER,POM.BRANCH_ID,P.RP_REASON_ID,P.FROM_SL_ID,P.TO_SL_ID,BA.DL_ID,POM.FUND_ID,P.RP_TYPE_ID,POM.POGM_STATUS_ID+7 POGM_STATUS_ID,POM.PAYMENT_ORIGIN_ID,PCT.BANK_ACCOUNT_ID,POM.PAYMENT_DATE,POM.COMMENTS,P.IS_MANUAL,POM.SENT_TO_BANK,POM.MASTER_UUID
              ,convert_to_timestamp(p.CREATION_DATE,p.CREATION_TIME) INSERTED_DATE_TIME,p.APPUSER_ID,convert_to_timestamp(p.MODIFICATION_DATE,p.MODIFICATION_TIME) UPDATED_DATE_TIME,p.APPUSER_ID
from  PAYMENT P
          INNER JOIN PAYMENT_OG_LINE POL ON POL.PAYMENT_ID=P.PAYMENT_ID
          INNER JOIN PAYMENT_OG_MASTER POM ON POM.PAYMENT_OGM_ID=POL.PAYMENT_OGM_ID
          INNER JOIN PAYABLE_CHECK_TEMPLATE PCT ON PCT.PC_TEMPLATE_ID=POM.PC_TEMPLATE_ID
          INNER JOIN BANK_ACCOUNT BA ON BA.BANK_ACCOUNT_ID=P.FORM_BANK_ACCOUNT_ID
          INNER JOIN BANK_BRANCH BB ON BB.BANK_BRANCH_ID=BA.BANK_BRANCH_ID
WHERE P.RP_TYPE_ID=9
/

INSERT INTO AHA_PAYMENT_DETAIL
select P.PAYMENT_ID,POM.PAYMENT_OGM_ID,NVL(TC.DL_ID,POL.TO_DL_ID),POL.PAYMENT_OGS_ID,POL.FUND_ORDER_ID,NVL(POL.BANK_ACCOUNT_ID,ABA.ID),POL.AMOUNT,POL.COMMENTS,POL.LINE_UUID
     ,AP.INSERTED_DATE_TIME,AP.INSERTED_USER_ID,AP.UPDATED_DATE_TIME,AP.UPDATED_USER_ID
from  PAYMENT P
          INNER JOIN PAYMENT_OG_LINE POL ON POL.PAYMENT_ID=P.PAYMENT_ID
          INNER JOIN PAYMENT_OG_MASTER POM ON POM.PAYMENT_OGM_ID=POL.PAYMENT_OGM_ID
          LEFT  JOIN T_CUSTOMER TC ON TC.CUSTOMER_ID=POL.TO_CUSTOMER_ID
          LEFT  JOIN AHA_BANK_ACCOUNT ABA  ON ABA.ID= POL.TO_DL_BANK_ACCOUNT_ID
          INNER JOIN AHA_PAYMENT AP ON AP.ID=POM.PAYMENT_OGM_ID
WHERE P.RP_TYPE_ID=9
/

insert into aha_payment
select distinct POM.PAYMENT_OGM_ID,POM.PAYMENT_NUMBER,POM.BRANCH_ID,pct.RP_REASON_ID,null,null,BA.DL_ID,POM.FUND_ID,decode(pol.to_customer_id,null,9,8),POM.POGM_STATUS_ID+7 POGM_STATUS_ID,POM.PAYMENT_ORIGIN_ID,PCT.BANK_ACCOUNT_ID,POM.PAYMENT_DATE,POM.COMMENTS,0,POM.SENT_TO_BANK,POM.MASTER_UUID
              ,pom.REG_DATE INSERTED_DATE_TIME,pom.APPUSER_ID,pom.REG_DATE UPDATED_DATE_TIME,pom.APPUSER_ID
from  payment_og_line pol
          inner join payment_og_master pom on pom.PAYMENT_OGM_ID=pol.PAYMENT_OGM_ID
          inner join PAYABLE_CHECK_TEMPLATE pct on pct.PC_TEMPLATE_ID = pom.PC_TEMPLATE_ID
          inner join bank_account ba on ba.bank_account_id=pct.bank_account_id
where pol.payment_id is null
  and not exists(select 1 from  aha_payment ap where ap.id = pom.PAYMENT_OGM_ID)
/

insert into aha_payment_detail
select pol.PAYMENT_OGL_ID,POM.PAYMENT_OGM_ID,NVL(TC.DL_ID,POL.TO_DL_ID),POL.PAYMENT_OGS_ID,POL.FUND_ORDER_ID,NVL(POL.BANK_ACCOUNT_ID,ABA.ID),POL.AMOUNT,POL.COMMENTS,POL.LINE_UUID
     ,pom.REG_DATE INSERTED_DATE_TIME,pom.APPUSER_ID,pom.REG_DATE UPDATED_DATE_TIME,pom.APPUSER_ID
from  payment_og_line pol
          inner join payment_og_master pom on pom.PAYMENT_OGM_ID=pol.PAYMENT_OGM_ID
          left  join t_customer tc on tc.customer_id=pol.TO_CUSTOMER_ID
          LEFT  JOIN AHA_BANK_ACCOUNT ABA  ON ABA.ID= POL.TO_DL_BANK_ACCOUNT_ID
where pol.payment_id is null
/
----------------------------------------------------------------------------------------------------
insert into AHA_VOUCHER_TYPE
select VOUCHER_TYPE_ID,VOUCHER_TYPE_NAMe,null,null,null,null from  voucher_type
/
----------------------------------------------------------------------------------------------------
insert into aha_VOUCHER_STATUS
select VOUCHER_STATUS_ID,VOUCHER_STATUS_NAME,null,null,null,null from  VOUCHER_STATUS
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_VOUCHER
select VOUCHER_ID,VOUCHER_TYPE_ID,BRANCH_ID,VOUCHER_STATUS_ID,FUND_ID,VOUCHER_NUMBER,VOUCHER_DATE,COMMENTS,IS_MANUAL
     ,convert_to_timestamp(CREATION_DATE,CREATION_TIME),APPUSER_ID,convert_to_timestamp(MODIFICATION_DATE,MODIFICATION_TIME),APPUSER_ID
from  VOUCHER_MASTER
/
----------------------------------------------------------------------------------------------------
INSERT INTO AHA_VOUCHER_DETAIL
select VOUCHER_LINE_ID,VOUCHER_ID,SL_ID,DL_ID,LINE_NUMBER,COMMENTS,DEBIT_AMOUNT,CREDIT_AMOUNT,REFERENCE_ID,NULL,NULL,NULL,NULL
from  VOUCHER_LINE
/
----------------------------------------------------------------------------------------------------
