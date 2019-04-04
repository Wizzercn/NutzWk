wk-starter
---------------------
扩展第三方功能的主文件夹

详细Demo代码见 [nutzboot-demo-custom-starter](https://github.com/nutzam/nutzboot/tree/dev/nutzboot-demo/nutzboot-demo-custom/nutzboot-demo-custom-starter)

```java
@IocBean
public class MainLauncher {

    public static void main(String[] args) {
        NbApp app = new NbApp();
        // 这里演示2种starter加载方式
        // 第一种,io.nutz.demo.custom.starter.MySimpleServerStarter
        //   它声明在 resources/META-INF/nutz/org.nutz.boot.starter.NbStarter
        //   不需要在代码中指明
        // 第二种, 自行添加, io.nutz.demo.custom.starter2.MyStarter2Add
        app.addStarterClass(MyStarter2Add.class);
        app.setPrintProcDoc(true);
        app.run();
    }
}
```