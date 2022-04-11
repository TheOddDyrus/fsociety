package com.thomax.letsgo.zoom.excel.constant;

/**
 * Excel导入时校验的正则表达式
 */
public class ExcelRegx {

     /*--------------------------------------------
     |                  数值类型                 |
     ============================================*/

    //自增ID
    private static final String ID = "^[1-9]+[0-9]{0,n}$";

    //自增ID，最大长度3
    public static final String ID_3 = ID.replace("n", "3");

    //自增ID，最大长度11
    public static final String ID_11 = ID.replace("n", "11");

    //自增ID，最大长度20
    public static final String ID_20 = ID.replace("n", "20");

    /*--------------------------------------------
     |                  浮点类型                 |
     ============================================*/

    //浮点
    private static final String DECIMAL = "^([1-9]+[0-9]{0,m}*)+(.[0-9]{1,n})?$";

    //浮点，整数长度最大15，浮点长度最大3
    public static final String DECIMAL_15_3 = DECIMAL.replace("m", "15").replace("n", "3");

    /*--------------------------------------------
     |                  字符类型                 |
     ============================================*/

    //任意字符串
    private static final String STR = "^.{1,n}$";

    //任意字符串，最大长度100
    public static final String STR_100 = STR.replace("n", "100");

    //机场三字码
    public static final String AIRPORT_CODE = "^[A-Z]{3}$";

    /*--------------------------------------------
     |                  日期类型                 |
     ============================================*/

    //日期
    public static final String DATE = "^([0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30))))$";

    //时间
    public static final String TIME = "^((([1-9]{1})|([0-1][0-9])|([1-2][0-3])):([0-5][0-9]):([0-5][0-9]))$";

    //日期时间
    public static final String DATE_TIME = DATE.replace("$", "") + "\\s" + TIME.replace("^", "");

}
