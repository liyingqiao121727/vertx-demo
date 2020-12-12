ALTER TABLE `engine_proj_info`
ADD COLUMN `build_area` decimal(15, 3) DEFAULT NULL COMMENT '建筑面积';
ALTER TABLE `engine_proj_info`
ADD COLUMN `air_condition` varchar(25) DEFAULT NULL COMMENT '空调面积';
ALTER TABLE `engine_proj_info`
ADD COLUMN `analyse` varchar(255) DEFAULT NULL COMMENT '生产成本分析';
ALTER TABLE `engine_proj_info`
ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT '备注';
ALTER TABLE `engine_proj_info`
ADD COLUMN `glodon_remark` varchar(255) DEFAULT NULL COMMENT '广联达成本报表备注';
ALTER TABLE `engine_proj_info`
ADD COLUMN `desc` varchar(255) DEFAULT NULL COMMENT '描述' ;
ALTER TABLE `engine_proj_info`
ADD COLUMN `longitude` varchar(255) DEFAULT NULL COMMENT '经度' ;
ALTER TABLE `engine_proj_info`
ADD COLUMN `latitude` varchar(255) DEFAULT NULL COMMENT '纬度' ;
ALTER TABLE `engine_proj_info`
ADD COLUMN `essential` varchar(255) DEFAULT NULL COMMENT '工程量要素' ;
ALTER TABLE `engine_proj_info`
ADD COLUMN `work_dept` char(24) DEFAULT NULL COMMENT '实施部门' ;
ALTER TABLE engine_proj_info`
ADD COLUMN `is_share` tinyint(1) NULL COMMENT '是否是分摊项目' DEFAULT 1 ;
update engine_proj_info set is_share = 1;