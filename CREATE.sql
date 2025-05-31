CREATE TABLE AHA_BANK
(
  ID    NUMBER(18),
  NAME  VARCHAR2(50 BYTE),
  IS_VALID NUMBER(1) NOT NULL,
  INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
  INSERTED_USER_ID    NUMBER(18)              NULL,
  UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
  UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_BANK ADD (
    CONSTRAINT PK_AHA_BANK PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_ACCOUNT_NATURE
(
  ID    NUMBER(18),
  NAME  VARCHAR2(200 BYTE),
  INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
  INSERTED_USER_ID    NUMBER(18)              NULL,
  UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
  UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_ACCOUNT_NATURE ADD (
    CONSTRAINT PK_AHA_ACCOUNT_NATURE PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_CALENDAR (
    ID                  NUMBER(18)              NOT NULL,
    CALENDAR_DATE       CHAR(10)                NOT NULL,
    IS_OFF              NUMBER(1)               NOT NULL,
    IS_VACATION         NUMBER(1)               NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_CALENDAR ADD (
    CONSTRAINT PK_AHA_CALENDAR PRIMARY KEY (ID)
)
/

CREATE UNIQUE INDEX AHA_CALENDAR_U01 ON AHA_CALENDAR
(CALENDAR_DATE)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_DETAIL_LEDGER_TYPE (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(50)            NOT NULL,
    F_ACCOUNT_NATURE_ID NUMBER                  NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/

ALTER TABLE AHA_DETAIL_LEDGER_TYPE ADD (
    CONSTRAINT PK_AHA_DETAIL_LEDGER_TYPE PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_DETAIL_LEDGER_TYPE ADD CONSTRAINT FK_AHA_DETAIL_LEDGER_TYPE_ACCOUNT_NATURE FOREIGN KEY (F_ACCOUNT_NATURE_ID)
REFERENCES AHA_ACCOUNT_NATURE (ID)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_DETAIL_LEDGER (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(150)            NOT NULL,
    CODE                VARCHAR2(50)            NOT NULL,
    F_DETAIL_LEDGER_TYPE_ID NUMBER(18)          NOT NULL,
    IS_ACTIVE           NUMBER(1)               NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/

ALTER TABLE AHA_DETAIL_LEDGER ADD (
    CONSTRAINT PK_AHA_DETAIL_LEDGER PRIMARY KEY (ID)
)

/

ALTER TABLE AHA_DETAIL_LEDGER ADD CONSTRAINT FK_AHA_DETAIL_LEDGER_AHA_DETAIL_LEDGER_TYPE FOREIGN KEY (F_DETAIL_LEDGER_TYPE_ID)
REFERENCES AHA_DETAIL_LEDGER_TYPE (ID)
/
-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_FILE_TYPE (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(50)            NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/


ALTER TABLE AHA_FILE_TYPE ADD (
    CONSTRAINT PK_AHA_FILE_TYPE PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_FUND (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(100)           NOT NULL,
    IS_ETF              NUMBER(1)               NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_FUND ADD (
    CONSTRAINT PK_AHA_FUND PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_FUND_BRANCH (
    ID                  NUMBER(18)              NOT NULL,
    IS_ACTIVE           NUMBER(1)               NOT NULL,
    CODE                VARCHAR2(50)            NOT NULL,
    NAME                VARCHAR2(50)            NOT NULL,
    MANAGER             VARCHAR2(50)            NULL,
    PHONE               VARCHAR2(50)            NULL,
    FAX                 VARCHAR2(50)            NULL,
    CELL_PHONE          VARCHAR2(50)            NULL,
    POSTAL_CODE         VARCHAR2(50)            NULL,
    ADDRESS             VARCHAR2(1000)          NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_FUND_BRANCH ADD (
    CONSTRAINT PK_AHA_FUND_BRANCH PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_FUND_FILE (
    ID                  NUMBER(18)              NOT NULL,
    FUND_ID             NUMBER(18)              NOT NULL,
    FILE_NAME           VARCHAR2(100)            NOT NULL,
    F_FILE_TYPE_ID      NUMBER(18)              NOT NULL,
    FILE_CONTENT        BLOB                    NULL,
    DESCRIPTION         VARCHAR2(300)           NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_FUND_FILE ADD (
    CONSTRAINT PK_AHA_FUND_FILE PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_FUND_INFORMATION (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(200)           NOT NULL,
    F_FUND_TYPE_ID      NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_FUND_INFORMATION ADD (
    CONSTRAINT PK_AHA_FUND_INFORMATION PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_FUND_TYPE (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(50)            NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_FUND_TYPE ADD (
    CONSTRAINT PK_AHA_FUND_TYPE PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_GENERAL_LEDGER_TYPE (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(50)            NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_GENERAL_LEDGER_TYPE ADD (
    CONSTRAINT PK_AHA_GENERAL_LEDGER_TYPE PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_GENERAL_LEDGER (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(400)           NOT NULL,
    CODE                VARCHAR2(50)            NOT NULL,
    IS_ACTIVE           NUMBER(1)               NOT NULL,
    F_AHA_GENERAL_LEDGER_TYPE_ID NUMBER(18)     NOT NULL,
    F_AHA_ACCOUNT_NATURE_ID NUMBER(18)          NOT NULL,  
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_GENERAL_LEDGER ADD (
    CONSTRAINT PK_AHA_GENERAL_LEDGER PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_GENERAL_LEDGER ADD CONSTRAINT FK_AHA_GENERAL_LEDGER_GENERAL_LEDGER_TYPE FOREIGN KEY (F_AHA_GENERAL_LEDGER_TYPE_ID)
REFERENCES AHA_GENERAL_LEDGER_TYPE (ID)
/

ALTER TABLE AHA_GENERAL_LEDGER ADD CONSTRAINT FK_AHA_GENERAL_LEDGER_ACCOUNT_NATURE FOREIGN KEY (F_AHA_ACCOUNT_NATURE_ID)
REFERENCES AHA_ACCOUNT_NATURE (ID)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_PARAMS_TYPE (
    ID                  NUMBER(18)              NOT NULL,
    NAME                VARCHAR2(50)            NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_PARAMS_TYPE ADD (
    CONSTRAINT PK_AHA_PARAMS_TYPE PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PARAMS_VALUE_TYPE
(
  ID    NUMBER(18),
  NAME  VARCHAR2(50 BYTE),
  INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
  INSERTED_USER_ID    NUMBER(18)              NULL,
  UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
  UPDATED_USER_ID     NUMBER(18)              NULL
)
/
ALTER TABLE AHA_PARAMS_VALUE_TYPE ADD (
    CONSTRAINT PK_AHA_PARAMS_VALUE_TYPE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PARAMS (
    ID                  NUMBER(18)              NOT NULL,
    CODE                VARCHAR2(100)           NOT NULL,
    NAME                VARCHAR2(200)           NOT NULL,
    VALUE               VARCHAR2(4000)          NULL,
    IS_ACTIVE           NUMBER(1)               NOT NULL,
    IS_EDITABLE         NUMBER(1)               NOT NULL,
    F_PARAMS_TYPE_ID    NUMBER(18)              NOT NULL,
    F_FUND_ID           NUMBER(18)              NULL,
    F_DETAIL_LEDGER_ID  NUMBER(18)              NULL,
    F_SUBSIDIARY_LEDGER_ID NUMBER(18)           NULL,
    F_PARAMS_VALUE_TYPE_ID NUMBER(18)           NULL,
    DATA_QUERY          VARCHAR2(4000)          NULL,
    IS_GLOBAL           NUMBER(1)               NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/

ALTER TABLE AHA_PARAMS ADD (
    CONSTRAINT PK_AHA_PARAMS PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_PARAMS ADD CONSTRAINT UNQ_AHA_PARAMS_CODE_FUND_ID UNIQUE (CODE,F_FUND_ID)
/

ALTER TABLE AHA_PARAMS ADD CONSTRAINT FK_AHA_PARAMS_PARAMS_VALUE_TYPE FOREIGN KEY (F_PARAMS_VALUE_TYPE_ID)
REFERENCES AHA_PARAMS_VALUE_TYPE (ID)
/

ALTER TABLE AHA_PARAMS ADD CONSTRAINT FK_AHA_PARAMS_AHA_FUND FOREIGN KEY (F_FUND_ID)
REFERENCES AHA_FUND (ID)
/

ALTER TABLE AHA_PARAMS ADD (
  CONSTRAINT CHK_AHA_PARAMS_GLOBAL_FUND
  CHECK (
    (IS_GLOBAL = 1 AND F_FUND_ID IS NULL) OR 
    (IS_GLOBAL = 0 AND F_FUND_ID IS NOT NULL)
))
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PARAMS_HISTORY (
    ID                  NUMBER(18)              NOT NULL,
    F_PARAMS_ID         NUMBER(18)              NOT NULL,
    VALUE               VARCHAR2(4000)          NULL,
    EFFECTIVE_DATE      CHAR(10)                NOT NULL,
    INSERTED_DATE_TIME  TIMESTAMP(6)            NULL,
    INSERTED_USER_ID    NUMBER(18)              NULL,
    UPDATED_DATE_TIME   TIMESTAMP(6)            NULL,
    UPDATED_USER_ID     NUMBER(18)              NULL
)
/

ALTER TABLE AHA_PARAMS_HISTORY ADD (
    CONSTRAINT PK_AHA_PARAMS_HISTORY PRIMARY KEY (ID)
)
/


ALTER TABLE AHA_PARAMS_HISTORY ADD CONSTRAINT UNQ_AHA_PARAMS_HISTORY_PARAMID_DATE UNIQUE (F_PARAMS_ID, EFFECTIVE_DATE)
/ 
-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_PAYMENT_REASON (
    ID                      NUMBER(18)              NOT NULL,
    NAME                    VARCHAR2(100)           NOT NULL,
    SYSTEM_DEFINED          NUMBER(1)               NOT NULL,
    FROM_SUBSIDIARY_LEDGER_ID NUMBER(18)            NULL,
    TO_SUBSIDIARY_LEDGER_ID   NUMBER(18)            NULL,
    FROM_DETAIL_LEDGER_ID    NUMBER(18)             NULL,
    TO_DETAIL_LEDGER_ID      NUMBER(18)             NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_PAYMENT_REASON ADD (
    CONSTRAINT PK_AHA_PAYMENT_REASON PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_VERIFICATION_CODE (
    ID                      NUMBER(18)              NOT NULL,
    SEED                    NVARCHAR2(300)          NOT NULL,
    COUNTER                 NUMBER(18)              NOT NULL,
    IS_ACTIVE               NUMBER(1)               NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_VERIFICATION_CODE ADD (
    CONSTRAINT PK_AHA_VERIFICATION_CODE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_MENU (
    ID NUMBER(19) NOT NULL,
    NAME NVARCHAR2(50) NOT NULL,
    F_MENU_ID NUMBER(19),
    MENU_LEVEL NUMBER(10) NOT NULL,
    INSERTED_DATE_TIME DATE,
    INSERTED_USER_ID NUMBER(19),
    UPDATED_DATE_TIME DATE,
    UPDATED_USER_ID NUMBER(19)    
)
/
ALTER TABLE AHA_MENU ADD (
    CONSTRAINT PK_MENU  PRIMARY KEY (ID)
)
/
ALTER TABLE AHA_MENU
ADD CONSTRAINT FK_MENU_MENU FOREIGN KEY (F_MENU_ID)
REFERENCES AHA_MENU (ID)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_PERMISSION (
    ID                      NUMBER(18)              NOT NULL,
    NAME                    VARCHAR2(300)           NOT NULL,
    URL                     VARCHAR2(300)           NOT NULL,
    IS_SENSITIVE            NUMBER(1)               NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_PERMISSION ADD (
    CONSTRAINT PK_AHA_PERMISSION PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_ROLE (
    ID                      NUMBER(18)              NOT NULL,
    NAME                    VARCHAR2(50)            NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_ROLE ADD (
    CONSTRAINT PK_AHA_ROLE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_ROLE_PERMISSION (
    ID                      NUMBER(18)              NOT NULL,
    F_ROLE_ID               NUMBER(18)              NOT NULL,
    F_PERMISSION_ID         NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_ROLE_PERMISSION ADD (
    CONSTRAINT PK_AHA_ROLE_PERMISSION PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_SUBSIDIARY_LEDGER (
    ID                      NUMBER(18)              NOT NULL,
    NAME                    NVARCHAR2(400)          NOT NULL,
    CODE                    NVARCHAR2(50)           NOT NULL,
    IS_ACTIVE               CHAR(1)                 NOT NULL,  
    F_GENERAL_LEDGER_ID     NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_SUBSIDIARY_LEDGER ADD (
    CONSTRAINT PK_AHA_SUBSIDIARY_LEDGER PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_USER_GROUP (
    ID                      NUMBER(18)              NOT NULL,
    NAME                    NVARCHAR2(50)           NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_USER_GROUP ADD (
    CONSTRAINT PK_AHA_USER_GROUP PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_PERSON (
    ID                              NUMBER(18),
    FIRST_NAME                      VARCHAR2(100 BYTE),
    LAST_NAME                       VARCHAR2(200 BYTE),
    PARENT                          VARCHAR2(200 BYTE),
    BIRTH_DATE                      CHAR(10 BYTE),
    ISSUING_CITY                    VARCHAR2(100 BYTE),
    BIRTH_CERTIFICATION_NUMBER      VARCHAR2(50 BYTE),
    BIRTH_CERTIFICATION_ID          VARCHAR2(30 BYTE),
    PHONE                           VARCHAR2(200 BYTE),
    NATIONAL_CODE                   VARCHAR2(20 BYTE),
    FAX                             VARCHAR2(100 BYTE),
    CELL_PHONE                      VARCHAR2(100 BYTE),  
    POSTAL_CODE                     VARCHAR2(20 BYTE),
    ADDRESS                         VARCHAR2(1000 BYTE),
    E_MAIL                          VARCHAR2(100 BYTE),
    IS_COMPANY                      NUMBER(1)     DEFAULT 0 NOT NULL,
    COMPANY_NAME                    VARCHAR2(200 BYTE),
    REGISTERATION_NUMBER            VARCHAR2(50 BYTE),
    LATIN_FIRST_NAME                VARCHAR2(200 BYTE),
    LATIN_LAST_NAME                 VARCHAR2(200 BYTE),
    IS_IRANIAN                      NUMBER(1),
    REF_ID                          NUMBER(18),  
    INSERTED_DATE_TIME              TIMESTAMP(6)            NULL,
    INSERTED_USER_ID                NUMBER(18)              NULL,
    UPDATED_DATE_TIME               TIMESTAMP(6)            NULL,
    UPDATED_USER_ID                 NUMBER(18)              NULL  
)
/
  
ALTER TABLE AHA_PERSON ADD (
    CONSTRAINT PK_AHA_PERSON PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_USERS (
    ID                      NUMBER(18)             NOT NULL,
    USERNAME                NVARCHAR2(50)          NOT NULL,
    PASSWORD                NVARCHAR2(200)         NOT NULL,
    IS_ACTIVE               NUMBER(1)              NOT NULL,
    IS_ADMIN                NUMBER(1)              NOT NULL,
    F_PERSON_ID             NUMBER(18)             NOT NULL, 
    F_VERIFICATION_CODE_ID NUMBER(18)              NULL,
    INSERTED_DATE_TIME     TIMESTAMP(6)            NULL,
    INSERTED_USER_ID       NUMBER(18)              NULL,
    UPDATED_DATE_TIME      TIMESTAMP(6)            NULL,
    UPDATED_USER_ID        NUMBER(18)              NULL
)
/
ALTER TABLE AHA_USERS ADD (
    CONSTRAINT PK_AHA_USERS PRIMARY KEY (ID)
)
/
CREATE UNIQUE INDEX IX_USERS ON AHA_USERS (USERNAME)
/

ALTER TABLE AHA_USERS ADD CONSTRAINT FK_AHA_USERS_AHA_PERSON FOREIGN KEY (F_PERSON_ID)
REFERENCES AHA_PERSON (ID)
/

ALTER TABLE AHA_USERS ADD CONSTRAINT FK_AHA_USERS_AHA_VERIFICATION_CODE FOREIGN KEY (F_VERIFICATION_CODE_ID)
REFERENCES AHA_VERIFICATION_CODE (ID)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_USER_GROUP_DETAIL (
    ID                      NUMBER(18)              NOT NULL,
    F_USER_GROUP_ID         NUMBER(18)              NOT NULL,
    F_USER_ID               NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_USER_GROUP_DETAIL ADD (
    CONSTRAINT PK_AHA_USER_GROUP_DETAIL PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_USER_GROUP_ROLE (
    ID                      NUMBER(18)              NOT NULL,
    F_USER_GROUP_ID         NUMBER(18)              NOT NULL,
    F_ROLE_ID               NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_USER_GROUP_ROLE ADD (
    CONSTRAINT PK_AHA_USER_GROUP_ROLE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_USER_PERMISSION (
    ID                      NUMBER(18)              NOT NULL,
    F_USER_ID               NUMBER(18)              NOT NULL,
    F_PERMISSION_ID         NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_USER_PERMISSION ADD (
    CONSTRAINT PK_AHA_USER_PERMISSION PRIMARY KEY (ID)
)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_USER_ROLE (
    ID                      NUMBER(18)              NOT NULL,
    F_USER_ID               NUMBER(18)              NOT NULL,
    F_ROLE_ID               NUMBER(18)              NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/
ALTER TABLE AHA_USER_ROLE ADD (
    CONSTRAINT PK_AHA_USER_ROLE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_COMPANY (
                               ID                      NUMBER(18)              NOT NULL,
                               SMS_INTO                NVARCHAR2(100)          NOT NULL,
                               NAME                    NVARCHAR2(500)          NOT NULL,
                               BOURSE_ACCOUNT_NUMBER   NUMBER(8)               NOT NULL,
                               BOURSE_ACCOUNT_NAME     NVARCHAR2(50)           NOT NULL,
                               INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
                               INSERTED_USER_ID        NUMBER(18)              NULL,
                               UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
                               UPDATED_USER_ID         NUMBER(18)              NULL
)
    /
ALTER TABLE AHA_COMPANY ADD (
    CONSTRAINT PK_AHA_COMPANY PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_MMTP_CONFIG
(
  ID                       NUMBER(18)           NOT NULL,
  BROKERAGE_CODE           CHAR(3 BYTE)         NOT NULL,
  APP_ID                   CHAR(1 BYTE)         NOT NULL,
  TRADER_ID                CHAR(5 BYTE)         NOT NULL,
  INS_MAX_LCODE_FUND2      CHAR(12 BYTE)                ,
  FUND_ACCOUNT_NUMBER      CHAR(16 BYTE)                ,
  INS_MAX_LCODE_FUND       CHAR(12 BYTE)                ,
  INS_MNEMONIC_CODE_FUND   CHAR(5 BYTE)                 ,
  RESERVE_ORDER_ORIGIN     NUMBER(1)                    ,
  F_FUND_ID                NUMBER               NOT NULL,
  INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
  INSERTED_USER_ID        NUMBER(18)              NULL,
  UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
  UPDATED_USER_ID         NUMBER(18)              NULL
)
    /

ALTER TABLE AHA_MMTP_CONFIG ADD (
    CONSTRAINT PK_AHA_MMTP_CONFIG PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_MMTP_CONFIG ADD CONSTRAINT FK_MMTP_CONFIG FOREIGN KEY (F_FUND_ID)
    REFERENCES AHA_FUND (ID)
/
-----------------------------------------------------------------------------------------------------

ALTER TABLE AHA_FUND_FILE ADD CONSTRAINT FK_AHA_FUND_FILE_AHA_FILE_TYPE FOREIGN KEY (F_FILE_TYPE_ID)
REFERENCES AHA_FILE_TYPE (ID)
/
ALTER TABLE AHA_FUND_FILE ADD CONSTRAINT FK_AHA_FUND_FILE_AHA_FUND FOREIGN KEY (FUND_ID)
REFERENCES AHA_FUND (ID)
/
ALTER TABLE AHA_FUND_INFORMATION ADD CONSTRAINT FK_AHA_FUND_INFORMATION_AHA_FUND_TYPE FOREIGN KEY (F_FUND_TYPE_ID)
REFERENCES AHA_FUND_TYPE (ID)
/
ALTER TABLE AHA_PARAMS ADD CONSTRAINT FK_AHA_PARAMS_AHA_DETAIL_LEDGER FOREIGN KEY (F_DETAIL_LEDGER_ID)
REFERENCES AHA_DETAIL_LEDGER (ID)
/
ALTER TABLE AHA_PARAMS ADD CONSTRAINT FK_AHA_PARAMS_AHA_PARAMS_TYPE FOREIGN KEY (F_PARAMS_TYPE_ID)
REFERENCES AHA_PARAMS_TYPE (ID)
/
ALTER TABLE AHA_PARAMS ADD CONSTRAINT FK_AHA_PARAMS_AHA_SUBSIDIARY_LEDGER FOREIGN KEY (F_SUBSIDIARY_LEDGER_ID)
REFERENCES AHA_SUBSIDIARY_LEDGER (ID)
/
ALTER TABLE AHA_PARAMS_HISTORY ADD CONSTRAINT FK_AHA_PARAMS_HISTORY_AHA_PARAMS FOREIGN KEY (F_PARAMS_ID)
REFERENCES AHA_PARAMS (ID)
/
ALTER TABLE AHA_PAYMENT_REASON ADD CONSTRAINT FK_AHA_PAYMENT_REASON_AHA_DETAIL_LEDGER FOREIGN KEY (FROM_DETAIL_LEDGER_ID)
REFERENCES AHA_DETAIL_LEDGER (ID)
/
ALTER TABLE AHA_PAYMENT_REASON ADD CONSTRAINT FK_AHA_PAYMENT_REASON_AHA_DETAIL_LEDGER1 FOREIGN KEY (TO_DETAIL_LEDGER_ID)
REFERENCES AHA_DETAIL_LEDGER (ID)
/
ALTER TABLE AHA_PAYMENT_REASON ADD CONSTRAINT FK_AHA_PAYMENT_REASON_AHA_SUBSIDIARY_LEDGER FOREIGN KEY (FROM_SUBSIDIARY_LEDGER_ID)
REFERENCES AHA_SUBSIDIARY_LEDGER (ID)
/
ALTER TABLE AHA_PAYMENT_REASON ADD CONSTRAINT FK_AHA_PAYMENT_REASON_AHA_SUBSIDIARY_LEDGER1 FOREIGN KEY (TO_SUBSIDIARY_LEDGER_ID)
REFERENCES AHA_SUBSIDIARY_LEDGER (ID)
/
ALTER TABLE AHA_ROLE_PERMISSION ADD CONSTRAINT FK_AHA_ROLE_PERMISSION_AHA_PERMISSION FOREIGN KEY (F_PERMISSION_ID)
REFERENCES AHA_PERMISSION (ID)
/
ALTER TABLE AHA_ROLE_PERMISSION ADD CONSTRAINT FK_AHA_ROLE_AHA_PERMISSION_ROLE FOREIGN KEY (F_ROLE_ID)
REFERENCES AHA_ROLE (ID)
/
ALTER TABLE AHA_SUBSIDIARY_LEDGER ADD CONSTRAINT FK_AHA_SUBSIDIARY_LEDGER_AHA_GENERAL_LEDGER FOREIGN KEY (F_GENERAL_LEDGER_ID)
REFERENCES AHA_GENERAL_LEDGER (ID)
/
ALTER TABLE AHA_USER_GROUP_DETAIL ADD CONSTRAINT FK_AHA_USER_GROUP_AHA_DETAIL_USER_GROUP FOREIGN KEY (F_USER_GROUP_ID)
REFERENCES AHA_USER_GROUP (ID)
/
ALTER TABLE AHA_USER_GROUP_DETAIL ADD CONSTRAINT FK_AHA_USER_GROUP_AHA_DETAIL_USERS FOREIGN KEY (F_USER_ID)
REFERENCES AHA_USERS (ID)
/
ALTER TABLE AHA_USER_GROUP_ROLE ADD CONSTRAINT FK_AHA_USER_GROUP_AHA_ROLE_ROLE FOREIGN KEY (F_ROLE_ID)
REFERENCES AHA_ROLE (ID)
/
ALTER TABLE AHA_USER_GROUP_ROLE ADD CONSTRAINT FK_AHA_USER_GROUP_ROLE_AHA_USER_GROUP FOREIGN KEY (F_USER_GROUP_ID)
REFERENCES AHA_USER_GROUP (ID)
/
ALTER TABLE AHA_USER_PERMISSION ADD CONSTRAINT FK_AHA_USER_PERMISSION_AHA_PERMISSION FOREIGN KEY (F_PERMISSION_ID)
REFERENCES AHA_PERMISSION (ID)
/
ALTER TABLE AHA_USER_PERMISSION ADD CONSTRAINT FK_AHA_USER_PERMISSION_AHA_USERS FOREIGN KEY (F_USER_ID)
REFERENCES AHA_USERS (ID)
/
ALTER TABLE AHA_USER_ROLE ADD CONSTRAINT FK_AHA_USER_ROLE_AHA_ROLE FOREIGN KEY (F_ROLE_ID)
REFERENCES AHA_ROLE (ID)
/
ALTER TABLE AHA_USER_ROLE ADD CONSTRAINT FK_AHA_USER_AHA_ROLE_USERS FOREIGN KEY (F_USER_ID)
REFERENCES AHA_USERS (ID)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_WAGE_RATE
(
    ID                      NUMBER(18)            NOT NULL,
    F_FUND_ID               NUMBER(18)            NOT NULL,
    F_INSTRUMENT_TYPE_ID    NUMBER(18)            NOT NULL,
    F_INST_TYPE_DERIVATIVES_ID NUMBER(18)         NOT NULL,
    IS_OTC                  NUMBER(1)             NOT NULL,
    IS_PURCHASE             NUMBER(1)             NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_WAGE_RATE ADD (
    CONSTRAINT PK_AHA_WAGE_RATE PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_WAGE_RATE ADD CONSTRAINT FK_AHA_FUND FOREIGN KEY (F_FUND_ID)
    REFERENCES AHA_FUND (ID)
/

ALTER TABLE AHA_WAGE_RATE
    ADD CONSTRAINT UNQ_AHA_WAGE_RATE
        UNIQUE (F_FUND_ID, F_INSTRUMENT_TYPE_ID,F_INST_TYPE_DERIVATIVES_ID,IS_OTC,IS_PURCHASE)
    ENABLE VALIDATE
/

-----------------------------------------------------------------------------------------------------

CREATE TABLE AHA_WAGE_RATE_DETAIL
(
    ID                        NUMBER(18)          NOT NULL,
    F_WAGE_RATE_ID            NUMBER(18)          NOT NULL,
    F_INDUSTRY_ID             NUMBER(18)                  ,
    F_INSTRUMENT_ID           NUMBER(18)                  ,
    F_BROKERAGE_ID            NUMBER(18)                  ,
    ISSUE_DATE                VARCHAR2(10 BYTE)   NOT NULL,
    BOURSE_CO                 NUMBER              DEFAULT 0                     NOT NULL,
    DEPOSIT_CO                NUMBER              DEFAULT 0                     NOT NULL,
    BOURSE_ORG                NUMBER              DEFAULT 0                     NOT NULL,
    IT_MANAGEMENT             NUMBER              DEFAULT 0                     NOT NULL,
    INTEREST                  NUMBER              DEFAULT 0                     NOT NULL,
    TAX                       NUMBER              DEFAULT 0                     NOT NULL,
    MAX_BOURSE_CO             NUMBER              DEFAULT 0                     NOT NULL,
    MAX_DEPOSIT_CO            NUMBER              DEFAULT 0                     NOT NULL,
    MAX_BOURSE_ORG            NUMBER              DEFAULT 0                     NOT NULL,
    MAX_IT_MANAGEMENT         NUMBER              DEFAULT 0                     NOT NULL,
    MAX_INTEREST              NUMBER              DEFAULT 0                     NOT NULL,
    RAYAN_BOURSE              NUMBER              DEFAULT 0                     NOT NULL,
    MAX_RAYAN_BOURSE          NUMBER              DEFAULT 0                     NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_WAGE_RATE_DETAIL ADD CONSTRAINT FK_WAGE_RATE FOREIGN KEY (F_WAGE_RATE_ID)
    REFERENCES AHA_WAGE_RATE (ID)
/

ALTER TABLE AHA_WAGE_RATE_DETAIL
    ADD CONSTRAINT UNQ_AHA_WAGE_RATE_DETAIL
        UNIQUE (F_WAGE_RATE_ID, ISSUE_DATE,F_INDUSTRY_ID,F_INSTRUMENT_ID,F_BROKERAGE_ID)
    ENABLE VALIDATE
/

ALTER TABLE AHA_WAGE_RATE_DETAIL ADD (
    CONSTRAINT PK_AHA_WAGE_RATE_DETAIL PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_CUSTOMER_STATUS
(
  ID                      NUMBER(18)              NOT NULL,
  NAME                    VARCHAR2(100)           NOT NULL,
  INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
  INSERTED_USER_ID        NUMBER(18)              NULL,
  UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
  UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_CUSTOMER_STATUS ADD (
    CONSTRAINT PK_AHA_CUSTOMER_STATUS PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_CUSTOMER
(
    ID                      NUMBER(18)          NOT NULL,
    F_DETAIL_LEDGER_ID      NUMBER(18)          NOT NULL,
    F_CUSTOMER_STATUS_ID    NUMBER(18)          NOT NULL,
    F_PERSON_ID             NUMBER(18)          NOT NULL,
    F_CUSTOMER_BANK_ACCOUNT_ID NUMBER(18)           NULL,
    COMMENTS                VARCHAR2(1000)          NULL,
    IS_SMS_SEND             NUMBER(1) DEFAULT 0 NOT NULL,
    IS_SEJAM                NUMBER(1) DEFAULT 0 NOT NULL,
    PROFIT_RATE             NUMBER  DEFAULT 100 NOT NULL,
    IS_PROFIT_ISSUE         NUMBER(1) DEFAULT 0 NOT NULL,
    IS_VAT                  NUMBER(1) DEFAULT 1 NOT NULL,
    IS_EPAYMENT_CUSTOMER    NUMBER(1) DEFAULT 0 NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_CUSTOMER ADD (
    CONSTRAINT PK_AHA_CUSTOMER PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_CUSTOMER ADD CONSTRAINT FK_AHA_CUSTOMER_STATUS FOREIGN KEY (F_CUSTOMER_STATUS_ID)
    REFERENCES AHA_CUSTOMER_STATUS (ID)
/

ALTER TABLE AHA_CUSTOMER ADD CONSTRAINT FK_AHA_DETAIL_LEDGER FOREIGN KEY (F_DETAIL_LEDGER_ID)
    REFERENCES AHA_DETAIL_LEDGER (ID)
/

ALTER TABLE AHA_CUSTOMER ADD CONSTRAINT FK_AHA_PERSON FOREIGN KEY (F_PERSON_ID)
    REFERENCES AHA_PERSON (ID)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_BANK_ACCOUNT_TYPE
(
    ID                      NUMBER(18)              NOT NULL,
    NAME                    NVARCHAR2(100)          NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_BANK_ACCOUNT_TYPE ADD (
    CONSTRAINT PK_AHA_BANK_ACCOUNT_TYPE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_BANK_ACCOUNT
(
    ID                      NUMBER(18)          NOT NULL,
    IS_ACTIVE               NUMBER(1) DEFAULT 1 NOT NULL,
    F_BANK_ACCOUNT_TYPE_ID  NUMBER(18)              NULL,
    F_BANK_ID               NUMBER(18)          NOT NULL,
    ACCOUNT_NUMBER          NVARCHAR2(50)       NOT NULL,
    ANNUAL_INTEREST         NUMBER    DEFAULT 0 NOT NULL,
    SHABA_NUMBER            NVARCHAR2(50)           NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_BANK_ACCOUNT ADD (
    CONSTRAINT PK_AHA_BANK_ACCOUNT PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_BANK_ACCOUNT ADD CONSTRAINT FK_BA_2_BANK_ACCOUNT_TYPE FOREIGN KEY (F_BANK_ACCOUNT_TYPE_ID)
    REFERENCES AHA_BANK_ACCOUNT_TYPE (ID)
/

-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_FUND_BANK_ACCOUNT
(
    ID                      NUMBER(18)          NOT NULL,
    F_FUND_ID               NUMBER(11)          NOT NULL,
    F_BANK_ACCOUNT_ID       NUMBER(18)          NOT NULL,
    F_DETAIL_LEDGER_ID      NUMBER(18)              NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_FUND_BANK_ACCOUNT ADD (
    CONSTRAINT PK_AHA_FUND_BANK_ACCOUNT PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_FUND_BANK_ACCOUNT ADD CONSTRAINT FK_FBA_2_FUND FOREIGN KEY (F_FUND_ID)
    REFERENCES AHA_FUND (ID)
/

ALTER TABLE AHA_FUND_BANK_ACCOUNT ADD CONSTRAINT FK_FBA_2_BANK_ACCOUNT FOREIGN KEY (F_BANK_ACCOUNT_ID)
    REFERENCES AHA_BANK_ACCOUNT (ID)
/

ALTER TABLE AHA_FUND_BANK_ACCOUNT ADD CONSTRAINT FK_FBA_2_DETAIL_LEDGER FOREIGN KEY (F_DETAIL_LEDGER_ID)
    REFERENCES AHA_DETAIL_LEDGER (ID)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_CUSTOMER_BANK_ACCOUNT
(
    ID                      NUMBER(18)          NOT NULL,
    F_CUSTOMER_ID           NUMBER(11)          NOT NULL,
    F_BANK_ACCOUNT_ID       NUMBER(18)          NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
    /

ALTER TABLE AHA_CUSTOMER_BANK_ACCOUNT ADD (
    CONSTRAINT PK_AHA_CUSTOMER_BANK_ACCOUNT PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_CUSTOMER_BANK_ACCOUNT ADD CONSTRAINT FK_CBA_2_CUSTOMER FOREIGN KEY (F_CUSTOMER_ID)
    REFERENCES AHA_CUSTOMER (ID)
/

ALTER TABLE AHA_CUSTOMER_BANK_ACCOUNT ADD CONSTRAINT FK_CBA_2_BANK_ACCOUNT FOREIGN KEY (F_BANK_ACCOUNT_ID)
    REFERENCES AHA_BANK_ACCOUNT (ID)
/

ALTER TABLE AHA_CUSTOMER ADD CONSTRAINT FK_CUS_2_AHA_CBA FOREIGN KEY (F_CUSTOMER_BANK_ACCOUNT_ID)
    REFERENCES AHA_CUSTOMER_BANK_ACCOUNT (ID)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT
(
    ID                      NUMBER(18)          NOT NULL,
    NAME                    NVARCHAR2(200)      NOT NULL,
    F_BANK_ACCOUNT_ID       NUMBER(18)          NOT NULL,
    F_DETAIL_LEDGER_ID      NUMBER(18)          NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT ADD (
    CONSTRAINT PK_AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT ADD CONSTRAINT FK_FIBA_2_BANK_ACCOUNT FOREIGN KEY (F_BANK_ACCOUNT_ID)
    REFERENCES AHA_BANK_ACCOUNT (ID)
    /

ALTER TABLE AHA_FINANCIAL_INSTITUTION_BANK_ACCOUNT ADD CONSTRAINT FK_FIBA_2_DETAIL_LEDGER FOREIGN KEY (F_DETAIL_LEDGER_ID)
    REFERENCES AHA_DETAIL_LEDGER (ID)
    /
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_TRADABLE_ITEM_DETAIL_LEDGER
(
    ID                      NUMBER(18)          NOT NULL,
    TRADABLE_ITEM_ID        NUMBER(18)          NOT NULL,
    TRADABLE_ITEM_TYPE_ID   NUMBER(18)          NOT NULL,
    TRADABLE_ITEM_GROUP_ID  NUMBER(18)          NOT NULL,
    F_DETAIL_LEDGER_ID      NUMBER(18)          NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_TRADABLE_ITEM_DETAIL_LEDGER ADD (
    CONSTRAINT PK_AHA_TRADABLE_ITEM_DETAIL_LEDGER PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_TRADABLE_ITEM_DETAIL_LEDGER ADD CONSTRAINT FK_TI_2_DETAIL_LEDGER FOREIGN KEY (F_DETAIL_LEDGER_ID)
    REFERENCES AHA_DETAIL_LEDGER (ID)
/

ALTER TABLE AHA_TRADABLE_ITEM_DETAIL_LEDGER
    ADD UNIQUE (TRADABLE_ITEM_ID, TRADABLE_ITEM_TYPE_ID, TRADABLE_ITEM_GROUP_ID)
    ENABLE VALIDATE
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_FUND_OWNERSHIP
(
    ID                      NUMBER(18)          NOT NULL,
    BOURSE_FUND_ID          NUMBER(18)              NULL,
    INSTRUMENT_ID           NUMBER(18)              NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
    /

ALTER TABLE AHA_FUND_OWNERSHIP ADD (
    CONSTRAINT PK_AHA_FUND_OWNERSHIP PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PAYMENT_TYPE
(
    ID                      NUMBER(18)          NOT NULL,
    NAME                    NVARCHAR2(50)       NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_PAYMENT_TYPE ADD (
    CONSTRAINT PK_AHA_PAYMENT_TYPE PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PAYMENT_STATUS
(
    ID                      NUMBER(18)          NOT NULL,
    NAME                    NVARCHAR2(100)      NOT NULL,
    USAGE_TYPE              NVARCHAR2(100)      NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)
)
    /

ALTER TABLE AHA_PAYMENT_STATUS ADD (
    CONSTRAINT PK_AHA_PAYMENT_STATUS PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PAYMENT_ORIGIN
(
    ID                      NUMBER(18)          NOT NULL,
    NAME                    NVARCHAR2(100)      NOT NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
    /

ALTER TABLE AHA_PAYMENT_ORIGIN ADD (
    CONSTRAINT PK_AHA_PAYMENT_ORIGIN PRIMARY KEY (ID)
)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PAYMENT
(
    ID                      NUMBER(18)          NOT NULL,
    CODE                    NVARCHAR2(300)      NOT NULL,
    F_FUND_BRANCH_ID        NUMBER(18)          NOT NULL,
    F_PAYMENT_REASON_ID     NUMBER(18)          NOT NULL,
    F_FROM_SUBSIDIARY_LEDGER_ID  NUMBER(18)         NULL,
    F_TO_SUBSIDIARY_LEDGER_ID  NUMBER(18)           NULL,
    F_FROM_DETAIL_LEDGER_ID      NUMBER(18)     NOT NULL,
    F_FUND_ID               NUMBER(18)          NOT NULL,
    F_PAYMENT_TYPE_ID       NUMBER(18)          NOT NULL,
    F_PAYMENT_STATUS_ID     NUMBER(18)              NULL,
    F_PAYMENT_ORIGIN_ID     NUMBER(18)              NULL,
    F_BANK_ACCOUNT_ID       NUMBER(18)              NULL,
    PAYMENT_DATE            NVARCHAR2(10)       NOT NULL,
    COMMENTS                NVARCHAR2(300)          NULL,
    IS_MANUAL               NUMBER(1)           NOT NULL,
    SENT_TO_BANK            NUMBER(1) DEFAULT 0 NOT NULL,
    UUID                    NVARCHAR2(50)           NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_PAYMENT ADD (
    CONSTRAINT PK_AHA_PAYMENT PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_FUND_BRANCH FOREIGN KEY (F_FUND_BRANCH_ID)
    REFERENCES AHA_FUND_BRANCH (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_PAYMENT_REASON FOREIGN KEY (F_PAYMENT_REASON_ID)
    REFERENCES AHA_PAYMENT_REASON (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_DETAIL_LEDGER FOREIGN KEY (F_FROM_DETAIL_LEDGER_ID)
    REFERENCES AHA_DETAIL_LEDGER (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_SUBSIDIARY_LEDGER FOREIGN KEY (F_FROM_SUBSIDIARY_LEDGER_ID)
    REFERENCES AHA_SUBSIDIARY_LEDGER (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_TO_SUBSIDIARY_LEDGER FOREIGN KEY (F_TO_SUBSIDIARY_LEDGER_ID)
    REFERENCES AHA_SUBSIDIARY_LEDGER (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_FUND FOREIGN KEY (F_FUND_ID)
    REFERENCES AHA_FUND (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_PAYMENT_TYPE FOREIGN KEY (F_PAYMENT_TYPE_ID)
    REFERENCES AHA_PAYMENT_TYPE (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_PAYMENT_STATUS FOREIGN KEY (F_PAYMENT_STATUS_ID)
    REFERENCES AHA_PAYMENT_STATUS (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_PAYMENT_ORIGIN FOREIGN KEY (F_PAYMENT_ORIGIN_ID)
    REFERENCES AHA_PAYMENT_ORIGIN (ID)
/

ALTER TABLE AHA_PAYMENT ADD CONSTRAINT FK_PAYMENT_2_BANK_ACCOUNT FOREIGN KEY (F_BANK_ACCOUNT_ID)
    REFERENCES AHA_BANK_ACCOUNT (ID)
/
-----------------------------------------------------------------------------------------------------
CREATE TABLE AHA_PAYMENT_DETAIL
(
    ID                      NUMBER(18)          NOT NULL,
    F_PAYMENT_ID            NUMBER(18)          NOT NULL,
    F_DETAIL_LEDGER_ID      NUMBER(18)          NOT NULL,
    F_PAYMENT_STATUS_ID     NUMBER(18)              NULL,
    F_ORDER_ID              NUMBER(18)              NULL,
    F_BANK_ACCOUNT_ID       NUMBER(18)              NULL,
    AMOUNT                  NUMBER              NOT NULL,
    COMMENTS                NVARCHAR2(300)          NULL,
    UUID                    NVARCHAR2(50)           NULL,
    INSERTED_DATE_TIME      TIMESTAMP(6)            NULL,
    INSERTED_USER_ID        NUMBER(18)              NULL,
    UPDATED_DATE_TIME       TIMESTAMP(6)            NULL,
    UPDATED_USER_ID         NUMBER(18)              NULL
)
/

ALTER TABLE AHA_PAYMENT_DETAIL ADD (
    CONSTRAINT PK_AHA_PAYMENT_DETAIL PRIMARY KEY (ID)
)
/

ALTER TABLE AHA_PAYMENT_DETAIL ADD CONSTRAINT FK_PAYMENT_DETAIL_2_DETAIL_LEDGER FOREIGN KEY (F_DETAIL_LEDGER_ID)
    REFERENCES AHA_DETAIL_LEDGER (ID)
/

ALTER TABLE AHA_PAYMENT_DETAIL ADD CONSTRAINT FK_PAYMENT_DETAIL_2_PAYMENT FOREIGN KEY (F_PAYMENT_ID)
    REFERENCES AHA_PAYMENT (ID)
/

ALTER TABLE AHA_PAYMENT_DETAIL ADD CONSTRAINT FK_PAYMENT_DETAIL_2_PAYMENT_STATUS FOREIGN KEY (F_PAYMENT_STATUS_ID)
    REFERENCES AHA_PAYMENT_STATUS (ID)
/

ALTER TABLE AHA_PAYMENT_DETAIL ADD CONSTRAINT FK_PAYMENT_DETAIL_2_BANK_ACCOUNT FOREIGN KEY (F_BANK_ACCOUNT_ID)
    REFERENCES AHA_BANK_ACCOUNT (ID)
/
-----------------------------------------------------------------------------------------------------
