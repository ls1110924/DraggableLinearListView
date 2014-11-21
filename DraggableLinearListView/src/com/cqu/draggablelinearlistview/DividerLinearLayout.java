package com.cqu.draggablelinearlistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 *	带分割线的LinearLayout
 *	支持源生属性设置，即android:showDividers="middle|end"
 *	也支持自定义属性设置，xmlns:divider="http://schemas.android.com/apk/res-auto"，divider:showDividers="middle|end"
 *	自定义属性设置新增了dividerWidth和dividerHeight属性，分别对应横向布局和纵向布局的LinearLayout的Diveder宽度设置
 *	推荐使用一种类型的属性设置，切勿两种混用，以免造成设置混乱(初始化逻辑按照先处理源生属性后处理自定义属性为准)
 * @author A Shuai
 *
 */
@SuppressLint("NewApi")
public class DividerLinearLayout extends LinearLayout{

	/**
	 *	抽取源生LinearLayout风格对应ID，当在源生系统支持分割线的条件下可直接进行系统实现
	 */
	private static final int[] R_styleable_LinearLayout = new int[] {
		/* 0 */ android.R.attr.divider,
		/* 1 */ android.R.attr.measureWithLargestChild,
		/* 2 */ android.R.attr.showDividers,
		/* 3 */ android.R.attr.dividerPadding,
	};
	private static final int LinearLayout_divider = 0;
	private static final int LinearLayout_measureWithLargestChild = 1;
	private static final int LinearLayout_showDividers = 2;
	private static final int LinearLayout_dividerPadding = 3;

	/**
	 *	不显示任何分割线
	 */
	public static final int SHOW_DIVIDER_NONE = 0x0;
	/**
	 *	在ViewGroup组前显示分割线
	 */
	public static final int SHOW_DIVIDER_BEGINNING = 0x1;
	/**
	 *	在ViewGroup组中的两个子View之间显示分割线
	 */
	public static final int SHOW_DIVIDER_MIDDLE = 0x2;
	/**
	 *	在ViewGroup组末尾显示分割线
	 */
	public static final int SHOW_DIVIDER_END = 0x4;

	/**
	 *	判断当前运行的SDK版本，因为Android3.0以上源生支持带分割线的
	 */
	private static final boolean IS_HONEYCOMB = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

	private Drawable mDivider;
	protected int mDividerWidth;
	protected int mDividerHeight;
	protected int mShowDividers;
	protected int mDividerPadding;
	/**
	 *	判断当前指定的Drawable是否为ColorDrawable，
	 *	如果为ColorDrawable，是取不到该Drawable指定绘制的宽度和高度
	 */
	private boolean mClipDivider;

	private boolean mUseLargestChild;
	
	/**
	 *	当前控件的宽高
	 */
	protected int mWidth;
	protected int mHeight;

	public DividerLinearLayout(Context context) {
		super(context);
	}

