package com.liangzhicheng.common.utils;

/**
 * 雪花算法工具类；
 *              分布式环境全局唯一id算法，
 *              SnowFlake的优点，整体上按照时间自增排序，
 *              整个分布式系统内不会产生id碰撞(由数据中心id和机器id作区分)，效率较高
 *              经测试，SnowFlake每秒能够产生26万ID左右(测试上传)
 * @author liangzhicheng
 */
public class SnowFlakeUtil {
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
	//开始时间戳
    private long twepoch = 1288834974657L;
	//机器id所占的位数
    private long workerIdBits = 5L;
	//数据标识id所占的位数
    private long datacenterIdBits = 5L;
	//支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
	//支持的最少数据标识id, 结果是31
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	//序列在id中占的位数
    private long sequenceBits = 12L;
	//机器ID向左移12位
    private long workerIdShift = sequenceBits;
	//工作机器ID(0~31)
    private long datacenterIdShift = sequenceBits + workerIdBits;
	//数据中心ID(0~31)
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	//毫秒内序列(0~4095)
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
	//上传生成ID的时间戳
    private long lastTimestamp = -1L;

    private static class IdGenHolder {
        private static final SnowFlakeUtil instance = new SnowFlakeUtil();
    }

    public static SnowFlakeUtil get() {
        return IdGenHolder.instance;
    }

    public SnowFlakeUtil() {
        this(0L, 0L);
    }

    public SnowFlakeUtil(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift)
                | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

}
