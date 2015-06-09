package com.company.pg.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.company.pg.R;
import com.company.pg.base.BaseActivity;
import com.company.pg.utils.AndroidUtils;

/**
 * 描述：关于
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	private TextView page_title;
	private TextView appNameTv;
	private TextView appVersionTv;
	private Button checkVersionBtn;
	private Button cancleBt;
	private Button okBt;

	@Override
	protected void initData() {

	}

	@Override
	protected void initView() {
		setContentView(R.layout.about_layout);

		page_title = (TextView) findViewById(R.id.page_title);
		appNameTv = (TextView) findViewById(R.id.appNameTv);
		appVersionTv = (TextView) findViewById(R.id.appVersionTv);
		checkVersionBtn = (Button) findViewById(R.id.checkVersionBtn);
		cancleBt = (Button) findViewById(R.id.cancleBt);
		okBt = (Button) findViewById(R.id.okBt);
	}

	@Override
	protected void setAttribute() {
		checkVersionBtn.setOnClickListener(this);
		cancleBt.setOnClickListener(this);
		okBt.setOnClickListener(this);

		page_title.setText(R.string.about_string);
		appNameTv.setText(String.format(getString(R.string.app_name_string), getString(R.string.app_name)));
		appVersionTv.setText(String.format(getString(R.string.app_version_string), AndroidUtils.getVersionName(this)));
	}

	@Override
	public void onClick(View v) {
		if(v == null) {
			return;
		}

		switch (v.getId()) {
		case R.id.checkVersionBtn:

			break;
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
}
