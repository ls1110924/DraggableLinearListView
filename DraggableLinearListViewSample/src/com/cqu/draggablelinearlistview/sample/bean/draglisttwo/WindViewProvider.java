package com.cqu.draggablelinearlistview.sample.bean.draglisttwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqu.draggablelinearlistview.extend.DraggableConvertView;
import com.cqu.draggablelinearlistview.sample.R;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.cqu.draggablelinearlistview.sample.dataset.DraggableListDataSet;
import com.cqu.draggablelinearlistview.sample.dataset.DraggableListDataSet.WindDataSet;

public class WindViewProvider implements IViewProvider{

	@Override
	public DraggableConvertView getItemView(DraggableConvertView convertView, ViewGroup parent,
			LayoutInflater mInflater, Object mData, OnGeneralListener mGeneralListener, int position) {
		
		ViewHolder mViewHoder = null;
		WindDataSet mDataSet = ((DraggableListDataSet)mData).getWind();
		
		if( convertView.mConvertView == null ){
			convertView.mConvertView = mInflater.inflate(R.layout.item_wind, parent, false);
			
			mViewHoder = new ViewHolder();
			mViewHoder.mView = convertView.mConvertView;
			mViewHoder.mWindmill = (ImageView)convertView.mConvertView.findViewById(R.id.item_windmill);
			mViewHoder.mDirection = (TextView)convertView.mConvertView.findViewById(R.id.item_wind_direction);
			mViewHoder.mSpeed = (TextView)convertView.mConvertView.findViewById(R.id.item_wind_speed);
			mViewHoder.mLevel = (TextView)convertView.mConvertView.findViewById(R.id.item_wind_level);
			
			convertView.mConvertView.setTag(mViewHoder);
			convertView.mAnchorView = convertView.mConvertView;
		} else {
			mViewHoder = (ViewHolder) convertView.mConvertView.getTag();
			convertView.mAnchorView = convertView.mConvertView;
		}
		
		mViewHoder.mPosition = position;
		Animation operatingAnim = AnimationUtils.loadAnimation(mInflater.getContext(),R.anim.windwill_rotate);
		operatingAnim.setDuration( mDataSet.mSpeed );
		mViewHoder.mWindmill.startAnimation(operatingAnim);
		
		mViewHoder.mDirection.setText(mDataSet.mDirection);
		mViewHoder.mSpeed.setText(mDataSet.mSpeedStr);
		mViewHoder.mLevel.setText(mDataSet.mLevel);
		
		return convertView;
	}

	public class ViewHolder extends  IViewHolder{
		
		int position;
		View mView;
		
		ImageView mWindmill;
		TextView mDirection;
		TextView mSpeed;
		TextView mLevel;

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
