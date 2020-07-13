package com.lxm.idgenerator.bean;

import com.lxm.idgenerator.enums.IdType;
import com.lxm.idgenerator.util.BitUtil;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/26
 * @time 9:34
 */
public class SecondIdMeta extends IdMeta {

    public SecondIdMeta() {
        super((byte) 31, (byte) 5, (byte) 5, (byte) 22);
        this.type = IdType.SECOND;
    }

}
