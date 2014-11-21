package com.cqu.draggablelinearlistview;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.cqu.draggablelinearlistview.adapter.DraggableBaseAdapter;
import com.cqu.draggablelinearlistview.adapter.LinearBaseAdapter;
import com.cqu.draggablelinearlistview.extend.DragTriggerType;
import com.cqu.draggablelinearlistview.extend.DraggableConvertView;
import com.cqu.draggablelinearlistview.extend.LinearTag;
import com.cqu.draggablelinearlistview.extend.ViewWithIndex;
import com.cqu.draggablelinearlistview.listener.OnDragStateChangeListener;
import com.cqu.draggablelinearlistview.listener.OnViewSwapListener;
import com.cqu.draggablelinearlistview.util.DensityUtil;
import com.cqu.draggablelinearlistview.util.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 *	以LinearLayout为基础定制的ListView，且其子view可执行拖拽
 * @author A Shuai
 *
 */
public class DraggableLinearListView extends LinearListView{
	private static final String LOG_TAG = "DraggableLinearListView";

	/**
	 *	执行属性解析所使用的ID值
	 */
	private static final int[] R_styleable_LinearLayout = new int[] {
		/* 0 */ android.R.attr.orientation,
	};

	private static final int LinearLayout_orientation = 0;

	/**
	 *	所执行的动画时长与距离的基准，为20DP对应150毫秒
	 */
	private static final float STANDARD_DISTANCE = 20;
	private static final long STANDARD_SWITCH_DURATION = 150;

	/**
	 *	控制动画执行时长的上限与下限
	 */
	private static final long MIN_SWITCH_DURATION = STANDARD_SWITCH_DURATION;
	private static final long MAX_SWITCH_DURATION = STANDARD_SWITCH_DURATION * 2;

	/**
	 *	执行的交换动画对应的基准距离20DP对应的像素值
	 */
	private float nominalDistanceScaled = 0;

	/**
	 *	无效的触摸事件PointerID
	 */
	private static final int INVALID_POINTER_ID = -1;

	/**
	 *	每个子view对应的一个执行动画的句柄，用于执行与被拖拽视图view的交换动画
	 */
	private SparseArray<SwitchChildAnimator> mSwitchChildrenAnimator;
	/**
	 *	当前正在被拖拽的子视图view的信息封装类，只需要一个此类的成员变量，切换被拖拽view后，修改必要的成员变量即可
	 */
	private DragChildView mDragChildView;
	
	private ViewConfiguration mVC = null;
	private int slop;

	//初始按下的Y坐标
	private int mDownY = -1;
	//触摸事件中与拖拽view所对应的事件ID
	private int mActivePointerId = INVALID_POINTER_ID;

	/**
	 *	分别用来绘制在拖拽影像上的边缘阴影Drawable，用于增强交互体验感
	 */
	private Drawable mImageTopShadowDrawable;
	private Drawable mImageBottomShadowDrawable;
	/**
	 *	是否需要前景蒙版，及前景蒙版
	 */
	private boolean mNeedForegroundMask;
	private Drawable mImageForegroundMaskDrawable;
	/* 绘制的边缘阴影高度 */
	private int mImageShadowHeight;

	/* 外层嵌套的滚动容器，用于当拖拽事件处于屏幕边缘时，触发自动滚动事件 */
	private ScrollView mScrollViewContainer;
	/* 触发自动滚动事件的敏感高度 */
	private int scrollSensitiveAreaHeight;
	private static final int DEFAULT_SCROLL_SENSITIVE_AREA_HEIGHT_DP = 100;
	private static final int MAX_DRAG_SCROLL_SPEED = 16;
	
	/**
	 *	触发拖拽事件的类型
	 */
	private DragTriggerType mDragTriggerType = null;
	
	protected DraggableBaseAdapter mAdapter = null;
	
	private Rect mBeingDragViewRect = null;
	private boolean isBeingLongPress = false;
	private LongPressRunnable mLongPressRunnable = null;
	private UpdateHandler mHandler = null;

	private OnDragStateChangeListener mOnDragStateChange = null;
	private OnViewSwapListener mSwapListener = null;

	public DraggableLinearListView(Context context) {
		super(context);
		setOrientation(VERTICAL);
		init( context );
	}

