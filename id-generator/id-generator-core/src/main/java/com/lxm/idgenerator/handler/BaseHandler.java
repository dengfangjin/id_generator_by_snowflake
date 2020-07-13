package com.lxm.idgenerator.handler;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.service.bean.Id;
import com.lxm.idgenerator.util.BitUtil;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/25
 * @time 14:02
 */
public abstract class BaseHandler implements IdHandler {


    protected long sequence = Id.INIT_SEQUEUE;
    protected long lastTimestamp = Id.INIT_TIMESTAMP;

    public BaseHandler() {
        super();
    }

    public long handle(TimeHandler timer, Id id, IdMeta meta) {
        long timestamp = timer.getTime();
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            // 允许5ms内的误差
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timer.getTime();
                    timer.validateClockBackward(lastTimestamp, timestamp);
                } catch (Exception e) {
                    throw new RuntimeException("id生成失败: 系统时钟被回拨");
                }
            }
        }
        // 如果在同一时间单位，则序列号自增,同时判断是否超过阈值, 假如超过阈值则暂停直至下一秒
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & meta.getSeqBitsMask();
            if (sequence == Id.INIT_SEQUEUE) {
                timestamp = timer.nextTime(lastTimestamp);
            }
        } else {
            // 新的时间范围，序列号重置. 此处使用随机数为其初始化序列, 避免跨时间单位生成的首个id都是0
            sequence = ThreadLocalRandom.current().nextLong(1, 10);
        }
        lastTimestamp = timestamp;
        id.setSeq(sequence);
        id.setTime(timestamp);
        return BitUtil.id2long(id, meta);
    }

}
