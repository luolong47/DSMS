-- 清空关联表数据
DELETE FROM t_role_permission;
DELETE FROM t_user_role;

-- 清空主表数据
DELETE FROM t_permission;
DELETE FROM t_role;
DELETE FROM t_user;

-- 初始化用户表数据
INSERT INTO t_user (id, username, password, email, created_at, updated_at, is_active)
VALUES ('1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

-- 密码为123456的MD5值：e10adc3949ba59abbe56e057f20f883e

-- 初始化角色表数据
INSERT INTO t_role (id, name, description, created_at, updated_at)
VALUES ('1', 'ROLE_ADMIN', '系统管理员', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('2', 'ROLE_USER', '普通用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化权限表数据
INSERT INTO t_permission (id, name, description, created_at, updated_at)
VALUES ('1', 'USER_CREATE', '创建用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('2', 'USER_READ', '读取用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('3', 'USER_UPDATE', '更新用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('4', 'USER_DELETE', '删除用户', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 初始化用户角色关联表数据
INSERT INTO t_user_role (user_id, role_id)
VALUES ('1', '1'),
       ('1', '2');

-- 初始化角色权限关联表数据
INSERT INTO t_role_permission (role_id, permission_id)
VALUES ('1', '1'),
       ('1', '2'),
       ('1', '3'),
       ('1', '4'),
       ('2', '2');