package com.lonuery.mealordering;  
   
import java.text.SimpleDateFormat;  
import java.util.Date;  
   
public class MyDate {  
    public static String getFileName() {  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String date = format.format(new Date(System.currentTimeMillis()));  
        return date;// 2012��10��03�� 23:41:31   
    }  
   
    public static String getDateEN() {  
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");  
        String date1 = format1.format(new Date(System.currentTimeMillis()));  
        return date1;// 2012-10-03 23:41:31   
    }  
    
    public static String getDateName(){
    	 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");  
         String date = format.format(new Date(System.currentTimeMillis()));  
         return date;// 2012-10-03 23:41:31 
    }
}  