package com.company.pg.fragment;

import java.util.List;

import com.company.pg.Define;
import com.company.pg.R;
import com.xtremeprog.xpgconnect.XPGWifiDevice;
import com.xtremeprog.xpgconnect.XPGWifiErrorCode;
import com.xtremeprog.xpgconnect.XPGWifiSDK;
import com.xtremeprog.xpgconnect.XPGWifiSDKListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 主菜单界面
 */
public class MainFragment extends Fragment  implements OnClickListener{
	public static final String FRAGMENT_TAG = MainFragment.class
			.getSimpleName();
	
	//实例化监听器 
	XPGWifiSDKListener xpgWifiSDKListener = new XPGWifiSDKListener(){ 
	    //注册用户回调
	    public void didRegisterUser(int result, String errorMessage, String uid,String token) {
	        if (result==XPGWifiErrorCode.XPGWifiError_NONE) 
	        { 
	            //注册成功，处理注册成功的逻辑
	        	Log.e("====", "注册成功");
	        }else{ 
	            //注册失败，处理注册失败的逻辑 
	        	Log.e("====", "注册失败");
	        } 
	    }; 
	    
	    @Override
	    public void didDiscovered(int error, List<XPGWifiDevice> devicesList) {
	    	// TODO Auto-generated method stub
	    	super.didDiscovered(error, devicesList);
	    	Log.e("====", devicesList.toString());
	    }
	}; 
	
	private ImageButton lockBtn,unlockBtn;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//注册监听器 
        XPGWifiSDK.sharedInstance().setListener(xpgWifiSDKListener); 
        //调用SDK注册接口
        XPGWifiSDK.sharedInstance().registerUser(Define.plantUserName, Define.plantUserPwd); 
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_fragment_layout, null);
		initView(view);
		setAttribute();
		return view;
	}

	// 初始化数据
	private void initData() {
	}

	// 初始化控件
	private void initView(View view) {
		lockBtn = (ImageButton) view.findViewById(R.id.main_lock_btn);
		lockBtn.setOnClickListener(this);
//		unlockBtn
	}

	// 设置控件属性
	private void setAttribute() {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.main_lock_btn:
			
			break;

		default:
			break;
		}
		
	}
}
