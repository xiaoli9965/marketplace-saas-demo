## 青云Saas 接入用例
- 后端 Spring boot 搭建
- 前端 Ant Design Pro 搭建


## SPI测试模拟脚本
```yaml
scripts/src/spi_req.py
```


## Nginx 配置
```yaml
upstream web-service {
        server 127.0.0.1:8080;
    }

  server {
    listen       80;
    server_name  localhost;

    location / {
        root   /opt/saas/web;
        try_files $uri $uri/ /index.html;

        index  index.html index.htm;
    }

    location ~^/api/ {
        proxy_set_header host $host;
        proxy_set_header X-Real-IP      $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;

        proxy_pass http://web-service;
  }
}
```