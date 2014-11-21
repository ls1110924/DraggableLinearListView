package com.cqu.draggablelinearlistview.sample;

import java.util.ArrayList;
import java.util.List;

import com.cqu.draggablelinearlistview.DraggableLinearListView;
import com.cqu.draggablelinearlistview.extend.DragTriggerType;
import com.cqu.draggablelinearlistview.sample.adapter.DraggableListTwoActivityListAdapter;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.AdviceItemBean;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.AdviceViewProvider;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.IItemBean;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.IViewProvider;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.SunItemBean;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.SunViewProvider;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.WindItemBean;
import com.cqu.draggablelinearlistview.sample.bean.draglisttwo.WindViewProvider;
import com.cqu.draggablelinearlistview.sample.dataset.DraggableListDataSet;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;

public class DraggableListTwoActivity extends BaseActionBarActivity{

	private List<IItemBean> mOptionDataSet = null;
	private List<Class<? extends IViewProvider>> mProvidersList = null;
	
	private DraggableLinearListView mListView = null;
	private DraggableListDataSet mListDataSet = null;
	private DraggableListTwoActivityListAdapter mListAdapter = null;
	
	private CommonCallbackListener mCommonListener = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draggablelisttwo);
		setActionBarHomeEnable(true);
		mCommonListener = new CommonCallbackListener();
		
		mListView = (DraggableLinearListView)findViewById(R.id.activity_draggablelisttwo_list);
		mListView.setDragTriggerType(DragTriggerType.LONG_PRESS);
		mListView.setScrollViewContainer( (ScrollView)findViewById(R.id.activity_draggablelisttwo_container) );
		
		init();
		mListDataSet = new DraggableListDataSet();
		mListAdapter = new DraggableListTwoActivityListAdapter(this, mOptionDataSet, mProvidersList, mListDataSet, mCommonListener);
		mListView.setDraggableAdapter(mListAdapter);
	}

	@Override
	protected boolean onHomeKeyDown() {
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in_part, R.anim.activity_slide_right_out);
		return true;
	}

	@Override
	protected boolean onBackKeyDown() {
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in_part, R.anim.activity_slide_right_out);
		return true;
	}
	
	private void init(){
		mOptionDataSet = new ArrayList<IItemBean>();
		mProvidersList = new ArrayList<Class<? extends IViewProvider>>();
		
		mOptionDataSet.add( new WindItemBean() );
		mOptionDataSet.add( new SunItemBean() );
		mOptionDataSet.add( new AdviceItemBean() );
		
		mProvidersList.add( WindViewProvider.class );
		mProvidersList.add( SunViewProvider.class );
		mProvidersList.add( AdviceViewProvider.class );
	}
	
	private class CommonCallbackListener implements OnGeneralListener{

		@Override
		public void onClick(View v) {
			switch( v.getId() ){
			
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  }
		
	}
	
	
}
