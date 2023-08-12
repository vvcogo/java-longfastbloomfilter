package io.github.vvcogo.longfastbloomfilter.framework.threadpool;

import java.util.List;

public class WorkerThread implements Runnable{

    private String msg;
    private List<?> jobs;

    public WorkerThread(String s){
        this.msg = s;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start: " + msg);
        process();
        System.out.println(Thread.currentThread().getName() + " end");
    }

    private void process(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
