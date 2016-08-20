package cn.wizzer.common;
import static org.mockito.Mockito.when;

import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.mockito.Mockito;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;
import org.nutz.mvc.adaptor.ParamExtractor;
import org.nutz.mvc.adaptor.Params;
import org.nutz.mvc.adaptor.injector.ObjectNaviNode;

public class SimpleTest extends TestBase {
    
    @Test
    public void test_dao_ok() {
        Dao dao = ioc.get(Dao.class);
        assertNotNull(dao);
    }
    
    @Test
    public void test_complex_prefix() throws Exception {
        String params = "draw=1&columns%5B0%5D%5Bdata%5D=userId&columns%5B0%5D%5Bname%5D=&columns%5B0%5D%5Bsearchable%5D=true&columns%5B0%5D%5Borderable%5D=true&columns%5B0%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B0%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B1%5D%5Bdata%5D=loginname&columns%5B1%5D%5Bname%5D=&columns%5B1%5D%5Bsearchable%5D=true&columns%5B1%5D%5Borderable%5D=true&columns%5B1%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B1%5D%5Bsearch%5D%5Bregex%5D=false&columns%5B2%5D%5Bdata%5D=nickname&columns%5B2%5D%5Bname%5D=&columns%5B2%5D%5Bsearchable%5D=true&columns%5B2%5D%5Borderable%5D=true&columns%5B2%5D%5Bsearch%5D%5Bvalue%5D=&columns%5B2%5D%5Bsearch%5D%5Bregex%5D=false&order%5B0%5D%5Bcolumn%5D=0&order%5B0%5D%5Bdir%5D=asc&start=0&length=10&search%5Bvalue%5D=&search%5Bregex%5D=false";
        NutMap map = new NutMap();
        for (String kv : params.split("&")) {
            System.out.println(kv);
            String[] tmp = kv.split("=");
            String key = URLDecoder.decode(tmp[0], "UTF-8");
            String value = URLDecoder.decode(tmp.length > 1 ? tmp[1] : "", "UTF-8");
            map.put(key, value);
        }
        System.out.println(map);
        String prefix = "columns";
        Object refer = map;
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameterMap()).thenReturn(new HashMap<String, String[]>());

        ObjectNaviNode no = new ObjectNaviNode();
        String pre = "";
        if ("".equals(prefix))
            pre = "node.";
        ParamExtractor pe = Params.makeParamExtractor(req, refer);
        for (Object name : pe.keys()) {
            String na = (String) name;
            if (na.startsWith(prefix)) {
                no.put(pre + na, pe.extractor(na));
            }
        }
        //Object model = no.get();
        //Object re = Mapl.maplistToObj(model, NutType.list(DataTableColumn.class));
        //System.out.println(re);
    }
}
