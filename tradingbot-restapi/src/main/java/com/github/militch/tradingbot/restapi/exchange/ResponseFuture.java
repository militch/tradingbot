package com.github.militch.tradingbot.restapi.exchange;

import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class ResponseFuture {
    private final SteamRequest request;
    private StreamResponse response;
    private StreamError error;
    private final long timeout;
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    public boolean isDone() {
        return response != null;
    }
    public StreamResponse waitResponse() throws Exception {
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                while (! isDone()) {
                    cond.await(timeout, TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            if (! isDone()) {
                throw new Exception("timeout");
            }
        }
        return response;
    }

    public void process(StreamResponse response) {
        lock.lock();
        try {
            this.response = response;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
}
