package cn.xuetang.common.util;

import org.apache.commons.lang.math.NumberUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Wizzer
 * Date: 2010-2-6
 * Time: 21:01:57
 * To change this template use File | Settings | File Templates.
 */
public class SortHashtable {

    /**
     * 方法名称：getSortedHashtable
     * 参数：Hashtable h 引入被处理的散列表
     * 描述：将引入的hashtable.entrySet进行排序，并返回
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map.Entry[] getSortedHashtableByKey(Hashtable h) {

        Set set = h.entrySet();

        Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);

        Arrays.sort(entries, new Comparator() {
            public int compare(Object arg0, Object arg1) {
                Object key1 = ((Map.Entry) arg0).getKey();
                Object key2 = ((Map.Entry) arg1).getKey();
                return ((Comparable) key1).compareTo(key2);
            }

        });

        return entries;
    }

    /**
     * 方法名称：getSortedHashtable
     * 参数：Hashtable h 引入被处理的散列表
     * 描述：将引入的hashtable.entrySet进行排序，并返回
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map.Entry[] getSortedHashtableByValue(Hashtable h) {
        Set set = h.entrySet();
        Map.Entry[] entries = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);

        Arrays.sort(entries, new Comparator() {
            public int compare(Object arg0, Object arg1) {
                int key1 = NumberUtils.toInt(((Map.Entry) arg0).getValue()
                        .toString());
                int key2 = NumberUtils.toInt(((Map.Entry) arg1).getValue()
                        .toString());
                return ((Comparable) key1).compareTo(key2);
            }
        });

        return entries;
    }

    public static LinkedHashMap sortByValue(Map map) {
        //将键值赌赢的entryset放到链表中
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {

            //将链表按照值得从大到小进行排序
            public int compare(Object o1, Object o2) {
                String key1 = String.valueOf(((Map.Entry) o1).getKey());
                String key2 = String.valueOf(((Map.Entry) o2).getKey());
                return key1.compareTo(key2);
            }
        });

        LinkedHashMap result = new LinkedHashMap();

        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.println("key::"+entry.getKey()+" val::"+entry.getValue());
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * @param args
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        Hashtable t = new Hashtable();

        t.put("10-11 02:45德利城VS德罗赫达联队", "德利城VS德罗赫达联队");
        t.put("10-11 02:45朗福德城VS谢尔伯恩", "朗福德城VS谢尔伯恩");
        t.put("10-11 05:00EL国民队VS托拉卡", "EL国民队VS托拉卡");
        t.put("10-11 02:00巴西VS厄瓜多尔", "巴西VS厄瓜多尔");

        Map.Entry[] set = getSortedHashtableByKey(t);

        // perportyTable
        for (int i = 0; i < set.length; i++) {

            System.out.println(set[i].getKey().toString());

            System.out.println(set[i].getValue().toString());

        }

    }


}
