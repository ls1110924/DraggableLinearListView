package com.cqu.draggablelinearlistview.sample;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public abstract class BaseActionBarActivity extends ActionBarActivity{

	protected ActionBar mActionBar = null;
	
	protected Cursor mCursor = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 *	隐藏物理菜单键
		 *	以便多余的菜单项能全部显示在ActionBar上
		 */
		try {
			ViewConfiguration mconfig = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(mconfig, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mActionBar = getSupportActionBar();
		
	}
	
	/**
	 *	设置ActionBarHome键的可用性
	 * @param enable
	 */
	protected void setActionBarHomeEnable( boolean enable ){
		mActionBar.setDisplayHomeAsUpEnabled(enable);
	}
	
	/**
	 *	ActionBar上的Home键被按下的回调
	 * @return	true 表示事件被消费
	 */
	protected abstract boolean onHomeKeyDown();
	
	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ){
			case android.R.id.home:
				onHomeKeyDown();
				return true;
		}
		return onOptionsItemSelected(item, 0);
	}
	
	/**
	 *	覆盖并设置原方法为终态，可避免被子类重复处理相同的事件
	 *	子类若需监听别的菜单点击事件，覆写本方法即可
	 * @param item
	 * @param flag
	 * @return
	 */
	public boolean onOptionsItemSelected(MenuItem item, int flag){
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 *	Back键按下回调事件
	 * @return	true 表示事件被消费
	 */
	protected abstract boolean onBackKeyDown();
	
	/**
	 *	Menu键按下的回调事件，各子类Activity可根据需要自行覆写该方法即可
	 * @return
	 */
	protected boolean onMenuKeyDown(){
		return false;
	}
	
	@Override
	public final boolean onKeyDown(int keyCode, KeyEvent event) {
		switch( keyCode ){
			case KeyEvent.KEYCODE_BACK:
				if( onBackKeyDown() ){
					return true;
				}
				break;
			case KeyEvent.KEYCODE_MENU:
				if( onMenuKeyDown() ){
					return true;
				}
				break;
			default:
				break;
		}
		return onKeyDown(keyCode, event, 0);
	}
	
	/**
	 *	类似于{@see #onOptionsItemSelected(MenuItem, int)}方法
	 * @param keyCode
	 * @param event
	 * @param flag
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event, int flag){
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 *	简单的打开一个新的Activity，并传入必要的参数
	 * @param target
	 * @param enterAnim
	 * @param exitAnim
	 * @param isFinish
	 * @param mBundle	可为空
	 */
	protected void startNewActivity( Class<? extends Activity> target, int enterAnim, int exitAnim, boolean isFinish, Bundle mBundle ){
		Intent mIntent = new Intent(this, target);
		if( mBundle != null ){
			mIntent.putExtras(mBundle);
		}
		startActivity(mIntent);
		overridePendingTransition(enterAnim, exitAnim);
		if( isFinish ){
			finish();
		}
	}
	
}
