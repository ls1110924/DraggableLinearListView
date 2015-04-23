package com.cqu.draggablelinearlistview.sample.bean.listtwo;

/**
 *	带有SwitchButton的ListItem的数据Bean类
 * @author A Shuai
 *
 */
public class SwitchButtonItemBean implements IItemBean{

	private String mOption;
	private boolean mSelected;
	
	public SwitchButtonItemBean( String mOString, boolean mSelected ){
		this.mOption = mOString;
		this.mSelected = mSelected;
	}
	
	public boolean isSelected() {
		return mSelected;
	}

	public void setSelected(boolean mSelected) {
		this.mSelected = mSelected;
	}

	public String getOption() {
		return mOption;
	}

	@Override
	public Class<? extends IViewProvider> getViewProviderClass() {
		return SwitchButtonViewProvider.class;
	}

}
