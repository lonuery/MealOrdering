   
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
 * @see ���Ǹ��Զ���ؼ�������ؼ��������ǿ����û��������룬�����û�������ʾ*/
public class DropdownView extends RelativeLayout{
	
	TextView delete;//EditText���ı�ɾ����ť
	ImageButton dropDownBtn;//popupWindow��ʾ/�رհ�ť
	EditText userId;
	boolean isdropDown = false;//dropDownBtn������ı�־λ��ture��ʾdropDownBtn����һ�ε����

	//��ʾpopupWindow.false��ʾdropDownBtn�ڶ��α�������ر�popupWindow
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
				userId.setHint("�˺�");
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
					}, 100);//��������ԭ����:������̴�ʱ�������ʾpopupwindow��ͬʱ������̹رգ�
					//��ôpopupwindowd����ʾλ�û��ֱ����ʾpopupwindow��λ�ò�һ�������Ե���ʾpopupwindow�������ӳ���ʾ
					//Ŀǰ��û���ҵ���������������ķ�����Ҳ��֪����������γɵ�ԭ��
					hideBtn();
				}else{
					hidePopup();
				}
			}
		});
	}
	
	//EditText���ı���������ʵ����EditText�е��ı���Ϊ��ʱ������ɾ���ı���ť����ʾ
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
		
		popupWindow = new PopupWindow(this, this.getWidth(),height);//popupWindowÿ�α��뱻��������ɾ��Itemʱ��popupWindow�е�Item�޷���ˢ��	
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
	
	//ʵ��ViewAccount�еĻص��ӿڣ�ͬʱ������ӿڶ��󷵻ظ�ViewAccout,�Ա�ViewAccount�ܹ��ص������onClick����
	public void accountItem(){
		ViewAccount.AccountItemClickListener itemClick = new AccountItemClickListener() {			
			@Override
			public void onClick(int position){
				//item�ص�3
				Log.i("DropDownItem", "item����ص�3");
				if(item!=null){
					item.onClick(position);
					hidePopup();
				}		
			}
		};
		viewAccount.setAccountItemClickListener(itemClick);
	}
	
	//ʵ��ViewAccount�еĻص��ӿڣ�ͬʱ������ӿڶ��󷵻ظ�ViewAccout,�Ա�ViewAccount�ܹ��ص������onClick����
	public void accountItemDelete(){
		ViewAccount.AccountItemDeleteClickListener itemClickDelete = new AccountItemDeleteClickListener() {		
			@Override
			public void onClick(int position) {	
				Log.i("deleteAccount", "click ����ص�3");
				if(deleteAccount!=null){
					Log.i("deleteAccount", "click ����ص�3+1");
					deleteAccount.onClick(position);
				}				
			}
		};
		viewAccount.setAccountItemDeleteClickListener(itemClickDelete);
	}
	
	/**
	 * ����ViewAccount���������������Activity�б����õģ���ʵҲ����ֱ����Activity�н��д�����
	 * Ȼ��viewAccount���󴫻ظ�DropdownView*/
	public void initDropDownView(List<LoginInfo> list,Context context){
		this.list = list;
		viewAccount = new ViewAccount(context, list);
	}
	
	/**
	 * Item�Ļص��ӿ�*/
	private DropDownItem item;
	public interface DropDownItem{
		public void onClick(int position);
	}
	public void setDropDownItem(DropDownItem item){
		this.item = item;
	}
	
	/**
	 * Item�е�ɾ����ť�ص��ӿ�*/
	private DropDownDelete deleteAccount;
	public interface DropDownDelete{
		public void onClick(int position);
	}
	public void setDropDownDelete(DropDownDelete deleteAccount){
		this.deleteAccount = deleteAccount;
	}
	
	/**
	 * ��������̵Ļص��ӿ�*/
	public interface HideSoftKeyBoard{
		public void hide();
	}
	private HideSoftKeyBoard hide;
	public void setHideSoftKeyBoard(HideSoftKeyBoard hide){
		this.hide = hide;
	}
}
