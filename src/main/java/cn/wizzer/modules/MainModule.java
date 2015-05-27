package cn.wizzer.modules;

import cn.wizzer.common.view.VelocityViewMaker;
import cn.wizzer.core.UrlMappingSet;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import cn.wizzer.core.StartSetup;

/** 
 * 类描述： 
 * 创建人：Wizzer 
 * 联系方式：www.wizzer.cn
 * 创建时间：2013-11-26 下午2:08:37 
 * @version 
 */

@Modules(scanPackage=true)
@Ok("raw")
@Fail("http:500")
@IocBy(type=ComboIocProvider.class,args={
	"*org.nutz.ioc.loader.json.JsonLoader","config",
	"*org.nutz.ioc.loader.annotation.AnnotationIocLoader","cn.wizzer"})
@SetupBy(value=StartSetup.class)
@UrlMappingBy(value=UrlMappingSet.class)
@Views({ VelocityViewMaker.class})
public class MainModule {
}
