-- 红包二维码表
CREATE TABLE `red_packet_qrcode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_id` varchar(32) NOT NULL COMMENT '批次ID，用于标识同一袋产品的二维码',
  `qrcode_id` varchar(64) NOT NULL COMMENT '二维码唯一标识',
  `amount` decimal(10,2) NOT NULL COMMENT '红包金额',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '二维码状态：0-未使用，1-已使用，2-已过期',
  `used_time` datetime DEFAULT NULL COMMENT '使用时间',
  `user_id` varchar(64) DEFAULT NULL COMMENT '使用用户ID',
  `withdraw_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '提现状态：0-未提现，1-提现中，2-提现成功，3-提现失败',
  `withdraw_time` datetime DEFAULT NULL COMMENT '提现时间',
  `order_no` varchar(64) DEFAULT NULL COMMENT '企业付款订单号',
  `total_amount` decimal(10,2) NOT NULL COMMENT '该批次总金额',
  `total_count` int(11) NOT NULL COMMENT '该批次总数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_qrcode_id` (`qrcode_id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='红包二维码表';

-- 批次信息表
CREATE TABLE `red_packet_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `batch_id` varchar(32) NOT NULL COMMENT '批次ID',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `total_count` int(11) NOT NULL COMMENT '总数量',
  `used_count` int(11) NOT NULL DEFAULT 0 COMMENT '已使用数量',
  `withdrawn_count` int(11) NOT NULL DEFAULT 0 COMMENT '已提现数量',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '批次状态：0-未生成，1-已生成，2-已过期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_id` (`batch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='红包批次信息表';