package com.cqu.draggablelinearlistview.listener;

/**
 *	监听可拖拽DraggableLinearListView控件中拖拽状态的回调
 * @author A Shuai
 *
 */
public interface OnDragStateChangeListener {

	/**
	 *	开始拖拽
	 */
	public void onStartDrag();
	
	/**
	 *	拖拽结束
	 */
	public void onStopDrag();
	
}
