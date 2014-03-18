package com.lonuery.mealordering;

import java.util.List;
import com.lonuery.mealordering.AccountAdapter.BtnOnClickListener;
import com.lonuery.mealordering.AccountAdapter.ItemOnClickListener;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author lonuery
 * @see 这个类是初始化下拉选择框，配置好ListView的适配器，同时将其转化为一个View，以便在popupwindow中使用setContentView将其设置进去
 * @contact lonuery@gmail.com*/
public class ViewAccount extends LinearLayout{
	
	List<LoginInfo> list;
	Context context;
	ListView listView;
	AccountAdapter adapter;
	
	private AccountItemClickListener itemClickListener;
	private AccountItemDeleteClickListener itemDeleteClickListener;
	
	public ViewAccount(Context context,List<LoginInfo>list){
		super(context);
		this.context = context;
		this.list = list;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.popupwindow, this, true);
		listView = (ListView) findViewById(R.id.accountlist);
		adapter = new AccountAdapter(context,list);
		listView.setAdapter(adapter);
		
		/**将在ViewAccount中所实现的AccountAdapter类中接口的的对象，返回给AccountAdapter，
		        这样就能够实现利用此对象来调用ViewAccount中的方法，这样就实现了回调的效果*/
		adapter.setItemOnClickListener(setItemOnClickListener());
		adapter.setBtnOnclickListener(setBtnOnClickListener());
	}
	/**
	 * 实现AccountAdapter中的Item的回调接口，其回调的就是下面的onClick方法*/
	private AccountAdapter.ItemOnClickListener setItemOnClickListener(){
		AccountAdapter.ItemOnClickListener click = new ItemOnClickListener(){		
			@Override
			public void onClick(int position){
				//item点击回调2
				if(itemClickListener!=null){					
					itemClickListener.onClick(position);//回调DropdownView中的onClick方法				
				}
			}
		};
		return click;
	}
	/**
	 * 实现AccountAdapter中Item中的删除按钮的回调接口，其回调的就是下面的onClick方法*/
	private AccountAdapter.BtnOnClickListener setBtnOnClickListener(){
		AccountAdapter.BtnOnClickListener click = new BtnOnClickListener() {			
			@Override
			public void onClick(int position){
				Log.i("itemDeleteClickListener", "click 点击回调2");
				if(itemDeleteClickListener!=null){
					Log.i("itemDeleteClickListener", "click 点击回调2+1");
					itemDeleteClickListener.onClick(position);//回调DropdownView中的onClick方法
				}
			}
		};
		return click;
	}
	
	/**
	 * Item中的回调接口,回调的是自定义控件DropdownView的onClick方法*/
	public interface AccountItemClickListener{
		public void onClick(int position);
	}
	public void setAccountItemClickListener(AccountItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}
	
	/**
	 * Item中的删除按钮回调接口，回调的是自定义控件DropdownView的onClick方法*/
	public interface AccountItemDeleteClickListener{
		public void onClick(int position);
	}
	public void setAccountItemDeleteClickListener(AccountItemDeleteClickListener itemDeleteClickListener){
		this.itemDeleteClickListener = itemDeleteClickListener;
	}		
}
