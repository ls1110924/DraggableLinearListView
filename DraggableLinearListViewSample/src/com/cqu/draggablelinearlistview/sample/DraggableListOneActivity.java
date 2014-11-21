package com.cqu.draggablelinearlistview.sample;

import com.cqu.draggablelinearlistview.DraggableLinearListView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 *	手动完成子视图与其相关的响应拖拽的锚视图的绑定
 * @author A Shuai
 *
 */
public class DraggableListOneActivity extends BaseActionBarActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draggablelist);
		setActionBarHomeEnable(true);
		
		DraggableLinearListView mListView = (DraggableLinearListView)findViewById(R.id.activity_draggablelist_list);
		mListView.setScrollViewContainer( (ScrollView)findViewById(R.id.activity_draggablelist_container) );
		
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child1), findViewById(R.id.activity_drag_child1));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child2), findViewById(R.id.activity_drag_child2));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child3), findViewById(R.id.activity_drag_child3));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child4), findViewById(R.id.activity_drag_child4));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child5), findViewById(R.id.activity_drag_child5));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child6), findViewById(R.id.activity_drag_child6_anchor));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child7), findViewById(R.id.activity_drag_child7));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child8), findViewById(R.id.activity_drag_child8));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child9), findViewById(R.id.activity_drag_child9));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child10), findViewById(R.id.activity_drag_child10_anchor));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child11), findViewById(R.id.activity_drag_child11));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child12), findViewById(R.id.activity_drag_child12_anchor));
		mListView.bindViewWithAnchorView(findViewById(R.id.activity_drag_child13), findViewById(R.id.activity_drag_child13));
		
		findViewById(R.id.activity_drag_child4).setOnClickListener( new CommonCallbackListener() );
		findViewById(R.id.activity_drag_child5_button).setOnClickListener( new CommonCallbackListener() );
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
	
	private class CommonCallbackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch( v.getId() ){
				case R.id.activity_drag_child4:
					Toast.makeText(DraggableListOneActivity.this, "第四孩子节点被点击", Toast.LENGTH_SHORT).show();
					break;
				case R.id.activity_drag_child5_button:
					Toast.makeText(DraggableListOneActivity.this, "第五孩子的内部button被点击", Toast.LENGTH_SHORT).show();
					break;
			}
		}
		
	}
	
}
