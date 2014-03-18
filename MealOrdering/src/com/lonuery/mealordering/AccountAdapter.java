package com.lonuery.mealordering;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter{
	
	List<String> picture;
	List<String> account;
	List<LoginInfo> loginInfo;
	Context context;
	ItemOnClickListener itemClickListener;
	BtnOnClickListener btnClickListener;
	
	public AccountAdapter(Context context,List<LoginInfo> loginInfo){
		this.loginInfo = loginInfo;
		this.context = context;
	}
	
	@Override
	public int getCount(){
		return loginInfo.size();
	}
	@Override
	public Object getItem(int position) {
		return loginInfo.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void setList(List<LoginInfo> list){
		this.loginInfo = list;
	}
	
	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		RelativeLayout ll = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.account_item, null);
		Log.i("getView", "position:"+position);
		if(contentView==null){
			contentView = ll;
			
			ImageView head = (ImageView)contentView.findViewById(R.id.account_item_head);
			TextView name = (TextView)contentView.findViewById(R.id.account_item_name);
			ImageButton btn = (ImageButton)contentView.findViewById(R.id.account_item_delete);
			
			head.setImageResource(R.drawable.h001);
			name.setText(loginInfo.get(position).getAccount());
			btn.setImageResource(R.drawable.unipay_tenpay_del);			
			
			ViewHolder holder = new ViewHolder();
			holder.setHead(head);
			holder.setName(name);
			holder.setBtn(btn);
			
			btn.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					Log.i("btnClickListener", "click ����ص�1");
					if(btnClickListener!=null){
						Log.i("btnClickListener", "click ����ص�1+1");
						btnClickListener.onClick(position);
					}
				}
			});
			contentView.setTag(holder);
		}else{
			ViewHolder hold = (ViewHolder)contentView.getTag();
			hold.getHead().setImageResource(R.drawable.h001);
			hold.getName().setText(loginInfo.get(position).getAccount());
			hold.getBtn().setImageResource(R.drawable.unipay_tenpay_del);
			
			hold.getBtn().setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View arg0) {
					Log.i("btnClickListener", "click ����ص�1");
					if(btnClickListener!=null){
						Log.i("btnClickListener", "click ����ص�1+1");
						btnClickListener.onClick(position);//�ص�ViewAccount�е�onClick����
					}										
				}
			});
		}
		contentView.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Log.i("ItemOnClickListener", "item ����ص�1");
				itemClickListener.onClick(position);//�ص�ViewAccount�е�onClick����
				//item ����ص�1
			}
		});
		return contentView;
	}
	
	/**
	 * item �ص��ӿڣ���Item�����ʱ�����ViewAccount��ʵ���˴˽ӿڣ���ô�ͻ�ص�ViewAccount�е�onClick����*/
	public interface ItemOnClickListener{
		public void onClick(int position);
	}
	public void setItemOnClickListener(ItemOnClickListener itemClickListener){
		this.itemClickListener = itemClickListener;
	}
	
	/**
	 * item�е�ɾ����ť�ص��ӿڣ����ViewAccount��ʵ���˴˽ӿڣ���ô�ͻ�ص�ViewAccount�е�onClick����*/
	public interface BtnOnClickListener{
		public void onClick(int position);
	}
	public void setBtnOnclickListener(BtnOnClickListener onClickListener){
		this.btnClickListener = onClickListener;				
	}
	
	
	class ViewHolder{
		ImageView head;
		TextView name;
		ImageButton btn;
		
		public ImageView getHead() {
			return head;
		}
		public void setHead(ImageView head) {
			this.head = head;
		}
		public TextView getName() {
			return name;
		}
		public void setName(TextView name) {
			this.name = name;
		}
		public ImageButton getBtn() {
			return btn;
		}
		public void setBtn(ImageButton btn) {
			this.btn = btn;
		}	
	}
}
