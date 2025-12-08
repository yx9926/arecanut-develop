INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('微信小程序用户表管理', 0, '1', 'layui-icon-home', '/wx/maUser/listPage', 'C', 'Y', 'maUser:list');
SELECT @parentId := LAST_INSERT_ID();
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('添加微信小程序用户表', @parentId, '1', '', '#', 'F', 'Y', 'maUser:add');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('编辑微信小程序用户表', @parentId, '2', '', '#', 'F', 'Y', 'maUser:edit');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('删除微信小程序用户表', @parentId, '3', '', '#', 'F', 'Y', 'maUser:del');