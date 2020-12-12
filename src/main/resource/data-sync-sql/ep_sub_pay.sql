ALTER TABLE `liyq`.`ep_sub_pay` 
ADD COLUMN `bill_time` datetime NULL DEFAULT NULL COMMENT '开票时间' ;
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '税率' ;
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `is_account` tinyint(1) NULL DEFAULT NULL COMMENT '是否到账';
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `is_deduct` tinyint(1) NULL DEFAULT NULL COMMENT '是否抵扣' ;
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `bill_money` decimal(15, 2) NULL DEFAULT NULL COMMENT '开票金额' ;
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `pay_proj` varchar(25) DEFAULT NULL COMMENT '支付项目' ;
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT '备注';
ALTER TABLE `liyq`.`ep_sub_pay`
ADD COLUMN `type_dic_id` varchar(24) DEFAULT NULL COMMENT '支付项目' ;

ALTER TABLE `liyq`.`sub_con_change`
ADD COLUMN `gmt_create` datetime NULL COMMENT '创建时间';
ADD COLUMN `remark` varchar(255) NULL COMMENT '备注';