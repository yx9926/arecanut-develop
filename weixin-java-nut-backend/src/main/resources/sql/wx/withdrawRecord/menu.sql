INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('微信用户提现记录表管理', 0, '1', 'layui-icon-home', '/wx/withdrawRecord/listPage', 'C', 'Y', 'withdrawRecord:list');
SELECT @parentId := LAST_INSERT_ID();
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('添加微信用户提现记录表', @parentId, '1', '', '#', 'F', 'Y', 'withdrawRecord:add');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('编辑微信用户提现记录表', @parentId, '2', '', '#', 'F', 'Y', 'withdrawRecord:edit');
INSERT INTO `sys_menu` (menu_name,parent_id,order_num,icon,url,menu_type,visible,perms) VALUES ('删除微信用户提现记录表', @parentId, '3', '', '#', 'F', 'Y', 'withdrawRecord:del');