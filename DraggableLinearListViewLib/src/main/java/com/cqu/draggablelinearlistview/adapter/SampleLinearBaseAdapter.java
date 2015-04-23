package com.cqu.draggablelinearlistview.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;;

/**
 *	LinearBaseAdapter的简易封装
 * @author A Shuai
 *
 */
public abstract class SampleLinearBaseAdapter extends LinearBaseAdapter{

	protected Context mContext;
	protected LayoutInflater mInflater = null;
	
	protected Resources mResources = null;

	public SampleLinearBaseAdapter( Context  mContext ){
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		
		mResources = this.mContext.getResources();
	}

}
