package com.agh.lab4.semaphores;

import java.io.File;
import java.io.FileWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.lang.System.nanoTime;

class PreferReadersSemaphore {

    static int readerCount = 0;
    static Semaphore rmutex = new Semaphore(1);
    static Semaphore resource = new Semaphore(1);

    static long writersTime = 0;
    static long readersTime = 0;

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                long nanoStart = nanoTime();

                rmutex.acquire();
                readerCount++;
                if (readerCount == 1) resource.acquire();
                rmutex.release();

                long nanoEnd = nanoTime();
                long time = nanoEnd - nanoStart;
                readersTime += time;

//                System.out.println("Watek " + Thread.currentThread().getName() + " czyta");
//                Thread.sleep(1);
//                System.out.println("Watek " + Thread.currentThread().getName() + " zakonczyl czytanie");

                rmutex.acquire();
                readerCount--;

                if (readerCount == 0) resource.release();
                rmutex.release();

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                long nanoStart = nanoTime();
                resource.acquire();

                long nanoEnd = nanoTime();
                long time = nanoEnd - nanoStart;
                writersTime += time;
//                System.out.println("Watek " + Thread.currentThread().getName() + " pisze do zasobu");
//                Thread.sleep(1);
//                System.out.println("Watek " + Thread.currentThread().getName() + " zakonczyl pisanie do zasobu");

                resource.release();

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String exec(int readersCount, int writersCount) throws Exception {
        Read read = new Read();
        Write write = new Write();

        List<Thread> readers = new ArrayList();
        List<Thread> writers = new ArrayList();

        for (int i = 0; i < readersCount; i++) {
            Thread t1 = new Thread(read);
            t1.setName(i + " - Czytelnik");
            readers.add(t1);
        }

        for (int i = 0; i < writersCount; i++) {
            Thread t1 = new Thread(write);
            t1.setName(i + " - Pisarz");
            writers.add(t1);
        }

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread writer : writers) {
            writer.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        for (Thread writer : writers) {
            writer.join();
        }

//        System.out.println("Pisarze: " + writersCount + " , Czytelnicy: " + readersCount);
//        System.out.println("Sredni czas oczekiwania pisarza: " + (writersTime / writersCount) / 1000 + "ns");
//        System.out.println("Sredni czas oczekiwania czytelnika: " + (readersTime / readersCount) / 1000 + "ns");
        long lastWritersTime = writersTime;
        long lastReadersTime = readersTime;

        writersTime = 0;
        readersTime = 0;

        return writersCount + " " + readersCount + " " + ((lastWritersTime / writersCount)) + " " + ((lastReadersTime / readersCount)) + "\n";
    }

    public static void main(String[] args) throws Exception {
        int maxReaders = 100;
        int maxWriters = 10;

        File file = new File("prefer-readers.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("writer_count readers_count writers_waiting_time readers_waiting_time\n");

        String sb = "";

            for(int i = 1; i <= maxWriters; i++) {
        for(int j = 10; j <= maxReaders; j++) {
                String result = exec(j, i);

                sb += (result);
            }
        }


        writer.write(sb);

    }
}


class PreferWriterSemaphore {

    static int readerCount = 0;
    static Semaphore x = new Semaphore(1);
    static Semaphore z = new Semaphore(1);
    static Semaphore rMutex = new Semaphore(1);
    static Semaphore resource = new Semaphore(1);

    static long writersTime = 0;
    static long readersTime = 0;

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                long nanoStart = nanoTime();

                z.acquire();
                rMutex.acquire();
                x.acquire();
                readerCount++;
                if (readerCount == 1) resource.acquire();

                long nanoEnd = nanoTime();
                long time = nanoEnd - nanoStart;
                readersTime += time;

                x.release();

//                System.out.println("Thread " + Thread.currentThread().getName() + " czyta");
//                Thread.sleep(1);
//                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl czytanie");

                x.acquire();
                readerCount--;
                if (readerCount == 0) resource.release();

                x.release();
                rMutex.release();
                z.release();

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                long nanoStart = nanoTime();

                rMutex.acquire();
                resource.acquire();

                long nanoEnd = nanoTime();
                long time = nanoEnd - nanoStart;
                writersTime += time;

//                System.out.println("Thread " + Thread.currentThread().getName() + " pisze do zasobu");
//                Thread.sleep(1);
//                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl pisanie do zasobu");

