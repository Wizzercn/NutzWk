package cn.wizzer.common.util;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.repo.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class DecodeUtil {
	private static final Logger log = LoggerFactory.getLogger(DecodeUtil.class);

	public static String Decrypt(String str,String salt)
    {
        String encoderText = "lsedfoglkwjemc-091324jlkmsda-0sd=1234;l;lsdkOPIER203-4LKJSLDJAS0D925JKNNC,MANSLDJQ32ELK1N4SAIp089er0234lkjo9df82l3kjlknf,nzxc,mn;lasdj9wquelq;d]qowe[;wq;qkwellsdkfj0-0POPOAR0W8RPOp-02@#$sdklj$#)0asdlksadLKJFA9820934)(&$3ij09sdj34-sdfj2po345-09dlkfjlkv,mxncv;laskdkl/a;au093hakjh2389~!@%&*%#&^539478(*&)^(&^_*8-*_+++|78w3ihsdnmnclksdj)(*#%*_@$(+#@$)&@#^*&^#@$()(*#@$HDFIkdhfgkjh098k;ldsk.sdv.c,msd;flkp0w34;2lk-=sd0p121o39-werl2k3;4lj09sdflskjlekfj,mv,mcxvjlksjdflksjdl*(#@!&akhduyqweperilmmdxcasnd*(#@9879327kjhasudfewr kwehriwueyrhc ausdgiq7w8e71 cdsh93ol2q32879y8932qwhdkjanhdskjaoe*&w#jh$)(*dsFshc na89wue32e981yewher12(*&#quds)(*i3o1928osaihdaklsdkalkduqowe3290874kljhklasdhlijhqweio4hwe89(*$#$eriho349oij(#*q$OIJHO)(&*#$_)(IUDSOIUoiOIUSAODFU034liusdrogiuet0lsdmc,.mg;lq-091lk3l;kjsdf--123098fe*(JOKJSFD983345oihjdp0(#*$&#@!HKJH!(@#*&ioysdk@#)uhOA7E98R7239845K(*&(#@*$&HKFDJHWERYIWoi)(*&#@&^%@!dsfoi;.;,p[osklejr230897*(&we2&^%@78*(&#@!(7~&*~^@*&^#(*&auroiqkjwrhoasdf89qlrlkjpour09werk23jh";
        encoderText+=salt;
        int seed, pre;
        try
        {
            seed = DecodeChar(str, 0);
        }
        catch (NumberFormatException e)
        {
        	log.debug(e.getMessage());
            return "";
        }
        try
        {
            pre = DecodeChar(str, 2);
        }
        catch (NumberFormatException e)
        {
        	log.debug(e.getMessage());
            return "";
        }

        pre = pre & 3;

        String ret = "";
        int x;
        int i, j;
        int len = str.length();
        int elen = encoderText.length();


        for (i = pre + pre + 4, j = seed; i < len; i += 2)
        {
            try
            {
                x = DecodeChar(str, i);

            }
            catch (NumberFormatException e)
            {
            	log.debug(e.getMessage());
                return "";
            }

            int intValue = encoderText.charAt(j);
            x ^= intValue;
            char ch = (char) x;
            ret += ch;
            if (++j >= elen)
                j = 0;
        }

        return ret;
    }

    public static String Encrypt(String str,String salt)
    {
        String encoderText = "lsedfoglkwjemc-091324jlkmsda-0sd=1234;l;lsdkOPIER203-4LKJSLDJAS0D925JKNNC,MANSLDJQ32ELK1N4SAIp089er0234lkjo9df82l3kjlknf,nzxc,mn;lasdj9wquelq;d]qowe[;wq;qkwellsdkfj0-0POPOAR0W8RPOp-02@#$sdklj$#)0asdlksadLKJFA9820934)(&$3ij09sdj34-sdfj2po345-09dlkfjlkv,mxncv;laskdkl/a;au093hakjh2389~!@%&*%#&^539478(*&)^(&^_*8-*_+++|78w3ihsdnmnclksdj)(*#%*_@$(+#@$)&@#^*&^#@$()(*#@$HDFIkdhfgkjh098k;ldsk.sdv.c,msd;flkp0w34;2lk-=sd0p121o39-werl2k3;4lj09sdflskjlekfj,mv,mcxvjlksjdflksjdl*(#@!&akhduyqweperilmmdxcasnd*(#@9879327kjhasudfewr kwehriwueyrhc ausdgiq7w8e71 cdsh93ol2q32879y8932qwhdkjanhdskjaoe*&w#jh$)(*dsFshc na89wue32e981yewher12(*&#quds)(*i3o1928osaihdaklsdkalkduqowe3290874kljhklasdhlijhqweio4hwe89(*$#$eriho349oij(#*q$OIJHO)(&*#$_)(IUDSOIUoiOIUSAODFU034liusdrogiuet0lsdmc,.mg;lq-091lk3l;kjsdf--123098fe*(JOKJSFD983345oihjdp0(#*$&#@!HKJH!(@#*&ioysdk@#)uhOA7E98R7239845K(*&(#@*$&HKFDJHWERYIWoi)(*&#@&^%@!dsfoi;.;,p[osklejr230897*(&we2&^%@78*(&#@!(7~&*~^@*&^#(*&auroiqkjwrhoasdf89qlrlkjpour09werk23jh";
        encoderText+=salt;
        int seed = (int) Random(255);
        int pre = seed & 3;
        int len = str.length();
        int elen = encoderText.length();
        int i, j;

        String ret = "";
        ret += EncodeChar(seed);
        ret += EncodeChar((((int) Random(255)) & 0xfc) + pre);
        for (i = 0; i < pre; i++)
            ret += EncodeChar((int) Random(255));

        for (i = 0, j = seed; i < len; i++)
        {
            ret += EncodeChar((str.charAt(i)) ^ (encoderText.charAt(j)));
            if (++j >= elen)
                j = 0;
        }

        //System.out.println("enc: "+ret);
        return ret;
    }

    public static double Random(int x)
    {
        return (Math.floor(Math.random() * 1000000)) % x;
    }

    public static String EncodeChar(int c)
    {
        String s = "";
        double x = Math.floor(c / 16);
        if (x > 9)
        {
            s += (char) (((int) x) - 10 + 0x61);
        }
        else
        {
            s += (int) x;
        }
        x = c % 16;
        if (x > 9)
        {
            s += (char) (((int) x) - 10 + 0x61);
        }
        else
        {
            s += (int) x;
        }
        return s;
    }

    public static int DecodeChar(String str, int i) throws NumberFormatException
    {
        return  Integer.parseInt(str.substring(i, i + 2), 16);
    }
    public static String getSalt(int len){
        byte[] salt = new byte[len];
        Random rand = new Random();
        for(int i=0; i<len; i++){
            int choice = rand.nextInt(2)%2 == 0 ? 65 : 97;
            salt[i] = (byte) ((rand.nextInt(26)+choice) & 0x007f);
        }

        return new String(salt);
    }
    public static void main(String ars[]){
//        String salt=getSalt(60);
//        System.out.println(salt);
//        String pass=Encrypt("test", "UCUeqU");
//        System.out.println("加密："+pass);
//        String y=Decrypt(pass,"UCUeqU");
//        System.out.println("解密："+y);
        Map<String,String> s1=new HashMap<String, String>();
        s1.put("a","a");
        s1.put("a1","a1");
        s1.put("b","b");
        s1.put("b2","b2");
        s1.put("b1","b1");
        Map<String,String> s2=new HashMap<String, String>();
        s2.put("b1","b1");
        s2.put("b2","b2");
        s2.put("b","b");
        s2.put("a1","a1");
        s2.put("a","a");
        SortHashtable sortHashtable=new SortHashtable();
        String ss1=Json.toJson(s1, JsonFormat.full());
        String ss2=Json.toJson(s2, JsonFormat.compact());
        LinkedHashMap map1=Json.fromJson(LinkedHashMap.class, ss1);
        //map1.remove("a");
        LinkedHashMap map2=Json.fromJson(LinkedHashMap.class,ss2);
        //map2.remove("a");
        System.out.println(Base64.encodeToString(Json.toJson(sortHashtable.sortByValue(map1), JsonFormat.compact()).getBytes(),true));
        System.out.println(Base64.encodeToString(Json.toJson(sortHashtable.sortByValue(map1), JsonFormat.compact()).getBytes(),false));
        System.out.println(Json.toJson(sortHashtable.sortByValue(map2), JsonFormat.compact()));

    }
}
