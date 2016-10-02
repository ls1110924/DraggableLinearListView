package com.yunxian.draggablelinearlistview.sample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.yunxian.draggablelinearlistview.lib.DraggableLinearListView;
import com.yunxian.draggablelinearlistview.lib.extend.DragTriggerType;
import com.yunxian.draggablelinearlistview.lib.listener.OnDragStateChangeListener;
import com.yunxian.draggablelinearlistview.lib.listener.OnViewSwapListener;
import com.yunxian.draggablelinearlistview.sample.adapter.DraggableListTwoActivityListAdapter;
import com.yunxian.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.AdviceItemBean;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.AdviceViewProvider;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.IItemBean;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.IViewProvider;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.SunItemBean;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.SunViewProvider;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.WindItemBean;
import com.yunxian.draggablelinearlistview.sample.bean.draglisttwo.WindViewProvider;
import com.yunxian.draggablelinearlistview.sample.dataset.DraggableListDataSet;

import java.util.ArrayList;
import java.util.List;

public class DraggableListTwoActivity extends BaseActionBarActivity {

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

        mListView = (DraggableLinearListView) findViewById(R.id.activity_draggablelisttwo_list);
        mListView.setDragTriggerType(DragTriggerType.LONG_PRESS);
        mListView.setScrollViewContainer((ScrollView) findViewById(R.id.activity_draggablelisttwo_container));
        mListView.setOnViewSwapListener(mCommonListener);
        mListView.setOnDragStateChangeListener(mCommonListener);

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

    private void init() {
        mOptionDataSet = new ArrayList<IItemBean>();
        mProvidersList = new ArrayList<Class<? extends IViewProvider>>();

        mOptionDataSet.add(new WindItemBean());
        mOptionDataSet.add(new SunItemBean());
        mOptionDataSet.add(new AdviceItemBean());

        mProvidersList.add(WindViewProvider.class);
        mProvidersList.add(SunViewProvider.class);
        mProvidersList.add(AdviceViewProvider.class);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO 自动生成的方法存根
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO 自动生成的方法存根
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_draggablelisttwo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item, int flag) {
        switch (item.getItemId()) {
            case R.id.action_draglisttwo_modify:
                mListDataSet.modify();
                mListAdapter.notifyDataSetChanged();
                break;
            case R.id.action_draglisttwo_restore:
                mListDataSet.restore();
                mListAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    private class CommonCallbackListener implements OnGeneralListener, OnViewSwapListener, OnDragStateChangeListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_advice_dress:
                    Toast.makeText(DraggableListTwoActivity.this, "穿衣", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_advice_car:
                    Toast.makeText(DraggableListTwoActivity.this, "洗车", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_advice_excercise:
                    Toast.makeText(DraggableListTwoActivity.this, "锻炼", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_advice_flu:
                    Toast.makeText(DraggableListTwoActivity.this, "感冒", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        }

        @Override
        public void onSwap(View firstView, int firstPosition, View secondView, int secondPosition) {
            IItemBean mTemp = null;
            if (firstPosition > secondPosition) {
                mTemp = mOptionDataSet.get(secondPosition);
                mOptionDataSet.remove(secondPosition);
                mOptionDataSet.add(firstPosition, mTemp);
            } else {
                mTemp = mOptionDataSet.get(firstPosition);
                mOptionDataSet.remove(firstPosition);
                mOptionDataSet.add(secondPosition, mTemp);
            }

        }

        @Override
        public void onStartDrag() {
        }

        @Override
        public void onStopDrag() {
            mListAdapter.notifyDataSetChanged();
        }

    }


}
