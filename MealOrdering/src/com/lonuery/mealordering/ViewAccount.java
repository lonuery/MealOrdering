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
 * @see ������ǳ�ʼ������ѡ������ú�ListView����������ͬʱ����ת��Ϊһ��View���Ա���popupwindow��ʹ��setContentView�������ý�ȥ
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
		
		/**����ViewAccount����ʵ�ֵ�AccountAdapter���нӿڵĵĶ��󣬷��ظ�AccountAdapter��
		        �������ܹ�ʵ�����ô˶���������ViewAccount�еķ�����������ʵ���˻ص���Ч��*/
		adapter.setItemOnClickListener(setItemOnClickListener());
		adapter.setBtnOnclickListener(setBtnOnClickListener());
	}
	/**
	 * ʵ��AccountAdapter�е�Item�Ļص��ӿڣ���ص��ľ��������onClick����*/
	private AccountAdapter.ItemOnClickListener setItemOnClickListener(){
		AccountAdapter.ItemOnClickListener click = new ItemOnClickListener(){		
			@Override
			public void onClick(int position){
				//item����ص�2
				if(itemClickListener!=null){					
					itemClickListener.onClick(position);//�ص�DropdownView�е�onClick����				
				}
			}
		};
		return click;
	}
	/**
	 * ʵ��AccountAdapter��Item�е�ɾ����ť�Ļص��ӿڣ���ص��ľ��������onClick����*/
	private AccountAdapter.BtnOnClickListener setBtnOnClickListener(){
		AccountAdapter.BtnOnClickListener click = new BtnOnClickListener() {			
			@Override
			public void onClick(int position){
				Log.i("itemDeleteClickListener", "click ����ص�2");
				if(itemDeleteClickListener!=null){
					Log.i("itemDeleteClickListener", "click ����ص�2+1");
					itemDeleteClickListener.onClick(position);//�ص�DropdownView�е�onClick����
				}
			}
		};
		return click;
	}
	
	/**
	 * Item�еĻص��ӿ�,�ص������Զ���ؼ�DropdownView��onClick����*/
	public interface AccountItemClickListener{
		public void onClick(int position);
	}
	public void setAccountItemClickListener(AccountItemClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}
	
	/**
	 * Item�е�ɾ����ť�ص��ӿڣ��ص������Զ���ؼ�DropdownView��onClick����*/
	public interface AccountItemDeleteClickListener{
		public void onClick(int position);
	}
	public void setAccountItemDeleteClickListener(AccountItemDeleteClickListener itemDeleteClickListener){
		this.itemDeleteClickListener = itemDeleteClickListener;
	}		
}
