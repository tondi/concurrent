package com.agh;

public class Race {
    private static void exec() throws InterruptedException {
        Counter cnt = new Counter(0);


        IThread iThread = new IThread(cnt);

        DThread dThread = new DThread(cnt);
        iThread.start();
        dThread.start();

        iThread.join();
        dThread.join();

        System.out.println(cnt.value());
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            exec();
        }
    }
}
