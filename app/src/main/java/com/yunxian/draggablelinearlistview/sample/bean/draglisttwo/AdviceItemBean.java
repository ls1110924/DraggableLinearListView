package com.yunxian.draggablelinearlistview.sample.bean.draglisttwo;

public class AdviceItemBean implements IItemBean{

	@Override
	public Class<? extends IViewProvider> getViewProviderClass() {
		return AdviceViewProvider.class;
	}

}
