INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('红包批次信息表管理', 0, '1', 'layui-icon-home', '/wx/redPacketBatch/listPage', 'C', 'Y', 'redPacketBatch:list');
SELECT @parentId := LAST_INSERT_ID();
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('添加红包批次信息表', @parentId, '1', '', '#', 'F', 'Y', 'redPacketBatch:add');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('编辑红包批次信息表', @parentId, '2', '', '#', 'F', 'Y', 'redPacketBatch:edit');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('删除红包批次信息表', @parentId, '3', '', '#', 'F', 'Y', 'redPacketBatch:del');