package com.cqu.draggablelinearlistview.sample;

import android.os.Bundle;
import android.widget.TextView;

public class EmptyActivity extends BaseActionBarActivity{

	private TextView mTxt = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		
		mTxt = (TextView)findViewById(R.id.activity_empty_txt);
		
		Bundle mBundle = getIntent().getExtras();
		mTxt.setText( mBundle.getString("KEY") );
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

}
