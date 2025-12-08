-- 修复红包批次表，添加逻辑删除字段
ALTER TABLE `red_packet_batch` 
ADD COLUMN `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是' AFTER `status`,
ADD COLUMN `create_by` varchar(64) DEFAULT NULL COMMENT '创建人' AFTER `deleted`,
ADD COLUMN `update_by` varchar(64) DEFAULT NULL COMMENT '更新人' AFTER `create_by`,
ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT '备注' AFTER `update_by`,
ADD COLUMN `amount_type` varchar(10) DEFAULT 'fixed' COMMENT '金额类型：fixed-固定金额，random-随机金额' AFTER `remark`;

-- 更新现有数据的deleted字段为0（未删除）
UPDATE `red_packet_batch` SET `deleted` = 0 WHERE `deleted` IS NULL;