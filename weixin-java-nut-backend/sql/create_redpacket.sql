CREATE TABLE `redpacket` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `packet_num` int(11) DEFAULT NULL COMMENT '红包数量',
                             `symbol` int(11) DEFAULT 0 COMMENT '是否有钱：0无 1有',
                             `pass_time` date DEFAULT NULL COMMENT '过期时间',
                             `packet_url` varchar(500) DEFAULT NULL COMMENT '红包图片URL',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='红包表';