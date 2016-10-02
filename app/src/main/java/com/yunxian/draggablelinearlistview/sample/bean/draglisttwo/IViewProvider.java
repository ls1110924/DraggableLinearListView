package com.yunxian.draggablelinearlistview.sample.bean.draglisttwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunxian.draggablelinearlistview.lib.extend.DraggableConvertView;
import com.yunxian.draggablelinearlistview.lib.extend.LinearTag;
import com.yunxian.draggablelinearlistview.sample.bean.OnGeneralListener;

/**
 * 为item实例化布局对象的封装接口类
 *
 * @author A Shuai
 */
public interface IViewProvider {

    public abstract DraggableConvertView getItemView(DraggableConvertView convertView, ViewGroup parent, LayoutInflater mInflater, Object mData, OnGeneralListener mGeneralListener, int position);

    /**
     * 为模仿OnItemClickListener回调需要识别的ID序号的对象持有类
     * 这是向view中设置的tag对象，请务必是它继承于LinearTag
     *
     * @author A Shuai
     */
    public abstract class IViewHolder extends LinearTag {

        public abstract int getID();

        public abstract View getView();

    }

}
