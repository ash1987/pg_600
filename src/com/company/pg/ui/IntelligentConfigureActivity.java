package com.company.pg.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;

/*
 * 智能配置
 */
public class IntelligentConfigureActivity extends BaseActivity implements OnClickListener {
	private TextView page_title;
	private Button cancleBt;
	private Button okBt;

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
		setContentView(R.layout.intelligent_config_layout);

		page_title = (TextView) findViewById(R.id.page_title);
		cancleBt = (Button) findViewById(R.id.cancleBt);
		okBt = (Button) findViewById(R.id.okBt);
	}

	@Override
	protected void setAttribute() {
		cancleBt.setOnClickListener(this);
		okBt.setOnClickListener(this);

		page_title.setText(R.string.smart_config_settings);
	}
}
