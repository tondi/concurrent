package com.agh.lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class PKmon {
    public static long exec() throws InterruptedException {
        int bufferSize = 8;

        long start = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();
        Buffer buffer = new Buffer(bufferSize);

        for (int j = 0; j < 4; j++) {
            Producer producer = new Producer(buffer, new Random().nextInt(bufferSize));
            threads.add(producer);
//
            Consumer consumer = new Consumer(buffer, new Random().nextInt(bufferSize));
            threads.add(consumer);
        }

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

//        for(int i = 5; i <= 5; i++) {
//            System.out.println(i + " " + exec(i));
//        }

        exec();
    }
}
class Buffer {
    private List<Integer> _buf;
    private int _size;

    public Buffer(int size) {
        this._size = size;
        _buf = new ArrayList(size);
    }

    public synchronized void put(List<Integer> data) {
        while (data.size() > _size - _buf.size()) {
            try {
                System.out.println("proba produkcji  " + data.size() +
                    ", ilosc wolnych w buforze " + (_size - _buf.size()));
                wait();
            } catch (InterruptedException e) {
            }
        }

        System.out.println("Dodaje " + data.size() + " elementow");

        _buf.addAll(data);
        notify();
    }

    public synchronized List<Integer> get(int i) {
        while (i >_buf.size()) {
            try {
                System.out.println("proba konsumpcji " + i + " zasobow" + ", wolnych: " + _buf.size());
                wait();
            } catch (InterruptedException e) {
            }
        }

        List<Integer> newList = new ArrayList<>(i);

        for (int j = 0 ; j < i; j++) {
            newList.add(_buf.remove(0));
        }

        System.out.print("Zwracam " + i + " wartosci: ");
        for (int j = 0; j < i; j++) {
            System.out.print(newList.get(j) + " ");
        }
        System.out.println();

        notify();
        return newList;
    }
}
class Consumer extends Thread {
    private Buffer _buf;
    private int _i;

    public Consumer(Buffer buffer, int i) {
        this._buf = buffer;
        this._i = i;
    }

    public void run() {
        try {
            Thread.sleep((new Random()).nextInt(500));
        } catch(InterruptedException e) {}
        _buf.get(_i);
    }
}
class Producer extends Thread {
    private Buffer _buf;
    private int _i;

    public Producer(Buffer buffer, int i) {
        this._buf = buffer;
        this._i = i;
    }

    public void run() {
        try {
            Thread.sleep((new Random()).nextInt(500));
        } catch(InterruptedException e) {}

        List<Integer> data = new ArrayList<>(_i);
        for (int j = 0; j < _i; j++) {
            data.add(j);
        }

        _buf.put(data);
    }
}
