package com.lonuery.mealordering;

import java.util.List;
import com.lonuery.mealordering.DropdownView.DropDownDelete;
import com.lonuery.mealordering.DropdownView.DropDownItem;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class LoginActivity extends Activity implements OnClickListener,OnFocusChangeListener{
	ClearableEditText password;
	DropdownView userId;
	Button btnLogin;
	DataBase data;
	List<LoginInfo> list;
	public static int displayMetrics;//屏幕密度
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_loginpage);
		
		password = (ClearableEditText)findViewById(R.id.password);
		userId = (DropdownView)findViewById(R.id.userId);
		
		btnLogin = (Button)findViewById(R.id.login);
		btnLogin.setOnClickListener(this);
		DisplayMetrics dm =getResources().getDisplayMetrics();
		displayMetrics = dm.densityDpi;//获取屏幕密度
		
		LogcatHelper.getInstance(this).start();
	
		data = new DataBase(this);		
		list = data.queryData("loginInfo");
		userId.initDropDownView(list,this);
		
		//存在历史账号则显示下拉按钮否则不显示
		if(list.size()>0){
			userId.showdropDownBtn();
		}else{
			userId.hidedropDownBtn();
		}	
		dropDownItem();
		dropDownDelete(this);
		hideSoftKeyBoard();
		
		userId.getEditText().setOnFocusChangeListener(this);
		password.getEditText().setOnFocusChangeListener(this);
	}
	
	@Override
	public void onClick(View arg0){
		if(password.getEditText().getText().length()!=0 && userId.getEditText().getText().length()!=0){
			data.insertData( userId.getEditText().getText().toString(),password.getEditText().getText().toString());
		}else{
			Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 Log.i("onTouchEvent", "hide....");//是点击控件外软键盘也能够隐藏
		 InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);  
		 return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);  
	}
	
	public void hideSoftKeyBoard(){    
	    DropdownView.HideSoftKeyBoard hide = new DropdownView.HideSoftKeyBoard() {			
			@Override
			public void hide() {
				InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			    mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
		};
		userId.setHideSoftKeyBoard(hide);
	}
	
	/**
	 * 实现DropdownView中的Item回调接口，并将这个接口对象返回给DropdownView*/	
	public void dropDownItem(){
		DropdownView.DropDownItem item = new DropDownItem() {			
			@Override
			public void onClick(int position) {
				//item点击回调4
				Log.i("LoginActivity", "item点击回调4");
				userId.getEditText().setText(list.get(position).getAccount());
				password.getEditText().setText(list.get(position).getPsw());
				
				userId.hideBtn();
				password.hideBtn();
			}
		};
		userId.setDropDownItem(item);
		userId.accountItem();
	}
	/**
	 * 实现dropDownDelete中Item中的删除按钮的回调接口，并将这个接口对象返回给DropdownView
	 */
	public void dropDownDelete(final Context context){
		DropdownView.DropDownDelete deleteAccount = new DropDownDelete() {			
			@Override
			public void onClick( final int position) {
				Log.i("LoginActivity", "click 点击回调4");
			    final MyDialog mydialog = new MyDialog(LoginActivity.this,R.style.MyDialog,"删除");
				mydialog.show();
						
				WindowManager m = getWindowManager();
				Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用//
				WindowManager.LayoutParams params = mydialog.getWindow().getAttributes();  
				params.height = (int) (d.getHeight() * 0.3);
				params.width = (int) (d.getWidth() * 0.75);  
				mydialog.getWindow().setAttributes(params);
				Window dialogWindow = mydialog.getWindow(); 
				dialogWindow.setGravity(Gravity.CENTER);
				mydialog.setListener(new OnClickListener() {					
					@Override
					public void onClick(View view) {
						if(view.getId() == R.id.sure){
							if(userId.getEditText().getText()!=null){
								if(list.get(position).getAccount().equals(
										userId.getEditText().getText().toString())){
									userId.getEditText().setText(null);
									password.getEditText().setText(null);
								}
							}
							list.remove(position);
							userId.hidePopup();							
							if(list.size()==0){
								userId.hidedropDownBtn();
							}
							mydialog.dismiss();
						}else if(view.getId() == R.id.cancel){
							mydialog.dismiss();
						}
					}
				});
			}
		};
		userId.setDropDownDelete(deleteAccount);
		userId.accountItemDelete();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		LogcatHelper.getInstance(this).stop();
	}

	/**
	 * EditText文本框焦点监听，当一个文本框获取焦点后立即隐藏前一个文本框中的删除按钮
	 * 当当前文本框的文本长度不为零时显示当前文本框的删除按钮*/
	@Override
	public void onFocusChange(View view, boolean flag) {
		if(view.getId()==R.id.dropdown_userId&&!userId.isIsdropDown()){
			if(userId.getEditText().getText().length()>0){
				userId.showBtn();
			}
			password.hideBtn();
		}else if(view.getId()==R.id.clearable_psw_et){
			if(password.getEditText().getText().length()>0){
				password.showBtn();
			}
			userId.hideBtn();
		}		
	}
}
