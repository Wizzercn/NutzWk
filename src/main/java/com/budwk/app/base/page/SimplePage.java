package com.budwk.app.base.page;

import java.util.ArrayList;
import java.util.List;

public class SimplePage implements java.io.Serializable, Paginable {
    private static final long serialVersionUID = 1L;
    public static final int DEF_COUNT = 10;
    private List<Integer> localArrayList = new ArrayList<Integer>();

    public List<Integer> getSegment() {
        return localArrayList;
    }

    /**
     * 检查页码 checkPageNo
     *
     * @param pageNo
     * @return if pageNo==null or pageNo 小于 1 then return 1 else return pageNo
     */
    public static int cpn(Integer pageNo) {
        return (pageNo == null || pageNo < 1) ? 1 : pageNo;
    }

    public SimplePage() {
    }

    /**
     * 构造器
     *
     * @param pageNo     页码
     * @param pageSize   每页几条数据
     * @param totalCount 总共几条数据
     */
    public SimplePage(int pageNo, int pageSize, int totalCount) {
        setTotalCount(totalCount);
        setPageSize(pageSize);
        setPageNo(pageNo);
        adjustPageNo();
        int totalPages = getTotalPage();
        minPage = minPage < 1 ? 1 : minPage;
        maxPage = maxPage > totalPages ? totalPages : maxPage;
        for (int i = minPage; i <= maxPage; i++) {
            localArrayList.add(i);
        }
    }

    /**
     * 调整页码，使不超过最大页数
     */
    public void adjustPageNo() {
        if (pageNo == 1) {
            return;
        }
        int tp = getTotalPage();
        if (pageNo > tp) {
            pageNo = tp;
        }
    }

    /**
     * 获得页码
     */
    public int getPageNo() {
        return pageNo;
    }

    /**
     * 每页几条数据
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 总共几条数据
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 总共几页
     */
    public int getTotalPage() {
        int totalPage = totalCount / pageSize;
        if (totalPage == 0 || totalCount % pageSize != 0) {
            totalPage++;
        }
        return totalPage;
    }

    /**
     * 是否第一页
     */
    public boolean isFirstPage() {
        return pageNo <= 1;
    }

    /**
     * 是否最后一页
     */
    public boolean isLastPage() {
        return pageNo >= getTotalPage();
    }

    /**
     * 下一页页码
     */
    public int getNextPage() {
        if (isLastPage()) {
            return pageNo;
        } else {
            return pageNo + 1;
        }
    }

    /**
     * 上一页页码
     */
    public int getPrePage() {
        if (isFirstPage()) {
            return pageNo;
        } else {
            return pageNo - 1;
        }
    }

    protected int totalCount = 0;
    protected int pageSize = 20;
    protected int pageNo = 1;

    /**
     * if totalCount 小于 0 then totalCount=0
     *
     * @param totalCount
     */
    public void setTotalCount(int totalCount) {
        if (totalCount < 0) {
            this.totalCount = 0;
        } else {
            this.totalCount = totalCount;
        }
    }

    /**
     * if pageSize 小于 1 then pageSize=DEF_COUNT
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = DEF_COUNT;
        } else {
            this.pageSize = pageSize;
        }
    }

    /**
     * if pageNo 小于 1 then pageNo=1
     *
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    int minPage = pageNo - (int) Math.floor((pageSize - 1) / 2.0D);
    int maxPage = pageNo + (int) Math.ceil((pageSize - 1) / 2.0D);
    int totalPage = getTotalPage();
}
