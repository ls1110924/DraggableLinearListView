package com.cqu.draggablelinearlistview.sample.bean.draglisttwo;

public class SunItemBean implements IItemBean{

	@Override
	public Class<? extends IViewProvider> getViewProviderClass() {
		return SunViewProvider.class;
	}

}
