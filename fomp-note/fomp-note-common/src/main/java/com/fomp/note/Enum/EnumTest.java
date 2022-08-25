package com.fomp.note.Enum;

/**
 * @author: ymx
 * @date: 2022/8/3
 * @version: 1.0.0.0
 */
public enum EnumTest {
    SUCCESS("1","请求成功"),
    FAIL("0","请求失败");
    private String code;
    private String msg;

    EnumTest(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    public static boolean containsCode(String code){
        for(EnumTest item: EnumTest.class.getEnumConstants()){
            if(item.getCode() == code){
                return true;
            }
        }
        return false;
    }
    public static String getByCode(String code){
        for(EnumTest item: EnumTest.class.getEnumConstants()){
            if(item.getCode() == code){
                return item.getMsg();
            }
        }
        return "";
    }
}
