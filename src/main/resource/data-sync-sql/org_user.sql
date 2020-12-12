ALTER TABLE `org_user`
DROP COLUMN `del_flag`,
CHANGE COLUMN `IS_DELETED` `del_flag` smallint(6) NULL DEFAULT 0 ;

ALTER TABLE `org_user_detail`
CHANGE COLUMN `ID` `id` varchar(24) NOT NULL FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE

ALTER TABLE `dictionary`
ADD COLUMN `earth_energy_type` tinyint(1) DEFAULT 0;

ALTER TABLE `con_change`
ADD COLUMN `fie_file_time` datetime NULL COMMENT '创建时间' AFTER `operate_type`;
