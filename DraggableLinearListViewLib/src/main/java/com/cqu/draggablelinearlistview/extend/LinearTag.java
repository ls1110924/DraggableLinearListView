package com.cqu.draggablelinearlistview.extend;

/**
 *	LinearListView中的子视图Tag对象的基类
 * @author A Shuai
 *
 */
public class LinearTag {
	
	public static final int INVALID_ID = -1;
	
	/**
	 *	孩子视图所在的索引序号
	 */
	public int mPosition;
	
	/**
	 *	默认构造方法
	 *	孩子视图索引序号标记为-1，表示为无效ID
	 */
	public LinearTag(){
		mPosition = INVALID_ID;
	}
	
	/**
	 *	推荐使用此初始化方法
	 * @param mPosition		取值范围为0到正无穷
	 */
	public LinearTag( int mPosition ){
		this.mPosition = mPosition;
	}
	
	/* getter和setter方法，均不可覆写 */
	public final int getPosition() {
		return mPosition;
	}

	public final void setPosition(int mPosition) {
		this.mPosition = mPosition;
	}
	
}
