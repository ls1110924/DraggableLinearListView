package com.cqu.draggablelinearlistview.sample;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MainActivity extends BaseActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected boolean onHomeKeyDown() {
		return false;
	}

	@Override
	protected boolean onBackKeyDown() {
		return false;
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		private CallbackListener mCallbackListener = null;

		public PlaceholderFragment() {
			mCallbackListener = new CallbackListener();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,false);
			
			rootView.findViewById(R.id.fragment_main_dividerlayout).setOnClickListener(mCallbackListener);
			rootView.findViewById(R.id.fragment_main_linearlist).setOnClickListener(mCallbackListener);
			rootView.findViewById(R.id.fragment_main_linearlisttwo).setOnClickListener(mCallbackListener);
			rootView.findViewById(R.id.fragment_main_draggablelinearlistone).setOnClickListener(mCallbackListener);
			rootView.findViewById(R.id.fragment_main_draggablelinearlisttwo).setOnClickListener(mCallbackListener);
			
			return rootView;
		}
		
		
		private class CallbackListener implements OnClickListener{

			@Override
			public void onClick(View v) {
				switch( v.getId() ){
					case R.id.fragment_main_dividerlayout:
						((MainActivity)getActivity()).startNewActivity(DividerLayoutActivity.class, 
								R.anim.activity_slide_right_in, R.anim.activity_slide_left_out_part, false, null);
						break;
					case R.id.fragment_main_linearlist:
						((MainActivity)getActivity()).startNewActivity(LinearListActivity.class, 
								R.anim.activity_slide_right_in, R.anim.activity_slide_left_out_part, false, null);
						break;
					case R.id.fragment_main_linearlisttwo:
						((MainActivity)getActivity()).startNewActivity(LinearListTwoActivity.class, 
								R.anim.activity_slide_right_in, R.anim.activity_slide_left_out_part, false, null);
						break;
					case R.id.fragment_main_draggablelinearlistone:
						((MainActivity)getActivity()).startNewActivity(DraggableListOneActivity.class, 
								R.anim.activity_slide_right_in, R.anim.activity_slide_left_out_part, false, null);
						break;
					case R.id.fragment_main_draggablelinearlisttwo:
						((MainActivity)getActivity()).startNewActivity(DraggableListTwoActivity.class, 
								R.anim.activity_slide_right_in, R.anim.activity_slide_left_out_part, false, null);
						break;
				}
			}
			
		}
	}

}
