package com.company.pg.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.scrollDatePicker.WheelMain;
import com.company.pg.scrollDatePicker.WheelView;

/*
 * 空調控制
 */
public class AirConditioningActivity extends BaseActivity implements OnClickListener {
	private TextView page_title;
	private Button cancleBt;
	private Button okBt;
	private LinearLayout airConditioningContainer;
	private WheelView temperatureWhellView;
	
	@Override
	public void onClick(View v) {
		if(v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.cancleBt:
			this.finish();
			break;
		case R.id.okBt:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void initView() {
		setContentView(R.layout.air_conditioning_layout);
		
		page_title = (TextView) findViewById(R.id.page_title);
		airConditioningContainer = (LinearLayout) findViewById(R.id.airConditioningContainer);
		cancleBt = (Button) findViewById(R.id.cancleBt);
		okBt = (Button) findViewById(R.id.okBt);
		temperatureWhellView = (WheelView) findViewById(R.id.temperatureWhellView);
	}

	@Override
	protected void setAttribute() {
		cancleBt.setOnClickListener(this);
		okBt.setOnClickListener(this);
		
		page_title.setText(R.string.air_conditioning_string);
		WheelMain main = new WheelMain(null, airConditioningContainer, this);
		main.showDateTimePicker(null);
	}
}
