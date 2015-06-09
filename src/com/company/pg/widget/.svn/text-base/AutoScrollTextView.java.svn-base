package com.company.pg.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * 
 * TODO 单行文本跑马灯控件
 * 
 */
public class AutoScrollTextView extends TextView {
	public final static String TAG = AutoScrollTextView.class.getSimpleName();

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f;// 文字的横坐标
	private float y = 0f;// 文字的纵坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	public boolean isStarting = false;// 是否开始滚动
	private Paint paint = null;// 绘图样式
	private String text = "";// 文本内容
	private float stepOffset = 0.8f; // 每次重繪文本偏移量，可以控制文本跑馬燈的速度
	private List<Float> textLen;
	private final static String PADDING = "　　";

	private Shader shader;

	public AutoScrollTextView(Context context) {
		super(context);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// setOnTouchListener(this);
	}

	/**
	 * 文本初始化，每次更改文本内容或者文本效果等之后都需要重新初始化一下
	 */
	public void init(Activity activity) {
		this.init(activity, null);
	}

	public void init(Activity activity, List<String> list) {
		textLen = new ArrayList<Float>();
		paint = getPaint();

		textLength = 0;
		if (list != null && list.size() > 0) {
			text = "";
			for (String item : list) {
				textLen.add(paint.measureText(item + PADDING));
				text += item + PADDING;
				textLength += paint.measureText(item + PADDING);
			}
		} else {
			String aa[] = getText().toString().split(";");
			text = "";
			for (String item : aa) {
				textLen.add(paint.measureText(item + PADDING));
				text += item + PADDING;
				textLength += paint.measureText(item + PADDING);
			}
		}

		viewWidth = getWidth();
		if (viewWidth == 0) {
			DisplayMetrics metric = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
			viewWidth = metric.widthPixels;
		}
		step = textLength;
		temp_view_plus_text_length = viewWidth + textLength;
		temp_view_plus_two_text_length = viewWidth + textLength * 2;
		y = getTextSize() + getPaddingTop();

		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		shader = new LinearGradient(0, 0, viewWidth, 0, new int[] { Color.BLUE,
				Color.GREEN, Color.RED }, new float[] { 0, 0.7f, 1 },
				TileMode.MIRROR);
		paint.setShader(shader);
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);

		ss.step = step;
		ss.isStarting = isStarting;

		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		step = ss.step;
		isStarting = ss.isStarting;
	}

	public static class SavedState extends BaseSavedState {
		public boolean isStarting = false;
		public float step = 0.0f;

		SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeBooleanArray(new boolean[] { isStarting });
			out.writeFloat(step);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}

			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
		};

		private SavedState(Parcel in) {
			super(in);
			boolean[] b = null;
			
			if(in == null) {
				return;
			}
			
			in.readBooleanArray(b);
			if (b != null && b.length > 0)
				isStarting = b[0];
			step = in.readFloat();
		}
	}

	/**
	 * 开始滚动
	 */
	public void startScroll() {
		isStarting = true;
		invalidate();
	}

	/**
	 * 停止滚动
	 */
	public void stopScroll() {
		isStarting = false;
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		step += stepOffset;
		if (step > temp_view_plus_two_text_length) {
			step = textLength;
		}
		Matrix matrix = new Matrix();
		if (isStarting) {
			matrix.setTranslate(step - textLength, 0);
		} else {
			matrix.setTranslate(0, 0);
			return;
		}

		shader.setLocalMatrix(matrix);
		canvas.drawText(text, temp_view_plus_text_length - step, y, paint);

		invalidate();
	}

	public int getTouchSelectedText(float curX) {
		float offsetX = curX + step - temp_view_plus_text_length;
		for (int i = 0; i < textLen.size(); i++) {
			offsetX -= textLen.get(i).floatValue();
			if (offsetX <= 0) {
				return i;
			}
		}
		return textLen.size() - 1;
	}

	public float getStepOffset() {
		return stepOffset;
	}

	public void setStepOffset(float value) {
		this.stepOffset = value;
	}
}
