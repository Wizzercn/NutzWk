package com.budwk.app.base.page;

import org.nutz.lang.Lang;

import java.util.List;

public class Pagination extends SimplePage implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public Pagination() {
    }

    /**
     * 构造器
     *
     * @param pageNo     页码
     * @param pageSize   每页几条数据
     * @param totalCount 总共几条数据
     */
    public Pagination(int pageNo, int pageSize, int totalCount) {
        super(pageNo, pageSize, totalCount);
    }

    /**
     * 构造器
     *
     * @param pageNo     页码
     * @param pageSize   每页几条数据
     * @param totalCount 总共几条数据
     * @param list       分页内容
     */
    public Pagination(int pageNo, int pageSize, int totalCount, List list) {
        super(pageNo, pageSize, totalCount);
        this.list = list;
    }

    /**
     * 第一条数据位置
     *
     * @return
     */
    public int getFirstResult() {
        return (pageNo - 1) * pageSize;
    }

    /**
     * 当前页的数据
     */
    private List list;

    /**
     * 获得分页内容
     *
     * @return
     */
    public <T> List<T> getList() {
        return list;
    }

    /**
     * @param classOfT 列表容器內的元素类型
     * @param <T>      列表容器內的元素类型
     * @return
     */
    public <T> List<T> getList(Class<T> classOfT) {
        return Lang.collection2list(list, classOfT);
    }


    /**
     * 设置分页内容
     *
     * @param list
     */
    public void setList(List list) {
        this.list = list;
    }
}
