package com.fomp.note.Enum;

public class EnumUtil {
    public static <T extends CodeEnum> String getByCode(Integer code, Class<T> t){
        for(T item: t.getEnumConstants()){
            if(item.getCode() == code){
                return item.getMsg();
            }
        }
        return "";
    }

    public static void main(String[] args) {
        String msg = EnumUtil.getByCode(CustomEnum.SUCCESS.getCode(), CustomEnum.class);
    }
}
