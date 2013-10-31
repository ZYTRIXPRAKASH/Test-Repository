package com.flourish.pulltorefresh;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.flourish.R;
import com.pcr.android.widget.PullActivateLayout;
import com.pcr.android.widget.PullActivateLayout.OnPullListener;
import com.pcr.android.widget.PullActivateLayout.OnPullStateListener;

public class PullToRefresh  implements OnPullListener ,OnPullStateListener{
	
	private final static int MSG_LOADING = 1;
	private final static int MSG_LOADED = 2;
	Activity mContext=null;
	
	private PullActivateLayout mPullLayout;
	private TextView mActionText;
	private TextView mTimeText;
	private View mProgress;
	private View mActionImage;
	private AsyncTask<Void, Void, Void> asyncTask;
	private Animation mRotateUpAnimation;
	private Animation mRotateDownAnimation;

	/* Variable */
	private boolean mInLoading = false;
	ResetAsync resetAsync = null;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_LOADING:
					//XXX at these load data, and notify complete by MSG_LOADED
					sendEmptyMessageDelayed(MSG_LOADED, 2000);
					break;
				case MSG_LOADED:
					dataLoaded();
					break;
			}
		}
	};
	

	public PullToRefresh(Activity activity, AsyncTask<Void, Void, Void> asyncTask, View pullContainer) {
		mContext = activity;
		this.asyncTask = asyncTask; 
		initView(pullContainer);
		resetAsync = (ResetAsync) activity;
		
	}
	
	public PullToRefresh(Fragment fragment,	AsyncTask<Void, Void, Void> asyncTask, View pullContainer) {
		mContext = fragment.getActivity();
		this.asyncTask = asyncTask; 
		initView(pullContainer);
		resetAsync = (ResetAsync) fragment;
	}

	private void initView(View mView) {
		mRotateUpAnimation=AnimationUtils.loadAnimation(mContext, R.anim.rotate_up);
	
		mRotateDownAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_down);
		
 	//View mView;
	//LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//mView=inflater.inflate(R.layout.refresh_layout, null);
		
		mPullLayout=(PullActivateLayout)mView.findViewById(R.id.pull_container);
		
		
		mPullLayout.setOnActionPullListener(this);
		mPullLayout.setOnPullStateChangeListener(this);

		mProgress = mView.findViewById(android.R.id.progress);
		mActionImage =mView. findViewById(android.R.id.icon);
		mActionText = (TextView)mView. findViewById(R.id.pull_note);
		mTimeText = (TextView) mView.findViewById(R.id.refresh_time);

		mTimeText.setText(R.string.note_not_update);
		mActionText.setText(R.string.note_pull_down);
//		getListView().setOnItemClickListener(this);
	}

/*	private void initData() {
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			data.add(String.format("Item %d", i));
		}

//		ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, data);
//	//	setListAdapter(adapter);
	}*/

	
	
	private void startLoading() {
		if (!mInLoading) {
			mInLoading = true;
			mPullLayout.setEnableStopInActionView(true);
			
			mActionImage.clearAnimation();
			mActionImage.setVisibility(View.GONE);
			mProgress.setVisibility(View.VISIBLE);
			mActionText.setText(R.string.note_pull_loading);
			mHandler.sendEmptyMessage(MSG_LOADING);
			if(asyncTask != null)
			
				asyncTask.execute();
		
		}
	}

	public void dataLoaded() {
		if (mInLoading) {
			mInLoading = false;
			mPullLayout.setEnableStopInActionView(false);
			mPullLayout.hideActionView();
			mActionImage.setVisibility(View.VISIBLE);
			mProgress.setVisibility(View.GONE);

			if (mPullLayout.isPullOut()) {
				mActionText.setText(R.string.note_pull_refresh);
				mActionImage.clearAnimation();
				mActionImage.startAnimation(mRotateUpAnimation);
			} else {
				mActionText.setText(R.string.note_pull_down);
			}

			
			mTimeText.setText(""+mContext.getString(R.string.note_update_at, ""+DateFormat.getTimeInstance().format(new Date(System.currentTimeMillis()))));
		
		
		}
	}

	@Override
	public void onPullOut() {
		if (!mInLoading) {
			mActionText.setText(R.string.note_pull_refresh);
			mActionImage.clearAnimation();
			mActionImage.startAnimation(mRotateUpAnimation);
			resetAsync.resetAsync();
		}
	}

	@Override
	public void onPullIn() {
		if (!mInLoading) {
			mActionText.setText(R.string.note_pull_down);
			mActionImage.clearAnimation();
			mActionImage.startAnimation(mRotateDownAnimation);
		}
	}

	@Override
	public void onSnapToTop() {
		startLoading();
	}

	@Override
	public void onShow() {

	}

	@Override
	public void onHide() {

	}

	public interface ResetAsync{
		public void resetAsync();
	}

	public void setAsync(AsyncTask<Void, Void, Void> testAsyncTask) {
		asyncTask = testAsyncTask;
		
	}
	
}
