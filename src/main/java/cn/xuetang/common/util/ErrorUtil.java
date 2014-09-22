package cn.xuetang.common.util;

import org.nutz.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wizzer on 14-4-2.
 */
public class ErrorUtil {
    public static String getErrorMsg(int res){
        Map<String,Object> map=new HashMap<String, Object>();
        switch (res){
            case -1:
                map.put("errcode",-1);
                map.put("errmsg","来源未通过验证");
                break;
            case 1:
                map.put("errcode",1);
                map.put("errmsg","操作成功");
                break;
            case 2:
                map.put("errcode",2);
                map.put("errmsg","操作失败");
                break;
            case 3:
                map.put("errcode",3);
                map.put("errmsg","参数错误");
                break;
            case 4:
                map.put("errcode",4);
                map.put("errmsg","内容未发布");
                break;
            case 5:
                map.put("errcode",5);
                map.put("errmsg","未读取到内容");
                break;
            default:
                map.put("errcode",0);
                map.put("errmsg","系统错误");
                break;

        }
        return Json.toJson(map);
    }
}
