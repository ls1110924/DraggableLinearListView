package com.cqu.draggablelinearlistview.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 *	工具类
 * @author A Shuai
 *
 */
public class Utils {

	/**
	 *	根据一个指定的view构造并返回一个此view画出的位图对象
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

}
