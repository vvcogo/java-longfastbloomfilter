package io.github.vvcogo.longfastbloomfilter.framework.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadPool {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(4); //4 threads in the pool

        for (int i = 0; i < 10; i++){ //corre para 10 tarefas
            Runnable rw = new WorkerThread(""+i);
            exec.execute(rw);
        }
        exec.shutdown();
        System.out.println("end all");
    }
}