	public DividerLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		originateInit(context, attrs);
		deriveInit(context, attrs);
	}

	public DividerLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		originateInit(context, attrs);
		deriveInit(context, attrs);
	}

	/**
	 *	根据源生属性进行初始化
	 * @param mContext
	 * @param attrs
	 */
	private void originateInit( Context mContext, AttributeSet attrs ){
		if( attrs == null ){
			return;
		}
		TypedArray a = mContext.obtainStyledAttributes(attrs, /*com.android.internal.R.styleable.*/R_styleable_LinearLayout);

		setDividerDrawable(a.getDrawable(/*com.android.internal.R.styleable.*/LinearLayout_divider));
		mShowDividers = a.getInt(/*com.android.internal.R.styleable.*/LinearLayout_showDividers, SHOW_DIVIDER_NONE);
		mDividerPadding = a.getDimensionPixelSize(/*com.android.internal.R.styleable.*/LinearLayout_dividerPadding, 0);
		mUseLargestChild = a.getBoolean(/*com.android.internal.R.styleable.*/LinearLayout_measureWithLargestChild, false);

		a.recycle();
	}

	/**
	 *	处理自定义属性设置
	 *	处理逻辑为如果自定义属性未设置则采用源生的属性设置
	 * @param mContext
	 * @param attrs
	 */
	private void deriveInit( Context mContext, AttributeSet attrs ){
		if( attrs == null ){
			return;
		}
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.LinearLayout);
		if( mDivider == null ){
			setDividerDrawable( a.getDrawable(R.styleable.LinearLayout_linearDivider) );
		}
		mShowDividers = a.getInt(R.styleable.LinearLayout_linearShowDividers, mShowDividers);
		mDividerPadding = a.getDimensionPixelSize(R.styleable.LinearLayout_linearDividerPadding, mDividerPadding);
		mUseLargestChild =  a.getBoolean(R.styleable.LinearLayout_linearMeasureWithLargestChild, mUseLargestChild);
		mDividerWidth = a.getDimensionPixelSize(R.styleable.LinearLayout_linearDividerWidth, mDividerWidth);
		mDividerHeight = a.getDimensionPixelSize(R.styleable.LinearLayout_linearDividerHeight, mDividerHeight);

		a.recycle();

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = w;
		mHeight = h;
	}

	/**
	 *	设置Divider在items之间如何显示
	 * @param showDividers 这之中的一个或多个的组合 {@link #SHOW_DIVIDER_BEGINNING},
	 *						{@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
	 *						or {@link #SHOW_DIVIDER_NONE} 不显示Divider
	 */
	public void setShowDividers(int showDividers) {
		if (showDividers != mShowDividers) {
			requestLayout();
			invalidate();
		}
		mShowDividers = showDividers;
	}

	/**
	 * @return 一个标志位集指明了应当如何显示items旁边的Dividers
	 * @see #setShowDividers(int)
	 */
	public int getShowDividers() {
		return mShowDividers;
	}

	/**
	 *	设置一个Drawable，用于绘制items之间的分割线
	 * @param divider
	 */
	public void setDividerDrawable(Drawable divider) {
		if (divider == mDivider) {
			return;
		}
		mDivider = divider;
		mClipDivider = divider instanceof ColorDrawable;
		if (divider != null && !mClipDivider) {
			mDividerWidth = divider.getIntrinsicWidth();
			mDividerHeight = divider.getIntrinsicHeight();
		} else {
			mDividerWidth = 0;
			mDividerHeight = 0;
		}
		/**
		 * 判断分割物divider是否为空，即是否需要绘制，即便当用户设置了需要绘制，
		 *	但是没有指定分割物divider的情况下也是无法绘制的，这时可设置此标志，
		 *	即通知上层容器，此层不需绘制，可进一步优化性能
		 */
		setWillNotDraw(divider == null);
		requestLayout();
	}

	/**
	 *	设置每个Divider末端显示的Padding值
	 *	这里的Padding值一般是指对应Divider两旁的间距值
	 *	如横向局部的LinearLayout的Divider的Padding值为Divider的上下间距值
	 *	纵向的LinearLayout的Divider的Padding值为Divider的左右间距值
	 *
	 * @param padding 每个Divider末端padding的像素值
	 *
	 * @see #setShowDividers(int)
	 * @see #setDividerDrawable(android.graphics.drawable.Drawable)
	 * @see #getDividerPadding()
	 */
	public void setDividerPadding(int padding) {
		if( padding == mDividerPadding ){
			return;
		}
		mDividerPadding = padding;
	}

	/**
	 *	获取用于插入到divider的Padding的像素值
	 *
	 * @see #setShowDividers(int)
	 * @see #setDividerDrawable(android.graphics.drawable.Drawable)
	 * @see #setDividerPadding(int)
	 */
	public int getDividerPadding() {
		return mDividerPadding;
	}

	/**
	 *	设置所用的Divider的宽度值
	 * @param mWidth
	 * 
	 * @see #getDividerWidth()
	 */
	public void setDividerWidth( int mWidth ){
		if( getOrientation() == VERTICAL  ){
			if( mWidth == mDividerHeight ){
				return;
			}
			mDividerHeight = mWidth;
		} else {
			if( mWidth == mDividerWidth ){
				return;
			}
			mDividerWidth = mWidth;
		}
		requestLayout();
	}

	/**
	 *	得到所设置的Divider的宽度值
	 *	当为横向布局时，返回值为Divider的宽度
	 *	当为纵向布局时，返回值为Divider的高度
	 *
	 * @hide Used internally by framework.
	 */
	public int getDividerWidth() {
		return getOrientation() == VERTICAL ? mDividerHeight : mDividerWidth;
	}

	/**
	 *	当设置为true时，所有带有权重值的孩子视图View使用所有孩子视图中最大的视图的尺寸作为其最小尺寸
	 *	如果为false，所有的孩子视图均正常测量和使用正常的大小
	 *
	 * @return True
	 *
	 * @attr ref android.R.styleable#LinearLayout_measureWithLargestChild
	 */
	public boolean isMeasureWithLargestChildEnabled() {
		return mUseLargestChild;
	}

	/**
	 *
	 * @param enabled 
	 *
	 * @attr ref android.R.styleable#LinearLayout_measureWithLargestChild
	 * @see #isMeasureWithLargestChildEnabled()
	 */
	public void setMeasureWithLargestChildEnabled(boolean enabled) {
		mUseLargestChild = enabled;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if( mUseLargestChild ){
			switch( getOrientation() ){
			case VERTICAL:
				useLargestChildVertical();
				break;
			case HORIZONTAL:
				useLargestChildHorizontal();
				break;
			}
		}
	}

	/**
	 *	当使用了measureWithLargestChild属性为true或mUseLargestChild设置为true时
	 *	且当前布局为纵向布局时，重新测量和设置子视图的大小
	 *	最后重新设置自己的大小
	 */
	private void useLargestChildVertical(){
		final int mCount = getChildCount();

		int mLargestChildHeight = 0;
		View mChildView = null;
		for( int i = 0; i < mCount; i++ ){
			mChildView = getChildAt(i);
			mLargestChildHeight = Math.max(mChildView.getMeasuredHeight(), mLargestChildHeight);
		}

		int mTotalHeight = 0;
		LayoutParams lp = null;
		for( int i = 0; i < mCount; i++ ){
			mChildView = getChildAt(i);
			if( mChildView == null || mChildView.getVisibility() == GONE ){
				continue;
			}

			lp = (LayoutParams) mChildView.getLayoutParams();
			if( lp.weight > 0 ){
				mChildView.measure(MeasureSpec.makeMeasureSpec(mChildView.getMeasuredWidth(), MeasureSpec.EXACTLY), 
						MeasureSpec.makeMeasureSpec(mLargestChildHeight, MeasureSpec.EXACTLY));
				mTotalHeight += mLargestChildHeight;
			} else {
				mTotalHeight += mChildView.getMeasuredHeight();
			}

			mTotalHeight += lp.topMargin + lp.bottomMargin;	
		}
		mTotalHeight += getPaddingTop() + getPaddingBottom();

		setMeasuredDimension(getMeasuredWidth(), mTotalHeight);
	}

	/**
	 *	当使用了measureWithLargestChild属性为true或mUseLargestChild设置为true时
	 *	且当前布局为横向布局时，重新测量和设置子视图的大小
	 *	最后重新设置自己的大小
	 */
	private void useLargestChildHorizontal(){
		final int mCount = getChildCount();

		int mLargestChildWidth = 0;
		View mChildView = null;
		for( int i = 0; i < mCount; i++ ){
			mChildView = getChildAt(i);
			mLargestChildWidth = Math.max(mChildView.getMeasuredWidth(), mLargestChildWidth);
		}

		int mTotalWidth = 0;
		LayoutParams lp = null;
		for( int i = 0; i < mCount; i++ ){
			mChildView = getChildAt(i);
			if( mChildView == null || mChildView.getVisibility() == GONE ){
				continue;
			}

			lp = (LayoutParams) mChildView.getLayoutParams();
			if( lp.weight > 0 ){
				mChildView.measure(MeasureSpec.makeMeasureSpec(mLargestChildWidth, MeasureSpec.EXACTLY), 
						MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
				mTotalWidth += mLargestChildWidth;
			} else {
				mTotalWidth += mChildView.getMeasuredWidth();
			}

			mTotalWidth += lp.leftMargin + lp.rightMargin;	
		}
		mTotalWidth += getPaddingLeft() + getPaddingRight();

		setMeasuredDimension(mTotalWidth, getMeasuredHeight());
	}

	@Override
	protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
		final int mIndex = indexOfChild(child);
		final int mOrientation = getOrientation();
		final LayoutParams lp = (LayoutParams) child.getLayoutParams();
		if( hasDividerBeforeChildAt(mIndex) ){
			switch( mOrientation ){
				case VERTICAL:
					lp.topMargin = mDividerHeight;
					break;
				case HORIZONTAL:
					lp.leftMargin = mDividerWidth;
					break;
			}
		}

		if( mIndex == (getChildCount() - 1) ){
			if( hasDividerBeforeChildAt( mIndex + 1 ) ){
				switch( mOrientation ){
					case VERTICAL:
						lp.bottomMargin = mDividerHeight;
						break;
					case HORIZONTAL:
						lp.rightMargin = mDividerWidth;
						break;
				}
			}
		}

		super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
	}

	/**
	 *	确定指定的序号子视图需要填充的Padding值以实现给Divider留下间隔
	 *	相当于计算指定需要的View前面需不需要Divider，如存在20个子视图，
	 *	共有0-19个序号，其中20序号相当于计算19号后面需不需要预留Divider
	 *
	 * @param childIndex 确定视图前面的Divider的子视图序号
	 * @return true 表示指定序号的子视图前面需要一个Divider
	 * @hide 
	 */
	protected boolean hasDividerBeforeChildAt(int childIndex) {
		if (childIndex == 0) {
			return (mShowDividers & SHOW_DIVIDER_BEGINNING) != 0;
		} else if (childIndex == getChildCount()) {
			return (mShowDividers & SHOW_DIVIDER_END) != 0;
		} else if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
			boolean hasVisibleViewBefore = false;
			for (int i = childIndex - 1; i >= 0; i--) {
				if (getChildAt(i).getVisibility() != GONE) {
					hasVisibleViewBefore = true;
					break;
				}
			}
			return hasVisibleViewBefore;
		}
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if( mDivider != null ){
			switch( getOrientation() ){
			case VERTICAL:
				drawDividersVertical(canvas);
				break;
			case HORIZONTAL:
				drawDividersHorizontal(canvas);
				break;
			}
		}
		super.onDraw(canvas);
	}

	/**
	 *	绘制垂直方向上的分割线
	 * @param canvas
	 */
	protected void drawDividersVertical( Canvas canvas ){
		final int mCount = getChildCount();
		View mChild = null;
		LayoutParams lp = null;
		
		for( int i = 0; i < mCount; i++ ){
			mChild = getChildAt(i);
			if( mChild != null && mChild.getVisibility() != GONE ){
				if( hasDividerBeforeChildAt(i) ){
					lp = (LayoutParams) mChild.getLayoutParams();
					drawHorizontalDividerForVertical(canvas, mChild.getTop() - lp.topMargin);
				}
			}
		}
		
		if( hasDividerBeforeChildAt(mCount) ){
			mChild = getChildAt(mCount - 1);
			if( mChild == null ){
				drawHorizontalDividerForVertical(canvas, getHeight() - getPaddingBottom() - mDividerHeight);
			} else {
				drawHorizontalDividerForVertical(canvas, mChild.getBottom());
			}
		}
	}

	/**
	 *	绘制水平方向上的分割线
	 * @param canvas
	 */
	protected void drawDividersHorizontal( Canvas canvas ){
		final int mCount = getChildCount();
		View mChild = null;
		LayoutParams lp = null;
		
		for( int i = 0; i < mCount; i++ ){
			mChild = getChildAt(i);
			if( mChild != null && mChild.getVisibility() != GONE ){
				if( hasDividerBeforeChildAt(i) ){
					lp = (LayoutParams) mChild.getLayoutParams();
					drawVerticalDividerForHorizontal(canvas, mChild.getLeft() - lp.leftMargin);
				}
			}
		}
		
		if( hasDividerBeforeChildAt(mCount) ){
			mChild = getChildAt(mCount - 1);
			if( mChild == null ){
				drawVerticalDividerForHorizontal(canvas, getWidth() - getPaddingRight() - mDividerWidth);
			} else {
				drawVerticalDividerForHorizontal(canvas, mChild.getRight());
			}
		}
	}

	/**
	 *	绘制一条具体的水平方向上的分割线，用于LinearLayout为垂直方向布局时使用
	 * @param canvas
	 * @param top	Y轴的起始绘制坐标
	 */
	void drawHorizontalDividerForVertical(Canvas canvas, int top) {
		if(mClipDivider && !IS_HONEYCOMB) {
			canvas.save();
			canvas.clipRect(getPaddingLeft() + mDividerPadding, top, mWidth - getPaddingRight() - mDividerPadding, top + mDividerHeight);
			mDivider.draw(canvas);
			canvas.restore();
		} else {
			mDivider.setBounds(getPaddingLeft() + mDividerPadding, top, mWidth - getPaddingRight() - mDividerPadding, top + mDividerHeight);
			mDivider.draw(canvas);
		}
	}

	/**
	 *	绘制一条具体的垂直方向上的分割线，用于LinearLayout为水平方向布局时使用
	 * @param canvas
	 * @param left		X轴的起始绘制坐标
	 */
	void drawVerticalDividerForHorizontal(Canvas canvas, int left) {
		if(mClipDivider && !IS_HONEYCOMB) {
			canvas.save();
			canvas.clipRect(left, getPaddingTop() + mDividerPadding, left + mDividerWidth, mHeight - getPaddingBottom() - mDividerPadding);
			mDivider.draw(canvas);
			canvas.restore();
		} else {
			mDivider.setBounds(left, getPaddingTop() + mDividerPadding, left + mDividerWidth, mHeight - getPaddingBottom() - mDividerPadding);
			mDivider.draw(canvas);
		}
	}


}
