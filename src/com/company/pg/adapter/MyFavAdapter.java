package com.company.pg.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.pg.R;
import com.company.pg.bean.FavBean.Body;
import com.company.pg.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 我的收藏列表适配器
 */
public class MyFavAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Body> favLists;
	private Callback callback;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public interface Callback {
		public void sendCollect(int pos);

		public void deleItem(int pos);
	}

	public MyFavAdapter(Context mContext, ArrayList<Body> favLists,
			Callback callback) {
		this.mContext = mContext;
		this.favLists = favLists;
		this.callback = callback;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnLoading(R.drawable.img_loading)
				.showImageOnFail(R.drawable.img_loading)
				.showImageForEmptyUri(R.drawable.img_loading).cacheOnDisc(true)
				.resetViewBeforeLoading(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();
	}

	@Override
	public int getCount() {
		if (favLists == null) {
			return 0;
		}
		return favLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return favLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View converView, ViewGroup arg2) {
		ViewHolder holder = new ViewHolder();
		if (converView == null) {
//			converView = LayoutInflater.from(mContext).inflate(
//					R.layout.myfav_item_layout, null);
//			holder.favoriteImage = (ImageView) converView
//					.findViewById(R.id.favoriteImage);
//			holder.favoriteGoodName = (TextView) converView
//					.findViewById(R.id.favoriteGoodName);
//			holder.deleteRl = (RelativeLayout) converView
//					.findViewById(R.id.deleteRl);
//			holder.collectRl = (RelativeLayout) converView
//					.findViewById(R.id.collectRl);

			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

//		imageLoader.displayImage(AndroidUtils.getImgUrlOnServer(favLists.get(
//				position).getPicture()), holder.favoriteImage, options);
//		holder.favoriteGoodName.setText(favLists.get(position).getFoodName());

		holder.deleteRl.setOnClickListener(new delOnclick(position));
		holder.collectRl.setOnClickListener(new CollectOnclick(position));

		return converView;
	}

	class delOnclick implements OnClickListener {
		private int pos;

		delOnclick(int pos) {
			this.pos = pos;
		}

		@Override
		public void onClick(View v) {
			callback.deleItem(pos);
		}
	}

	class CollectOnclick implements OnClickListener {
		private int pos;

		CollectOnclick(int pos) {
			this.pos = pos;
		}

		@Override
		public void onClick(View v) {
			callback.sendCollect(pos);
		}
	}

	private static class ViewHolder {
		private ImageView favoriteImage;// 商品图片
		private TextView favoriteGoodName;// 商品名称

		private RelativeLayout deleteRl;
		private RelativeLayout collectRl;
	}
}
