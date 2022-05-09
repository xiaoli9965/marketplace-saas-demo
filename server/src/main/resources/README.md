### 部署

**数据库初始化**

```shell
# 拉取指定版本
docker pull postgres:12
# 运行 u:postgres p:saasapp  port:35432
docker run --name SAAS -e POSTGRES_PASSWORD=saasapp -e TZ=PRC -p 35432:5432  -d postgres:12
```

**Nginx 配置**
```yaml
upstream web-service {
        server 127.0.0.1:8080;
    }

server {
    listen       80;
    server_name  localhost;

    #access_log  /var/log/nginx/host.access.log  main;

	location / {
                root   /opt/saas/web;
                try_files $uri $uri/ /index.html;

                index  index.html index.htm;
        }

        location /api/* {
                proxy_pass http://web-service;
        }
}
```