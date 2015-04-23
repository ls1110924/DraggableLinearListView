package com.cqu.draggablelinearlistview.listener;

import com.cqu.draggablelinearlistview.LinearListView;

import android.view.View;

/**
 *	自定义的回调接口
 *	当这个LinearListView中的单个子item被点击的时候触发
 *
 * @author A Shuai
 *
 */
public interface OnItemClickListener {

	/**
	 *	当LinearListView中的item被点击的时候被调用
	 *	
	 * @param parent	触发了哪一个LinearListView的点击事件
	 * @param view		被点击的LinearListView中的子item对象
	 * @param position		此子item所在adapter的位置
	 * @param id				此子item所对应的ID
	 */
	void onItemClick(LinearListView parent, View view, int position, long id);

}
