# DSMS (数据结构管理系统)

## 项目结构

- `dsms-web/`: 前端项目 (Vue.js)
- `dsms-backend/`: 后端项目 (Spring Boot)


## 数据库设计规范

### 表命名规范

1. 所有表名必须以 `t_` 前缀开头
2. 表名使用小写字母
3. 多个单词之间使用下划线 `_` 分隔
4. 表名应该使用英文单词，并且能够清晰表达表的用途

### 字段命名规范

1. 字段名使用小写字母
2. 多个单词之间使用下划线 `_` 分隔
4. 创建时间字段统一命名为 `created_at`
5. 更新时间字段统一命名为 `updated_at`
6. 最后修改人字段统一命名为 `last_modified_by`
7. 布尔类型字段使用 `is_` 前缀，如 `is_active`

### 索引命名规范

1. 主键索引：`pk_表名`
2. 唯一索引：`uk_表名_字段名`
3. 普通索引：`idx_表名_字段名`

## 系统设计

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

### 批次管理功能流程图

```mermaid
flowchart TD
    A[开始] --> B[显示批次列表]
    B --> T[筛选批次]
    T --> T1[按批次号搜索]
    T --> T2[按日期范围筛选]
    T1 --> B
    T2 --> B
    B --> C[选择操作类型]
    C --> D[新增批次]
    C --> E[编辑批次]
    C --> F[删除批次]
    C --> G[查看批次详情]
    
    D --> H[填写批次信息]
    H --> I{验证批次信息}
    I -->|验证失败| J[显示错误信息]
    J --> H
    I -->|验证通过| K[保存批次信息]
    K --> B
    
    E --> L[加载批次信息]
    L --> M[修改批次信息]
    M --> N{验证批次信息}
    N -->|验证失败| O[显示错误信息]
    O --> M
    N -->|验证通过| P[更新批次信息]
    P --> B
    
    F --> Q{确认删除}
    Q -->|取消| B
    Q -->|确认| R[删除批次]
    R --> B
    
    G --> S[显示批次详细信息]
    S --> B
```

### 批次管理ER图

```mermaid
erDiagram
    t_BATCH {
        string batch_no PK
        datetime start_date
        datetime end_date
        datetime created_at
        datetime updated_at
        string last_modified_by
    }
```

### 产品批次关联管理功能流程图

```mermaid
flowchart TD
    A[开始] --> B[显示产品批次关联列表]
    B --> T[筛选关联记录]
    T --> T1[按产品名称搜索]
    T --> T2[按批次号搜索]
    T --> T3[按日期范围筛选]
    T1 --> B
    T2 --> B
    T3 --> B
    
    B --> C[选择操作类型]
    C --> D[新增关联]
    C --> E[编辑关联]
    C --> F[删除关联]
    C --> G[查看关联详情]
    
    D --> H1[选择产品]
    H1 --> H2[选择批次]
    H2 --> H3[填写关联信息]
    H3 --> I{验证关联信息}
    I -->|验证失败| J[显示错误信息]
    J --> H3
    I -->|验证通过| K[保存关联信息]
    K --> B
    
    E --> L[加载关联信息]
    L --> M[修改关联信息]
    M --> N{验证关联信息}
    N -->|验证失败| O[显示错误信息]
    O --> M
    N -->|验证通过| P[更新关联信息]
    P --> B
    
    F --> Q{确认删除}
    Q -->|取消| B
    Q -->|确认| R[删除关联]
    R --> B
    
    G --> S[显示关联详细信息]
    S --> B
```

### 产品批次关联管理ER图

```mermaid
erDiagram
    t_PRODUCT ||--o{ t_PRODUCT_BATCH : contains
    t_PRODUCT {
        string product_name_en PK
        string product_name_cn
        string description
        datetime created_at
        datetime updated_at
        string last_modified_by
    }
    
    t_BATCH ||--o{ t_PRODUCT_BATCH : belongs_to
    t_BATCH {
        string batch_no PK
        datetime start_date
        datetime end_date
        datetime created_at
        datetime updated_at
        string last_modified_by
    }
    
    t_PRODUCT_BATCH {
        string product_batch_id PK
        string product_name_en FK
        string batch_no FK
        datetime created_at
        datetime updated_at
        string last_modified_by
    }
```

