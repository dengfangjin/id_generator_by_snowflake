package com.lxm.idgenerator.service.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/22
 * @time 17:07
 */
@Data
public class Id implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final long INIT_SEQUEUE = 0;
    public static final long INIT_TIMESTAMP = -1;

    private long fixed;

    private long time;

    private long datacenterId;

    private long workerId;

    private long seq;

    public Id() {
    }

    public Id(long time, long datacenterId, long workerId, long seq) {
        this.datacenterId = datacenterId;
        this.workerId = workerId;
        this.seq = seq;
        this.time = time;
        this.fixed = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("fixed=").append(fixed).append(",");
        sb.append("time=").append(time).append(",");
        sb.append("dataCenterId=").append(datacenterId).append(",");
        sb.append("workerId=").append(workerId).append(",");
        sb.append("seq=").append(seq).append("]");
        return sb.toString();
    }

}
