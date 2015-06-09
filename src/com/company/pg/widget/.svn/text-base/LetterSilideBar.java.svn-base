package com.company.pg.widget;

import com.company.pg.R;
import com.company.pg.utils.AndroidUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class LetterSilideBar extends View {
	private char[] l;
	private SectionIndexer sectionIndexter = null;
	private ListView list;
	private TextView mDialogText;
	Bitmap mbitmap;
	private int type = 1;
	private int color = 0xff858c94;
	private float textSize;

	public LetterSilideBar(Context context) {
		super(context);
		init(context);
	}

	public LetterSilideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LetterSilideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		textSize = 13 * AndroidUtils.getDeviceScaledDensity(context
				.getApplicationContext());
		l = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '#' };
	}

	public void setListView(ListView _list, Adapter adapter) {
		list = _list;
		sectionIndexter = (SectionIndexer) adapter;

	}

	public void setTextView(TextView mDialogText) {
		this.mDialogText = mDialogText;
	}

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		int i = (int) event.getY();
		int idx = i / (getMeasuredHeight() / l.length);
		if (idx >= l.length) {
			idx = l.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			setBackgroundResource(R.drawable.scrollbar_bg);
			mDialogText.setVisibility(View.VISIBLE);
			/**
			 * 中央显示的提示首字符
			 */

			mDialogText.setText(String.valueOf(l[idx]));
			mDialogText.setTextSize(34);

			if (sectionIndexter == null) {
				sectionIndexter = (SectionIndexer) list.getAdapter();
			}
			int position = sectionIndexter.getPositionForSection(l[idx]);

			if (position == -1) {
				return true;
			}
			list.setSelection(position);
		} else {
			mDialogText.setVisibility(View.INVISIBLE);
			setBackgroundDrawable(null);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			setBackgroundDrawable(new ColorDrawable(0x00000000));
		}
		return true;
	}

	Paint paint = new Paint();

	protected void onDraw(Canvas canvas) {
		paint.setColor(color);
		if (textSize == 0) {
			textSize = 13f;
		}
		paint.setTextSize(textSize);
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.CENTER);
		float widthCenter = getMeasuredWidth() - (textSize + 3);
		if (l.length > 0) {
			float height = getMeasuredHeight() / l.length;
			for (int i = 0; i < l.length; i++) {
				canvas.drawText(String.valueOf(l[i]), widthCenter, (i + 1)
						* height, paint);
			}
		}
		this.invalidate();
		super.onDraw(canvas);
	}
}
