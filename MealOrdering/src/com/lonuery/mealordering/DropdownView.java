   
package com.lonuery.mealordering;

import java.util.List;
import com.lonuery.mealordering.ViewAccount.AccountItemClickListener;
import com.lonuery.mealordering.ViewAccount.AccountItemDeleteClickListener;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**@author lonuery
 * @see 这是个自定义控件，这个控件的作用是控制用户名的输入，其他用户名的显示*/
public class DropdownView extends RelativeLayout{
	
	TextView delete;//EditText中文本删除按钮
	ImageButton dropDownBtn;//popupWindow显示/关闭按钮
	EditText userId;
	boolean isdropDown = false;//dropDownBtn被点击的标志位：ture表示dropDownBtn被第一次点击，

	//显示popupWindow.false表示dropDownBtn第二次被点击，关闭popupWindow
	private PopupWindow popupWindow;
	ViewAccount viewAccount;
	List<LoginInfo> list;
	
	Handler handler = new Handler();
	
	public DropdownView(Context context){
		super(context);
	}
	public DropdownView(Context context,AttributeSet attrs,int defStyle){
		super(context, attrs, defStyle);
	}
	
	public DropdownView(Context context,AttributeSet attrs){
		super(context, attrs);
		
		if (isInEditMode()){
        	return;
		}
		LayoutInflater.from(context).inflate(R.layout.login_dropdown, this);
		
		delete = (TextView)findViewById(R.id.delete_userId);
		dropDownBtn = (ImageButton)findViewById(R.id.togglebutton);
		userId = (EditText)findViewById(R.id.dropdown_userId);		
		userId.addTextChangedListener(tw);
		
		delete.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				hideBtn();
				userId.setText(null);
				userId.setHint("账号");
			}
		});
		
		dropDownBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(isdropDown){
					isdropDown=false;
				}else{
					isdropDown=true;
				}
				if(isdropDown){
					if(hide!=null){
						hide.hide();
					}
					handler.postDelayed(new Runnable() {						
						@Override
						public void run() {
							showPopup();
						}
					}, 100);//这样做的原因是:当软件盘打开时，如果显示popupwindow的同时将软键盘关闭，
					//那么popupwindowd的显示位置会和直接显示popupwindow的位置不一样，所以当显示popupwindow都进行延迟显示
					//目前还没有找到解决上面这个问题的方法，也不知道这个问题形成的原因。
					hideBtn();
				}else{
					hidePopup();
				}
			}
		});
	}
	
	//EditText的文本监听，以实现在EditText中的文本不为空时，控制删除文本按钮的显示
	TextWatcher tw = new TextWatcher(){	
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int start, int count, int after){
			
		}
		
		@Override
		public void afterTextChanged(Editable et){
			if(et.length()==0){
				hideBtn();
			}else{
				showBtn();
			}		
		}
	};
	
	public void showBtn(){
		delete.setVisibility(VISIBLE);
	}
	public void hideBtn(){
		delete.setVisibility(GONE);
	}
	
	public EditText getEditText(){
		return userId;
	}
	public boolean isIsdropDown() {
		return isdropDown;
	}
	public void showdropDownBtn(){
		dropDownBtn.setVisibility(VISIBLE);
	}
	public void hidedropDownBtn(){
		dropDownBtn.setVisibility(GONE);
	}
	
	public void showPopup(){
		dropDownBtn.setBackgroundResource(R.drawable.login_more_up);
		int height = Math.round(list.size()*(60*((float)LoginActivity.displayMetrics/160)));
		
		popupWindow = new PopupWindow(this, this.getWidth(),height);//popupWindow每次必须被创建，但删除Item时，popupWindow中的Item无法被刷新	
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);		
		popupWindow.setOutsideTouchable(true);
		popupWindow.setContentView(viewAccount);
		popupWindow.setOnDismissListener(new OnDismissListener() {			
			@Override
			public void onDismiss() {
				dropDownBtn.setBackgroundResource(R.drawable.login_more);
				isdropDown = false;
			}
		});
		userId.clearFocus();
		popupWindow.showAsDropDown(this);	
	}	
	public void hidePopup(){				
		if(popupWindow!=null && popupWindow.isShowing()){
			dropDownBtn.setBackgroundResource(R.drawable.login_more);
			popupWindow.dismiss();
			//isdropDown = false;
		}
	}
	
	//实现ViewAccount中的回调接口，同时将这个接口对象返回给ViewAccout,以便ViewAccount能够回调下面的onClick方法
	public void accountItem(){
		ViewAccount.AccountItemClickListener itemClick = new AccountItemClickListener() {			
			@Override
			public void onClick(int position){
				//item回调3
				Log.i("DropDownItem", "item点击回调3");
				if(item!=null){
					item.onClick(position);
					hidePopup();
				}		
			}
		};
		viewAccount.setAccountItemClickListener(itemClick);
	}
	
	//实现ViewAccount中的回调接口，同时将这个接口对象返回给ViewAccout,以便ViewAccount能够回调下面的onClick方法
	public void accountItemDelete(){
		ViewAccount.AccountItemDeleteClickListener itemClickDelete = new AccountItemDeleteClickListener() {		
			@Override
			public void onClick(int position) {	
				Log.i("deleteAccount", "click 点击回调3");
				if(deleteAccount!=null){
					Log.i("deleteAccount", "click 点击回调3+1");
					deleteAccount.onClick(position);
				}				
			}
		};
		viewAccount.setAccountItemDeleteClickListener(itemClickDelete);
	}
	
	/**
	 * 创建ViewAccount对象，这个方法是在Activity中被调用的，其实也可以直接在Activity中进行创建，
	 * 然后将viewAccount对象传回给DropdownView*/
	public void initDropDownView(List<LoginInfo> list,Context context){
		this.list = list;
		viewAccount = new ViewAccount(context, list);
	}
	
	/**
	 * Item的回调接口*/
	private DropDownItem item;
	public interface DropDownItem{
		public void onClick(int position);
	}
	public void setDropDownItem(DropDownItem item){
		this.item = item;
	}
	
	/**
	 * Item中的删除按钮回调接口*/
	private DropDownDelete deleteAccount;
	public interface DropDownDelete{
		public void onClick(int position);
	}
	public void setDropDownDelete(DropDownDelete deleteAccount){
		this.deleteAccount = deleteAccount;
	}
	
	/**
	 * 隐藏软键盘的回调接口*/
	public interface HideSoftKeyBoard{
		public void hide();
	}
	private HideSoftKeyBoard hide;
	public void setHideSoftKeyBoard(HideSoftKeyBoard hide){
		this.hide = hide;
	}
}
