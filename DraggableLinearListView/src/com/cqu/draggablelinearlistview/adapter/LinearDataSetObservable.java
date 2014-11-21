package com.cqu.draggablelinearlistview.adapter;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.database.Observable;

/**
 *	{@link Observable}是对{@link LinearDataSetObserver}的特殊化，用于提供一些方法向一组
 *	{@link LinearDataSetObserver}对象发送通知
 * @author A Shuai
 *
 */
public class LinearDataSetObservable extends Observable<LinearDataSetObserver>{

	/**
	 *	当数据集的内容发生变化时，激活每一个观察者的{@link LinearDataSetObserver#onChanged}方法。
	 *	即下次操作时，数据集应当包含新的内容
	 *	这里是被用来通知需要刷新所有的子视图
	 */
	public void notifyChanged() {
		synchronized(mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onChanged();
			}
		}
	}
	
	/**
	 *	@see #notifyChanged()
	 *	
	 *	这里只用通知指定索引的子视图需要刷新即可
	 * @param index		此值指定的子视图需要刷新
	 */
	public void notifyChanged( int index ) {
		synchronized(mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onChanged( index );
			}
		}
	}
	
	/**
	 *	@see #notifyChanged()
	 *
	 *	这里只用通知一组子视图需要刷新即可
	 * @param mIndexs	指定的一组需要刷新的子视图
	 */
	public void notifyChanged( ArrayList<Integer> mIndexs ) {
		synchronized(mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onChanged( mIndexs );
			}
		}
	}

	/**
	 *	当数据集不在有效且不能再次查询时，应当激活每个观察者的{@link DataSetObserver#onInvalidated}方法。
	 *	例如当数据集已经被关闭的时候
	 *	这里是被用来通知所有的子视图均无效，需要清理后重构
	 */
	public void notifyInvalidated() {
		synchronized (mObservers) {
			for (int i = mObservers.size() - 1; i >= 0; i--) {
				mObservers.get(i).onInvalidated();
			}
		}
	}

}
