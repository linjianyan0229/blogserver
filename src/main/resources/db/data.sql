-- ============================================================
--  博客系统初始化数据  Blog Server Seed Data
--  说明：使用 INSERT IGNORE 保证幂等（启动可重复执行）
--  账号：管理员 admin@blog.com / admin123    普通用户 user@blog.com / 123456
-- ============================================================
SET NAMES utf8mb4;

-- ---------------------------- 用户（密码为 BCrypt 加密） ----------------------------
INSERT IGNORE INTO `user` (`id`, `username`, `email`, `password`, `nickname`, `avatar`, `bio`, `gender`, `status`)
VALUES (1, 'admin', 'admin@blog.com', '$2b$10$8PMREi8iuRPWX2PQnfkXk.IEkVaDalckP4oCX6nipkjrMV76WLbgy', '超级管理员',
        'https://cravatar.cn/avatar/1?d=identicon', '博客系统管理员', 1, 1),
       (2, 'demo', 'user@blog.com', '$2b$10$vN9zuXcAGEAa/I4RmTcZOOLgAqjmiZ2BCkMblN57f4YVIPWRVlGq.', '演示用户',
        'https://cravatar.cn/avatar/2?d=identicon', '我是一名普通博客用户', 1, 1);

-- ---------------------------- 角色 ----------------------------
INSERT IGNORE INTO `role` (`id`, `name`, `code`, `description`, `status`)
VALUES (1, '超级管理员', 'ADMIN', '拥有系统全部权限', 1),
       (2, '普通用户', 'USER', '注册用户，可评论、改资料、看文章', 1);

-- ---------------------------- 用户-角色 ----------------------------
INSERT IGNORE INTO `user_role` (`user_id`, `role_id`)
VALUES (1, 1),
       (2, 2);

-- ---------------------------- 权限（菜单 + 按钮） ----------------------------
INSERT IGNORE INTO `permission` (`id`, `name`, `code`, `type`, `parent_id`, `path`, `icon`, `sort`)
VALUES (1, '系统管理', 'system', 1, 0, '/system', 'Setting', 1),
       (11, '用户管理', 'user:list', 2, 1, '/system/user', 'User', 11),
       (111, '新增用户', 'user:create', 3, 11, NULL, NULL, 1),
       (112, '修改用户', 'user:update', 3, 11, NULL, NULL, 2),
       (113, '删除用户', 'user:delete', 3, 11, NULL, NULL, 3),
       (12, '角色权限', 'role:list', 2, 1, '/system/role', 'Lock', 12),
       (121, '新增角色', 'role:create', 3, 12, NULL, NULL, 1),
       (122, '修改角色', 'role:update', 3, 12, NULL, NULL, 2),
       (123, '删除角色', 'role:delete', 3, 12, NULL, NULL, 3),
       (124, '分配权限', 'role:assign', 3, 12, NULL, NULL, 4),
       (13, '权限菜单', 'permission:list', 2, 1, '/system/permission', 'Menu', 13),
       (2, '内容管理', 'content', 1, 0, '/content', 'Document', 2),
       (21, '文章管理', 'article:list', 2, 2, '/content/article', 'Tickets', 21),
       (211, '新增文章', 'article:create', 3, 21, NULL, NULL, 1),
       (212, '修改文章', 'article:update', 3, 21, NULL, NULL, 2),
       (213, '删除文章', 'article:delete', 3, 21, NULL, NULL, 3),
       (22, '分类管理', 'category:list', 2, 2, '/content/category', 'Files', 22),
       (221, '新增分类', 'category:create', 3, 22, NULL, NULL, 1),
       (222, '修改分类', 'category:update', 3, 22, NULL, NULL, 2),
       (223, '删除分类', 'category:delete', 3, 22, NULL, NULL, 3),
       (23, '标签管理', 'tag:list', 2, 2, '/content/tag', 'CollectionTag', 23),
       (231, '新增标签', 'tag:create', 3, 23, NULL, NULL, 1),
       (232, '修改标签', 'tag:update', 3, 23, NULL, NULL, 2),
       (233, '删除标签', 'tag:delete', 3, 23, NULL, NULL, 3),
       (24, '评论管理', 'comment:list', 2, 2, '/content/comment', 'ChatDotRound', 24),
       (241, '删除评论', 'comment:delete', 3, 24, NULL, NULL, 1),
       (25, '友链管理', 'link:list', 2, 2, '/content/link', 'Link', 25),
       (251, '新增友链', 'link:create', 3, 25, NULL, NULL, 1),
       (252, '修改友链', 'link:update', 3, 25, NULL, NULL, 2),
       (253, '删除友链', 'link:delete', 3, 25, NULL, NULL, 3),
       (14, '接口限流', 'api:list', 2, 1, '/system/api-limit', 'Histogram', 14),
       (141, '新增限流规则', 'api:create', 3, 14, NULL, NULL, 1),
       (142, '修改限流规则', 'api:update', 3, 14, NULL, NULL, 2),
       (143, '删除限流规则', 'api:delete', 3, 14, NULL, NULL, 3),
       (15, '站点配置', 'config:list', 2, 1, '/system/site-config', 'Tools', 15),
       (151, '修改站点配置', 'config:update', 3, 15, NULL, NULL, 1);

