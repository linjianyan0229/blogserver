# 博客后端系统 · 接口文档

- 接口根地址：`http://localhost:8080/api`
- 在线调试：`http://localhost:8080/api/swagger-ui.html`（Swagger 中文文档，每个字段均标注必填/类型/说明）
- 鉴权方式：请求头 `Authorization: Bearer <token>`
- 统一响应：`{ "code": 200, "message": "操作成功", "data": {}, "timestamp": 1700000000000 }`

**字段标注说明**：下表「必填」列 `✓`=必填、`-`=可选（含默认值的会注明）。

| code | 含义 |
|------|------|
| 200 | 成功 |
| 400 | 参数校验失败 |
| 401 | 未登录/令牌失效 |
| 403 | 无访问权限 |
| 429 | 请求过于频繁（触发限流） |
| 500 | 服务器异常 |
| 1001~1010 | 业务错误（邮箱密码错误、邮箱已注册、验证码错误、文章未公开等） |

分页响应：`data.records`（列表）、`data.total`、`data.current`、`data.size`、`data.pages`。

---

## 一、认证模块（无需登录）

### POST `/auth/send-code` 发送邮箱验证码
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| email | string | ✓ | 邮箱，需合法邮箱格式 |
| type | string | ✓ | 验证码类型：`register` 注册 / `reset` 重置密码 |

> 限流：同一 IP 每 60 秒最多 1 次（可在后台「接口限流」调整）。

### POST `/auth/register` 邮箱注册
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| username | string | ✓ | 用户名，2~20 位 |
| email | string | ✓ | 邮箱，合法格式 |
| password | string | ✓ | 密码，6~32 位 |
| code | string | ✓ | 邮箱验证码 |

### POST `/auth/login` 邮箱密码登录
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| email | string | ✓ | 邮箱 |
| password | string | ✓ | 密码 |

返回：`data.token`、`data.tokenPrefix`、`data.expiresIn`(秒)、`data.userInfo`（含 `roles`、`permissions`）。

### POST `/auth/reset-password` 忘记密码-重置
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| email | string | ✓ | 邮箱 |
| code | string | ✓ | 邮箱验证码（type=reset） |
| newPassword | string | ✓ | 新密码，6~32 位 |

### POST `/auth/logout` 退出登录
无参数（客户端丢弃 token 即可）。

---

## 二、公开接口（游客 + 登录用户）

### GET `/public/articles` 文章列表（分页）
| 参数 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| page | long | - | 页码，默认 1 |
| size | long | - | 每页大小，默认 10 |
| categoryId | long | - | 按分类过滤 |
| tagId | long | - | 按标签过滤 |
| keyword | string | - | 标题/摘要模糊搜索 |

返回列表项字段：`id, title, summary, thumbnail, categoryName, authorName, authorAvatar, viewCount(访问数), likeCount(点赞数), commentCount(评论数), isPublic, top, tags[], createTime`。

### GET `/public/articles/{id}` 文章详情
| 参数 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long(path) | ✓ | 文章ID |

公开文章游客可看；非公开（isPublic=0）需登录否则返回 1010。访问数自动 +1。
返回含：`content(正文)、toc(目录：level/text/anchor)、thumbnail(顶部背景)、tags、categoryName、authorName/authorAvatar、各计数`。

### POST `/public/articles/{id}/like` 点赞
| 参数 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long(path) | ✓ | 文章ID |

返回最新点赞数。

### GET `/public/articles/{id}/comments` 文章评论列表（树形）
| 参数 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long(path) | ✓ | 文章ID |

### GET `/public/categories`、`/public/tags`、`/public/links`
无参数，分别返回全部分类、全部标签、显示中的友链。

### GET `/public/site-config` 站点基础配置
无参数，返回 `key→value` 的配置 Map，前端按 key 取用。内置键：
| key | 说明 | 类型 |
|-----|------|------|
| site_name | 网站名称 | text |
| site_icon | 网站图标(favicon) | image |
| home_background | 首页背景图 | image |
| nav_logo | 导航栏Logo | image |
| nav_title | 导航栏文字 | text |
| home_intro | 首页介绍文本 | textarea |
| footer_text | 页脚文本 | textarea |

