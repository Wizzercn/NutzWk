package cn.wizzer.common.page;

import org.nutz.dao.pager.Pager;

/** 
 * 类描述： 
 * 创建人：Wizzer 
 * 联系方式：www.wizzer.cn
 * 创建时间：2013-12-12 下午3:16:21 
 * @version 
 */
public class ExplicitPager extends Pager {

	private static final long serialVersionUID = 1L;
	int offset;
	int count;
	
	public ExplicitPager(int offset, int count) {
		super();
		this.offset = offset;
		this.count = count;
	}

	public void setOffset( int offset) {
		this.offset = offset;
	}
	
	public void setSelectCount( int count) {
		this.count = count;
	}
	
	@Override
	public int getPageSize() {
		return count;
	}

	@Override
	public int getOffset() {
		return offset;
	}
	
}
