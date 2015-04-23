package com.cqu.draggablelinearlistview.sample.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqu.draggablelinearlistview.adapter.LinearBaseAdapter;
import com.cqu.draggablelinearlistview.extend.LinearTag;
import com.cqu.draggablelinearlistview.sample.R;
import com.cqu.draggablelinearlistview.sample.dataset.LinearListDataSet;

public class LinearListActivityListAdapter extends LinearBaseAdapter{

	protected Context mContext;
	protected LayoutInflater mInflater = null;
	
	protected Resources mResources = null;
	
	private LinearListDataSet mDataSet = null;
	
	public LinearListActivityListAdapter( Context  mContext, LinearListDataSet mDataSet ){
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		
		mResources = this.mContext.getResources();
		
		this.mDataSet = mDataSet;
	}
	
	@Override
	public int getCount() {
		return mDataSet.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataSet.getIndexItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemHolder mHolder = null;
		if( convertView == null ){
			mHolder = new ItemHolder();
			
			convertView = mInflater.inflate(R.layout.item_list_common, parent, false);
			mHolder.txt = (TextView) convertView.findViewById(R.id.text);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (ItemHolder) convertView.getTag();
		}
		
		mHolder.txt.setText("Message " + mDataSet.getIndexItem(position));
		
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return mDataSet.size();
	}

	@Override
	public int getCountOfIndexViewType(int mType) {
		switch( mType ){
			case 0:
				return mDataSet.size();
			default:
				break;
		}
		return 0;
	}
	
	private class ItemHolder extends LinearTag{
		
		TextView txt;
		
	}

}
