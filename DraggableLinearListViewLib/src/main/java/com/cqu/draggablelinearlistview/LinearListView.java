package com.cqu.draggablelinearlistview;

import java.util.ArrayList;

import com.cqu.draggablelinearlistview.adapter.LinearBaseAdapter;
import com.cqu.draggablelinearlistview.adapter.LinearDataSetObserver;
import com.cqu.draggablelinearlistview.extend.LinearTag;
import com.cqu.draggablelinearlistview.extend.ViewWithIndex;
import com.cqu.draggablelinearlistview.listener.OnItemClickListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SoundEffectConstants;
import android.view.View;

/**
 *	以LinearLayout为基础的ListView
 * @author A Shuai
 *
 */
public class LinearListView extends DividerLinearLayout{

	protected SparseArray<ArrayList<ViewWithIndex>> mChildren = null;

	protected View mEmptyView;
	private LinearBaseAdapter mAdapter;
	protected boolean mAreAllItemsSelectable;
	protected OnItemClickListener mOnItemClickListener;
	protected InternalDataSetObserver mDataObserver = null;

	protected CommonInternalListener mInternalListener = null;

	public LinearListView(Context context) {
		super(context);
		init();
	}

	public LinearListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LinearListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 *	初始化
	 */
	private final void init(){
		mChildren = new SparseArray<ArrayList<ViewWithIndex>>();
		
		mDataObserver = new InternalDataSetObserver();
		mInternalListener = new CommonInternalListener();
	}

	public LinearBaseAdapter getLinearAdapter(){
		return mAdapter;
	}

