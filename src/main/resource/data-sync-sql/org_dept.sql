ALTER TABLE `org_dept`
DROP COLUMN `del_flag`,
CHANGE COLUMN `IS_DELETED` `del_flag` smallint(6) NULL DEFAULT 0 ;