package com.cqu.draggablelinearlistview.adapter;

import java.util.ArrayList;

import android.database.DataSetObserver;

/**
 *	因为此类所实现ListView的子View全部保持着强引用，
 *	这样每次刷新数据时，无论刷新的数据量多大，都需要刷新全部的子视图，
 *	当只有个别子视图需要刷新时，这样造成的开销过大，
 *	因此实现了可以针对单个或一组视图刷新的接口
 * @author A Shuai
 *
 */
public class LinearDataSetObserver extends DataSetObserver{

	/**
	 *	当指定索引的子视图数据发生变更时，只需要刷新对应子视图就可
	 * @param index		需要刷新的指定子视图索引
	 */
	public void onChanged( int index ) {  }
	
	/**
	 *	当有一组子视图需要刷新时，可调用子接口
	 * @param mIndexs	需要刷新的一组子视图索引集
	 */
	public void onChanged( ArrayList<Integer> mIndexs ){  }
	
}
