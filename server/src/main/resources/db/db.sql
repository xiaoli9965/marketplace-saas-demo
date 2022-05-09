DROP TABLE IF EXISTS saas_user;
CREATE TABLE saas_user
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)                         NOT NULL,
    account     VARCHAR(50)                         NOT NULL,
    password    VARCHAR(50)                         NOT NULL,
    role        VARCHAR(50)                         NOT NULL,
    instance    VARCHAR(255),
    create_time TIMESTAMP DEFAULT current_timestamp NOT NULL
);

insert into saas_user(username, account, password, role, instance)
VALUES ('超级管理员', 'admin', '123456', 'admin', null);

DROP TABLE IF EXISTS saas_third_app;
CREATE TABLE saas_third_app
(
    app_id     VARCHAR(255) PRIMARY KEY,
    app_name   VARCHAR(255) DEFAULT '',
    app_key    VARCHAR(255) DEFAULT '' NOT NULL,
    sso_key    VARCHAR(255) DEFAULT '' NOT NULL,
    mock_retry BOOLEAN      default FALSE
);
-- ALTER TABLE saas_third_app ADD mock_retry BOOLEAN default FALSE;
insert into saas_third_app(app_id, app_name, app_key, sso_key, mock_retry)
VALUES ('app-xxxxx', '测试 1', '2UNHjKQE9u6A4np4kI++o5iXPcn3cA6yZjDEdHN3iXfDFwCRaKtCQHSLT9HdeF+1', 'sso_key-1', true),
       ('app-rxm3bnm8', '请求测试', 'Qa6Y3LaG8e6cW/RmR9mbw/XsSTt1zyVmeqJcVq4N5Y+Y5MHtPwNYX/T1wgK2dR0C', 'sso_key-1', true);

DROP TABLE IF EXISTS saas_instance;
CREATE TABLE saas_instance
(
    instance_id   VARCHAR(255) PRIMARY KEY,
    app_id        VARCHAR(255),
    spec          VARCHAR(255),
    period        VARCHAR(255),
    third_user_id VARCHAR(255),
    user_id       BIGINT,
    debug         BOOLEAN   DEFAULT FALSE             NOT NULL,
    cloud_info    VARCHAR(1000),
    create_time   TIMESTAMP DEFAULT current_timestamp NOT NULL,
    status        BOOLEAN   default true,
    is_del        BOOLEAN   default false
);

DROP TABLE IF EXISTS saas_instance_renew;
CREATE TABLE saas_instance_renew
(
    id          BIGSERIAL PRIMARY KEY,
    instance_id VARCHAR(255),
    spec        VARCHAR(255),
    period      VARCHAR(255),
    action      VARCHAR(255),
    create_time TIMESTAMP DEFAULT current_timestamp NOT NULL
);

DROP TABLE IF EXISTS saas_spi_record;
CREATE TABLE saas_spi_record
(
    id          BIGSERIAL PRIMARY KEY,
    action      VARCHAR(255),
    primary_id  VARCHAR(255),
    params      VARCHAR(2000),
    req         VARCHAR(2000),
    resp        VARCHAR(1000),
    create_time TIMESTAMP DEFAULT current_timestamp NOT NULL

)
