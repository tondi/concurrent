package com.agh.lab1;

class Counter {
    private int _val;
    public Counter(int n) {
        _val = n;
    }
    public void inc() {
        _val++;
    }
    public void dec() {
        _val--;
    }
    public int value() {
        return _val;
    }

    private boolean locked = false;

    public boolean isLocked() {
        return locked;
    }

    public void lockCounter() {
        locked = true;
    }
    public void unlockCounter() {
        locked = false;
    }
}
