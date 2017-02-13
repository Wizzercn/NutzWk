# wk-code-generator

源于 @enilu 贡献的 nutzwk-code-generator 项目
 
https://github.com/enilu/nutzwk-code-generator 

## 功能介绍
- 根据事先建立好的model类，生成model、service、controller和view、国际化语言文件
- 包括功能：添加，修改，删除，批量删除，分页查询功能

## 使用手册

```
java -jar wk-code-generator-1.0.jar [-lo]
```

        usage: Main [options] [all|entity|service|controller|view]
         -loader                默认是entity,按pojo生成service和module类, 可以改成table,按数据库信息生成pojo
         -c,--config <arg>      spring datasource config file(classpath)
         -ctr,--package <arg>   controller base package
                                name,default:${package}/controllers
         -f,--force             force generate file even if file exists
         -h,--help              show help message
         -i,--include <arg>     include table pattern
         -mod,--package <arg>   model base package name,default:${package}/models
         -o,--output <arg>      output directory, default is base path + src/main/java
         -p,--package <arg>     base package name,default:com.aebiz.app
         -b,--package <arg>     base path
         -sev,--package <arg>   service base package
                                name,default:${package}/services
         -u,--base-uri <arg>    base uri prefix, default is /
         -v,--views <arg>       for generator pages,default:all pages,eg: -v index_detail  ,will generate index.html and
                                detail.html
         -x,--exclude <arg>     exclude table pattern



## 用法
### 在自己的项目中添加依赖

```xml
        <dependency>
               <groupId>cn.wizzer</groupId>
               <artifactId>wk-code-generator</artifactId>
               <version>1.0.1</version>
        </dependency>
```        

### 1,根据java实体生成相关代码(推荐使用）
- 准备 java model类：

```java
        @Comment("国家")
        @Table("dic_country")
        public class DicCountry implements Serializable {
            private static final long serialVersionUID = 1L;
            @Name
            @Prev(els = {@EL("uuid()")})
            private String id;            
            @Column
            @Comment("编码")
            @ColDefine(type = ColType.VARCHAR)
            private String code;            
            @Column
            @Comment("名称")
            @ColDefine(type = ColType.VARCHAR)
            private String name;            
            setter...
            getter...   
        }
```

- 运行Generator类的时候加上如下参数：         
    
        -i DicCountry -p cn.wizzer.app.sys.modules.models.sys  -u /platform/sys

 
### 2,根据表生成相关代码（不推荐使用）

- 比如使用下面语句建表：

```sql
        CREATE TABLE `dic_country` (
          `id` varchar(32) NOT NULL,
          `code` varchar(32) DEFAULT NULL COMMENT 'label:值',
          `name` varchar(64) DEFAULT NULL COMMENT 'label:显示值',
          `createAt` int(30) DEFAULT NULL,
          `updateAt` int(30) DEFAULT NULL,
          PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='label:国家号';
```

- 更改db.properties(如果没有请参考本项目中新建）
- 确保项目中有mysql驱动
- 运行Generator类的时候加上如下参数：         
    
        -i dic_country -p cn.wizzer.app.sys.modules  -u /platform/sys
        

- 会生成和上图一致的代码
 
