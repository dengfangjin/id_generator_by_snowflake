package com.lxm.idgenerator.bean;

import com.lxm.idgenerator.enums.IdType;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/26
 * @time 9:45
 */
public class MillisecondIdMeta extends IdMeta {

    public MillisecondIdMeta() {
        super((byte) 41, (byte) 5, (byte) 5, (byte) 12);
        this.type = IdType.MILLISECOND;
    }
}
