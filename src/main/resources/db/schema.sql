-- ============================================================
--  博客系统数据库表结构  Blog Server Schema
--  数据库：blog   字符集：utf8mb4
--  说明：启动时由 spring.sql.init 自动执行（IF NOT EXISTS 幂等）
-- ============================================================
SET NAMES utf8mb4;

-- ---------------------------- 用户表 ----------------------------
CREATE TABLE IF NOT EXISTS `user`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`        VARCHAR(50)  NOT NULL COMMENT '用户名',
    `email`           VARCHAR(100) NOT NULL COMMENT '邮箱（登录账号）',
    `password`        VARCHAR(100) NOT NULL COMMENT '密码（BCrypt 加密）',
    `nickname`        VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`          VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `bio`             VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `gender`          TINYINT      DEFAULT 0 COMMENT '性别：0未知 1男 2女',
    `status`          TINYINT      DEFAULT 1 COMMENT '状态：0禁用 1正常',
    `last_login_time` DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`   VARCHAR(50)  DEFAULT NULL COMMENT '最后登录IP',
    `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_email` (`email`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- ---------------------------- 角色表 ----------------------------
CREATE TABLE IF NOT EXISTS `role`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '角色名称',
    `code`        VARCHAR(50) NOT NULL COMMENT '角色编码（如 ADMIN/USER）',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    `status`      TINYINT      DEFAULT 1 COMMENT '状态：0禁用 1正常',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';

-- ---------------------------- 权限表 ----------------------------
CREATE TABLE IF NOT EXISTS `permission`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name`        VARCHAR(50)  NOT NULL COMMENT '权限名称',
    `code`        VARCHAR(100) DEFAULT NULL COMMENT '权限编码（如 article:create）',
    `type`        TINYINT      DEFAULT 2 COMMENT '类型：1目录 2菜单 3按钮',
    `parent_id`   BIGINT       DEFAULT 0 COMMENT '父权限ID',
    `path`        VARCHAR(255) DEFAULT NULL COMMENT '前端路由路径',
    `component`   VARCHAR(255) DEFAULT NULL COMMENT '前端组件',
    `icon`        VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `sort`        INT          DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='权限/菜单表';

