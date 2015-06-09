package com.company.pg.adapter;

import com.company.pg.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 系统设置 列表适配器
 */
public class SettingsHomeAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private String[] textStrings;
	private int[] icons = new int[] { R.drawable.smartconfiguration,
			R.drawable.remotecontrol, R.drawable.detector, R.drawable.user,
			R.drawable.language,
			R.drawable.delaytime, R.drawable.sirenrun_time};
//, R.drawable.sms ,R.drawable.phone,R.drawable.siren
	
	public SettingsHomeAdapter(Context mContext) {
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		textStrings = mContext.getResources().getStringArray(
				R.array.settings_home_array);
	}

	@Override
	public int getCount() {
		return textStrings.length;
	}

	@Override
	public Object getItem(int position) {
		return textStrings[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup viewGroup) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
			converView = inflater.inflate(R.layout.smarthome_list_item, null);
			holder.imageView = (ImageView) converView
					.findViewById(R.id.smartHomeItemImage);
			holder.textView = (TextView) converView
					.findViewById(R.id.smartHomeItemText);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		holder.textView.setText(textStrings[position]);
		holder.imageView.setImageResource(icons[position]);
		return converView;
	}

	private static class ViewHolder {
		private ImageView imageView;
		private TextView textView;
	}
}