#!/usr/bin/python
# -*- coding: utf-8 -*-
import os.path, sys, subprocess
import shutil, zipfile, urllib

if os.path.exists("dst"):
   shutil.rmtree("dst")
os.makedirs("dst")

subprocess.call("mvn clean install -Dmaven.test.skip=true -U -fn", shell=True)

# 逐个插件模块进行编译
for fname in os.listdir(os.getcwd()):
    if fname.startswith("wk-starter") :
       subprocess.call("mvn clean package assembly:single -Dmaven.test.skip=true -U", shell=True, cwd=fname)
    elif fname.startswith("wk-app") or fname.startswith("wk-framework") :
       subprocess.call("mvn clean package install -Dmaven.test.skip=true", shell=True, cwd=fname)
    
for root, dirs, files in os.walk(os.getcwd()):
        for name in files:
            if name.startswith("wk-starter-") and name.endswith("jar-with-dependencies.jar"):
                fsource = os.path.join(root, name)
                shutil.copyfile(fsource, "dst/starter.jar")
            elif name.endswith("jar-with-dependencies.jar") :
                fsource = os.path.join(root, name)
                shutil.copyfile(fsource, "dst/plugins/" + name[0:-26] + ".jar")
            elif name.startswith("nutzwk") and name.endswith(".war") :
                fsource = os.path.join(root, name)
                shutil.copyfile(fsource, "dst/ROOT.war")
                
# 创建Runnable War
# 创建Runnable War
subprocess.check_call("java -jar starter.jar -inject ROOT.war -output nutzwk.jar", shell=True, cwd="dst/")
subprocess.check_call("pack200 -r -G nutzwk.jar", shell=True, cwd="dst/")
if os.path.exists("dst/ROOT.war"):
   os.remove("dst/ROOT.war")
if os.path.exists("dst/starter.jar"):
   os.remove("dst/starter.jar")


# 拷贝数据库配置文件
os.makedirs("dst/config/custom")
shutil.copyfile("wk-app/wk-web/src/main/resources/config/custom/db.properties", "dst/config/custom/db.properties")

with open(u"dst/start.bat", "w") as f :
    f.write('''cd %~dp0
java -Dfile.encoding=UTF-8 -jar nutzwk.jar ''')

# 写一下说明文件
with open(u"dst/readme.txt", "w") as f :
    f.write('''
# 启动须知

1. 必须使用JDK8
2. 数据库账号密码位于 config\custom\db.properties

# 后台管理系统

1. 地址 http://127.0.0.1:8080/sysadmin
2. 账号密码 superadmin/1

# 其他

项目地址: https://github.com/Wizzercn/NutzWk

反馈和意见,请访问 https://nutz.cn
    ''')
