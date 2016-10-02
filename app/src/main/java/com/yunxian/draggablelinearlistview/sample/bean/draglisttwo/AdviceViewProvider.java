package com.yunxian.draggablelinearlistview.sample.bean.draglisttwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunxian.draggablelinearlistview.lib.extend.DraggableConvertView;
import com.yunxian.draggablelinearlistview.sample.R;
import com.yunxian.draggablelinearlistview.sample.bean.OnGeneralListener;
import com.yunxian.draggablelinearlistview.sample.dataset.DraggableListDataSet;
import com.yunxian.draggablelinearlistview.sample.dataset.DraggableListDataSet.Advice;

public class AdviceViewProvider implements IViewProvider {

    @Override
    public DraggableConvertView getItemView(DraggableConvertView convertView, ViewGroup parent,
                                            LayoutInflater mInflater, Object mData, OnGeneralListener mGeneralListener, int position) {
        ViewHolder mViewHolder = null;
        Advice mDataSet = ((DraggableListDataSet) mData).getAdvice();

        if (convertView.mConvertView == null) {
            convertView.mConvertView = mInflater.inflate(R.layout.item_advice, parent, false);
            mViewHolder = new ViewHolder();

            mViewHolder.mView = convertView.mConvertView;
            mViewHolder.mDress = (TextView) convertView.mConvertView.findViewById(R.id.item_advice_dress);
            mViewHolder.mCar = (TextView) convertView.mConvertView.findViewById(R.id.item_advice_car);
            mViewHolder.mExcercise = (TextView) convertView.mConvertView.findViewById(R.id.item_advice_excercise);
            mViewHolder.mFlu = (TextView) convertView.mConvertView.findViewById(R.id.item_advice_flu);

            mViewHolder.mDress.setOnClickListener(mGeneralListener);
            mViewHolder.mCar.setOnClickListener(mGeneralListener);
            mViewHolder.mExcercise.setOnClickListener(mGeneralListener);
            mViewHolder.mFlu.setOnClickListener(mGeneralListener);

            convertView.mConvertView.setTag(mViewHolder);
            convertView.mAnchorView = convertView.mConvertView;

        } else {
            mViewHolder = (ViewHolder) convertView.mConvertView.getTag();
            convertView.mAnchorView = convertView.mConvertView;
        }

        mViewHolder.mPosition = position;
        mViewHolder.mDress.setText(mDataSet.mDress);
        mViewHolder.mCar.setText(mDataSet.mCar);
        mViewHolder.mExcercise.setText(mDataSet.mExcercise);
        mViewHolder.mFlu.setText(mDataSet.mFlu);

        return convertView;
    }

    public class ViewHolder extends IViewHolder {

        int position;
        View mView;

        TextView mDress;
        TextView mCar;
        TextView mExcercise;
        TextView mFlu;

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
