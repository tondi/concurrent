package com.agh.lab1;

// Watek, ktory inkrementuje licznik 100.000 razy
class IThread extends Thread {
    Counter counter;

    public IThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        int i = 0;
        while(i < 100000) {
            while(this.counter.isLocked()) {}

            this.counter.lockCounter();
            this.counter.inc();
            this.counter.unlockCounter();
            i++;
        }
    }
}

// ithread run
// dthread run
// ustawienie locka przez watek 1
//