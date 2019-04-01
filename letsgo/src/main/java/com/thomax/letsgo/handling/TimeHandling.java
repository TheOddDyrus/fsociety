package com.thomax.letsgo.handling;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * SimpleDateFormat所有格式：
	G 年代标志符
	y 年
	M 月
	d 日
	h 时 在上午或下午 (1~12)
	H 时 在一天中 (0~23)
	m 分
	s 秒
	S 毫秒
	E 星期
	D 一年中的第几天
	F 一月中第几个星期几
	w 一年中第几个星期
	W 一月中第几个星期
	a 上午 / 下午 标记符
	k 时 在一天中 (1~24)
	K 时 在上午或下午 (0~11)
	z 时区

  java.util.Date与java.sql.Date互相转换：
	① java.sql.Date sqlDate = new java.sql.Date((new java.util.Date()).getTime());
	② java.sql.Time sqlTime = new java.sql.Time((new java.util.Date()).getTime());
	③ java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp((new java.util.Date()).getTime());
	④ 像mysql内的DateTime或Year类型，利用SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS")来格式化得到
 */
public class TimeHandling {
	public static void main(String[] args) {
		Calendar ca = Calendar.getInstance();
		System.out.println(ca.getTime());
	}
}

/**class1*/  //java.text.SimpleDateFormat; java.util.Date;
class FormatTime{ 
    public static void main(String[] args){
    	Date date = new Date();
    	System.out.println(date); //Data类获取具体数值的日期时间
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(); //new SimpleDateFormat()
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");  //请求模型 SimpleDateFormat.applyPattern(String format)
        System.out.println(sdf.format(date));
    	
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf2 = new SimpleDateFormat(strDateFormat); //new SimpleDateFormat(strFormat)
        System.out.println(sdf2.format(new Date())); //SimpleDateFormat.format(new Date())
    }
}
//Tue Mar 13 17:03:50 CST 2018
//2018-03-13 17:03:50 下午
//2018-03-13 17:03:50


/**class2-获取年份、月份等*/  
//java.util.Calendar
class GetYearMonth {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
 
        System.out.println("当期时间: " + cal.getTime());
        System.out.println("日期: " + cal.get(Calendar.DATE));
        System.out.println("月份: " + (cal.get(Calendar.MONTH) + 1)); //月份要+1
        System.out.println("年份: " + cal.get(Calendar.YEAR));
        System.out.println("一周的第几天: " + cal.get(Calendar.DAY_OF_WEEK));  // 星期日为一周的第一天输出为1，Tue输出为3
        System.out.println("一月中的第几天: " + cal.get(Calendar.DAY_OF_MONTH));
        System.out.println("一年的第几天: " + cal.get(Calendar.DAY_OF_YEAR));
    }
}
//当期时间: Tue Mar 13 17:10:56 CST 2018
//日期: 13
//月份: 3
//年份: 2018
//一周的第几天: 3
//一月中的第几天: 13
//一年的第几天: 72


class TimeStamp{
    public static void main(String[] args){
        Long timeStamp = System.currentTimeMillis();  //获取当前时间戳（毫秒）
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        
        System.out.println("String: " + String.valueOf(timeStamp));
        System.out.println("Long: " + Long.parseLong(String.valueOf(timeStamp)));
        
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));   // 时间戳转换成时间
        System.out.println(sd);
        
        Date date = new Date(timeStamp + 1000*3600*24); // 一天整的毫秒数 = 1000*3600*24
        System.out.println("明天: " + sdf.format(date));
   }
}
//String: 1520933370232
//Long: 1520933370232
//2018-03-13
//明天: 2018-03-14













