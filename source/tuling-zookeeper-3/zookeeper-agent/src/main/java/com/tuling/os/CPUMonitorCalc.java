package com.tuling.os;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;

public class CPUMonitorCalc {

    private static CPUMonitorCalc instance = new CPUMonitorCalc();

    private OperatingSystemMXBean osMxBean;
    private ThreadMXBean threadBean;
    private long preTime = System.nanoTime();
    private long preUsedTime = 0;

    private CPUMonitorCalc() {
        //返回Java虚拟机的线程系统的托管bean。
        osMxBean = ManagementFactory.getOperatingSystemMXBean();
        //返回运行Java虚拟机的操作系统的托管bean。
        threadBean = ManagementFactory.getThreadMXBean();
    }

    public static CPUMonitorCalc getInstance() {
        return instance;
    }

    public double getProcessCpu() {
        long totalTime = 0;
        for (long id : threadBean.getAllThreadIds()) {
            //返回指定ID的线程的总CPU时间(纳米)
            totalTime += threadBean.getThreadCpuTime(id);
        }
        long curtime = System.nanoTime();
        long usedTime = totalTime - preUsedTime;
        long totalPassedTime = curtime - preTime;
        preTime = curtime;
        preUsedTime = totalTime;
        return (((double) usedTime) / totalPassedTime / osMxBean.getAvailableProcessors()) * 100;
    }
}