package com.fomp.note.Enum;

public enum CustomEnum implements CodeEnum {
    
    SUCCESS(1,"请求成功"),
    
    FAIL(0,"请求失败")
    ;

    private Integer code;
    private String msg;

    CustomEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
