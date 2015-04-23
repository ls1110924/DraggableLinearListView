package com.cqu.draggablelinearlistview.sample.dataset;

public class DraggableListDataSet implements IDataSet{

	private WindDataSet mWind;
	private Advice mAdvice;
	private Sun mSun;
	
	public DraggableListDataSet(){
		mWind = new WindDataSet();
		mAdvice = new Advice();
		mSun = new Sun();
	}
	
	@Override
	public int size() {
		return 3;
	}

	@Override
	public void clear() {
		
	}
	
	public WindDataSet getWind() {
		return mWind;
	}

	public Advice getAdvice() {
		return mAdvice;
	}

	public Sun getSun() {
		return mSun;
	}
	
	public void modify(){
		mWind.modify();
		mAdvice.modify();
		mSun.modify();
	}
	
	public void restore(){
		mWind.restore();
		mAdvice.restore();
		mSun.restore();
	}


	public static class WindDataSet{
		public int mSpeed;		//风车转速
		public String mSpeedStr;
		public String mDirection;
		public String mLevel;
		
		public WindDataSet(){
			restore();
		}
		
		public void modify(){
			mSpeed = 5000;
			mSpeedStr = "风速：   5.0Km/h";
			mDirection = "风向：  东南风";
			mLevel = "风力等级：6级";
		}
		
		public void restore(){
			mSpeed = 10000;
			mSpeedStr = "风速：   3.0Km/h";
			mDirection = "风向：  西北风";
			mLevel = "风力等级：1级";
		}
	}
	
	public static class Advice{
		public String mDress;
		public String mCar;
		public String mExcercise;
		public String mFlu;
		
		public Advice(){
			restore();
		}
		
		public void modify(){
			mDress = "穿衣建议：  夹克";
			mCar = "洗车建议：  不适宜";
			mExcercise = "晨练建议：  不适宜";
			mFlu = "感冒指数：  易发";
		}
		
		public void restore(){
			mDress = "穿衣建议：  短裤";
			mCar = "洗车建议：  适宜";
			mExcercise = "晨练建议：  适宜";
			mFlu = "感冒指数：  不易发";
		}
	}
	
	public static class Sun{
		public String mSun;
		
		public Sun(){
			restore();
		}
		
		public void modify(){
			mSun = "7:23 AM 日出       5:56 PM 日落";
		}
		
		public void restore(){
			mSun = "8:23 AM 日出       6:56 PM 日落";
		}
		
	}

}
