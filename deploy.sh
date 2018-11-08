#!/usr/bin/env bash
#编译和部署weikai_server项目

#需配置如下参数
#项目路径，Execute Shell中配置项目路径， pwd 就可以获得该项目路径
#export PROJ_PATH=这个jenkins任务在部署机器上的路径

#输入你的环境上tomcat的全路径
#export TOMCAT_APP_PATH=tomcat在部署机器上的路径

### base函数
killTomcat()
{
	pid=`pa -ef|grep tomcat|grep java|awk '{print $2}'`
    echo "tomcat Id list :$pid"
    if ["$pid" = ""]
    then
    	echo "no tomcat pid alive"
    else
    	kill -9 $pid
    fi
}
cd $PROJ_PATH/weikai_server
mvn clean install

#停tomcat
killTomcat
#删除原有工程
rm -rf $TOMCAT_APP_PATH/webapps/ROOT
rm -f $TOMCAT_APP_PATH/webapps/ROOT.war
rm -f $TOMCAT_APP_PATH/webapps/weikai_server.war

#复制新的工程
cp $PROJ_PATH/weikai_server/target/weikai_server.war $TOMCAT_APP_PATH/webapps/

cd $TOMCAT_APP_PATH/webapps/
mv weikai_server.war ROOT.war

#启动Tomcat
cd $TOMCAT_APP_PATH/
sh bin/startup.sh