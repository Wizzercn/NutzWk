var chain={
	"default" : {
		"ps" : [
			  "cn.wizzer.common.mvc.processor.LogTimeProcessor",
			  "cn.wizzer.common.mvc.processor.GlobalsSettingProcessor",
			  "org.nutz.mvc.impl.processor.UpdateRequestAttributesProcessor",
		      "org.nutz.mvc.impl.processor.EncodingProcessor",
		      "cn.wizzer.common.mvc.processor.XSSProcessor",
		      "org.nutz.mvc.impl.processor.ModuleProcessor",
		      "cn.wizzer.common.mvc.processor.NutShiroProcessor",
		      "org.nutz.mvc.impl.processor.ActionFiltersProcessor",
		      "org.nutz.mvc.impl.processor.AdaptorProcessor",
		      "org.nutz.mvc.impl.processor.MethodInvokeProcessor",
			  "org.nutz.mvc.impl.processor.ViewProcessor"
		],
		"error" : 'org.nutz.mvc.impl.processor.FailProcessor'
	}
};