package com.cqu.draggablelinearlistview.sample.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cqu.draggablelinearlistview.adapter.DraggableBaseAdapter;
import com.cqu.draggablelinearlistview.extend.DraggableConvertView;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.IItemBean;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.IViewProvider;
import com.cqu.draggablelinearlistview.sample.dataset.DraggableListDataSet;

public class DraggableListTwoActivityListAdapter extends DraggableBaseAdapter{

	protected Context mContext;
	protected LayoutInflater mInflater = null;
	
	protected Resources mResources = null;
	
	private DraggableListDataSet mDataSet = null;
	
	private HashMap<String, Object> mProvidersMap;
	private List<? extends IItemBean> mItemBeanList;
	private List<Class<? extends IViewProvider>> mProviders;
	
	private OnGeneralListener mOnGeneralListener = null;
	
	public DraggableListTwoActivityListAdapter( Context mContext, List<? extends IItemBean> mItemBeanList, List<Class<? extends IViewProvider>> mProviders, DraggableListDataSet mDataSet, OnGeneralListener mOnGeneralListener){
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		
		mResources = this.mContext.getResources();
		
		mProvidersMap = new HashMap<String, Object>();
		this.mItemBeanList = mItemBeanList;
		this.mProviders = mProviders;
		this.mDataSet = mDataSet;
		this.mOnGeneralListener = mOnGeneralListener;
	}
	
	@Override
	public int getCount() {
		return mDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		if (mItemBeanList != null && position < mItemBeanList.size() && position >= 0) {
			return mItemBeanList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if( mItemBeanList == null || position < 0 || position >= mItemBeanList.size() ){
			return 0;
		}
		
		IItemBean mItemBean = mItemBeanList.get(position);
		Class<? extends IViewProvider> providerClass = mItemBean.getViewProviderClass();

		for (int i = 0, size = mProviders.size(); i < size; i++) {
			if (providerClass.getName().equals(mProviders.get(i).getName())) {
				return i;
			}
		}

		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return mProviders.size() == 0 ? 1 : mProviders.size();
	}

	@Override
	public int getCountOfIndexViewType(int mType) {
		int mCount = 0;
		if( mType >= mProviders.size() ){
			return 0;
		}
		String mClassName = mProviders.get(mType).getName();
		for( IItemBean mIItemBean : mItemBeanList ){
			if( mIItemBean.getViewProviderClass().getName().equals(mClassName) ){
				mCount++;
			}
		}
		
		return mCount;
	}

	@Override
	public DraggableConvertView getView(int position, DraggableConvertView convertView, ViewGroup parent) {
		IItemBean mItemBean = mItemBeanList.get(position);
		String viewProviderName = mItemBean.getViewProviderClass().getName();
		
		IViewProvider mIViewProvider = (IViewProvider) mProvidersMap.get(viewProviderName);
		if( mIViewProvider == null ){
			try {
				mIViewProvider = mItemBean.getViewProviderClass().newInstance();
				mProvidersMap.put(viewProviderName, mIViewProvider);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		convertView = mIViewProvider.getItemView(convertView, parent, mInflater, mDataSet, mOnGeneralListener, position);
		
		return convertView;
	}

}
