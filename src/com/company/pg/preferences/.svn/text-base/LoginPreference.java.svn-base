package com.company.pg.preferences;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

/**
 * 登录成功后信息 保存
 */
public class LoginPreference extends BasePreferences {

	// 文件名
	private final static String PERFERENCE_NAME = "login_perferences";
	// 是否已经登录
	private final static String LOGINSTATE = "login_state";
	// 手机验证
	private final static String PHONEACTIVATE = "PhoneActivate";
	// 用户帐号余额
	private final static String USERMONEY = "Usermoney";
	// 用户编号
	private final static String DATAID = "DataID";
	// 昵称
	private final static String NAME = "Name";
	// 真实名称
	private final static String TRUENAME = "TrueName";
	// 性别
	private final static String SEX = "Sex";
	// 手机
	private final static String TELL = "Tell";
	// 电话
	private final static String PHONE = "Phone";
	// QQ
	private final static String QQ = "QQ";
	// MSN
	private final static String MSN = "MSN";
	// 注册时间
	private final static String REGTIME = "RegTime";
	// 积分
	private final static String POINT = "Point";
	// 头像
	private final static String PICTURE = "Picture";
	// 状态
	private final static String STATE = "State";
	// Email
	private final static String EMAIL = "EMAIL";
	// 密码
	private final static String PASSWORD = "Password";
	// 用户登录次数
	private final static String GROUPID = "GroupID";
	// 所在站点
	private final static String WEBSITE = "WebSite";
	// openid
	private final static String OPENID = "openid";
	// 记录是哪个第三方网站
	private final static String WTYPE = "wtype";
	// 支付密码
	private final static String PAYPASSWORD = "PayPassword";

	public LoginPreference(Context context) {
		super(context, PERFERENCE_NAME);
	}

	public void saveLoginInfo(Context context, String PhoneActivate,
			String Usermoney, String DataID, String Name, String TrueName,
			String Sex, String Tell, String Phone, String QQ, String MSN,
			String RegTime, String Point, String Picture, String State,
			String EMAIL, String Password, String GroupID, String WebSite,
			String openid, String wtype, String PayPassword) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		if (!TextUtils.isEmpty(PhoneActivate)) {
			values.put(PHONEACTIVATE, PhoneActivate);
		}
		if (!TextUtils.isEmpty(Usermoney)) {
			values.put(USERMONEY, Usermoney);
		}
		if (!TextUtils.isEmpty(DataID)) {
			values.put(DATAID, DataID);
		}
		if (!TextUtils.isEmpty(Name)) {
			values.put(NAME, Name);
		}
		if (!TextUtils.isEmpty(TrueName)) {
			values.put(TRUENAME, TrueName);
		}
		if (!TextUtils.isEmpty(Sex)) {
			values.put(SEX, Sex);
		}
		if (!TextUtils.isEmpty(Tell)) {
			values.put(TELL, Tell);
		}
		if (!TextUtils.isEmpty(Phone)) {
			values.put(PHONE, Phone);
		}
		if (!TextUtils.isEmpty(QQ)) {
			values.put(QQ, QQ);
		}
		if (!TextUtils.isEmpty(MSN)) {
			values.put(MSN, MSN);
		}
		if (!TextUtils.isEmpty(RegTime)) {
			values.put(REGTIME, RegTime);
		}
		if (!TextUtils.isEmpty(Point)) {
			values.put(POINT, Point);
		}
		if (!TextUtils.isEmpty(Picture)) {
			values.put(PICTURE, Picture);
		}
		if (!TextUtils.isEmpty(State)) {
			values.put(STATE, State);
		}
		if (!TextUtils.isEmpty(EMAIL)) {
			values.put(EMAIL, EMAIL);
		}
		if (!TextUtils.isEmpty(Password)) {
			values.put(PASSWORD, Password);
		}
		if (!TextUtils.isEmpty(GroupID)) {
			values.put(GROUPID, GroupID);
		}
		if (!TextUtils.isEmpty(WebSite)) {
			values.put(WEBSITE, WebSite);
		}
		if (!TextUtils.isEmpty(openid)) {
			values.put(OPENID, openid);
		}
		if (!TextUtils.isEmpty(wtype)) {
			values.put(WTYPE, wtype);
		}
		if (!TextUtils.isEmpty(PayPassword)) {
			values.put(PAYPASSWORD, PayPassword);
		}
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 保存登录状态
	public void setLoginState(Context context, boolean isLogin) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(LOGINSTATE, isLogin);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取登录状态
	public boolean getLoginState() {
		return getBoolean(LOGINSTATE, false);
	}

	// 保存 用户名
	public void setUserName(Context context, String userName) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(NAME, userName);
		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取用户名
	public String getUserName() {
		return getString(NAME);
	}

	// 获取头像
	public String getPicture() {
		return getString(PICTURE);
	}

	// 获取真实姓名
	public String getTrueName() {
		return getString(TRUENAME);
	}

	// 获取性别
	public String getSex() {
		return getString(SEX);
	}

	// 获取QQ号
	public String getQQ() {
		return getString(QQ);
	}

	// 获取用户编号
	public String getDataID() {
		return getString(DATAID);
	}

	// 获取余额
	public String getUsermoney() {
		return getString(USERMONEY);
	}

	// 获取手机
	private String getTell() {
		return getString(TELL);
	}

	// 获取电话
	private String getPhone() {
		return getString(PHONE);
	}

	// 获取MSN
	private String getMSN() {
		return getString(MSN);
	}

	// 获取注册时间
	private String getRegTime() {
		return getString(REGTIME);
	}

	// 获取积分
	private String getPoint() {
		return getString(POINT);
	}

	// 获取状态
	private String getState() {
		return getString(STATE);
	}

	// 获取email
	private String getEmail() {
		return getString(EMAIL);
	}

	// 获取密码
	private String getPassworkd() {
		return getString(PASSWORD);
	}

	// 获取登录次数
	private String getGroupId() {
		return getString(GROUPID);
	}

	// 获取所有站点
	private String getWebSite() {
		return getString(WEBSITE);
	}

	// 获取openid
	private String getOpenId() {
		return getString(OPENID);
	}

	// 获取wtype
	private String getWtype() {
		return getString(WTYPE);
	}

	// 获取支付密码
	private String getPayPassword() {
		return getString(PAYPASSWORD);
	}

	// 退出登录
	public void setLoginOut(Context context) {
		if (null == preferenses) {
			throw new NullPointerException("SharedPreferences is null!");
		}

		if (null == context) {
			throw new NullPointerException("context is null!");
		}

		ContentValues values = new ContentValues();
		values.put(LOGINSTATE, false);
		values.put(PHONEACTIVATE, "");
		values.put(USERMONEY, "");
		values.put(DATAID, "");
		values.put(NAME, "");
		values.put(TRUENAME, "");
		values.put(SEX, "");
		values.put(TELL, "");
		values.put(PHONE, "");
		values.put(QQ, "");
		values.put(MSN, "");
		values.put(REGTIME, "");
		values.put(POINT, "");
		values.put(PICTURE, "");
		values.put(STATE, "");
		values.put(EMAIL, "");
		values.put(PASSWORD, "");
		values.put(GROUPID, "");
		values.put(WEBSITE, "");
		values.put(OPENID, "");
		values.put(WTYPE, "");
		values.put(PAYPASSWORD, "");

		try {
			write(values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
