package com.cqu.draggablelinearlistview.sample.bean.listtwo;

/**
 *	只有一个title和箭头的普通ListItem的数据Bean
 * @author A Shuai
 *
 */
public class GeneralItemBean implements IItemBean{

	private String mOption;
	
	public GeneralItemBean( String mOption ){
		this.mOption = mOption;
	}
	
	public String getOption() {
		return mOption;
	}
	
	public void setOption( String mOption ){
		this.mOption = mOption;
	}

	@Override
	public Class<? extends IViewProvider> getViewProviderClass() {
		return GeneralViewProvider.class;
	}

}