                resource.release();
                rMutex.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public static String exec(int readersCount, int writersCount) throws Exception {
        Read read = new Read();
        Write write = new Write();

        List<Thread> readers = new ArrayList();
        List<Thread> writers = new ArrayList();

        for (int i = 0; i < readersCount; i++) {
            Thread t1 = new Thread(read);
            t1.setName(i + " - Czytelnik");
            readers.add(t1);
        }

        for (int i = 0; i < writersCount; i++) {
            Thread t1 = new Thread(write);
            t1.setName(i + " - Pisarz");
            writers.add(t1);
        }

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread writer : writers) {
            writer.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        for (Thread writer : writers) {
            writer.join();
        }

//        System.out.println("Pisarze: " + writersCount + " , Czytelnicy: " + readersCount);
//        System.out.println("Sredni czas oczekiwania pisarza: " + (writersTime / writersCount) / 1000 + "ns");
//        System.out.println("Sredni czas oczekiwania czytelnika: " + (readersTime / readersCount) / 1000 + "ns");
        long lastWritersTime = writersTime;
        long lastReadersTime = readersTime;

        writersTime = 0;
        readersTime = 0;

        return writersCount + " " + readersCount + " " + ((lastWritersTime / writersCount)) + " " + ((lastReadersTime / readersCount)) + "\n";
    }

    public static void main(String[] args) throws Exception {
        int maxReaders = 100;
        int maxWriters = 10;

        File file = new File("prefer-writers.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("writer_count | readers_count | writers_waiting_time | readers_waiting_time\n");

        String sb = "";

        for(int i = 1; i <= maxWriters; i++) {
            for(int j = 10; j <= maxReaders; j++) {
                String result = exec(j, i);

                writer.write(result);
//                sb += (result);
                // reset for new iter
//                writersTime = 0;
//                readersTime = 0;
            }
        }


//        writer.write(sb);

    }
}


class FifoSemaphore {

    static int readerCount = 0;
    static Semaphore x = new Semaphore(1);
    static Semaphore rMutex = new Semaphore(1);
    static Semaphore resource = new Semaphore(1);

    static long writersTime = 0;
    static long readersTime = 0;

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                long nanoStart = nanoTime();

                rMutex.acquire();
                x.acquire();
                readerCount++;
                if (readerCount == 1) resource.acquire();

                long nanoEnd = nanoTime();
                long time = nanoEnd - nanoStart;
                readersTime += time;

                x.release();

                System.out.println("Thread " + Thread.currentThread().getName() + " czyta");
                Thread.sleep(10);
                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl czytanie");

                x.acquire();
                readerCount--;
                if (readerCount == 0) resource.release();


                x.release();
                rMutex.release();

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                long nanoStart = nanoTime();

                rMutex.acquire();
                resource.acquire();
                System.out.println("Thread " + Thread.currentThread().getName() + " pisze do zasobu");
                Thread.sleep(10);
                System.out.println("Thread " + Thread.currentThread().getName() + " zakonczyl pisanie do zasobu");

                resource.release();

                long nanoEnd = nanoTime();
                long time = nanoEnd - nanoStart;
                writersTime += time;

                rMutex.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static String exec(int readersCount, int writersCount) throws Exception {
        Read read = new Read();
        Write write = new Write();

        List<Thread> readers = new ArrayList();
        List<Thread> writers = new ArrayList();

        for (int i = 0; i < readersCount; i++) {
            Thread t1 = new Thread(read);
            t1.setName(i + " - Czytelnik");
            readers.add(t1);
        }

        for (int i = 0; i < writersCount; i++) {
            Thread t1 = new Thread(write);
            t1.setName(i + " - Pisarz");
            writers.add(t1);
        }

        for (Thread reader : readers) {
            reader.start();
        }

        for (Thread writer : writers) {
            writer.start();
        }

        for (Thread reader : readers) {
            reader.join();
        }

        for (Thread writer : writers) {
            writer.join();
        }

        System.out.println("Pisarze: " + writersCount + " , Czytelnicy: " + readersCount);
        System.out.println("Sredni czas oczekiwania pisarza: " + (writersTime / writersCount) / 1000 + "ns");
        System.out.println("Sredni czas oczekiwania czytelnika: " + (readersTime / readersCount) / 1000 + "ns");
        long lastWritersTime = writersTime;
        long lastReadersTime = readersTime;

        writersTime = 0;
        readersTime = 0;

        return writersCount + " " + readersCount + " " + ((lastWritersTime / writersCount)) + " " + ((lastReadersTime / readersCount)) + "\n";
    }

    public static void main(String[] args) throws Exception {
        int maxReaders = 100;
        int maxWriters = 10;

        File file = new File("fifo.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("writer_count | readers_count | writers_waiting_time | readers_waiting_time\n");

        String sb = "";

        for(int i = 1; i <= maxWriters; i++) {
            for(int j = 10; j <= maxReaders; j++) {
                String result = exec(j, i);

                writer.write(result);
//                sb += (result);
                // reset for new iter
//                writersTime = 0;
//                readersTime = 0;
            }
        }


//        writer.write(sb);

    }
}
