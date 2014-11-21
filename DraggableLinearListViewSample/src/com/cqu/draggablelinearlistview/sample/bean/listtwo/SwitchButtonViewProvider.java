package com.cqu.draggablelinearlistview.sample.bean.listtwo;

import com.cqu.draggablelinearlistview.sample.R;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;

import me.imid.view.SwitchButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *	带有SwitchButton的ListItem的布局实例化和数据填充类
 * @author A Shuai
 *
 */
public class SwitchButtonViewProvider implements IViewProvider{

	@Override
	public View getItemView(View convertView, ViewGroup parent, LayoutInflater mInflater, Object mData, OnGeneralListener mGeneralListener, int position) {
		ViewHoder mViewHoder = null;
		SwitchButtonItemBean mBean = (SwitchButtonItemBean)mData;
		
		if( convertView == null ){
			convertView = mInflater.inflate(R.layout.item_activity_listview_with_switchbutton, parent, false);
			
			mViewHoder = new ViewHoder();
			mViewHoder.position = position;
			mViewHoder.txt = (TextView)convertView.findViewById(R.id.item_activity_listview_with_switchbutton_text);
			mViewHoder.button = (SwitchButton)convertView.findViewById(R.id.item_activity_listview_with_switchbutton_button);
			mViewHoder.button.setTag(mViewHoder);
			convertView.setTag(mViewHoder);
		} else {
			mViewHoder = (ViewHoder)convertView.getTag();
		}
		
		mViewHoder.txt.setText(mBean.getOption());
		mViewHoder.button.setChecked(mBean.isSelected());
		mViewHoder.button.setOnCheckedChangeListener(mGeneralListener);
		
		return convertView;
	}
	
	public class ViewHoder extends IViewHolder{
		int position;
		View mView;
		
		TextView txt;
		SwitchButton button;
		
		public String getOptionStr(){
			return txt.getText().toString();
		}

		@Override
		public int getID() {
			return position;
		}

		@Override
		public View getView() {
			return mView;
		}
		
		public SwitchButton getButton(){
			return button;
		}
	}

}