	public DraggableLinearListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init( context );
		initAttr(context, attrs);
	}

	public DraggableLinearListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init( context );
		initAttr(context, attrs);
	}

	private final void init( Context context ){
		nominalDistanceScaled = DensityUtil.dip2px(context, STANDARD_DISTANCE);

		mSwitchChildrenAnimator = new SparseArray<SwitchChildAnimator>();
		mDragChildView = new DragChildView();

		mVC = ViewConfiguration.get(context);
		slop = mVC.getScaledTouchSlop();

		Resources resources = getResources();
		mImageTopShadowDrawable = resources.getDrawable(R.drawable.ab_solid_shadow_holo_flipped);
		mImageBottomShadowDrawable = resources.getDrawable(R.drawable.ab_solid_shadow_holo);
		mImageShadowHeight = resources.getDimensionPixelSize(R.dimen.downwards_drop_shadow_height);

		scrollSensitiveAreaHeight = DensityUtil.dip2px(context, DEFAULT_SCROLL_SENSITIVE_AREA_HEIGHT_DP);
		
		mDragTriggerType = DragTriggerType.LONG_PRESS;
		
		mBeingDragViewRect = new Rect();
		mLongPressRunnable = new LongPressRunnable();
		mHandler = new UpdateHandler(this);
		
		mNeedForegroundMask = false;
		
	}

	/**
	 *	初始化参数
	 * @param context
	 * @param attrs
	 */
	private final void initAttr( Context context, AttributeSet attrs ){
		TypedArray a = context.obtainStyledAttributes(attrs, /*com.android.internal.R.styleable.*/R_styleable_LinearLayout);
		int index = a.getInt(LinearLayout_orientation, 1);
		if( index >= 0 ){
			setOrientation(index);
		}
		a.recycle();
		
		TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.DraggableListView);
		index = b.getInt(R.styleable.DraggableListView_draggableTriggerType, 0);
		setNeedForegroundMask(b.getBoolean(R.styleable.DraggableListView_needForeground, false));
		setDragTriggerType(index);
		b.recycle();
	}

	
	@Override
	public final LinearBaseAdapter getLinearAdapter() {
		throw new IllegalArgumentException("ilegal invode");
	}

	@Override
	public final void setLinearAdapter(LinearBaseAdapter adapter) {
		throw new IllegalArgumentException("ilegal invode");
	}

	public DraggableBaseAdapter getDragableAdapter(){
		return mAdapter;
	}

	public void setDraggableAdapter(DraggableBaseAdapter adapter) {
		if (mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataObserver);
		}

		mAdapter = adapter;

		if (mAdapter != null) {
			mAdapter.registerDataSetObserver(mDataObserver);
			mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
		}

		buildAllChildren();
	}

	/**
	 *	覆写此方法，检查布局方向参数设置
	 *	@param orientation
	 */
	@Override
	public void setOrientation(int orientation) {
		if( orientation == getOrientation() ){
			return;
		}
		if(LinearLayout.HORIZONTAL == orientation){
			throw new IllegalArgumentException("DraggableLinearListView must be VERTICAL.");
		}
		super.setOrientation(orientation);
	}

	/**
	 *	覆写分发子视图绘制方法，绘制拖拽影像
	 *	@param canvas
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if(mDragChildView.valid && (mDragChildView.dragging || mDragChildView.settling())){
			canvas.save();
			canvas.translate(0, mDragChildView.totalDragOffset);
			mDragChildView.viewDrawable.draw(canvas);
			if( mNeedForegroundMask ){
				mImageForegroundMaskDrawable.draw(canvas);
			}

			final int left = mDragChildView.viewDrawable.getBounds().left;
			final int right = mDragChildView.viewDrawable.getBounds().right;
			final int top = mDragChildView.viewDrawable.getBounds().top;
			final int bottom = mDragChildView.viewDrawable.getBounds().bottom;

			/**
			 *	分别绘制边缘阴影
			 */
			mImageBottomShadowDrawable.setBounds(left, bottom, right, bottom + mImageShadowHeight);
			mImageBottomShadowDrawable.draw(canvas);

			if(null != mImageTopShadowDrawable){
				mImageTopShadowDrawable.setBounds(left, top - mImageShadowHeight, right, top);
				mImageTopShadowDrawable.draw(canvas);
			}

			canvas.restore();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch(MotionEventCompat.getActionMasked(event)){
			case MotionEvent.ACTION_DOWN: {
				//存在一个潜在的拖拽对象或正在执行还原动画
				if(mDragChildView.valid){
					return false;
				}
				mDownY = (int) MotionEventCompat.getY(event, 0);
				mActivePointerId = MotionEventCompat.getPointerId(event, 0);
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				if(!mDragChildView.valid){
					return false;
				}

				if(INVALID_POINTER_ID == mActivePointerId){
					break;
				}
				final int pointerIndex = event.findPointerIndex(mActivePointerId);
				final float y = MotionEventCompat.getY(event, pointerIndex);
				final float dy = y - mDownY;
				if(Math.abs(dy) > slop){
					startDrag();
					return true;
				}
				return false;
			}
			case MotionEvent.ACTION_POINTER_UP: {
				final int pointerIndex = MotionEventCompat.getActionIndex(event);
				final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

				if(pointerId != mActivePointerId){
					break;
				}
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				onTouchEnded();

				if(mDragChildView.valid){
					mDragChildView.setInvalid();
				}
				break;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(MotionEventCompat.getActionMasked(event)){
			case MotionEvent.ACTION_DOWN: {
				
				if( mDragTriggerType == DragTriggerType.SHORT_PRESS ){
					if(!mDragChildView.valid || mDragChildView.settling()){
						return false;
					}
					startDrag();
				} else {
					
				}
				
				return true;
			}
			case MotionEvent.ACTION_MOVE: {
				int pointerIndex = event.findPointerIndex(mActivePointerId);
				int lastEventY = (int) MotionEventCompat.getY(event, pointerIndex);
				int lastEventX = (int) MotionEventCompat.getX(event, pointerIndex);
				
				if( isBeingLongPress ){
					if( !mBeingDragViewRect.contains(lastEventX, lastEventY) ){
						isBeingLongPress = false;
						mHandler.removeCallbacks( mLongPressRunnable );
					}
				}
				
				if(!mDragChildView.dragging){
					break;
				}
				if(INVALID_POINTER_ID == mActivePointerId){
					break;
				}

				int deltaY = lastEventY - mDownY;

				onDrag(deltaY);
				return true;
			}
			case MotionEvent.ACTION_POINTER_UP: {
				final int pointerIndex = MotionEventCompat.getActionIndex(event);
				final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

				mHandler.removeCallbacks( mLongPressRunnable );
				
				if(pointerId != mActivePointerId){
					break;
				}
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				onTouchEnded();

				mHandler.removeCallbacks( mLongPressRunnable );
				
				if(mDragChildView.dragging){
					stopDrag();
				}
				return true;
			}
		}
		return false;
	}

	/**
	 *	向子视图队列末尾添加一个新的子视图及其锚视图
	 *	并调用 {@see #setViewDraggable(View, View)} 完成子视图与锚视图的绑定
	 * @param child
	 * @param dragHandle
	 */
	public void addDragView(View child, View mAnchorView){
		addView(child);
		bindViewWithAnchorView(child, mAnchorView);
	}

	/**
	 *	向指定位置添加一个子视图及其锚视图
	 *	并调用 {@see #setViewDraggable(View, View)} 完成子视图与锚视图的绑定
	 * @param child
	 * @param dragHandle
	 * @param index
	 */
	public void addDragView(View child, View mAnchorView, int index){
		addView(child, index);

		final int numMappings = mSwitchChildrenAnimator.size();
		for(int i = numMappings - 1; i >= 0; i--){
			final int key = mSwitchChildrenAnimator.keyAt(i);
			if(key >= index){
				mSwitchChildrenAnimator.put(key + 1, mSwitchChildrenAnimator.get(key));
			}
		}

		bindViewWithAnchorView(child, mAnchorView);
	}

	/**
	 *	为指定子视图绑定一个相应拖拽事件的锚视图
	 *	此锚视图可以是子视图本身，也可以是子视图中的子视图
	 * @param child				需要绑定响应拖拽锚视图的子视图
	 * @param dragHandle	所绑定的锚视图，锚视图需是子视图本身或其直接或间接子视图
	 */
	public void bindViewWithAnchorView(View child, View dragHandle){
		if(this == child.getParent()){
			dragHandle.setOnTouchListener(new DragHandleOnTouchListener(child));
			mSwitchChildrenAnimator.put(indexOfChild(child), new SwitchChildAnimator());
		} else {
			Log.e(LOG_TAG, child + " is not a child, cannot make draggable.");
		}
	}

	/**
	 *	移除指定索引位置的子视图
	 *	并更新子视图对应的拖拽动画句柄图，因为在移除的子视图索引之后的子视图均向前移动了一位，
	 *	因此需要将对应的动画句柄也向前移动一位
	 * @param child
	 */
	public void removeDragView(View child){
		if(this == child.getParent()){
			final int index = indexOfChild(child);
			removeView(child);

			final int mappings = mSwitchChildrenAnimator.size();
			for(int i = 0; i < mappings; i++){
				final int key = mSwitchChildrenAnimator.keyAt(i);
				if(key >= index){
					SwitchChildAnimator next = mSwitchChildrenAnimator.get(key + 1);
					if(null == next){
						mSwitchChildrenAnimator.delete(key);
					} else {
						mSwitchChildrenAnimator.put(key, next);
					}
				}
			}
		}
	}
	
	/**
	 *	根据索引值设定响应拖拽事件的类型
	 * @param mDragTriggerTypeIndex
	 */
	private void setDragTriggerType( int mDragTriggerTypeIndex ){
		switch( mDragTriggerTypeIndex ){
			case 0:
				mDragTriggerType = DragTriggerType.SHORT_PRESS;
				slop = mVC.getScaledTouchSlop() / 2;
				break;
			case 1:
				mDragTriggerType = DragTriggerType.LONG_PRESS;
				slop = mVC.getScaledTouchSlop();
				break;
		}
	}
	
	/**
	 *	设置拖拽响应类型
	 * @param mDragTriggerType	{@see #com.cqu.draggablelinearlistview.extend.DragTriggerType}
	 */
	public void setDragTriggerType( DragTriggerType mDragTriggerType ){
		this.mDragTriggerType = mDragTriggerType;
		switch( mDragTriggerType ){
			case SHORT_PRESS:
				slop = mVC.getScaledTouchSlop() / 2;
				break;
			case LONG_PRESS:
				slop = mVC.getScaledTouchSlop();
				break;
		}
	}

	@Override
	protected void refreshIndexChild(int index) {
		
		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );
		if( mAdapter == null ){
			return;
		}

		View mOldChild = null;
		View mNewChild = null;
		
		ViewWithIndex mChildView = null;
		DraggableConvertView mChild = null;
		
		ArrayList<ViewWithIndex> mChilds = mChildren.get( mAdapter.getItemViewType(index) );
		for( int i = 0, size = mChilds.size(); i < size; i++ ){
			if( mChilds.get(i).mPosition == index ){
				mChildView = mChilds.get(i);
				break;
			}
		}
		
		if( mChildView == null ){
			return;
		}
		
		mOldChild = mChildView.mView;
		
		if( mOldChild == null ){
			mChild = mAdapter.getView(index, new DraggableConvertView(), this);
		} else {
			mChild = new DraggableConvertView(mOldChild);
			mChild = mAdapter.getView(index, mChild, this);
		}
		mNewChild = mChild.mConvertView;
		mChildView.mView = mNewChild;
		
		if( mChild.mConvertView.getTag() != null && mChild.mConvertView.getTag() instanceof LinearTag ){
			((LinearTag)mChild.mConvertView.getTag()).mPosition = index;
		} else {
			mChild.mConvertView.setTag( new LinearTag(index) );
		}
		
		if( mNewChild != mOldChild ){
			addViewInLayout(mNewChild, index, mNewChild.getLayoutParams(), true);
		}
		
		bindViewWithAnchorView(mChild.mConvertView, mChild.mAnchorView);
		
	}

	@Override
	protected void refreshIndexsChildren(ArrayList<Integer> mIndexs) {
		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );
		if( mAdapter == null ){
			return;
		}

		View mOldChild = null;
		View mNewChild = null;
		
		ViewWithIndex mChildView = null;
		DraggableConvertView mChild = null;
		
		for( int index : mIndexs ){
			
			ArrayList<ViewWithIndex> mChilds = mChildren.get( mAdapter.getItemViewType(index) );
			for( int i = 0, size = mChilds.size(); i < size; i++ ){
				if( mChilds.get(i).mPosition == index ){
					mChildView = mChilds.get(i);
					break;
				}
			}
			
			if( mChildView == null ){
				continue;
			}
			
			mOldChild = mChildView.mView;
			
			if( mOldChild == null ){
				mChild = mAdapter.getView(index, new DraggableConvertView(), this);
			} else {
				mChild = new DraggableConvertView(mOldChild);
				mChild = mAdapter.getView(index, mChild, this);
			}
			mNewChild = mChild.mConvertView;
			mChildView.mView = mNewChild;
			
			if( mChild.mConvertView.getTag() != null && mChild.mConvertView.getTag() instanceof LinearTag ){
				((LinearTag)mChild.mConvertView.getTag()).mPosition = index;
			} else {
				mChild.mConvertView.setTag( new LinearTag(index) );
			}
			
			if( mNewChild != mOldChild ){
				addViewInLayout(mNewChild, index, mNewChild.getLayoutParams(), true);
			}
			
			bindViewWithAnchorView(mChild.mConvertView, mChild.mAnchorView);
			
		}
		
	}

	@Override
	protected void refreshAllChildren() {
		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );
		if( mAdapter == null ){
			return;
		}

		View mOldChild = null;
		View mNewChild = null;
		
		ViewWithIndex mChildView = null;
		DraggableConvertView mChild = null;
		
		for( int j = 0, msize = mChildren.size(); j < msize; j++ ){
			int key = mChildren.keyAt(j);
			ArrayList<ViewWithIndex> mChilds = mChildren.get(key);
			
			for( int i = 0; i < mChilds.size(); i++ ){
				mChildView = mChilds.get(i);
				
				mOldChild = mChildView.mView;
				
				if( mOldChild == null ){
					mChild = mAdapter.getView(mChildView.mPosition, new DraggableConvertView(), this);
				} else {
					mChild = new DraggableConvertView(mOldChild);
					mChild = mAdapter.getView(mChildView.mPosition, mChild, this);
				}
				mNewChild = mChild.mConvertView;
				mChildView.mView = mNewChild;
				
				if( mChild.mConvertView.getTag() != null && mChild.mConvertView.getTag() instanceof LinearTag ){
					((LinearTag)mChild.mConvertView.getTag()).mPosition = mChildView.mPosition;
				} else {
					mChild.mConvertView.setTag( new LinearTag(mChildView.mPosition) );
				}
				
				if( mNewChild != mOldChild ){
					addViewInLayout(mNewChild, mChildView.mPosition, mNewChild.getLayoutParams(), true);
				}
				
				bindViewWithAnchorView(mChild.mConvertView, mChild.mAnchorView);
			}
		}
	}

	@Override
	protected void buildAllChildren() {
		removeAllViews();
		
		updateEmptyStatus(  (mAdapter == null) || mAdapter.isEmpty()  );

		if( mAdapter == null ){
			return;
		}
		
		mChildren.clear();
		
		int mItemViewType = 0;
		int mItemViewCount = mAdapter.getViewTypeCount();
		
		for( int i = 0; i < mItemViewCount; i++ ){
			mChildren.put(i, new ArrayList<ViewWithIndex>());
		}

		for (int i = 0; i < mAdapter.getCount(); i++) {
			DraggableConvertView child = mAdapter.getView(i, new DraggableConvertView(), this);
			
			mItemViewType = mAdapter.getItemViewType(i);
			if( mItemViewType >= mItemViewCount ){
				throw new IllegalArgumentException("ItemViewType is bigger than ViewTypeCount");
			} else {
				mChildren.get( mItemViewType ).add( new ViewWithIndex(i, child.mConvertView) );
			}
			
			if( child.mConvertView.getTag() != null && child.mConvertView.getTag() instanceof LinearTag ){
				((LinearTag)child.mConvertView.getTag()).mPosition = i;
			} else {
				child.mConvertView.setTag( new LinearTag(i) );
			}
//			if (mAreAllItemsSelectable || mAdapter.isEnabled(i)) {
//				child.mConvertView.setOnClickListener( mInternalListener );
//			}
			addViewInLayout(child.mConvertView, -1, child.mConvertView.getLayoutParams(), true);
			bindViewWithAnchorView(child.mConvertView, child.mAnchorView);
		}

	}

	/**
	 *	设置一个外层滚动容器
	 * @param scrollView
	 */
	public void setScrollViewContainer(ScrollView scrollView){
		this.mScrollViewContainer = scrollView;
	}

	/**
	 *	设置滚动敏感区域高度，即外层滚动容器的上或下边缘高度，
	 *	当拖拽事件发生在此区域时且注册了外层滚动容器，即可自动滚动
	 * @param height	以像素为单位的高度
	 */
	public void setScrollSensitiveHeight(int height){
		this.scrollSensitiveAreaHeight = height;
	}

	/**
	 *	得到以像素为单位的触发自动滚动的敏感滚动区域高度
	 * @return
	 */
	public int getScrollSensitiveHeight(){
		return scrollSensitiveAreaHeight;
	}
	
	/**
	 *	设置拖拽监听器
	 * @param mOnDragStateChange
	 */
	public void setOnDragStateChangeListener( OnDragStateChangeListener mOnDragStateChange ){
		this.mOnDragStateChange = mOnDragStateChange;
	}

	/**
	 *	注册触发的交换位置的回调监听器
	 *	See {@link com.cqu.draggablelinearlistview.listener.OnViewSwapListener}
	 * @param swapListener
	 */
	public void setOnViewSwapListener(OnViewSwapListener swapListener){
		this.mSwapListener = swapListener;
	}
	
	/**
	 *	设置是否需要前景蒙版
	 * @param mNeedForegroundMask
	 */
	public void setNeedForegroundMask( boolean mNeedForegroundMask ){
		if( this.mNeedForegroundMask == mNeedForegroundMask ){
			return;
		}
		this.mNeedForegroundMask = mNeedForegroundMask;
		if( mNeedForegroundMask ){
			setForegroundMask(51, Color.BLUE);
		} else {
			mImageForegroundMaskDrawable = null;
		}
	}
	
	/**
	 *	得到是否需要前景蒙版
	 * @return
	 */
	public boolean isNeedForegroundMask(){
		return mNeedForegroundMask;
	}
	
	/**
	 *	设置前景蒙版
	 * @param mAlpha
	 * @param mColor
	 */
	public void setForegroundMask( int mAlpha, int mColor ){
		mImageForegroundMaskDrawable = new ColorDrawable(mColor);
		mImageForegroundMaskDrawable.setAlpha(mAlpha);
	}

	/**
	 *	根据当前指定的偏移量与基准距离，基准时间做比值得到对应的线性动画时长
	 *	但是存在一个动画时长的下限与上限
	 * @param distance
	 * @return
	 */
	private long getTranslateAnimationDuration(int distance){
		return Math.min(MAX_SWITCH_DURATION, 
				Math.max(MIN_SWITCH_DURATION, (long)(STANDARD_SWITCH_DURATION * Math.abs(distance) / nominalDistanceScaled)));
	}

	/**
	 *	开始侦测拖拽的子视图
	 * @param child
	 */
	private void startDetectingDrag(View child){
		//当前已存在一个潜在的拖拽子视图
		if(mDragChildView.valid)
			return;

		final int position = indexOfChild(child);

		/**
		 *	完成必要的初始化
		 */
		mSwitchChildrenAnimator.get(position).endExistingAnimation();

		mDragChildView.setValidOnPossibleDrag(child, position);
	}

	/**
	 *	开始拖拽
	 *	此时就需要拦截传播给子视图的触摸事件
	 */
	private void startDrag(){
		if( mOnDragStateChange != null ){
			mOnDragStateChange.onStartDrag();
		}
		
		mDragChildView.onDragStart();
		requestDisallowInterceptTouchEvent(true);
	}

	/**
	 *	开始拖拽
	 *	根据指定的偏移量完成子视图的交换动画及其他动作
	 * @param offset
	 */
	private void onDrag(final int offset){
		//更新总共的偏移量
		mDragChildView.setTotalOffset(offset);
		invalidate();

		int currentTop = mDragChildView.startTop + mDragChildView.totalDragOffset;

		//处理外层滚动容器的自动滚动
		handleContainerScroll(currentTop);

		//分别取得当前拖拽视图的上一个兄弟视图与下一个兄弟视图
		int belowPosition = nextDraggablePosition(mDragChildView.position);
		int abovePosition = previousDraggablePosition(mDragChildView.position);

		View belowView = getChildAt(belowPosition);
		View aboveView = getChildAt(abovePosition);

		//判断当前拖拽影像位置的下边缘的Y坐标是否大于下面兄弟视图的一半高度的Y坐标，即是否向下的运动覆盖了一半以上的下面的兄弟视图
		final boolean isBelow = (belowView != null) && 
				(currentTop + mDragChildView.height > belowView.getTop() + belowView.getHeight() / 2);
		//同样判断是否覆盖了上面的兄弟视图的一半
		final boolean isAbove = (aboveView != null) &&
				(currentTop < aboveView.getTop() + aboveView.getHeight() / 2);

		if(isBelow || isAbove){
			final View switchView = isBelow ? belowView : aboveView;

			if(null == switchView){
				Log.e(LOG_TAG, "Switching with null");
				return;
			}

			//交换元素
			final int originalPosition = mDragChildView.position;
			final int switchPosition = isBelow ? belowPosition : abovePosition;
			//这里记录了未交换位置时的Y轴坐标
			final int switchViewStartTop = switchView.getTop();

//			if(null != mSwapListener){
//				mSwapListener.onSwap(mDragChildView.view, mDragChildView.position, switchView, switchPosition);
//			}
			
			if( mAdapter != null ){
				final int mOriginalType = mAdapter.getItemViewType(originalPosition);
				final int mSwitchType = mAdapter.getItemViewType(switchPosition);
				ViewWithIndex mOriginalViewIndex = null;
				ViewWithIndex mSwitchViewIndex = null;
				
				ArrayList<ViewWithIndex> mViewIndex = null;
				mViewIndex = mChildren.get(mOriginalType);
				for( int i = 0, size = mViewIndex.size(); i < size; i++ ){
					if( mViewIndex.get(i).mPosition == originalPosition ){
						mOriginalViewIndex = mViewIndex.get(i);
						break;
					}
				}
				mViewIndex = mChildren.get(mSwitchType);
				for( int i = 0, size = mViewIndex.size(); i < size; i++ ){
					if( mViewIndex.get(i).mPosition == switchPosition ){
						mSwitchViewIndex = mViewIndex.get(i);
						break;
					}
				}
				if( mOriginalViewIndex != null ){
					mOriginalViewIndex.mPosition = switchPosition;
					
					if( mOriginalViewIndex.mView.getTag() != null && mOriginalViewIndex.mView.getTag() instanceof LinearTag ){
						((LinearTag)mOriginalViewIndex.mView.getTag()).mPosition = switchPosition;
					}
				}
				if( mSwitchViewIndex != null ){
					mSwitchViewIndex.mPosition = originalPosition;
					if( mSwitchViewIndex.mView.getTag() != null && mSwitchViewIndex.mView.getTag() instanceof LinearTag ){
						((LinearTag)mSwitchViewIndex.mView.getTag()).mPosition = originalPosition;
					}
				}
			}
			
			if(null != mSwapListener){
				mSwapListener.onSwap(mDragChildView.view, mDragChildView.position, switchView, switchPosition);
			}

			if(isBelow){
				removeViewAt(switchPosition);
				removeViewAt(originalPosition);

				addView(belowView, originalPosition);
				addView(mDragChildView.view, switchPosition);
			} else {
				removeViewAt(originalPosition);
				removeViewAt(switchPosition);

				addView(mDragChildView.view, switchPosition);
				addView(aboveView, originalPosition);
			}
			//更新拖拽的影像视图对应的真正的子视图索引，以便结束拖拽后能回到正确的位置
			//（其实已经回到了正确的位置，只不过是不可见的，需要做的是让影像视图完成向真实位置的移动动画）
			mDragChildView.position = switchPosition;

			//switchView指向的是需要执行的动画兄弟视图
			final ViewTreeObserver switchViewObserver = switchView.getViewTreeObserver();
			switchViewObserver.addOnPreDrawListener(new OnPreDrawListener() {
				@Override
				public boolean onPreDraw() {
					switchViewObserver.removeOnPreDrawListener(this);

					//这里switchView已经交换了位置，已经被布局到了正确的位置，此时得到的getTop()Y轴坐标是后来的了
					final ObjectAnimator switchAnimator = ObjectAnimator.ofFloat(switchView, "y",switchViewStartTop, switchView.getTop())
							.setDuration(getTranslateAnimationDuration(switchView.getTop() - switchViewStartTop));
					switchAnimator.addListener(new AnimatorListenerAdapter(){
						@Override
						public void onAnimationStart(Animator animation) {
							mSwitchChildrenAnimator.get(originalPosition).swapAnimation = switchAnimator;
						}
						@Override
						public void onAnimationEnd(Animator animation) {
							//直接赋空即可，不需要清除
							mSwitchChildrenAnimator.get(originalPosition).swapAnimation = null;
						}
					});
					switchAnimator.start();

					return true;
				}
			});

			//本身draggedItem.view是不可见的，如果可见才会回调OnPreDrawListener监听器
			//所以当会回调此监听器的时候已经结束拖拽了，应当清空开始执行一个动画，完成影像归位动画
			final ViewTreeObserver observer = mDragChildView.view.getViewTreeObserver();
			observer.addOnPreDrawListener(new OnPreDrawListener(){
				@Override
				public boolean onPreDraw() {
					observer.removeOnPreDrawListener(this);
					mDragChildView.updateTargetTop();

					if(mDragChildView.settling()){
						Log.d(LOG_TAG, "Updating settle animation");
						mDragChildView.settleAnimation.removeAllListeners();
						mDragChildView.settleAnimation.cancel();
						stopDrag();
					}
					return true;
				}
			});
		}
	}

	/**
	 *	结束拖拽
	 *	执行拖拽影像归为动画
	 */
	private void stopDrag(){
		mDragChildView.settleAnimation = ValueAnimator.ofFloat(mDragChildView.totalDragOffset, mDragChildView.totalDragOffset - mDragChildView.targetTopOffset)
				.setDuration(getTranslateAnimationDuration(mDragChildView.targetTopOffset));
		mDragChildView.settleAnimation.addUpdateListener(new AnimatorUpdateListener(){
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//已经停止了
				if(!mDragChildView.valid)
					return;

				mDragChildView.setTotalOffset(((Float) animation.getAnimatedValue()).intValue());

				final int shadowAlpha = (int)((1 - animation.getAnimatedFraction()) * 255);
				if(null != mImageTopShadowDrawable)
					mImageTopShadowDrawable.setAlpha(shadowAlpha);

				mImageBottomShadowDrawable.setAlpha(shadowAlpha);
				invalidate();
			}
		});
		mDragChildView.settleAnimation.addListener(new AnimatorListenerAdapter(){
			@Override
			public void onAnimationStart(Animator animation) {
				mDragChildView.onDragStop();
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				if(!mDragChildView.valid){
					return; 
				}

				mDragChildView.settleAnimation = null;
				mDragChildView.setInvalid();

				if(null != mImageTopShadowDrawable)
					mImageTopShadowDrawable.setAlpha(255);
				mImageBottomShadowDrawable.setAlpha(255);
				
				if( mOnDragStateChange != null ){
					mOnDragStateChange.onStopDrag();
				}
			}
		});
		mDragChildView.settleAnimation.start();
	}

	/**
	 *	由于自动滚动而触发的更新状态所用
	 */
	private Runnable mAutoScrollUpdater;
	
	private void handleContainerScroll(final int currentTop){
		if(null != mScrollViewContainer){
			final int startScrollY = mScrollViewContainer.getScrollY();
			final int absTop = getTop() - startScrollY + currentTop;
			final int height = mScrollViewContainer.getHeight();

			final int delta;

			if(absTop < scrollSensitiveAreaHeight){
				delta = (int)(-MAX_DRAG_SCROLL_SPEED * smootherStep(scrollSensitiveAreaHeight, 0, absTop));
			} else if(absTop > height - scrollSensitiveAreaHeight){
				delta = (int)(MAX_DRAG_SCROLL_SPEED * smootherStep(height - scrollSensitiveAreaHeight, height, absTop));
			} else {
				delta = 0;
			}

			mScrollViewContainer.removeCallbacks(mAutoScrollUpdater);
			mScrollViewContainer.smoothScrollBy(0, delta);
			mAutoScrollUpdater = new Runnable(){
				@Override
				public void run() {
					if(mDragChildView.dragging && startScrollY != mScrollViewContainer.getScrollY()){
						onDrag(mDragChildView.totalDragOffset + delta);
					}
				}
			};
			mScrollViewContainer.post(mAutoScrollUpdater);
		}
	}

	/**
	 *	根据指定数据得到一个平滑的自动滚动速度，增强交互体验
	 *	即在自动滚动敏感区域的拖拽偏移量越大，触发的速度越快
	 * @param e1
	 * @param e2
	 * @param x
	 * @return
	 */
	private static float smootherStep(float e1, float e2, float x){
		x = Math.max(0, Math.min((x - e1) / (e2 - e1), 1));
		return x * x * x * (x * (x * 6 - 15) + 10);
	}

	/**
	 *	查询指定位置的子视图的前一个子视图索引，用于{@see #nextDraggablePosition(int)}配合使用
	 *	用于确定此次交换位置是向上还是向下
	 * @param position
	 * @return
	 */
	private int previousDraggablePosition(int position){
		int startIndex = mSwitchChildrenAnimator.indexOfKey(position);
		if(startIndex < 1 || startIndex > mSwitchChildrenAnimator.size())
			return -1;
		return mSwitchChildrenAnimator.keyAt(startIndex - 1);
	}

	/**
	 *	查询指定位置的子视图的下一个视图，用于{@see #previousDraggablePosition(int)}配合使用
	 *	用于确定此次交换位置是向上还是向下
	 * @param position
	 * @return
	 */
	private int nextDraggablePosition(int position){
		int startIndex = mSwitchChildrenAnimator.indexOfKey(position);
		if(startIndex < -1 || startIndex > mSwitchChildrenAnimator.size() - 2)
			return -1;
		return mSwitchChildrenAnimator.keyAt(startIndex + 1);
	}

	/**
	 *	触摸事件结束，无论是否发生了拖拽，均还原必要的变量
	 */
	private void onTouchEnded(){
		mDownY = -1;
		mActivePointerId = INVALID_POINTER_ID;
	}


	/**
	 *	根据给定的View视图生成一个该视图对应的BitmapDrawable对象
	 * @param view
	 * @return
	 */
	private BitmapDrawable getDragDrawable(View view) {
		int top = view.getTop();
		int left = view.getLeft();

		Bitmap bitmap = Utils.getBitmapFromView(view);

		BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
		if( mNeedForegroundMask ){
			mImageForegroundMaskDrawable.setBounds(left, top, left + view.getWidth(), top + view.getHeight());
		}

		drawable.setBounds(new Rect(left, top, left + view.getWidth(), top + view.getHeight()));

		return drawable;
	}
	
	/**
	 *	判断长按的Runnable
	 * @author A Shuai
	 *
	 */
	private class LongPressRunnable implements Runnable{

		private View mView = null;
		
		@Override
		public void run() {
			isBeingLongPress =  false;
			startDetectingDrag(mView);
			startDrag();
		}
		
	}
	
	/**
	 *	更新用的Handler
	 * @author A Shuai
	 *
	 */
	private static class UpdateHandler extends Handler{

		private WeakReference<DraggableLinearListView> mView = null;
		
		public UpdateHandler( DraggableLinearListView mView ){
			this.mView = new WeakReference<DraggableLinearListView>( mView );
		}
		
		@Override
		public void handleMessage(Message msg) {
			this.mView.get();
			
			
		}
		
	}

	/**
	 *	拖拽锚视图的Touch监听器
	 * @author A Shuai
	 *
	 */
	private class DragHandleOnTouchListener implements OnTouchListener {

		/**
		 *	锚视图监听器所持有的子视图句柄
		 *	这里锚视图不一定等于子视图，可是是子视图中的子视图
		 */
		private final View view;

		public DragHandleOnTouchListener(final View view){
			this.view = view;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZ");
			switch( mDragTriggerType ){
				case SHORT_PRESS:
					if(MotionEvent.ACTION_DOWN == MotionEventCompat.getActionMasked(event)){
						System.out.println("111111111111111111111");
						startDetectingDrag(view);
					}
					break;
				case LONG_PRESS:
					switch( MotionEventCompat.getActionMasked(event) ){
						case MotionEvent.ACTION_DOWN:
							isBeingLongPress = true;
							view.getHitRect(mBeingDragViewRect);
							mLongPressRunnable.mView = view;
							mHandler.postDelayed(mLongPressRunnable, 1500);
							break;
						case MotionEvent.ACTION_MOVE:
							
							break;
						case MotionEvent.ACTION_POINTER_UP:
						case MotionEvent.ACTION_UP:
						case MotionEvent.ACTION_CANCEL:
							mHandler.removeCallbacks( mLongPressRunnable );
							break;
					}
					return false;
			}
			return false;
		}
	}

	/**
	 *	每个子View可能需要执行的交换位置动画的句柄封装类
	 * @author A Shuai
	 *
	 */
	private class SwitchChildAnimator {

		/**
		 *	如果非空，则存在一个引用到正在执行的位置交换动画
		 */
		private ValueAnimator swapAnimation;

		/**
		 *	结束动画
		 */
		public void endExistingAnimation(){
			if(null != swapAnimation){
				swapAnimation.end();
			}
		}
	}

	/**
	 *	保存当前正在拖拽item的状态信息的句柄类
	 *	
	 *	成员方法调用声明周期
	 *	设置潜在的可拖拽对象	@see #setValidOnPossibleDrag(View, int) - #valid = true
	 *	判定为拖拽，开始拖拽	@see #onDragStart() - #dragging = true
	 *	拖拽结束，开始执行拖拽对象回归动画	@see #onDragStop() - #dragging = false, #settleAnimation = true
	 *	动画执行结束，设置为无效	@see #setInvalid() - #valid = false
	 *	
	 * @author A Shuai
	 *
	 */
	private class DragChildView {
		//潜在拖拽或正在拖拽或正在执行回归动画的的view
		private View view;
		//起始可见状态
		private int startVisibility;
		//用户绘制拖拽影像
		private BitmapDrawable viewDrawable;
		//当前拖拽view所在的索引位置，对应于应当对应的位置，如拖拽view起始对应于2号位，当与3号位交换后，3号view占据了2号位，那么此拖拽view对应的位置索引为3
		private int position;
		//拖拽view对应的起始Y坐标
		private int startTop;
		//拖拽view对应的高度
		private int height;
		//总共的拖拽偏移量，应当为拖拽影像对应的上边缘Y坐标
		private int totalDragOffset;
		//距离目标位置应该移动的偏移量
		private int targetTopOffset;
		//对应的回归属性动画句柄
		private ValueAnimator settleAnimation;

		//当前view是否存在潜在的可拖拽或正在拖拽
		private boolean valid;
		//当前的view正在拖拽
		private boolean dragging;

		public DragChildView(){
			setInvalid();
		}

		/**
		 *	初始化潜在的拖拽对象及属性
		 * @param view
		 * @param position
		 */
		public void setValidOnPossibleDrag(final View view, final int position){
			this.view = view;
			this.startVisibility = view.getVisibility();
			this.viewDrawable = getDragDrawable(view);
			this.position = position;
			this.startTop = view.getTop();
			this.height = view.getHeight();
			this.totalDragOffset = 0;
			this.targetTopOffset = 0;
			this.settleAnimation = null;

			this.valid = true;
		}

		/**
		 *	拖拽开始
		 */
		public void onDragStart(){
			view.setVisibility(View.INVISIBLE);
			this.dragging = true;
		}

		/**
		 *	更新距离初始位置的总体偏移量
		 * @param offset
		 */
		public void setTotalOffset(int offset){
			totalDragOffset = offset;
			updateTargetTop();
		}

		/**
		 *	更新距离目标位置的偏移量
		 */
		public void updateTargetTop(){
			targetTopOffset = startTop - view.getTop() + totalDragOffset;
		}

		/**
		 *	停止拖拽
		 */
		public void onDragStop(){
			this.dragging = false;
		}

		/**
		 *	判断回归动画是否正在执行
		 * @return
		 */
		public boolean settling(){
			return null != settleAnimation;
		}

		/**
		 *	回归动画执行完毕，回归无效状态并还原必要的设置和释放资源
		 */
		public void setInvalid(){
			this.valid = false;
			if(null != view)
				view.setVisibility(startVisibility);
			view = null;
			startVisibility = -1;
			if( viewDrawable != null ){
				viewDrawable.getBitmap().recycle();
			}
			viewDrawable = null;
			position = -1;
			startTop = -1;
			height = -1;
			totalDragOffset = 0;
			targetTopOffset = 0;
			if(null != settleAnimation)
				settleAnimation.end();
			settleAnimation = null;
		}
	}
	

}
