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
	public static int displayMetrics;//��Ļ�ܶ�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_loginpage);
		
		password = (ClearableEditText)findViewById(R.id.password);
		userId = (DropdownView)findViewById(R.id.userId);
		
		btnLogin = (Button)findViewById(R.id.login);
		btnLogin.setOnClickListener(this);
		DisplayMetrics dm =getResources().getDisplayMetrics();
		displayMetrics = dm.densityDpi;//��ȡ��Ļ�ܶ�
		
		LogcatHelper.getInstance(this).start();
	
		data = new DataBase(this);		
		list = data.queryData("loginInfo");
		userId.initDropDownView(list,this);
		
		//������ʷ�˺�����ʾ������ť������ʾ
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
			Toast.makeText(this, "�˺Ż����벻��Ϊ��", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 Log.i("onTouchEvent", "hide....");//�ǵ���ؼ��������Ҳ�ܹ�����
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
	 * ʵ��DropdownView�е�Item�ص��ӿڣ���������ӿڶ��󷵻ظ�DropdownView*/	
	public void dropDownItem(){
		DropdownView.DropDownItem item = new DropDownItem() {			
			@Override
			public void onClick(int position) {
				//item����ص�4
				Log.i("LoginActivity", "item����ص�4");
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
	 * ʵ��dropDownDelete��Item�е�ɾ����ť�Ļص��ӿڣ���������ӿڶ��󷵻ظ�DropdownView
	 */
	public void dropDownDelete(final Context context){
		DropdownView.DropDownDelete deleteAccount = new DropDownDelete() {			
			@Override
			public void onClick( final int position) {
				Log.i("LoginActivity", "click ����ص�4");
			    final MyDialog mydialog = new MyDialog(LoginActivity.this,R.style.MyDialog,"ɾ��");
				mydialog.show();
						
				WindowManager m = getWindowManager();
				Display d = m.getDefaultDisplay(); // ��ȡ��Ļ������//
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
	 * EditText�ı��򽹵��������һ���ı����ȡ�������������ǰһ���ı����е�ɾ����ť
	 * ����ǰ�ı�����ı����Ȳ�Ϊ��ʱ��ʾ��ǰ�ı����ɾ����ť*/
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