	public void setLinearAdapter(LinearBaseAdapter adapter) {
		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataObserver);
		}

		mAdapter = adapter;

		if (mAdapter != null) {
			mAdapter.registerDataSetObserver(mDataObserver);
			mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
		}

		buildAllChildren();
	}

	/**
	 *	注册一个回调监听器用于当LinearListView中的item被点击的时候触发
	 *	
	 * @param listener	将要被回调用的监听器
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	/**
	 *	返回注册的OnItemClickListener回调监听器
	 *	@return 
	 */
	public final OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	/**
	 *	回调OnItemClickListener，当它定义了的时候
	 * 
	 * @param view	被点击的LinearListView对象中的item对象 
	 * @param position		被点击的item所在adapter中的位置
	 * @param id	被点击的item的ID值
	 * @return True 如果存在关联的OnItemClickListener对象；False对应其他情况
	 */
	public boolean performItemClick(View view, int position, long id) {
		if (mOnItemClickListener != null) {
			playSoundEffect(SoundEffectConstants.CLICK);
			mOnItemClickListener.onItemClick(this, view, position, id);
			return true;
		}
		return false;
	}

	/**
	 * Sets the view to show if the adapter is empty
	 */
	/**
	 *	设置一个emptyView，当adapter为null或者empty的时候显示用
	 * @param emptyView
	 */
	public void setEmptyView(View emptyView) {
		mEmptyView = emptyView;

		final LinearBaseAdapter adapter = getLinearAdapter();
		updateEmptyStatus( (adapter == null) || adapter.isEmpty() );
	}

	/**
	 *	当当前的adapter为空是，此LinearListView可显示一个特殊的空view对象
	 * 
	 * @return 当adapter为空时，显示的特殊view对象
	 */
	public View getEmptyView() {
		return mEmptyView;
	}

	/**
	 *	当adapter为空时，更新此ListView显示状态
	 * @param empty
	 */
	protected void updateEmptyStatus(boolean empty) {
		if (empty) {
			if (mEmptyView != null) {
				mEmptyView.setVisibility(View.VISIBLE);
				setVisibility(View.GONE);
			} else {
				setVisibility(View.VISIBLE);
			}
		} else {
			if (mEmptyView != null)
				mEmptyView.setVisibility(View.GONE);
			setVisibility(View.VISIBLE);
		}
	}

	/**
	 *	刷新指定索引的子视图
	 * @param index
	 */
	protected void refreshIndexChild( int index ){

		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );
		if( mAdapter == null ){
			return;
		}

		View mOldChild = null;
		View mNewChild = null;

		mOldChild = getChildAt(index);
		if( mOldChild != null ){
			mNewChild = mAdapter.getView(index, mOldChild, this);
		} else {
			mNewChild = mAdapter.getView(index, null, this);
		}

		if( mNewChild.getTag() != null && mNewChild.getTag() instanceof LinearTag ){
			((LinearTag)mNewChild.getTag()).mPosition = index;
		} else {
			mNewChild.setTag( new LinearTag(index) );
		}

		if (mAreAllItemsSelectable || mAdapter.isEnabled(index)) {
			mNewChild.setOnClickListener( mInternalListener );
		}

		if( mNewChild != mOldChild ){
			addViewInLayout(mNewChild, index, mNewChild.getLayoutParams(), true);
		}

	}

	/**
	 *	刷新指定索引集的一组子视图
	 * @param mIndexs
	 */
	protected void refreshIndexsChildren( ArrayList<Integer> mIndexs ){

		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );
		if( mAdapter == null ){
			return;
		}

		View mOldChild = null;
		View mNewChild = null;

		for( int i : mIndexs ){
			mOldChild = getChildAt(i);
			if( mOldChild != null ){
				mNewChild = mAdapter.getView(i, mOldChild, this);
			} else {
				mNewChild = mAdapter.getView(i, null, this);
			}

			if( mNewChild.getTag() != null && mNewChild.getTag() instanceof LinearTag ){
				((LinearTag)mNewChild.getTag()).mPosition = i;
			} else {
				mNewChild.setTag( new LinearTag(i) );
			}

			if (mAreAllItemsSelectable || mAdapter.isEnabled(i)) {
				mNewChild.setOnClickListener( mInternalListener );
			}

			if( mNewChild != mOldChild ){
				addViewInLayout(mNewChild, i, mNewChild.getLayoutParams(), true);
			}
		}

	}

	/**
	 *	刷新全部子视图
	 */
	protected void refreshAllChildren(){

		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );
		if( mAdapter == null ){
			return;
		}

		View mOldChild = null;
		View mNewChild = null;
		for (int i = 0; i < mAdapter.getCount(); i++) {

			mOldChild = getChildAt(i);
			if( mOldChild != null ){
				mNewChild = mAdapter.getView(i, mOldChild, this);
			} else {
				mNewChild = mAdapter.getView(i, null, this);
			}

			if( mNewChild.getTag() != null && mNewChild.getTag() instanceof LinearTag ){
				((LinearTag)mNewChild.getTag()).mPosition = i;
			} else {
				mNewChild.setTag( new LinearTag(i) );
			}

			if (mAreAllItemsSelectable || mAdapter.isEnabled(i)) {
				mNewChild.setOnClickListener( mInternalListener );
			}

			if( mNewChild != mOldChild ){
				addViewInLayout(mNewChild, i, mNewChild.getLayoutParams(), true);
			}
		}

	}


	/**
	 *	先销毁全部子视图，在重新构建全部子视图
	 */
	protected void buildAllChildren(){
		removeAllViews();

		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );

		if( mAdapter == null ){
			return;
		}
		
		mChildren.clear();

		int mItemViewType = 0;
		int mItemViewCount = mAdapter.getViewTypeCount();
		
		for( int i = 0; i < mItemViewCount; i++ ){
			mChildren.put(i, new ArrayList<ViewWithIndex>());
		}
		
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View child = mAdapter.getView(i, null, this);
			
			mItemViewType = mAdapter.getItemViewType(i);
			if( mItemViewType >= mItemViewCount ){
				throw new IllegalArgumentException("ItemViewType is bigger than ViewTypeCount");
			} else {
				mChildren.get( mItemViewType ).add( new ViewWithIndex(i, child) );
			}
			
			if( child.getTag() != null && child.getTag() instanceof LinearTag ){
				((LinearTag)child.getTag()).mPosition = i;
			} else {
				child.setTag( new LinearTag(i) );
			}
			if (mAreAllItemsSelectable || mAdapter.isEnabled(i)) {
				child.setOnClickListener( mInternalListener );
			}
			addViewInLayout(child, -1, child.getLayoutParams(), true);
		}

	}


	/**
	 *	为本类所注册使用的观察者
	 * @author A Shuai
	 *
	 */
	protected class InternalDataSetObserver extends LinearDataSetObserver{

		/**
		 *	指定索引的子视图需要刷新
		 */
		@Override
		public void onChanged(int index) {
			refreshIndexChild(index);
		}

		/**
		 *	指定的一组索引的子视图需要刷新
		 */
		@Override
		public void onChanged(ArrayList<Integer> mIndexs) {
			refreshIndexsChildren(mIndexs);
		}

		/**
		 *	全部视图需要刷新
		 */
		@Override
		public void onChanged() {
			refreshAllChildren();
		}

		/**
		 *	全部视图无效，需要销毁后重建
		 */
		@Override
		public void onInvalidated() {
			buildAllChildren();
		}

	}

	/**
	 *	公共回调监听器
	 * @author A Shuai
	 *
	 */
	private class CommonInternalListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if( v.getTag() != null && v.getTag() instanceof LinearTag ){
				LinearTag mTag = (LinearTag) v.getTag();
				if( mOnItemClickListener != null && mAdapter != null ){
					mOnItemClickListener.onItemClick(LinearListView.this, v, mTag.mPosition, mAdapter.getItemId(mTag.mPosition));
				}
			}
		}

	}

}
