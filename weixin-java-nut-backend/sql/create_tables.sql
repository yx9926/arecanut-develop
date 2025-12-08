-- 微信小程序用户表创建语句
CREATE TABLE `wx_ma_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `appid` varchar(32) NOT NULL COMMENT '小程序appid',
  `openid` varchar(64) NOT NULL COMMENT '用户唯一标识',
  `session_key` varchar(128) DEFAULT NULL COMMENT '会话密钥',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '用户昵称',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
  `gender` tinyint DEFAULT 0 COMMENT '性别 0-未知、1-男、2-女',
  `phone_number` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `country` varchar(32) DEFAULT NULL COMMENT '国家/地区',
  `province` varchar(32) DEFAULT NULL COMMENT '省份',
  `city` varchar(32) DEFAULT NULL COMMENT '城市',
  `language` varchar(16) DEFAULT NULL COMMENT '语言',
  `status` tinyint DEFAULT 0 COMMENT '用户状态 0-正常、1-禁用',
  `balance` decimal(10,2) DEFAULT 0.00 COMMENT '余额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志 0-未删除、1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_openid_appid` (`openid`,`appid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信小程序用户表';

-- 微信用户提现记录表创建语句
CREATE TABLE `wx_withdraw_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `appid` varchar(32) NOT NULL COMMENT '小程序appid',
  `openid` varchar(64) NOT NULL COMMENT '微信用户openid',
  `amount` decimal(10,2) NOT NULL COMMENT '提现金额',
  `status` tinyint DEFAULT 0 COMMENT '提现状态：0-申请中，1-已处理，2-已拒绝',
  `method` tinyint DEFAULT 0 COMMENT '提现方式：0-微信支付',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `process_time` datetime DEFAULT NULL COMMENT '处理时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `reject_reason` varchar(255) DEFAULT NULL COMMENT '拒绝原因',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '交易单号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志 0-未删除、1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_openid` (`openid`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='微信用户提现记录表';