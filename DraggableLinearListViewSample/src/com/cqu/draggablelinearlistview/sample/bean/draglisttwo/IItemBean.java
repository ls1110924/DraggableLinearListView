package com.cqu.draggablelinearlistview.sample.bean.draglisttwo;

/**
 *	该ListView单个item的数据封装接口
 * @author A Shuai
 *
 */
public interface IItemBean {
	
	public abstract Class<? extends IViewProvider> getViewProviderClass();
	
}
