package com.agh;

// Watek, ktory dekrementuje licznik 100.000 razy
class DThread extends Thread {
    Counter counter;

    public DThread(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        int i = 0;
        while(i < 100000) {
            while(this.counter.isLocked()) {}

            this.counter.lockCounter();
            this.counter.dec();
            this.counter.unlockCounter();
            i++;
        }
    }
}
