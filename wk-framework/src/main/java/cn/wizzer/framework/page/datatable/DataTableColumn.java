package cn.wizzer.framework.page.datatable;

/**
 * Created by wizzer on 2016/6/27.
 */
public class DataTableColumn {
    protected String data;
    protected String name;
    protected boolean searchable;
    protected boolean orderable;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }
}