-- ---------------------------- 角色-权限（ADMIN 拥有全部） ----------------------------
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, `id` FROM `permission`;

-- ---------------------------- 分类 ----------------------------
INSERT IGNORE INTO `category` (`id`, `name`, `description`, `sort`)
VALUES (1, '后端开发', 'Java / Spring 等后端技术', 1),
       (2, '前端开发', 'Vue / React 等前端技术', 2),
       (3, '数据库', 'MySQL / Redis 等数据存储', 3),
       (4, '生活随笔', '记录生活点滴', 4);

-- ---------------------------- 标签 ----------------------------
INSERT IGNORE INTO `tag` (`id`, `name`)
VALUES (1, 'Java'), (2, 'SpringBoot'), (3, 'MyBatis-Plus'),
       (4, 'Vue3'), (5, 'MySQL'), (6, 'JWT'), (7, '随笔');

-- ---------------------------- 示例文章 ----------------------------
INSERT IGNORE INTO `article` (`id`, `title`, `summary`, `content`, `thumbnail`, `category_id`, `author_id`,
                              `view_count`, `like_count`, `comment_count`, `is_public`, `top`, `status`)
VALUES (1, 'Spring Boot 3 + JWT 打造安全的博客后端',
        '从零搭建一套基于 Spring Boot 3、Spring Security 与 JWT 的博客后端，涵盖邮箱登录、RBAC 权限与完整 CRUD。',
        '# Spring Boot 3 + JWT 打造安全的博客后端\n\n## 一、技术选型\n\n本项目采用 Spring Boot 3.4、Spring Security、JWT、MyBatis-Plus、MySQL。\n\n## 二、鉴权流程\n\n用户使用邮箱密码登录，服务端签发 JWT，后续请求通过 `Authorization: Bearer <token>` 携带令牌。\n\n## 三、RBAC 权限模型\n\n用户 → 角色 → 权限，三级关联，使用 `@PreAuthorize` 进行方法级校验。\n\n## 四、总结\n\n这是一套可直接投入使用的博客后端骨架。',
        'https://picsum.photos/seed/blog1/1200/400', 1, 1, 1280, 86, 0, 1, 1, 1),
       (2, 'MyBatis-Plus 分页与逻辑删除实战',
        '介绍 MyBatis-Plus 的分页插件、逻辑删除、自动填充等常用特性，并给出博客项目中的实际用法。',
        '# MyBatis-Plus 分页与逻辑删除实战\n\n## 分页插件\n\n注册 `MybatisPlusInterceptor` 并添加 `PaginationInnerInterceptor`。\n\n## 逻辑删除\n\n配置 `logic-delete-field: deleted`，删除即更新标记位。\n\n## 自动填充\n\n实现 `MetaObjectHandler` 自动填充创建/更新时间。',
        'https://picsum.photos/seed/blog2/1200/400', 3, 1, 642, 41, 0, 1, 0, 1),
       (3, '游客也能看的公开文章示例',
        '本篇为公开文章（is_public=1），未登录游客也可以浏览，用于演示公开接口。',
        '# 公开文章示例\n\n这是一篇任何人都能阅读的公开文章。\n\n登录后还可以点赞与评论。',
        'https://picsum.photos/seed/blog3/1200/400', 4, 1, 305, 12, 0, 1, 0, 1),
       (4, '仅登录用户可见的进阶文章',
        '本篇为非公开文章（is_public=0），仅登录用户可阅读全文，游客只能看到列表摘要。',
        '# 进阶文章\n\n本文为会员专享内容，游客无法阅读全文。\n\n这里是只有登录后才能看到的正文细节……',
        'https://picsum.photos/seed/blog4/1200/400', 1, 1, 158, 9, 0, 0, 0, 1);

