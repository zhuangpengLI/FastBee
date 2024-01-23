## 1.clone本项目
```
   git clone https://gitee.com/zhuangpengli/fastbee-docker.git
   cd fastbee-docker
   cp -rf ./data /var
```
## 2.编译java包
```
   git clone https://gitee.com/zhuangpengli/FastBee.git
   cd FastBee/springboot
   mvn clean package -Dmaven.test.skip=true
   cp ./fastbee-admin/target/fastbee-admin.jar /var/data/java/fastbee-admin.jar
```

## 3.打包前端目录
```
   git clone https://gitee.com/zhuangpengli/FastBee.git
   cd FastBee/vue
   npm install
   npm run build:prod
   cp -rf ./dist/* /var/data/htnl
```

## 4.启动项目
```
   cd /var/data
   docker-compose up -d
```
