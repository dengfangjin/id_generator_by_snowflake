package com.lxm.idgenerator.handler;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.bean.MillisecondIdMeta;
import com.lxm.idgenerator.bean.SecondIdMeta;
import com.lxm.idgenerator.enums.IdType;
import com.lxm.idgenerator.util.BitUtil;
import com.lxm.idgenerator.util.SysTimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class TimeHandler {
    /**
     * 默认起始时间戳: 2018-01-01 00:00:00
     */
    public static long INIT_TIMESTAMP = 1514736000000L;

    /**
     * 可以使用的最大年限阈值，该值由{@link IdMeta}的timeBits决定
     */
    protected long maxTime;

    protected IdMeta idMeta;

    public TimeHandler(IdMeta meta) {
        this.idMeta = meta;
        // 计算可以使用的最大年限阈值
        this.maxTime = BitUtil.max(this.idMeta.getTimeBits());
    }

    /**
     * 校验系统时间是否被回拨
     * @param lastTimestamp 上次生成id的时间
     * @param currentTimestamp 本次要生成id的时间
     * @exception RuntimeException lastTimestamp > currentTimestamp时抛异常
     */
    public void validateClockBackward(long lastTimestamp, long currentTimestamp) {
        if (currentTimestamp < lastTimestamp) {
            String errMsg = String.format("id生成失败: 系统时钟被回拨. current timestamp=[%s] < last timestamp=[%s]",
                    currentTimestamp, lastTimestamp);
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * 获取下一个时间点
     * @param lastTimestamp 已此为基准,获取它之后的下一个时间点
     * @return 下一个时间点的时间戳
     * @exception RuntimeException 如果秒数已经达到所能达到的阈值，将会抛出运行时异常。
     * 阈值由 {@link #maxTime} 决定。
     */
    public long nextTime(long lastTimestamp) {
        long timestamp = getTime();
        while (timestamp <= lastTimestamp) {
            timestamp = getTime();
        }
        return timestamp;
    }


    /**
     * 获取当前时间距离{@link #INIT_TIMESTAMP }的秒数
     * @return 秒数
     * @exception  RuntimeException 如果秒数已经达到所能达到的阈值，将会抛出运行时异常。
     * 阈值由 {@link #maxTime} 决定。
     */
    public long getTime() {
        long timestamp = 0L;
        if (idMeta.getType() == IdType.SECOND) {
            timestamp = (SysTimeUtil.currentTimeMillis()- INIT_TIMESTAMP) / 1000;
        } else {
            timestamp = (SysTimeUtil.currentTimeMillis() - INIT_TIMESTAMP);
        }
        if (timestamp >= maxTime) {
            String errMsg = String.format("id生成失败：已达到年限阈值(%s >= %s). 请重置起始时间戳",
                    timestamp, maxTime);
            log.error(errMsg);
            throw new RuntimeException(errMsg);
        }
        return timestamp;
    }

    public Date transTime(long time) {
        if (this.idMeta instanceof MillisecondIdMeta) {
            return new Date(time + INIT_TIMESTAMP);
        } else {
            return new Date(time * 1000 + INIT_TIMESTAMP);
        }
    }
}
