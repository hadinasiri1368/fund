CREATE  OR REPLACE VIEW vw_tradable_item
AS
SELECT i.instrument_id             id,
       i.farsi_abbreviation        description,
       i.bourse_account,
       i.instrument_type_id        type_id,
       it.instrument_type_name     type_name,
       1                           tradable_item_group
FROM site.instrument  i INNER JOIN instrument_type it ON it.instrument_type_id = i.instrument_type_id
UNION ALL
SELECT c.contract_id             id,
       c.contract_desc           description,
       TRIM (contract_symbol)    bourse_account,
       CASE c.market_type
           WHEN 1 THEN 201
           WHEN 2 THEN 202
           WHEN 3 THEN 203
           END                       type_id,
       CASE c.market_type
           WHEN 1 THEN 'FUTURE'
           WHEN 2 THEN 'OPTION'
           WHEN 3 THEN 'CD'
           END                       type_name,
       2                         tradable_item_group
FROM site.cf_contract c
UNION ALL
SELECT bf.bourse_fund_id             id,
       bf.bourse_fund_name           description,
       bf.latin_bourse_fund_name     bourse_account,
       ft.fund_type_id               type_id,
       ft.fund_type_name             type_name,
       3                             tradable_item_group
FROM bourse_fund  bf
         INNER JOIN fund_type ft ON ft.fund_type_id = bf.fund_type_id
UNION ALL
SELECT b.brokerage_id             id,
       b.brokerage_name           description,
       b.brokerage_code           bourse_account,
       1                          type_id,
       'کارگزاری'                     type_name,
       4                          tradable_item_group
FROM brokerage  b
    /