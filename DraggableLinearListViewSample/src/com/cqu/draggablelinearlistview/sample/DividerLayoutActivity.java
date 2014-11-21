package com.cqu.draggablelinearlistview.sample;

import android.os.Bundle;

public class DividerLayoutActivity extends BaseActionBarActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dividerlayout);
		setActionBarHomeEnable(true);
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
