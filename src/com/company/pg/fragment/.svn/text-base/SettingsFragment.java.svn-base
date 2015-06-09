package com.company.pg.fragment;

import com.company.pg.R;
import com.company.pg.adapter.SettingsHomeAdapter;
import com.company.pg.adapter.SmartHomeAdapter;
import com.company.pg.ui.LightsSettingActivity;
import com.company.pg.ui.SmartConfigActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 设置界面
 */
public class SettingsFragment extends Fragment implements OnItemClickListener {
	public static final String FRAGMENT_TAG = SettingsFragment.class
			.getSimpleName();

	ListView settingsList;
	SettingsHomeAdapter settingsAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings_fragment_layout, null);
		initView(view);
		setAttribute();
		return view;
	}

	// 初始化数据
	private void initData() {
	}

	// 初始化控件
	private void initView(View view) {
		settingsList = (ListView) view.findViewById(R.id.settingshomeList);
	}

	// 设置控件属性
	private void setAttribute() {
		settingsAdapter = new SettingsHomeAdapter(getActivity());
		settingsList.setAdapter(settingsAdapter);
		settingsList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Class targetClass = SmartConfigActivity.class;
		
		switch (position) {
		case 0://灯光设置
			targetClass = SmartConfigActivity.class;
			break;

		default:
			break;
		}
		
		startActivity(new Intent(SettingsFragment.this.getActivity(), targetClass));
	}
}
