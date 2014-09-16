package cn.xuetang.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class OnlineUtil {
	private static List<String[]> list = new ArrayList<String[]>();
	public static void addUser(Object object, String ex) {
        String nowtime = DateUtil.getTime("HH:mm:ss");
        String[] str = new String[3];
        str[0] = object.toString();
        str[1] = nowtime;
        str[2] = ex;
        //temp数组用来临时存放从list里面取出的用户ip和访问时间
        String[] temp = new String[3];
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
            if (temp[0].equals(str[0]) && temp[2].equals(ex)) {
                list.set(i, str);
                return;
            }

        }
        list.add(str);
        str = null;
        temp = null;
    }

    public static int getOnlineCount(String ex) {
        //返回当前在线人数
        String[] temp = new String[3];
        String nowtime = DateUtil.getTime("HH:mm:ss");
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
             //假如用户超过10分钟没有访问，则认为不在线
            if (subTime(nowtime, temp[1]) > 600 && temp[2].equals(ex)) {
                list.remove(i);
            }

        }
        return list.size();
    }

    public static List<String[]> getOnline() {
        return list;
    }

    /**
     * 计算两个时间差，返回相差分数
     */
    private static int subTime(String src, String des) {
        int n = 0;
        java.util.Calendar ca = java.util.Calendar.getInstance();
        long time1 =
                DateUtil.compareStringTime(src, des, "HH:mm:ss");
        ca.setTimeInMillis(time1);
        n = (ca.get(java.util.Calendar.MINUTE)) * 60;
        n = n + ca.get(java.util.Calendar.SECOND);
        return n;
    }

}