### 数据库参数管理功能流程图

```mermaid
flowchart TD
    A[开始] --> B[显示数据库配置列表]
    B --> T[筛选配置]
    T --> T1[按配置名称搜索]
    T --> T2[按url筛选]
    T --> T3[按username筛选]
    T1 --> B
    T2 --> B
    T3 --> B

    B --> C[选择操作类型]
    C --> D[新增配置]
    C --> E[编辑配置]
    C --> F[删除配置]
    C --> G[复制配置]
    
    D --> D1[填写配置名称]
    D1 --> D2[输入数据库URL]
    D2 --> D3[自动识别数据库类型]
    D3 --> D4[自动填充驱动类名]
    D4 --> D5[自动获取Schema列表]
    D5 --> D6[填写用户名密码]
    D6 --> D7[测试连接]
    D7 -->|连接失败| D8[显示错误信息]
    D8 --> D6
    D7 -->|连接成功| D9{是否保存}
    D9 -->|取消| B
    D9 -->|确认| D10[保存配置]
    D10 --> B
    
    E --> E1[加载配置信息]
    E1 --> E2[修改配置信息]
    E2 --> E3[测试连接]
    E3 -->|连接失败| E4[显示错误信息]
    E4 --> E2
    E3 -->|连接成功| E5{是否保存}
    E5 -->|取消| B
    E5 -->|确认| E6[更新配置]
    E6 --> B
    
    F --> F1{确认删除}
    F1 -->|取消| B
    F1 -->|确认| F2[删除配置]
    F2 --> B
    
    G --> G1[复制现有配置]
    G1 --> G2[自动填充配置信息]
    G2 --> D1
```

### 数据库参数管理ER图

```mermaid
erDiagram
    t_DB_CONFIG {
        string config_name PK
        string type "数据库类型"
        string url "数据库连接地址"
        string username "数据库用户名"
        string password "数据库密码"
        string driver_class_name "数据库驱动类名"
        string schema "数据库schema"
        datetime created_at
        datetime updated_at
        string last_modified_by
    }
```

### 产品批次配置管理功能流程图

```mermaid
flowchart TD
    A[开始] --> B[显示产品批次配置列表]
    B --> T[筛选配置]
    T --> T1[按产品名称搜索]
    T --> T2[按批次号搜索]
    T --> T3[按数据库配置名称搜索]
    T1 --> B
    T2 --> B
    T3 --> B
    
    B --> C[选择操作类型]
    C --> D[新增配置]
    C --> E[编辑配置]
    C --> F[删除配置]
    C --> G[查看配置详情]
    C --> H[测试数据库连接]
    
    D --> D1[选择产品批次]
    D1 --> D2[选择数据库配置]
    D2 --> D3[填写配置信息]
    D3 --> D4[测试连接]
    D4 -->|连接失败| D5[显示错误信息]
    D5 --> D3
    D4 -->|连接成功| D6{是否保存}
    D6 -->|取消| B
    D6 -->|确认| D7[保存配置]
    D7 --> B
    
    E --> E1[加载配置信息]
    E1 --> E2[修改配置信息]
    E2 --> E3[测试连接]
    E3 -->|连接失败| E4[显示错误信息]
    E4 --> E2
    E3 -->|连接成功| E5{是否保存}
    E5 -->|取消| B
    E5 -->|确认| E6[更新配置]
    E6 --> B
    
    F --> F1{确认删除}
    F1 -->|取消| B
    F1 -->|确认| F2[删除配置]
    F2 --> B
    
    G --> G1[显示配置详细信息]
    G1 --> B
    
    H --> H1[测试数据库连接]
    H1 -->|连接失败| H2[显示错误信息]
    H2 --> B
    H1 -->|连接成功| H3[显示成功信息]
    H3 --> B
```

### 产品批次配置管理ER图

```mermaid
erDiagram
    t_PRODUCT_BATCH ||--o{ t_PRODUCT_BATCH_CONFIG : has
    t_DB_CONFIG ||--o{ t_PRODUCT_BATCH_CONFIG : used_by
    
    t_PRODUCT_BATCH_CONFIG {
        string config_id PK "product_batch_id下划线拼接config_name"
        string product_batch_id FK
        string config_name FK
        datetime created_at
        datetime updated_at
        string last_modified_by
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
