package com.zmk.github.utils;

/**
 * @Author zmk
 * @Date: 2020/09/11/ 14:15
 * @Description
 */
public class SnowflakeUtils {
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private static SnowflakeUtils idWorker;

    static {
        try {
            long datacenterId = (int) (1 + Math.random() * (30 - 1 + 1));
            long serverId = System.nanoTime() % 21L + (int) (1 + Math.random() * (10 - 1 + 1));
            idWorker = new SnowflakeUtils(serverId, datacenterId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public SnowflakeUtils(long workerId, long datacenterId) {
        if (workerId <= 31L && workerId >= 0L) {
            if (datacenterId <= 31L && datacenterId >= 0L) {
                this.workerId = workerId;
                this.datacenterId = datacenterId;
            } else {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", 31L));
            }
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", 31L));
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & 4095L;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            return timestamp - 1420041600000L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
        }
    }

    public synchronized String nextID64() {
        return NumericConvertUtils.toOtherNumberSystem(this.nextId(), 62);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp=this.timeGen();
        while (timestamp<=lastTimestamp){
            timestamp=this.timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static long nextLongId() {
        return idWorker.nextId();
    }

    public static String next64Id() {
        return idWorker.nextID64();
    }

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 100; ++i) {
            System.out.println("---------------");
            System.out.println((int) (1 + Math.random() * (30 - 1 + 1)));
            System.out.println(System.nanoTime()%21+ (int) (1 + Math.random() * (10 - 1 + 1)));
            System.out.println("---------------");
        }
    }
}

