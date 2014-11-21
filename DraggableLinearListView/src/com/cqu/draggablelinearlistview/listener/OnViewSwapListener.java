package com.cqu.draggablelinearlistview.listener;

import android.view.View;

/**
 * Use with {@link com.cqu.draggablelinearlistview.DraggableLinearListView#com.cqu.draggablelinearlistview.DraggableLinearListView.setOnViewSwapListener(com.cqu.draggablelinearlistview.listener.OnViewSwapListener)}
 * to listen for draggable view swaps.
 */
/**
 *	自定义监听拖拽子view时触发的交换子view交换位置的回调监听器
 *	使用此{@link com.cqu.draggablelinearlistview.LinearListView#com.cqu.draggablelinearlistview.DraggableLinearListView.setOnViewSwapListener(OnViewSwapListener)}
 *	来监听可拖拽子view的交换事件
 * @author A Shuai
 *
 */
public interface OnViewSwapListener {

	/**
	 *	由于一个拖拽事件而触发此回调
	 *	经过此次交换，firstPosition所指向的view会占据secondPosition所指向的view位置，反之亦然
	 *	不保证两个数有固定的大小顺序
	 * @param firstView
	 * @param firstPosition
	 * @param secondView
	 * @param secondPosition
	 */
	public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition);
	
}
