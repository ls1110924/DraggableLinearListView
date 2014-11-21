package com.cqu.draggablelinearlistview.extend;

import android.view.View;

/**
 *	可拖拽DraggableLinearListView中的回调
 * @author A Shuai
 *
 */
public class DraggableConvertView {

	/**
	 *	真正的内容视图
	 */
	public View mConvertView;
	
	/**
	 *	针对此内容视图，响应他的拖拽事件的锚视图
	 *	此锚视图需是内容视图自己或其直接或间接子视图，特别不允许定位到别的兄弟视图上
	 */
	public View mAnchorView;
	
	public DraggableConvertView(){
		mConvertView = null;
		mAnchorView = null;
	}
	
	public DraggableConvertView( View mConvertView ){
		this.mConvertView = mConvertView;
		mAnchorView = null;
	}
	
}
