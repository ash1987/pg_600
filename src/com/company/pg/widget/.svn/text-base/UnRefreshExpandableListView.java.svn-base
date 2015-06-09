package com.company.pg.widget;

import java.util.Date;

import com.company.pg.R;
import com.company.pg.utils.AndroidUtils;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UnRefreshExpandableListView extends ExpandableListView
		implements
			OnScrollListener {

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;
	private LayoutInflater inflater;
	// 显示顶端加载的界面
	private LinearLayout headView;
	// 显示底部加载的界面
	private RelativeLayout autoLoadFooterView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;
	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private onMoreListener moreListener;
	private boolean isRefreshable;
	private boolean isMore;

	private int mFitstVisibleItem;
	private int mVisibleItemCount;
	private int mTotalItemCount;
	/* ListView在没有数据时显示的控件 */
	private LinearLayout emptyView = null;
	/* ListView的高度 */
	int emptyView_height = 0;

	public UnRefreshExpandableListView(Context context) {
		super(context);
		init(context);
	}

	public UnRefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);
		emptyView = (LinearLayout) inflater.inflate(R.layout.complaint_empty,
				null);
		headView = (LinearLayout) inflater.inflate(R.layout.head, null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
		isMore = false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (emptyView_height == 0 && h != 0) {
			emptyView_height = h;
			initEmptyView();
			addEmptyView();
		}
	}

	// ListView适配器观察者
	private DataSetObserver observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			super.onChanged();
			addEmptyView();
		}
	};

	/**
	 * 重置无数据控件的高度
	 * 
	 * @param context
	 */
	private void initEmptyView() {
		LinearLayout llLayout = (LinearLayout) emptyView
				.findViewById(R.id.empty_ll);
		android.view.ViewGroup.LayoutParams lp = llLayout.getLayoutParams();
		/**
		 * addFooterView后，EmptyView的高度会变为适配内容的高度，所以将其高度设置为ListView高度减去分割线高度
		 * 使得无数据控件居中
		 */
		lp.height = emptyView_height - this.getDividerHeight();
	}

	/**
	 * 
	 * 添加底端加载更多
	 * 
	 * @param context
	 *            上下文
	 */
	public void addAutoLoadFooterView(Context context) {
		if (getFooterViewsCount() == 0 && isFillScreenItem()) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			autoLoadFooterView = (RelativeLayout) layoutInflater.inflate(
					R.layout.auto_load_footer_lv, null);
			autoLoadFooterView.setEnabled(false);
			addFooterView(autoLoadFooterView);
		}
	}

	/**
	 * 
	 * 隐藏底端加载更多
	 */
	public void hideAutoLoadFooterView() {
		removeFooterView(autoLoadFooterView);
	}

	/**
	 * 条目是否填满整个屏幕
	 */
	private boolean isFillScreenItem() {
		final int firstVisiblePosition = getFirstVisiblePosition();
		final int lastVisiblePostion = getLastVisiblePosition()
				- getFooterViewsCount();
		final int visibleItemCount = lastVisiblePostion - firstVisiblePosition
				+ 1;
		final int totalItemCount = getCount() - getFooterViewsCount();

		if (visibleItemCount < totalItemCount) {
			return true;
		}

		return false;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFitstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		mTotalItemCount = totalItemCount;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& mFitstVisibleItem + mVisibleItemCount == getCount()
				&& isMore && getFooterViewsCount() == 0 && isFillScreenItem()) {
			onMore();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN :
					if (mFitstVisibleItem == 0 && !isRecored) {
						isRecored = true;
						startY = (int) event.getY();
					}
					break;

				case MotionEvent.ACTION_UP :

					if (state != REFRESHING && state != LOADING) {
						if (state == DONE) {
							// 什么都不做
						}
						if (state == PULL_To_REFRESH) {
							state = DONE;
							changeHeaderViewByState();
						}
						if (state == RELEASE_To_REFRESH) {
							state = REFRESHING;
							changeHeaderViewByState();
							onRefresh();
						}
					}

					isRecored = false;
					isBack = false;

					break;

				case MotionEvent.ACTION_MOVE :
					int tempY = (int) event.getY();

					if (!isRecored && mFitstVisibleItem == 0) {
						isRecored = true;
						startY = tempY;
					}

					if (state != REFRESHING && isRecored && state != LOADING) {

						// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

						// 可以松手去刷新了
						if (state == RELEASE_To_REFRESH) {

							setSelection(0);

							// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
							if (((tempY - startY) / RATIO < headContentHeight)
									&& (tempY - startY) > 0) {
								state = PULL_To_REFRESH;
								changeHeaderViewByState();
							}
							// 一下子推到顶了
							else if (tempY - startY <= 0) {
								state = DONE;
								changeHeaderViewByState();
							}
							// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
							else {
								// 不用进行特别的操作，只用更新paddingTop的值就行了
							}
						}
						// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
						if (state == PULL_To_REFRESH) {

							setSelection(0);

							// 下拉到可以进入RELEASE_TO_REFRESH的状态
							if ((tempY - startY) / RATIO >= headContentHeight) {
								state = RELEASE_To_REFRESH;
								isBack = true;
								changeHeaderViewByState();
							}
							// 上推到顶了
							else if (tempY - startY <= 0) {
								state = DONE;
								changeHeaderViewByState();
							}
						}

						// done状态下
						if (state == DONE) {
							if (tempY - startY > 0) {
								state = PULL_To_REFRESH;
								changeHeaderViewByState();
							}
						}

						// 更新headView的size
						if (state == PULL_To_REFRESH) {
							headView.setPadding(0, -1 * headContentHeight
									+ (tempY - startY) / RATIO, 0, 0);

						}

						// 更新headView的paddingTop
						if (state == RELEASE_To_REFRESH) {
							headView.setPadding(0, (tempY - startY) / RATIO
									- headContentHeight, 0, 0);
						}

					}

					break;
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 
	 * 当状态改变时候，调用该方法，以更新界面
	 */
	private void changeHeaderViewByState() {
		switch (state) {
			case RELEASE_To_REFRESH :
				arrowImageView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);

				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(animation);

				tipsTextview.setText("松开刷新");
				break;
			case PULL_To_REFRESH :
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.VISIBLE);
				// 是由RELEASE_To_REFRESH状态转变来的
				if (isBack) {
					isBack = false;
					arrowImageView.clearAnimation();
					arrowImageView.startAnimation(reverseAnimation);

					tipsTextview.setText("下拉刷新");
				} else {
					tipsTextview.setText("下拉刷新");
				}
				break;

			case REFRESHING :
				headView.setPadding(0, 0, 0, 0);

				progressBar.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.GONE);
				tipsTextview.setText("正在刷新...");
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				break;
			case DONE :
				headView.setPadding(0, -1 * headContentHeight, 0, 0);

				progressBar.setVisibility(View.GONE);
				arrowImageView.clearAnimation();
				arrowImageView
						.setImageResource(R.drawable.ic_pulltorefresh_arrow);
				tipsTextview.setText("下拉刷新");
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void setonMoreListener(onMoreListener moreListener) {
		this.moreListener = moreListener;
		isMore = true;
	}

	public interface OnRefreshListener {

		public void onRefresh();
	}

	public interface onMoreListener {

		public void onMore();
	}

	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("最近更新:"
				+ AndroidUtils.getDateToString(
						String.valueOf(new Date().getTime()),
						"yyyy-MM-dd HH:mm"));
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	private void onMore() {
		if (moreListener != null) {
			moreListener.onMore();
		}
	}

	/**
	 * 
	 * 获取视图的实际宽高
	 * 
	 * @param child
	 *            视图对象
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseExpandableListAdapter adapter) {
		lastUpdatedTextView.setText("最近更新:"
				+ AndroidUtils.getDateToString(
						String.valueOf(new Date().getTime()),
						"yyyy-MM-dd HH:mm"));
		super.setAdapter(adapter);
		if (getAdapter() != null && observer != null) {
			getAdapter().registerDataSetObserver(observer);
		}
	}

	/**
	 * 添加ListView无数据控件
	 */
	public void addEmptyView() {
		if (emptyView != null) {
			removeFooterView(emptyView);
		}
		if (getAdapter() != null && getAdapter().getCount() == 1) {
			addFooterView(emptyView);
		}
	}

	// 注销Adpter观察者
	public void unRegistObserver() {
		if (observer != null) {
			getAdapter().unregisterDataSetObserver(observer);
		}
	}
}
