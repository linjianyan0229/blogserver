# 博客后端系统 Blog Server

一套成熟、可直接运行的博客后端：基于 **Spring Boot 3.4 + Spring Security + JWT + MyBatis-Plus + MySQL**，
内置严谨的鉴权体系（邮箱登录、RBAC 权限）、邮件 SMTP、文章/分类/标签/评论/友链全套增删改查与后台管理，
并提供完整的 Swagger 中文接口文档。

---

## 一、技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.4.1 | 主框架（稳定版，生态成熟） |
| Java | 17 | LTS |
| Spring Security | 6.x | 鉴权与方法级权限 |
| JWT (jjwt) | 0.12.6 | 无状态令牌 |
| MyBatis-Plus | 3.5.9 | ORM + 分页 + 逻辑删除 |
| MySQL | 8.x | 数据库（root/123456） |
| springdoc-openapi | 2.7.0 | Swagger 中文文档 |
| Spring Mail | - | 邮箱验证码 SMTP |
| Caffeine | - | 验证码本地缓存 |
| Hutool | 5.8.34 | 工具库 |
| Lombok | - | 简化样板代码 |

> 说明：原脚手架为 Spring Boot 4.1.0（过新，Swagger/MyBatis-Plus/JWT 生态尚未稳定适配），
> 已统一降级到生态最成熟的 3.4.1，以满足“成熟项目”的要求。

---

## 二、快速开始

### 1. 准备 MySQL
本地启动 MySQL，账号/密码 `root/123456`。**无需手动建库建表** —— 应用启动时会：
- 通过 `createDatabaseIfNotExist=true` 自动创建 `blog` 数据库；
- 自动执行 `schema.sql` 建表、`data.sql` 灌初始数据。

### 2. 配置邮箱（可选，用于注册/找回密码）
编辑 `src/main/resources/application.yml` 的 `spring.mail`：
```yaml
spring:
  mail:
    username: 你的发件邮箱@qq.com
    password: 你的SMTP授权码   # 注意是授权码，不是登录密码
```
> 未配置邮箱时，除“发送验证码”外的所有接口均可正常使用。

### 3. 启动
```bash
./mvnw spring-boot:run
```
启动成功后控制台会打印清晰的访问地址与默认账号。

### 4. 访问
- 接口根地址：`http://localhost:8080/api`
- **Swagger 中文文档**：`http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON：`http://localhost:8080/api/v3/api-docs`

### 默认账号
| 角色 | 邮箱 | 密码 |
|------|------|------|
| 超级管理员 | admin@blog.com | admin123 |
| 普通用户 | user@blog.com | 123456 |

---

## 三、鉴权说明

1. 调用 `POST /api/auth/login` 获取 `token`；
2. 后续请求在请求头携带：`Authorization: Bearer <token>`；
3. 在 Swagger 页面点击右上角 **Authorize** 填入 token 即可调试受保护接口。

权限模型为 **RBAC**：用户 → 角色 → 权限。后台接口使用 `@PreAuthorize("hasAuthority('xxx')")` 做方法级校验。
游客（未登录）可访问公开接口；非公开文章（`is_public=0`）需登录后查看全文。

---

## 四、目录结构

```
src/main/java/com/example/blogserver
├── common          统一响应 Result / 分页 / 异常处理
├── config          MyBatis-Plus / Security / Swagger / 启动日志
│   └── properties  JWT、邮件配置
├── controller      接口层（公开 / 用户中心 / 后台 admin）
├── dto             请求参数对象
├── entity          数据库实体
├── mapper          MyBatis-Plus Mapper
├── security        JWT 工具 / 过滤器 / UserDetails / 鉴权处理器
├── service         业务逻辑
├── util            Markdown 目录解析等工具
└── vo              响应视图对象
src/main/resources
├── application.yml 配置
├── banner.txt      启动 banner
└── db/
    ├── schema.sql  完整表结构
    └── data.sql    初始化数据
```

## 五、进阶能力

- **文件上传**：`POST /file/upload`（需登录），本地存储按 `yyyy/MM` 分目录，返回可直接访问的图片 URL，用于文章缩略图/顶部背景、头像。
- **接口限流（API 管理配置）**：基于本地 Caffeine 的固定窗口限流，**规则存库、后台可动态增删改查**（`/admin/api-limits`，约 10 秒生效），支持按 IP/用户/全局维度、按 Ant 路径与方法匹配。已内置登录、注册、验证码、上传、评论、点赞等默认规则。超限返回响应体 `code:429`。
- **统一约定**：所有业务异常均返回 HTTP 200 + 响应体 `code`（401/403/429/1001+ 等），便于前端统一处理。

详见 [API.md](./API.md) 完整接口文档（每个接口字段均标注必填/类型/说明）与 [数据库表结构](./src/main/resources/db/schema.sql)。
