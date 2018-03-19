package cn.wizzer.framework.page.datatable;

import java.io.Serializable;

/**
 * Created by wizzer on 2016/6/27.
 */
public class DataTableOrder implements Serializable {
    private static final long serialVersionUID = 1L;

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
