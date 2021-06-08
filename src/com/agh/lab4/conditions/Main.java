//package com.agh.lab4.conditions;
//
//import java.lang.management.ManagementFactory;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Semaphore;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
//class PreferReadersCondition {
//    public static final ReentrantLock lock = new ReentrantLock(true);
//    public static final Condition readerCondition = lock.newCondition();
//    public static final Condition writerCondition = lock.newCondition();
//
//    static int readerCount = 0;
//
//    static int writersTime = 0;
//    static int readersTime = 0;
//
//    static class Read implements Runnable {
//        @Override
//        public void run() {
//            try {
//                readerSemaphore.acquire();
//                readerCount++;
//                if (readerCount == 1) writerSemaphore.acquire();
//                readerSemaphore.release();
//
//                System.out.println("Watek " + Thread.currentThread().getName() + " czyta");
//                Thread.sleep(10);
//                System.out.println("Watek " + Thread.currentThread().getName() + " zakonczyl czytanie");
//
//                readerSemaphore.acquire();
//                readerCount--;
//
//                long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//                readersTime += nanos;
//
//                if (readerCount == 0) writerSemaphore.release();
//                readerSemaphore.release();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    static class Write implements Runnable {
//        @Override
//        public void run() {
//            try {
//                writerSemaphore.acquire();
//                System.out.println("Watek " + Thread.currentThread().getName() + " pisze do zasobu");
//                Thread.sleep(10);
//                System.out.println("Watek " + Thread.currentThread().getName() + " zakonczyl pisanie do zasobu");
//
//                long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//                writersTime += nanos;
//
//                writerSemaphore.release();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        Read read = new Read();
//        Write write = new Write();
//
//        List<Thread> readers = new ArrayList();
//        List<Thread> writers = new ArrayList();
//
//        int readersCount = 100;
//        int writerCount = 10;
//
//        for (int i = 0; i < readersCount; i++) {
//            Thread t1 = new Thread(read);
//            t1.setName(i + " - Czytelnik");
//            readers.add(t1);
//        }
//
//        for (int i = 0; i < writerCount; i++) {
//            Thread t1 = new Thread(write);
//            t1.setName(i + " - Pisarz");
//            writers.add(t1);
//        }
//
//        for (Thread reader : readers) {
//            reader.start();
//        }
//
//        for (Thread writer : writers) {
//            writer.start();
//        }
//
//        Thread.sleep(2000); // delay
//
//        System.out.println("Pisarze: " + writerCount + " , Czytelnicy: " + readersCount);
//        System.out.println("Sredni czas pisarza: " + (writersTime / writerCount) / 1000 + "ms");
//        System.out.println("Sredni czas czytelnika: " + (readersTime / readersCount) / 1000 + "ms");
//    }
//}
//
//
//class PreferWriterCondition {
//
//    static int readerCount = 0;
//    static Semaphore x = new Semaphore(1);
//    static Semaphore z = new Semaphore(1);
//    static Semaphore readerSemaphore = new Semaphore(1);
//    static Semaphore writerSemaphore = new Semaphore(1);
//
//    static int writersTime = 0;
//    static int readersTime = 0;
//
//    static class Read implements Runnable {
//        @Override
//        public void run() {
//            try {
//                z.acquire();
//                readerSemaphore.acquire();
//                x.acquire();
//                readerCount++;
//                if (readerCount == 1) writerSemaphore.acquire();
//                x.release();
//
//                System.out.println("Thread " + Thread.currentThread().getName() + " czyta");
//                Thread.sleep(10);
//                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl czytanie");
//
//                x.acquire();
//                readerCount--;
//                if (readerCount == 0) writerSemaphore.release();
//
//                long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//                readersTime += nanos;
//
//                x.release();
//                readerSemaphore.release();
//                z.release();
//
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    static class Write implements Runnable {
//        @Override
//        public void run() {
//            try {
//                readerSemaphore.acquire();
//                writerSemaphore.acquire();
//                System.out.println("Thread " + Thread.currentThread().getName() + " pisze do zasobu");
//                Thread.sleep(10);
//                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl pisanie do zasobu");
//
//                long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//                writersTime += nanos;
//
//                writerSemaphore.release();
//                readerSemaphore.release();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        Read read = new Read();
//        Write write = new Write();
//
//        List<Thread> readers = new ArrayList();
//        List<Thread> writers = new ArrayList();
//
//        int readersCount = 100;
//        int writerCount = 10;
//
//        for (int i = 0; i < readersCount; i++) {
//            Thread t1 = new Thread(read);
//            t1.setName(i + " - Czytelnik");
//            readers.add(t1);
//        }
//
//        for (int i = 0; i < writerCount; i++) {
//            Thread t1 = new Thread(write);
//            t1.setName(i + " - Pisarz");
//            writers.add(t1);
//        }
//
//        for (Thread reader : readers) {
//            reader.start();
//        }
//
//        for (Thread writer : writers) {
//            writer.start();
//        }
//
//        Thread.sleep(2000); // delay
//
//        System.out.println("Pisarze: " + writerCount + " , Czytelnicy: " + readersCount);
//        System.out.println("Sredni czas pisarza: " + (writersTime / writerCount) / 1000 + "ms");
//        System.out.println("Sredni czas czytelnika: " + (readersTime / readersCount) / 1000 + "ms");
//    }
//}
//
//
//class FifoCondition {
//
//    static int readerCount = 0;
//    static Semaphore x = new Semaphore(1);
//    static Semaphore readerSemaphore = new Semaphore(1);
//    static Semaphore writerSemaphore = new Semaphore(1);
//
//    static int writersTime = 0;
//    static int readersTime = 0;
//
//    static class Read implements Runnable {
//        @Override
//        public void run() {
//            try {
//                readerSemaphore.acquire();
//                x.acquire();
//                readerCount++;
//                if (readerCount == 1) writerSemaphore.acquire();
//                x.release();
//
//                System.out.println("Thread " + Thread.currentThread().getName() + " czyta");
//                Thread.sleep(10);
//                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl czytanie");
//
//                x.acquire();
//                readerCount--;
//                if (readerCount == 0) writerSemaphore.release();
//
//                long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//                readersTime += nanos;
//
//                x.release();
//                readerSemaphore.release();
//
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    static class Write implements Runnable {
//        @Override
//        public void run() {
//            try {
//                readerSemaphore.acquire();
//                writerSemaphore.acquire();
//                System.out.println("Thread " + Thread.currentThread().getName() + " pisze do zasobu");
//                Thread.sleep(10);
//                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl pisanie do zasobu");
//
//                long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
//                writersTime += nanos;
//
//                writerSemaphore.release();
//                readerSemaphore.release();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        Read read = new Read();
//        Write write = new Write();
//
//        List<Thread> readers = new ArrayList();
//        List<Thread> writers = new ArrayList();
//
//        int readersCount = 100;
//        int writerCount = 10;
//
//        for (int i = 0; i < readersCount; i++) {
//            Thread t1 = new Thread(read);
//            t1.setName(i + " - Czytelnik");
//            readers.add(t1);
//        }
//
//        for (int i = 0; i < writerCount; i++) {
//            Thread t1 = new Thread(write);
//            t1.setName(i + " - Pisarz");
//            writers.add(t1);
//        }
//
//        for (Thread reader : readers) {
//            reader.start();
//        }
//
//        for (Thread writer : writers) {
//            writer.start();
//        }
//
//        Thread.sleep(2000); // delay
//
//        System.out.println("Pisarze: " + writerCount + " , Czytelnicy: " + readersCount);
//        System.out.println("Sredni czas pisarza: " + (writersTime / writerCount) / 1000 + "ms");
//        System.out.println("Sredni czas czytelnika: " + (readersTime / readersCount) / 1000 + "ms");
//    }
//}
