package com.cqu.draggablelinearlistview.sample.dataset;

import java.util.ArrayList;

public class LinearListDataSet implements IDataSet{

	private ArrayList<Integer> mDataSet = null;
	
	public LinearListDataSet(){
		mDataSet = new ArrayList<Integer>();
	}
	
	public void addItem( int mItem ){
		mDataSet.add(mItem);
	}
	
	/**
	 *	修改指定索引的数据
	 * @param index
	 * @param mItem
	 */
	public void modifyIndexItem( int index, int mItem ){
		mDataSet.set(index, mItem);
	}
	
	/**
	 *	获取指定索引的数据
	 * @param index
	 * @return
	 */
	public int getIndexItem( int index ){
		return mDataSet.get(index);
	}
	
	@Override
	public int size() {
		return mDataSet.size();
	}

	@Override
	public void clear() {
		mDataSet.clear();
	}

}
