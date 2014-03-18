package com.lonuery.mealordering;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClearableEditText extends RelativeLayout{

	public EditText editText;
	public TextView clearTv;

	public ClearableEditText(Context context){
		super(context);
	}
	public ClearableEditText(Context context,AttributeSet attrs,int defStyle){
		super(context, attrs, defStyle);
	}
	
	public ClearableEditText(Context context,AttributeSet attrs){
		super(context, attrs);
		init(context);
	}	
	
	public void init(Context context){
		LayoutInflater.from(context).inflate(R.layout.clearable_edit_text, this);	
		editText = (EditText)findViewById(R.id.clearable_psw_et);
		clearTv = (TextView)findViewById(R.id.clearable_delete_psw);
		
		editText.addTextChangedListener(tw);
		
		clearTv.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				hideBtn();
				editText.setText(null);
				editText.setHint("√‹¬Î");
			}
		});
	}
	
	TextWatcher tw = new TextWatcher(){
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0,int start, int count, int after) {		

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
		clearTv.setVisibility(VISIBLE);
	}
	public void hideBtn(){
		clearTv.setVisibility(GONE);
	}
	
	public EditText getEditText(){
		return editText;
	}
	public TextView getImageButton(){
		return clearTv;
	}
}
