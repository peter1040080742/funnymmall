echo "==================部署mmall前端======================"
cd /developer/git-repository/funnymmall/mmall-fe

#clear
rm -rf ./dist

# npm install
npm install --registry=http://registry.npm.taobao.org

# npn 打包
npm run dist   


rm /product/front/mmall_fe/dist -rf   


cp -r ./dist  /product/front/mmall_fe/

