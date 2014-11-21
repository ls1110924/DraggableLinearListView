package com.cqu.draggablelinearlistview.sample;

import java.util.ArrayList;
import java.util.List;

import com.cqu.draggablelinearlistview.LinearListView;
import com.cqu.draggablelinearlistview.listener.OnItemClickListener;
import com.cqu.draggablelinearlistview.sample.adapter.LinearListTwoActivityListAdapter;
import com.cqu.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.GeneralItemBean;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.GeneralViewProvider;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.IItemBean;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.IViewProvider;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.SwitchButtonItemBean;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.SwitchButtonViewProvider;
import com.cqu.draggablelinearlistview.sample.bean.listtwo.SwitchButtonViewProvider.ViewHoder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

public class LinearListTwoActivity extends BaseActionBarActivity{

	private List<IItemBean> mOptionDataSet = null;
	private List<Class<? extends IViewProvider>> mProvidersList = null;
	
	private LinearListView mListView = null;
	private LinearListTwoActivityListAdapter mListViewAdapter = null;
	
	private CommonCallbackListener mCommonListener = null;
	
	protected Bundle mBundle = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linearlisttwo);
		setActionBarHomeEnable(true);
		mCommonListener = new CommonCallbackListener();
		
		mListView = (LinearListView)findViewById(R.id.activity_linearlisttwo_list);
		mListView.setOnItemClickListener( mCommonListener );
		
		mOptionDataSet = new ArrayList<IItemBean>();
		mProvidersList = new ArrayList<Class<? extends IViewProvider>>();
		
		mOptionDataSet.add( new SwitchButtonItemBean("Message 1", true) );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 2", false) );
		mOptionDataSet.add( new GeneralItemBean("Message 3") );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 4", true) );
		mOptionDataSet.add( new GeneralItemBean("Message 5") );
		mOptionDataSet.add( new GeneralItemBean("Message 6") );
		mOptionDataSet.add( new GeneralItemBean("Message 7") );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 8", true) );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 9", false) );
		mOptionDataSet.add( new GeneralItemBean("Message 10") );
		mOptionDataSet.add( new GeneralItemBean("Message 11") );
		mOptionDataSet.add( new GeneralItemBean("Message 12") );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 13", true) );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 14", false) );
		mOptionDataSet.add( new GeneralItemBean("Message 15") );
		mOptionDataSet.add( new GeneralItemBean("Message 16") );
		mOptionDataSet.add( new GeneralItemBean("Message 17") );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 18", true) );
		mOptionDataSet.add( new SwitchButtonItemBean("Message 19", false) );
		mOptionDataSet.add( new GeneralItemBean("Message 20") );
		
		mProvidersList.add( SwitchButtonViewProvider.class );
		mProvidersList.add( GeneralViewProvider.class );
		
		mListViewAdapter = new LinearListTwoActivityListAdapter(this, mOptionDataSet, mProvidersList, mCommonListener);
		mListView.setLinearAdapter(mListViewAdapter);
		
		mBundle = new Bundle();
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
		getMenuInflater().inflate(R.menu.menu_linearlisttwo, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item, int flag) {
		switch( item.getItemId() ){
			case R.id.action_lineartwo_refreshone:
				if( mOptionDataSet.size() != 20 ){
					break;
				}
				((SwitchButtonItemBean)mOptionDataSet.get(0)).setSelected(false);
				mListViewAdapter.notifyDataSetChanged(0);
				break;
			case R.id.action_lineartwo_refreshgroup:
				if( mOptionDataSet.size() != 20 ){
					break;
				}
				ArrayList<Integer> mList = new ArrayList<Integer>();
				mList.add(1);
				mList.add(2);
				mList.add(3);
				((SwitchButtonItemBean)mOptionDataSet.get(1)).setSelected(true);
				((GeneralItemBean)mOptionDataSet.get(2)).setOption("Message 3+++");
				((SwitchButtonItemBean)mOptionDataSet.get(3)).setSelected(false);
				mListViewAdapter.notifyDataSetChanged(mList);
				break;
			case R.id.action_lineartwo_refreshall:
				if( mOptionDataSet.size() != 20 ){
					break;
				}
				((SwitchButtonItemBean)mOptionDataSet.get(0)).setSelected(true);
				((SwitchButtonItemBean)mOptionDataSet.get(1)).setSelected(false);
				((GeneralItemBean)mOptionDataSet.get(2)).setOption("Message 3");
				((SwitchButtonItemBean)mOptionDataSet.get(3)).setSelected(true);
				mListViewAdapter.notifyDataSetChanged();
				break;
			case R.id.action_lineartwo_rebuild:
				for( int i = 19; i > 11; i-- ){
					mOptionDataSet.remove(i);
				}
				mListViewAdapter.notifyDataSetInvalidated();
				break;
		}
		return true;
	}
	
	/**
	 *	公共回调监听器
	 * @author A Shuai
	 *
	 */
	private class CommonCallbackListener implements OnItemClickListener, OnGeneralListener{

		@Override
		public void onItemClick(LinearListView parent, View view, int position, long id) {
			switch( parent.getId() ){
				case R.id.activity_linearlisttwo_list:
					switch( position ){
						case 0:
						case 1:
						case 3:
						case 7:
						case 8:
						case 12:
						case 13:
						case 17:
						case 18:
							SwitchButtonViewProvider.ViewHoder mViewHoder = (ViewHoder) view.getTag();
							if( mViewHoder.getButton().isChecked() ){
								mBundle.clear();
								mBundle.putString("KEY", "位置：" + position + "，" + mViewHoder.getOptionStr());
							} else {
								mViewHoder.getButton().toggle();
								return;
							}
							break;
						default:
							mBundle.clear();
							mBundle.putString("KEY", "位置：" + position);
							break;
					}
					startNewActivity(EmptyActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out_part, false, mBundle);
					break;
				default:
					break;
			}
		}

		@Override
		public void onClick(View v) {
			
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			SwitchButtonViewProvider.ViewHoder mViewHoder = (ViewHoder) buttonView.getTag();
			String mOptionStr = mViewHoder.getOptionStr();
			if( mOptionStr.equals("Message 1") ){
				((SwitchButtonItemBean)mOptionDataSet.get(0)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 2") ){
				((SwitchButtonItemBean)mOptionDataSet.get(1)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 4") ){
				((SwitchButtonItemBean)mOptionDataSet.get(3)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 8") ){
				((SwitchButtonItemBean)mOptionDataSet.get(7)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 9") ){
				((SwitchButtonItemBean)mOptionDataSet.get(8)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 13") ){
				((SwitchButtonItemBean)mOptionDataSet.get(12)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 14") ){
				((SwitchButtonItemBean)mOptionDataSet.get(13)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 18") ){
				((SwitchButtonItemBean)mOptionDataSet.get(17)).setSelected(isChecked);
			} else if ( mOptionStr.equals("Message 19") ){
				((SwitchButtonItemBean)mOptionDataSet.get(18)).setSelected(isChecked);
			}
		}
		
	}
	
}