-- ---------------------------- 用户-角色关联表 ----------------------------
CREATE TABLE IF NOT EXISTS `user_role`
(
    `id`      BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- ---------------------------- 角色-权限关联表 ----------------------------
CREATE TABLE IF NOT EXISTS `role_permission`
(
    `id`            BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`       BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='角色权限关联表';

-- ---------------------------- 分类表 ----------------------------
CREATE TABLE IF NOT EXISTS `category`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '分类名称',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '分类描述',
    `sort`        INT          DEFAULT 0 COMMENT '排序',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='文章分类表';

-- ---------------------------- 标签表 ----------------------------
CREATE TABLE IF NOT EXISTS `tag`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '标签名称',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT  DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='文章标签表';

-- ---------------------------- 文章表 ----------------------------
CREATE TABLE IF NOT EXISTS `article`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title`         VARCHAR(200) NOT NULL COMMENT '标题',
    `summary`       VARCHAR(500) DEFAULT NULL COMMENT '摘要',
    `content`       LONGTEXT     DEFAULT NULL COMMENT '正文（Markdown）',
    `thumbnail`     VARCHAR(255) DEFAULT NULL COMMENT '缩略图/顶部背景图URL',
    `category_id`   BIGINT       DEFAULT NULL COMMENT '分类ID',
    `author_id`     BIGINT       NOT NULL COMMENT '作者ID',
    `view_count`    INT          DEFAULT 0 COMMENT '访问数',
    `like_count`    INT          DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT          DEFAULT 0 COMMENT '评论数',
    `is_public`     TINYINT      DEFAULT 1 COMMENT '是否公开（游客可见）：0否 1是',
    `top`           TINYINT      DEFAULT 0 COMMENT '是否置顶：0否 1是',
    `password`      VARCHAR(100) DEFAULT NULL COMMENT '访问密码（空=无需密码）',
    `status`        TINYINT      DEFAULT 1 COMMENT '状态：0草稿 1已发布',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_author` (`author_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='文章表';

-- ---------------------------- 文章-标签关联表 ----------------------------
CREATE TABLE IF NOT EXISTS `article_tag`
(
    `id`         BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `tag_id`     BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='文章标签关联表';

-- ---------------------------- 评论表 ----------------------------
CREATE TABLE IF NOT EXISTS `comment`
(
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `article_id`  BIGINT        NOT NULL COMMENT '文章ID',
    `user_id`     BIGINT        NOT NULL COMMENT '评论用户ID',
    `content`     VARCHAR(1000) NOT NULL COMMENT '评论内容',
    `parent_id`   BIGINT   DEFAULT 0 COMMENT '父评论ID（0为顶级评论）',
    `status`      TINYINT  DEFAULT 1 COMMENT '状态：0隐藏 1显示',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT  DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`),
    KEY `idx_article` (`article_id`),
    KEY `idx_user` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='评论表';

-- ---------------------------- 友链申请表 ----------------------------
CREATE TABLE IF NOT EXISTS `friend_link_apply`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '申请ID',
    `name`          VARCHAR(100) NOT NULL COMMENT '网站名称',
    `url`           VARCHAR(255) NOT NULL COMMENT '网站地址',
    `logo`          VARCHAR(255) DEFAULT NULL COMMENT '网站LOGO',
    `description`   VARCHAR(255) DEFAULT NULL COMMENT '网站描述',
    `apply_email`   VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
    `apply_user_id` BIGINT       DEFAULT NULL COMMENT '申请用户ID',
    `status`        VARCHAR(10)  DEFAULT 'PENDING' COMMENT '审核状态：PENDING待审核 APPROVED已通过 REJECTED已拒绝',
    `audit_remark`  VARCHAR(255) DEFAULT NULL COMMENT '审核备注/拒绝原因',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_apply_user` (`apply_user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='友链申请表';

-- ---------------------------- 站点基础配置表（key-value） ----------------------------
CREATE TABLE IF NOT EXISTS `site_config`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key`   VARCHAR(100) NOT NULL COMMENT '配置键',
    `config_value` TEXT         DEFAULT NULL COMMENT '配置值',
    `name`         VARCHAR(100) DEFAULT NULL COMMENT '配置中文名（后台展示）',
    `type`         VARCHAR(20)  DEFAULT 'text' COMMENT '类型：text单行/textarea多行/image图片/color颜色',
    `sort`         INT          DEFAULT 0 COMMENT '排序',
    `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='站点基础配置表';

-- ---------------------------- 接口限流规则表（API 管理配置） ----------------------------
CREATE TABLE IF NOT EXISTS `api_rate_limit`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '规则ID',
    `name`           VARCHAR(100) NOT NULL COMMENT '规则名称',
    `url_pattern`    VARCHAR(200) NOT NULL COMMENT '接口路径（Ant 风格，不含 context-path，如 /auth/**）',
    `method`         VARCHAR(10)  DEFAULT 'ALL' COMMENT '请求方法：ALL/GET/POST/PUT/DELETE',
    `dimension`      VARCHAR(10)  DEFAULT 'IP' COMMENT '限流维度：IP按客户端IP / USER按登录用户 / GLOBAL全局',
    `max_requests`   INT          NOT NULL COMMENT '窗口内最大请求次数',
    `window_seconds` INT          NOT NULL COMMENT '时间窗口（秒）',
    `enabled`        TINYINT      DEFAULT 1 COMMENT '是否启用：0停用 1启用',
    `remark`         VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_enabled` (`enabled`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='接口限流规则表';

-- ---------------------------- 友链表 ----------------------------
CREATE TABLE IF NOT EXISTS `friend_link`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '友链ID',
    `name`        VARCHAR(100) NOT NULL COMMENT '网站名称',
    `url`         VARCHAR(255) NOT NULL COMMENT '网站地址',
    `logo`        VARCHAR(255) DEFAULT NULL COMMENT '网站LOGO',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '网站描述',
    `sort`        INT          DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      DEFAULT 1 COMMENT '状态：0下线 1显示',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      DEFAULT 0 COMMENT '逻辑删除：0未删 1已删',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='友情链接表';

-- ---------------------------- 网站访问日志表 ----------------------------
CREATE TABLE IF NOT EXISTS `visit_log`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `ip`          VARCHAR(64)  DEFAULT NULL COMMENT '访客IP',
    `path`        VARCHAR(255) DEFAULT NULL COMMENT '访问路径',
    `referer`     VARCHAR(512) DEFAULT NULL COMMENT '来源页',
    `user_agent`  VARCHAR(512) DEFAULT NULL COMMENT '浏览器UA',
    `user_id`     BIGINT       DEFAULT NULL COMMENT '登录用户ID（游客为空）',
    `visit_date`  DATE         DEFAULT NULL COMMENT '访问日期（用于按天统计）',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (`id`),
    KEY `idx_visit_date` (`visit_date`),
    KEY `idx_ip` (`ip`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='网站访问日志表';

-- ============================================================
--  增量列迁移（针对已存在的表，CREATE IF NOT EXISTS 不会补列）
--  配合 application.yml 的 spring.sql.init.continue-on-error=true，
--  列已存在时报错被忽略，首次运行则补列。
-- ============================================================
ALTER TABLE `article` ADD COLUMN `password` VARCHAR(100) DEFAULT NULL COMMENT '访问密码（空=无需密码）';