---

## 三、用户中心（需登录）

### GET `/user/info` 当前登录用户信息
无参数，返回含 `roles`、`permissions`。

### PUT `/user/profile` 修改个人信息
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| nickname | string | - | 昵称，≤50 |
| avatar | string | - | 头像URL |
| bio | string | - | 简介，≤500 |
| gender | int | - | 0未知 1男 2女 |

### PUT `/user/password` 修改密码
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| oldPassword | string | ✓ | 原密码 |
| newPassword | string | ✓ | 新密码，6~32 位 |

---

## 四、评论（需登录）

### POST `/comment` 发表评论
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| articleId | long | ✓ | 文章ID |
| content | string | ✓ | 内容，≤1000 |
| parentId | long | - | 父评论ID，顶级评论传 0（默认 0） |

### DELETE `/comment/{id}` 删除自己的评论
| 参数 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long(path) | ✓ | 评论ID（仅本人可删） |

---

## 四点八、友链申请（需登录）

### POST `/link-apply` 提交友链申请
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| name | string | ✓ | 网站名称 |
| url | string | ✓ | 网站地址 |
| logo | string | - | 网站LOGO |
| description | string | - | 网站描述 |
| applyEmail | string | - | 联系邮箱（合法邮箱格式） |

> 提交后进入「待审核」，管理员审核通过后自动加入友链并展示。限流：每用户每小时 5 次。

### GET `/link-apply/mine` 我的友链申请记录
无参数，返回当前用户的申请列表及审核状态（PENDING/APPROVED/REJECTED）。

## 五、文件上传（需登录）

### POST `/file/upload` 上传图片
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| file | file(multipart) | ✓ | 图片文件，jpg/jpeg/png/gif/webp/bmp，≤5MB |

返回可直接访问的 URL（如 `/api/uploads/2026/06/xxx.png`），用于文章缩略图/顶部背景、用户头像。

---

## 六、后台管理（需登录 + 对应权限，缺权限返回 403）

### 文章管理（`article:*`）
| 方法 | 路径 | 权限 |
|------|------|------|
| GET | `/admin/articles?page&size&categoryId&tagId&keyword&status` | article:list |
| GET | `/admin/articles/{id}` | article:list |（编辑回显：返回全部表字段 + `tagIds`/`top`/`status`/`toc`，与下方保存字段一一对应）
| POST | `/admin/articles` | article:create |
| PUT | `/admin/articles` | article:update |
| DELETE | `/admin/articles/{id}` | article:delete |

文章保存（POST/PUT）字段：
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long | -(改时✓) | 修改时传 |
| title | string | ✓ | 标题 |
| summary | string | - | 摘要 |
| content | string | ✓ | 正文（Markdown） |
| thumbnail | string | - | 缩略图/顶部背景图URL |
| categoryId | long | - | 分类ID |
| tagIds | long[] | - | 标签ID列表 |
| isPublic | int | - | 是否公开 0否1是，默认1 |
| top | int | - | 是否置顶 0否1是，默认0 |
| status | int | - | 0草稿 1发布，默认1 |

### 分类管理（`category:*`）
保存字段：`name`(✓)、`description`(-)、`sort`(-默认0)、`id`(改时✓)。删除 `/admin/categories/{id}`。

### 标签管理（`tag:*`）
保存字段：`name`(✓)、`id`(改时✓)。删除 `/admin/tags/{id}`。

### 友链管理（`link:*`）
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long | -(改时✓) | |
| name | string | ✓ | 网站名称 |
| url | string | ✓ | 网站地址 |
| logo | string | - | LOGO |
| description | string | - | 描述 |
| sort | int | - | 排序，默认0 |
| status | int | - | 0下线 1显示，默认1 |

### 评论管理（`comment:*`）
`GET /admin/comments?page&size`（comment:list）；`DELETE /admin/comments/{id}`（comment:delete）。

### 用户管理（`user:*`）
| 方法 | 路径 | 权限 |
|------|------|------|
| GET | `/admin/users?page&size&keyword&status` | user:list |
| GET | `/admin/users/{id}` | user:list |
| POST/PUT | `/admin/users` | user:create / user:update |
| DELETE | `/admin/users/{id}` | user:delete |
| POST | `/admin/users/assign-roles` | user:update |

