package com.cqu.draggablelinearlistview.sample.bean.draglisttwo;

public class WindItemBean implements IItemBean {

	@Override
	public Class<? extends IViewProvider> getViewProviderClass() {
		return WindViewProvider.class;
	}

}
