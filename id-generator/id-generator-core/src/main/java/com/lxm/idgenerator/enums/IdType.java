package com.lxm.idgenerator.enums;

public enum IdType {
    SECOND("秒级"), MILLISECOND("毫秒级");

    private String desc;

    IdType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * val为true则返回秒级别,否则返回毫秒级别
     * @param val
     * @return
     */
    public static IdType parse(boolean val) {
        if (val) {
            return IdType.SECOND;
        } else {
            return IdType.MILLISECOND;
        }
    }
}
