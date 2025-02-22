# DSMS (数据结构管理系统)

## 项目结构

- `dsms-web/`: 前端项目 (Vue.js)
- `dsms-backend/`: 后端项目 (Spring Boot)

## 系统设计

### 产品管理功能流程图

```mermaid
flowchart TD
    A[开始] --> B[显示产品列表]
    B --> T[筛选产品]
    T --> T1[按产品英文名称搜索]
    T --> T2[按产品中文名称筛选]
    T1 --> B
    T2 --> B
    B --> C[选择操作类型]
    C --> D[新增产品]
    C --> E[编辑产品]
    C --> F[删除产品]
    C --> G[查看产品详情]
    
    D --> H[填写产品信息]
    H --> I{验证产品信息}
    I -->|验证失败| J[显示错误信息]
    J --> H
    I -->|验证通过| K[保存产品信息]
    K --> B
    
    E --> L[加载产品信息]
    L --> M[修改产品信息]
    M --> N{验证产品信息}
    N -->|验证失败| O[显示错误信息]
    O --> M
    N -->|验证通过| P[更新产品信息]
    P --> B
    
    F --> Q{确认删除}
    Q -->|取消| B
    Q -->|确认| R[删除产品]
    R --> B
    
    G --> S[显示产品详细信息]
    S --> B
```

### 登录功能流程图

```mermaid
flowchart TD
    A[开始] --> B[用户输入用户名和密码]
    B --> C{前端表单验证}
    C -->|验证失败| D[显示错误信息]
    D --> B
    C -->|验证通过| E[发送登录请求]
    E --> F{后端验证}
    F -->|验证失败| G[返回错误信息]
    G --> B
    F -->|验证成功| H[生成JWT令牌]
    H --> I[返回令牌和用户信息]
    I --> J[存储令牌和用户信息]
    J --> K[重定向到首页]
    K --> L[结束]
```

### 产品管理ER图

```mermaid
erDiagram
    t_PRODUCT {
        string product_name_en PK
        string product_name_cn
        string description
        datetime created_at
        datetime updated_at
        string last_modified_by
    }
```
### 用户认证ER图

```mermaid
erDiagram
    t_USER ||--o{ t_USER_ROLE : has
    t_USER {
        string id PK
        string username
        string password
        string email
        datetime created_at
        datetime updated_at
        boolean is_active
    }
    
    t_ROLE ||--o{ t_USER_ROLE : contains
    t_ROLE ||--o{ t_ROLE_PERMISSION : has
    t_ROLE {
        string id PK
        string name
        string description
    }
    
    t_USER_ROLE {
        string user_id FK
        string role_id FK
    }
    
    t_PERMISSION ||--o{ t_ROLE_PERMISSION : contains
    t_PERMISSION {
        string id PK
        string name
        string description
        string resource
        string action
    }
    
    t_ROLE_PERMISSION {
        string role_id FK
        string permission_id FK
    }
```

## 开发环境要求

### 前端
- yarn
- nuxt

### 后端
- Java JDK 17+
- Maven
- Spring Boot

## 启动说明

### 前端项目
```bash
cd dsms-web
npm install
npm run dev
```

### 后端项目
```bash
cd dsms-backend
./mvnw spring-boot:run