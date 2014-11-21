package com.cqu.draggablelinearlistview.sample.bean.listtwo;

import com.cqu.draggablelinearlistview.sample.R;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *	为通用item实例化布局对象的封装类
 * @author A Shuai
 *
 */
public class GeneralViewProvider implements IViewProvider{

	@Override
	public View getItemView(View convertView, ViewGroup parent, LayoutInflater mInflater, Object mData, OnGeneralListener mGeneralListener, int position) {
		ViewHoder mViewHoder = null;
		GeneralItemBean mBean = (GeneralItemBean)mData;
		
		if( convertView == null ){
			convertView = mInflater.inflate(R.layout.item_activity_listview_general, parent, false);
			
			mViewHoder = new ViewHoder();
			mViewHoder.position = position;
			mViewHoder.txt = (TextView)convertView.findViewById(R.id.item_activity_listview_general_text);
			convertView.setTag( mViewHoder );
		} else {
			mViewHoder = (ViewHoder)convertView.getTag();
		}
		
		mViewHoder.txt.setText(mBean.getOption());
		
		return convertView;
	}
	
	public class ViewHoder extends  IViewHolder{
		
		int position;
		View mView;
		
		TextView txt;

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
