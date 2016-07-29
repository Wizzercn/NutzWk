package cn.wizzer.common.util;

import org.nutz.json.Json;

public class TestUtils {
   public static void main(String[] args) {
	   String str = "123456###测试";
	   String[]  strs = str.split("###");
	   System.out.println(Json.toJson(strs));
   }
}
