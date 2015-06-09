package com.company.pg.ui;

import com.company.pg.R;
import com.company.pg.base.BaseFragmentActivity;
import com.company.pg.fragment.MainFragment;
import com.company.pg.fragment.SettingsFragment;
import com.company.pg.fragment.SmartHomeFragment;
import com.company.pg.utils.AndroidUtils;
import com.company.pg.widget.CustomToast;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 主界面，RadioGroup + Fragment的框架
 */
public class MainActivity extends BaseFragmentActivity {

	public static final int MAINPAGE_FRAGMENT_FLAG = 0;// 主界面
	public static final int SMARTHOME_FRAGMENT_FLAG = 1;// 智能家居
	public static final int SETTING_FRAGMENT_FLAG = 2;// 设置
	private String currentFragmentTag = MainFragment.FRAGMENT_TAG;
	private static final String STATE_FRAGMENT_TAG = "current:fragment_tag";

	private RadioGroup radioGroup;// 底部RadioGroup
	private float dencity = 0f;

	// radioButton
	private RadioButton mainRadioButton;
	private RadioButton settingRadioButton;
	private RadioButton smartHomeRadioButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setContentView(R.layout.activity_main);

		if (savedInstanceState != null) {
			String saveFlag = savedInstanceState.getString(STATE_FRAGMENT_TAG);
			if (!TextUtils.isEmpty(saveFlag)) {
				currentFragmentTag = saveFlag;
			}
		}

		initView();
		setAttribute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(STATE_FRAGMENT_TAG, currentFragmentTag);
		super.onSaveInstanceState(outState);
	}

	// 初始化数据
	private void initData() {
		dencity = AndroidUtils.getDeviceDisplayDensity(this);
	}

	// 初始化控件
	private void initView() {
		radioGroup = (RadioGroup) this.findViewById(R.id.home_radioBtn_group);
		mainRadioButton = (RadioButton) this.findViewById(R.id.homeRadioButton);
		settingRadioButton = (RadioButton) this
				.findViewById(R.id.settingRadioButton);
		smartHomeRadioButton = (RadioButton) this
				.findViewById(R.id.smartHomeRadioButton);

//		setRadioBtnSize(Math.round(30 * dencity));
	}

	// 设置控件属性
	private void setAttribute() {
		radioGroup.setOnCheckedChangeListener(new MyRadioGroupCheckListenner());
		changeFragment(getFragmentFlag(currentFragmentTag));
	}

	// 获取Fragment对应的Flag
	private int getFragmentFlag(String fragmentFlag) {
		int flag = -1;
		if (MainFragment.FRAGMENT_TAG.equals(fragmentFlag)) {
			flag = MAINPAGE_FRAGMENT_FLAG;
		} else if (SmartHomeFragment.FRAGMENT_TAG.equals(fragmentFlag)) {
			flag = SMARTHOME_FRAGMENT_FLAG;
		} else if (SettingsFragment.FRAGMENT_TAG.equals(fragmentFlag)) {
			flag = SETTING_FRAGMENT_FLAG;
		}
		return flag;
	}

	// RadioGroup选中事件
	private class MyRadioGroupCheckListenner implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.homeRadioButton:// 主菜单
				changeFragment(MAINPAGE_FRAGMENT_FLAG);
				break;
			case R.id.settingRadioButton:// 设置
				changeFragment(SETTING_FRAGMENT_FLAG);
				break;
			case R.id.smartHomeRadioButton://  智能家居
				changeFragment(SMARTHOME_FRAGMENT_FLAG);
				break;
			default:
				break;
			}
		}

	}

	// 加载Fragment
	private void changeFragment(int index) {
		final Fragment fragment;
		final String tag;

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction tr = fm.beginTransaction();
		if (currentFragmentTag != null) {
			final Fragment currentFragment = fm
					.findFragmentByTag(currentFragmentTag);
			if (currentFragment != null) {
				tr.hide(currentFragment);// 将当前当前即将消失的fragment移除
			}
		}

		switch (index) {
		case MAINPAGE_FRAGMENT_FLAG:// 显示首页Fragment
			mainRadioButton.setChecked(true);

			tag = MainFragment.FRAGMENT_TAG;
			final MainFragment homePageFragment = (MainFragment) fm
					.findFragmentByTag(tag);
			if (homePageFragment != null) {
				fragment = homePageFragment;
			} else {
				final MainFragment homePage = new MainFragment();
				fragment = homePage;
			}
			break;
		case SETTING_FRAGMENT_FLAG:// 设置
			settingRadioButton.setChecked(true);

			tag = SettingsFragment.FRAGMENT_TAG;
			final SettingsFragment setFragment = (SettingsFragment) fm
					.findFragmentByTag(tag);
			if (setFragment != null) {
				fragment = setFragment;
			} else {
				final SettingsFragment settingFragment = new SettingsFragment();
				fragment = settingFragment;
			}

			break;
		case SMARTHOME_FRAGMENT_FLAG:// 智能家居
			smartHomeRadioButton.setChecked(true);

			tag = SmartHomeFragment.FRAGMENT_TAG;
			final SmartHomeFragment myFragment = (SmartHomeFragment) fm
					.findFragmentByTag(tag);
			if (myFragment != null) {
				fragment = myFragment;
				// tr.remove(myFragment);
			} else {
				final SmartHomeFragment mine = new SmartHomeFragment();
				fragment = mine;
			}

			break;
		default:
			return;
		}

		// 选择显示的fragment,并提交事务
		if (fragment.isAdded()) {
			tr.show(fragment);
		} else {
			tr.add(R.id.mainFragmentLayout, fragment, tag);
		}
		tr.commitAllowingStateLoss();
		currentFragmentTag = tag;
	}

	// 重构RadioBtn的图片大小
//	private void setRadioBtnSize(int size) {
//		for (int i = 0; i < radioGroup.getChildCount(); i++) {
//			RadioButton button = (RadioButton) radioGroup.getChildAt(i);
//			Drawable[] drawables = button.getCompoundDrawables();
//			drawables[1].setBounds(0, 0, size, size);
//			button.setCompoundDrawables(drawables[0], drawables[1],
//					drawables[2], drawables[3]);
//		}
//	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