用户保存字段：
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long | -(改时✓) | |
| username | string | ✓ | 用户名 |
| email | string | ✓ | 邮箱 |
| password | string | 新增✓ | 修改时留空则不变 |
| nickname / avatar / bio | string | - | |
| gender | int | - | 默认0 |
| status | int | - | 0禁用 1正常，默认1 |
| roleIds | long[] | - | 角色ID列表 |

分配角色 `assign-roles`：`userId`(✓)、`roleIds`(✓)。

### 角色权限 / 权限配置页面（`role:*`）
| 方法 | 路径 | 权限 |
|------|------|------|
| GET | `/admin/roles` | role:list |
| GET | `/admin/roles/{id}/permissions` | role:list（回显勾选） |
| POST/PUT | `/admin/roles` | role:create / role:update |
| DELETE | `/admin/roles/{id}` | role:delete |
| POST | `/admin/roles/assign-permissions` | role:assign |
| GET | `/admin/permissions/tree` 、`/admin/permissions` | permission:list / role:list |

角色保存字段：`name`(✓)、`code`(✓)、`description`(-)、`status`(-默认1)、`permissionIds`(long[])、`id`(改时✓)。
分配权限 `assign-permissions`：`roleId`(✓)、`permissionIds`(✓ long[])。

### 接口限流管理（API 管理配置，`api:*`）
| 方法 | 路径 | 权限 |
|------|------|------|
| GET | `/admin/api-limits` | api:list |
| POST/PUT | `/admin/api-limits` | api:create / api:update |
| DELETE | `/admin/api-limits/{id}` | api:delete |

限流规则保存字段：
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| id | long | -(改时✓) | |
| name | string | ✓ | 规则名称 |
| urlPattern | string | ✓ | 接口路径（Ant 风格，不含 context-path，如 `/auth/**`） |
| method | string | - | ALL/GET/POST/PUT/DELETE，默认 ALL |
| dimension | string | - | IP/USER/GLOBAL，默认 IP |
| maxRequests | int | ✓ | 窗口内最大次数，≥1 |
| windowSeconds | int | ✓ | 时间窗口（秒），≥1 |
| enabled | int | - | 0停用 1启用，默认1 |
| remark | string | - | 备注 |

> 规则修改后约 10 秒内生效（本地缓存刷新），超限返回 429。

### 友链申请审核（`link:audit`）
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/admin/link-applies?status&page&size` | 申请分页，status 可选 PENDING/APPROVED/REJECTED |
| POST | `/admin/link-applies/{id}/approve` | 审核通过（自动写入友链并显示） |
| POST | `/admin/link-applies/{id}/reject?remark=拒绝原因` | 审核拒绝 |

### 站点配置管理（`config:*`）
| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | `/admin/site-config` | config:list | 配置列表（含中文名/类型，供表单渲染） |
| PUT | `/admin/site-config` | config:update | 批量更新（请求体为数组），修改即时生效 |

批量更新请求体（`List`），每项字段：
| 字段 | 类型 | 必填 | 说明 |
|------|------|:--:|------|
| configKey | string | ✓ | 配置键（如 site_name） |
| configValue | string | - | 配置值 |
| name | string | - | 中文名（新增配置项时传） |
| type | string | - | text/textarea/image/color（新增时传） |
| sort | int | - | 排序 |

---

## 七、权限编码一览

| 模块 | 权限编码 |
|------|----------|
| 用户 | user:list / user:create / user:update / user:delete |
| 角色 | role:list / role:create / role:update / role:delete / role:assign |
| 权限 | permission:list |
| 接口限流 | api:list / api:create / api:update / api:delete |
| 站点配置 | config:list / config:update |
| 文章 | article:list / article:create / article:update / article:delete |
| 分类 | category:list / category:create / category:update / category:delete |
| 标签 | tag:list / tag:create / tag:update / tag:delete |
| 评论 | comment:list / comment:delete |
| 友链 | link:list / link:create / link:update / link:delete / link:audit（友链申请审核） |

> ADMIN 默认拥有全部权限；USER 仅可用用户中心、评论、文件上传。
