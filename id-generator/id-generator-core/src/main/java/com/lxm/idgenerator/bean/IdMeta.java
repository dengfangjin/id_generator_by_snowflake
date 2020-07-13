package com.lxm.idgenerator.bean;

import com.lxm.idgenerator.enums.IdType;
import com.lxm.idgenerator.util.BitUtil;

import java.util.List;

/**
 * Id对象元数据, 描述Id的数据结构
 * Id统一8字节(64位)
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/25
 * @time 11:27
 */
public abstract class IdMeta {

    /**
     * 序列bit位
     */
    protected byte seqBits = 0;

    /**
     * 机器码bit位
     */
    protected byte workerBits = 0;

    /**
     * 数据中心bit位
     */
    protected byte dataCenterBits = 0;

    /**
     * 时间戳bit位
     */
    protected byte timeBits = 0;

    /**
     * 固定位
     */
    protected byte fixedBits = 0;

    /**
     * Id类型
     */
    protected IdType type;

    public IdMeta(byte timeBits, byte dataCenterBits, byte workerBits, byte seqBits) {
        this.timeBits = timeBits;
        this.dataCenterBits = dataCenterBits;
        this.workerBits = workerBits;
        this.seqBits = seqBits;
    }

    /**
     * 返回时间戳掩码
     * @return
     */
    public long getTimeBitsMask() {
        return BitUtil.mask(timeBits);
    }

    /**
     * 返回数据中心掩码
     * @return
     */
    public long getDataCenterBitsMask() {
        return BitUtil.mask(dataCenterBits);
    }

    /**
     * 返回机器码掩码
     * @return
     */
    public long getWorkerBitsMask() {
        return BitUtil.mask(workerBits);
    }

    /**
     * 返回序列号掩码
     * @return
     */
    public long getSeqBitsMask() {
        return BitUtil.mask(seqBits);
    }

    /**
     * 返回固定位掩码
     * @return
     */
    public long getFixedBitsMask() {
        return BitUtil.mask(fixedBits);
    }

    /**
     * 返回机器码的起始位置
     * @return
     */
    public long getWorkerBitsIndex() {
        return seqBits;
    }

    /**
     * 返回数据中心起始位置
     * @return
     */
    public long getDataCenterBitsIndex() {
        return getWorkerBitsIndex() + workerBits;
    }

    /**
     * 返回序列码的起始位置
     * @return
     */
    public long getTimeBitsIndex() {
        return getDataCenterBitsIndex() + dataCenterBits;
    }

    /**
     * 固定位的起始位置
     * @return
     */
    public long getFixedBitsIndex() {
        return getTimeBitsIndex() + timeBits;
    }

    public byte getFixedBits() {
        return fixedBits;
    }

    public byte getTimeBits() {
        return timeBits;
    }

    public byte getWorkerBits() {
        return workerBits;
    }

    public byte getDatacenterBits() {
        return dataCenterBits;
    }

    public IdType getType() {
        return type;
    }

}
