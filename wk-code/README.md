#NutzWk 5.0.1 代码生成器插件使用说明

  * 安装插件 IDEA Settings --> Plugins --> Install plugin from disk --> wk-code-ideaplugin-nb.jar
  * 创建实体类,必须有@Table,若字段需生成到模板页面则需加 @Comment 字段备注(注意命名规范和大小写)
  * 编译wk-model项目,使类编译至 target/ 目录(目的是让代码生成器可读取到class文件)
  * 在实体类java文件里鼠标右击 --> Generate --> wk-nb mvc
  
##目录说明

  * wk-code-generateor-nb  代码生成器源码
  * wk-code-ideaplugin-nb  IDEA插件源码
  * [wk-code-insight](https://github.com/threefish/NutzCodeInsight) IDEA快速编辑插件         