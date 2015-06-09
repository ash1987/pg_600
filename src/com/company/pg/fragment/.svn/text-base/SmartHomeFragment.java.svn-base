package com.company.pg.fragment;

import com.company.pg.R;
import com.company.pg.adapter.SmartHomeAdapter;
import com.company.pg.ui.AboutActivity;
import com.company.pg.ui.LightsSettingActivity;
import com.company.pg.ui.SmartConfigActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 智能家居界面
 */
public class SmartHomeFragment extends Fragment implements OnItemClickListener {
	public static final String FRAGMENT_TAG = SmartHomeFragment.class
			.getSimpleName();

	private ListView smartHomeList;

	private SmartHomeAdapter smartHomeAdapter;// 选项列表

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.smarthome_fragment_layout, null);
		initView(view);
		setAttribute();
		return view;
	}

	// 初始化数据
	private void initData() {
	}

	// 初始化控件
	private void initView(View view) {
		smartHomeList = (ListView) view.findViewById(R.id.smarthomeList);
	}

	// 设置控件属性
	private void setAttribute() {
		smartHomeAdapter = new SmartHomeAdapter(getActivity());
		smartHomeList.setAdapter(smartHomeAdapter);
		smartHomeList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Class targetClass = LightsSettingActivity.class;
		
		switch (position) {
		case 0://灯光设置
			targetClass = LightsSettingActivity.class;
			break;
		case 3://关于
			targetClass = AboutActivity.class;
			break;
		default:
			break;
		}
		
		startActivity(new Intent(SmartHomeFragment.this.getActivity(), targetClass));
	}
}
