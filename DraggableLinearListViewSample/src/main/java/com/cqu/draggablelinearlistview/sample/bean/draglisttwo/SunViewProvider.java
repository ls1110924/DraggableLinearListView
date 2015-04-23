package com.cqu.draggablelinearlistview.sample.bean.draglisttwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqu.draggablelinearlistview.extend.DraggableConvertView;
import com.cqu.draggablelinearlistview.sample.R;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.cqu.draggablelinearlistview.sample.dataset.DraggableListDataSet;
import com.cqu.draggablelinearlistview.sample.dataset.DraggableListDataSet.Sun;

public class SunViewProvider implements IViewProvider{

	@Override
	public DraggableConvertView getItemView(DraggableConvertView convertView, ViewGroup parent,
			LayoutInflater mInflater, Object mData, OnGeneralListener mGeneralListener, int position) {
		ViewHolder mViewHolder = null;
		Sun mDataSet = ((DraggableListDataSet)mData).getSun();
		
		if( convertView.mConvertView == null ){
			convertView.mConvertView = mInflater.inflate(R.layout.item_sun, parent, false);
			
			mViewHolder = new ViewHolder();
			mViewHolder.mView = convertView.mConvertView;
			mViewHolder.mSun = (TextView)convertView.mConvertView.findViewById(R.id.item_sun_info);
			
			convertView.mConvertView.setTag(mViewHolder);
			convertView.mAnchorView = convertView.mConvertView;
		} else {
			mViewHolder = (ViewHolder)convertView.mConvertView.getTag();
			convertView.mAnchorView = convertView.mConvertView;
		}
		
		mViewHolder.position = position;
		mViewHolder.mSun.setText( mDataSet.mSun );
		
		return convertView;
	}
	
	private class ViewHolder extends IViewHolder{

		int position;
		View mView;
		
		TextView mSun;
		
		@Override
		public int getID() {
			return position;
		}

		@Override
		public View getView() {
			return mView;
		}
		
	}

}
