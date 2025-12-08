-- 广告图表创建语句
CREATE TABLE `wx_advertisement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '广告标题',
  `image_url` varchar(500) NOT NULL COMMENT '广告图片URL',
  `duration` int DEFAULT 5 COMMENT '广告展示时长（秒），默认5秒',
  `link_url` varchar(500) DEFAULT NULL COMMENT '广告点击跳转链接',
  `start_time` datetime DEFAULT NULL COMMENT '开始展示时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束展示时间',
  `status` tinyint DEFAULT 1 COMMENT '广告状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标志 0-未删除、1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='广告图表';