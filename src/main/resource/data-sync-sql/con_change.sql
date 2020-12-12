ALTER TABLE `con_change`
ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT '备注' AFTER `del_flag`;
ALTER TABLE `con_change`
ADD COLUMN `exe_result` tinyint(1) COMMENT '执行结果' DEFAULT NULL;
ALTER TABLE `con_change`
ADD COLUMN `operator` varchar(24) COMMENT '操作人'  DEFAULT NULL;
ALTER TABLE `con_change`
ADD COLUMN `operate_type` varchar(24) COMMENT '操作类型' DEFAULT NULL ;