package com.lxm.idgenerator.util;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.service.bean.Id;

/**
 * 位运算工具
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/10/25
 * @time 16:19
 */
public class BitUtil {
    /**
     * 返回bit位数能够表达的最大数字
     * @param byteCount 数字占用的bit位数
     * @return 返回由<code>byteCount</code>指定的bit位数能够表达的最大数字
     */
    public static long max(int byteCount) {
        return (1L << byteCount) -1;
    }

    /**
     * 返回bit位数对应的掩码
     * @param byteCount 占了几个bit的掩码
     * @return 返回由<code>byteCount</code>指定长度的掩码
     */
    public static long mask(int byteCount) {
        return -1L ^ (-1L << byteCount);
    }

    /**
     * 将{@link Id}转成long型
     * @param id
     * @param meta
     * @return
     */
    public static long id2long(Id id, IdMeta meta) {

        long ret = 0;

        ret |= id.getSeq();

        ret |= id.getWorkerId() << meta.getWorkerBitsIndex();

        ret |= id.getDatacenterId() << meta.getDataCenterBitsIndex();

        ret |= id.getTime() << meta.getTimeBitsIndex();

        ret |= id.getFixed() << meta.getFixedBitsIndex();

        return ret;
    }

    /**
     * 将long型转成{@link Id}
     * @param id
     * @param meta
     * @return
     */
    public static Id long2Id(long id, IdMeta meta) {
        Id ret = new Id();

        ret.setSeq(id & meta.getSeqBitsMask());

        ret.setDatacenterId((id >>> meta.getDataCenterBitsIndex()) & meta.getDataCenterBitsMask());

        ret.setWorkerId((id >>> meta.getWorkerBitsIndex()) & meta.getWorkerBitsMask());

        ret.setTime((id >>> meta.getTimeBitsIndex()) & meta.getTimeBitsMask());

        ret.setFixed((id >>> meta.getFixedBitsIndex()) & meta.getFixedBitsMask());

        return ret;
    }
}
