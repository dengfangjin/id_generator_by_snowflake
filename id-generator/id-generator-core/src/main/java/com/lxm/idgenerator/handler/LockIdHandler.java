package com.lxm.idgenerator.handler;

import com.lxm.idgenerator.bean.IdMeta;
import com.lxm.idgenerator.service.bean.Id;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁方式更新时间戳及序列
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/6/25
 * @time 14:23
 */
public class LockIdHandler extends BaseHandler {

    private Lock lock = new ReentrantLock();

    public LockIdHandler() {
        super();
    }

    @Override
    public long handle(TimeHandler timer, Id id, IdMeta meta) {
        lock.lock();
        try {
            return super.handle(timer, id, meta);
        } finally {
            lock.unlock();
        }
    }
}