-- ---------------------------- 文章-标签 ----------------------------
INSERT IGNORE INTO `article_tag` (`article_id`, `tag_id`)
VALUES (1, 1), (1, 2), (1, 6),
       (2, 3), (2, 5),
       (3, 7),
       (4, 1), (4, 2);

-- ---------------------------- 友链 ----------------------------
INSERT IGNORE INTO `friend_link` (`id`, `name`, `url`, `logo`, `description`, `sort`, `status`)
VALUES (1, 'Spring 官网', 'https://spring.io', 'https://picsum.photos/seed/spring/100', 'Spring 生态官方网站', 1, 1),
       (2, 'MyBatis-Plus', 'https://baomidou.com', 'https://picsum.photos/seed/mp/100', 'MyBatis 增强工具', 2, 1),
       (3, 'Hutool', 'https://hutool.cn', 'https://picsum.photos/seed/hutool/100', 'Java 工具类库', 3, 1);

-- ---------------------------- 接口限流默认规则（可在后台动态调整） ----------------------------
INSERT IGNORE INTO `api_rate_limit` (`id`, `name`, `url_pattern`, `method`, `dimension`, `max_requests`, `window_seconds`, `enabled`, `remark`)
VALUES (1, '发送验证码限流', '/auth/send-code', 'POST', 'IP', 1, 60, 1, '同一IP每60秒最多发1次验证码'),
       (2, '登录限流', '/auth/login', 'POST', 'IP', 10, 60, 1, '同一IP每60秒最多尝试登录10次'),
       (3, '注册限流', '/auth/register', 'POST', 'IP', 5, 300, 1, '同一IP每5分钟最多注册5次'),
       (4, '文件上传限流', '/file/upload', 'USER', 'USER', 30, 60, 1, '每用户每60秒最多上传30张'),
       (5, '发表评论限流', '/comment', 'USER', 'USER', 20, 60, 1, '每用户每60秒最多评论20条'),
       (6, '点赞限流', '/public/articles/*/like', 'POST', 'IP', 60, 60, 1, '同一IP每60秒最多点赞60次');

-- ---------------------------- 站点基础配置（前端首页/导航/页脚等） ----------------------------
INSERT IGNORE INTO `site_config` (`config_key`, `config_value`, `name`, `type`, `sort`)
VALUES ('site_name', '我的博客', '网站名称', 'text', 1),
       ('site_icon', 'https://picsum.photos/seed/favicon/64', '网站图标(favicon)', 'image', 2),
       ('home_background', 'https://picsum.photos/seed/homebg/1920/600', '首页背景图', 'image', 3),
       ('nav_logo', 'https://picsum.photos/seed/logo/120/40', '导航栏Logo', 'image', 4),
       ('nav_title', '我的博客', '导航栏文字', 'text', 5),
       ('home_intro', '欢迎来到我的博客，这里记录技术与生活的点滴。', '首页介绍文本', 'textarea', 6),
       ('footer_text', 'Copyright © 2026 我的博客 · 基于 Spring Boot 构建', '页脚文本', 'textarea', 7);
