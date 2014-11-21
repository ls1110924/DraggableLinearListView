package com.cqu.draggablelinearlistview.adapter;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.widget.ListAdapter;

/**
 *	自定义的可优化所定义的LinearListView的适配器
 * @author A Shuai
 *
 */
public abstract class LinearBaseAdapter implements ListAdapter{

	private final LinearDataSetObservable mLinearDataSetObservable = new LinearDataSetObservable();
	
	@Override
	public final void registerDataSetObserver(DataSetObserver observer) {  }

	@Override
	public final void unregisterDataSetObserver(DataSetObserver observer) {  }

	public void registerDataSetObserver(LinearDataSetObserver observer) {
		mLinearDataSetObservable.registerObserver(observer);
	}

	public void unregisterDataSetObserver(LinearDataSetObserver observer) {
		mLinearDataSetObservable.unregisterObserver(observer);
	}

	/**
	 *	通知所绑定的观察者，其潜在的数据已经发生了变化，任何一个与此数据集产生映射关系的子视图应当刷新
	 */
	public void notifyDataSetChanged() {
		mLinearDataSetObservable.notifyChanged();
	}

	/**
	 *	通知指定索引的子视图需要刷新
	 *	@see #notifyDataSetChanged()
	 * @param index
	 */
	public void notifyDataSetChanged( int index ) {
		mLinearDataSetObservable.notifyChanged( index );
	}

	/**
	 *	通知指定索引集的子视图需要刷新
	 *	@see #notifyDataSetChanged()
	 * @param mIndexs
	 */
	public void notifyDataSetChanged( ArrayList<Integer> mIndexs ) {
		mLinearDataSetObservable.notifyChanged( mIndexs );
	}

	/**
	 *	通知所绑定的观察者，其潜在额数据已经无效。一旦报告了所在的Adapter不在有效，应当先销毁所有的子视图，并重构所有的子视图
	 */
	public void notifyDataSetInvalidated() {
		mLinearDataSetObservable.notifyInvalidated();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}

	/**
	 *	得到指定类型的子view的数量
	 * @param mType	需要知道数量的View的类型索引
	 * @return
	 */
	public abstract int getCountOfIndexViewType( int mType );


}
