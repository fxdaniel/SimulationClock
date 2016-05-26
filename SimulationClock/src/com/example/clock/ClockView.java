package com.example.clock;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * @author:hadoop
 * @date:2016年5月23日
 * @description:
 */
public class ClockView extends View implements Runnable{

	public enum POINTER_TYPE {HOUR,MINUTE,SECOND};// 指针的类型
	
	private final float UNIT_DEGREE=(float)(6*Math.PI/180);// 一个小格的度数
	
	private final float PANEL_RADIUS=200.0f;// 表盘半径
	
	private final float HOUR_POINTER_LENGTH=PANEL_RADIUS-100;// 指针长度
	private final float MINUTE_POINTER_LENGTH=PANEL_RADIUS-80;
	private final float SECOND_POINTER_LENGTH=PANEL_RADIUS-35;
	
	
	private Paint mPaint= new Paint();
	
	private float cx;
	private float cy;
	
	private final float ROLLER_RADIUS = 10.0f;
	
	public ClockView(Context context) {
		super(context);
	}
	
	public void drawBackground(Canvas canvas){
		cx=canvas.getWidth()/2;
		cy=canvas.getHeight()/2;
		
		mPaint.reset();
		mPaint.setStyle(Paint.Style.FILL); // 画笔填充模式
		mPaint.setColor(Color.WHITE);
		canvas.drawCircle(cx, cy, PANEL_RADIUS, mPaint);
	}
	
	public void drawBorder(Canvas canvas){
		mPaint.reset();
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeWidth(4);
		mPaint.setColor(0xBF3F6AB5);
		canvas.drawCircle(cx, cy, PANEL_RADIUS, mPaint);
	}
	
	public void drawScale(Canvas canvas){
		mPaint.reset();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(4);
		mPaint.setColor(Color.WHITE);
		
		for(int i=0;i<60;i++){
			if(i%5==0){
				canvas.drawLine(cx - PANEL_RADIUS, cy, cx-PANEL_RADIUS+40, cy, mPaint);
			}else{
				canvas.drawLine(cx-PANEL_RADIUS, cy, cx-PANEL_RADIUS+30, cy, mPaint);
			}
			canvas.rotate(6, cx, cy);
		}
		
	}
	
	public void drawInnerCircle(Canvas canvas){
		Paint p=new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setAntiAlias(true);
		p.setColor(Color.GRAY);
		canvas.drawCircle(cx, cy, ROLLER_RADIUS, p);
	}
	
	public void drawPointers(Canvas canvas){
		
		Calendar calendar=Calendar.getInstance();
		Date now = calendar.getTime();
		int nowHours=now.getHours();
		int nowMinutes=now.getMinutes();
		int nowSeconds=now.getSeconds();
		
		drawHourPointer(canvas, nowHours,nowMinutes);
		drawMinutePointer(canvas, nowMinutes);
		drawSecondPointer(canvas, nowSeconds);
	}
	
	private void drawHourPointer(Canvas canvas,int hours,int passedMinutesOfAnHour){
		mPaint.reset();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeWidth(6);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		
		int part=passedMinutesOfAnHour/12;
		drawPointer(canvas, POINTER_TYPE.HOUR, 5*hours+part);
	}
	
	private void drawMinutePointer(Canvas canvas,int minutes){
		mPaint.reset();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeWidth(6);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		drawPointer(canvas, POINTER_TYPE.MINUTE, minutes);
	}
	
	private void drawSecondPointer(Canvas canvas , int seconds){
		mPaint.reset();
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.RED);
		drawPointer(canvas, POINTER_TYPE.SECOND, seconds);
	}
	
	private void drawPointer(Canvas canvas,POINTER_TYPE pointerType,int value){
		
		float degree;
		float[] pointerHeadXY=new float[2];
		
		switch(pointerType){
		case HOUR:
			degree=(float)(value*UNIT_DEGREE);
			pointerHeadXY=getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
			break;
		case MINUTE:
			degree=(float)(value*UNIT_DEGREE);
			pointerHeadXY=getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
			break;
		case SECOND:
			degree=(float)(value*UNIT_DEGREE);
			pointerHeadXY=getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
			break;
		}
		
		canvas.drawLine(cx, cy, pointerHeadXY[0], pointerHeadXY[1], mPaint);
	}
	
	private float[] getPointerHeadXY(float pointerLength,float degree){
		float[] xy=new float[2];
		xy[0]=(float)(cx+pointerLength*Math.sin(degree));
		xy[1]=(float)(cy-pointerLength*Math.cos(degree));
		return xy;
	}
	
	
	@Override
	public void run() {
		while(true){
			threadSleep(1000);
			postInvalidate();
		}
	}
	private void threadSleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onDraw(Canvas canvas) {
		
//		canvas.drawColor(Color.BLACK);
		drawBackground(canvas);
		drawBorder(canvas);
		drawScale(canvas);
		drawInnerCircle(canvas);
		drawPointers(canvas);
	}
}

