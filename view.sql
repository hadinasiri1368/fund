CREATE  OR REPLACE VIEW vw_security
AS
SELECT i.instrument_id             id,
       i.farsi_abbreviation        description,
       i.bourse_account,
       i.instrument_type_id        security_type_id,
       it.instrument_type_name     security_type_name,
       0                           is_contract
FROM site.instrument  i INNER JOIN instrument_type it ON it.instrument_type_id = i.instrument_type_id
UNION ALL
SELECT c.contract_id             id,
       c.contract_desc           description,
       TRIM (contract_symbol)    bourse_account,
       c.market_type             security_type_id,
       CASE c.market_type
           WHEN 1 THEN 'FUTURE'
           WHEN 2 THEN 'OPTION'
           WHEN 3 THEN 'CD'
           END                       security_type_name,
       1                         is_contract
FROM site.cf_contract c
/