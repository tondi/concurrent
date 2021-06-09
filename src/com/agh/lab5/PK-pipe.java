package com.agh.lab5;

import java.util.ArrayList;
import java.util.List;

class PKmon {
    public static long exec(int i) throws InterruptedException {
        int bufferSize = 5;
        int iterations = 30;
        int convertersCount = i;

        List<Thread> threads = new ArrayList<Thread>();
        long start = System.currentTimeMillis();

        Buffer prev = new Buffer(bufferSize);
        Buffer next = prev; // start with first

        Producer producer = new Producer(next, iterations);
        threads.add(producer);

        for (int j = 0; j < convertersCount; j++) {
            prev = next;
            next = new Buffer(bufferSize);
            threads.add(new Converter(prev, next, iterations));
        }

        Consumer consumer = new Consumer(next, iterations);
        threads.add(consumer);

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long time = System.currentTimeMillis() - start;
//        System.out.println("czas: " + time);

        return time;
    }

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(exec(10));

        for(int i = 100; i <= 100; i++) {
            System.out.println(i + " " + exec(i));
        }
    }
}
class Buffer {
    private List<Integer> _buf;
    private int _size;

    public Buffer(int size) {
        this._size = size;
        _buf = new ArrayList(size);
    }

    public synchronized void put(int i) {
        while (_buf.size() == _size) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        _buf.add(i);
        notify();
    }

    public synchronized int get() {
        while (_buf.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        Integer value = _buf.remove(0);
        notify();
        return value;
    }
}
class Consumer extends Thread {
    private Buffer _buf;
    private int iter;

    public Consumer(Buffer buffer, int iterations) {
        this._buf = buffer;
        this.iter = iterations;
    }

    public void run() {
        for (int i = 0; i < iter; i++) {
            _buf.get();
        }
    }
}
class Converter extends Thread {
    private Buffer previous;
    private Buffer next;
    private int iter;

    public Converter(Buffer previous, Buffer next, int iterations) {
        this.previous = previous;
        this.next = next;
        this.iter = iterations;
    }

    public void run() {
        for (int i = 0; i < this.iter; i++) {
            int tmp = previous.get();
            tmp += 10;
            try {
                sleep(10);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            next.put(tmp);
        }
    }
}
class Producer extends Thread {
    private Buffer _buf;
    private int iter;

    public Producer(Buffer buffer, int iterations) {
        this._buf = buffer;
        this.iter = iterations;
    }

    public void run() {
        for (int i = 0; i < iter; i++) {
            _buf.put(i);
        }
    }
}
