package cn.wizzer.framework.page.datatable;

/**
 * Created by wizzer on 2016/6/27.
 */
public class DataTableOrder {
    protected int column;
    protected String dir;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
