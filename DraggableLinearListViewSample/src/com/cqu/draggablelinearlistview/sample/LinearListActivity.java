package com.cqu.draggablelinearlistview.sample;

import java.util.ArrayList;

import com.cqu.draggablelinearlistview.LinearListView;
import com.cqu.draggablelinearlistview.sample.adapter.LinearListActivityListAdapter;
import com.cqu.draggablelinearlistview.sample.dataset.LinearListDataSet;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LinearListActivity extends BaseActionBarActivity{

	private LinearListView mHorizontalList = null;
	private LinearListActivityListAdapter mHorizontalListAdapter = null;
	private LinearListDataSet mHorizontalListDataSet = null;
	
	private LinearListView mVerticalList = null;
	private LinearListActivityListAdapter mVerticalListAdapter = null;
	private LinearListDataSet mVerticalListDataSet = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linearlist);
		setActionBarHomeEnable(true);
		
		mHorizontalList = (LinearListView)findViewById(R.id.activity_linearlist_horizontal_list);
		mVerticalList = (LinearListView)findViewById(R.id.activity_linearlist_list);
		
		mHorizontalListDataSet = new LinearListDataSet();
		mVerticalListDataSet = new LinearListDataSet();
		
		mHorizontalListAdapter = new LinearListActivityListAdapter(this, mHorizontalListDataSet);
		mVerticalListAdapter = new LinearListActivityListAdapter(this, mVerticalListDataSet);
		
		for( int i = 0; i < 30; i++ ){
			mHorizontalListDataSet.addItem(i);
			mVerticalListDataSet.addItem(i);
		}
		
		mHorizontalList.setLinearAdapter(mHorizontalListAdapter);
		mVerticalList.setLinearAdapter(mVerticalListAdapter);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_linearlist, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item, int flag) {
		switch( item.getItemId() ){
			case R.id.action_divider_refreshone:
				mVerticalListDataSet.modifyIndexItem(0, 30);
				mVerticalListAdapter.notifyDataSetChanged(0);
				break;
			case R.id.action_divider_refreshgroup:
				ArrayList<Integer> mList = new ArrayList<Integer>();
				for( int i = 2; i < 10; i++ ){
					mList.add(i);
					mVerticalListDataSet.modifyIndexItem(i, i + 30);
				}
				mVerticalListAdapter.notifyDataSetChanged(mList);
				break;
			case R.id.action_divider_refreshall:
				if( mVerticalListDataSet.size() == 30 ){
					mVerticalListDataSet.clear();
					for( int i = 0; i < 30; i++ ){
						mVerticalListDataSet.addItem( 30 + i );
					}
				}
				mVerticalListAdapter.notifyDataSetChanged();
				break;
			case R.id.action_divider_rebuild:
				mVerticalListDataSet.clear();
				for( int i = 0; i < 40; i++ ){
					mVerticalListDataSet.addItem(i);
				}
				mVerticalListAdapter.notifyDataSetInvalidated();
				break;
		}
		return true;
	}
	
}
