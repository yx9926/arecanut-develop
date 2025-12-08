INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('广告图表管理', 0, '1', 'layui-icon-home', '/wx/advertisement/listPage', 'C', 'Y', 'advertisement:list');
SELECT @parentId := LAST_INSERT_ID();
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('添加广告图表', @parentId, '1', '', '#', 'F', 'Y', 'advertisement:add');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('编辑广告图表', @parentId, '2', '', '#', 'F', 'Y', 'advertisement:edit');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('删除广告图表', @parentId, '3', '', '#', 'F', 'Y', 'advertisement:del');