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
   cp -rf ./dist/* /var/data/vue
```

## 4.启动项目
```
   cd /var/data
   setenforce 0
   chmod 777 -R /var/data 
   #使用netty版本mqtt broker 输入该命令：
   sudo cp -rf docker-compose-netty.yml docker-compose.yml
   #使用emqx直接启动
   docker-compose up -d
```
