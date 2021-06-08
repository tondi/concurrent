package com.agh.lab2;

class Main {
    public static void main(String[] args) throws InterruptedException {
        Semafor s = new Semafor();
        Licznik licznik = new Licznik();
        DThread decrementThread = new DThread(licznik, s);
        IThread incrementThread = new IThread(licznik, s);

        incrementThread.start();
        decrementThread.start();
        incrementThread.join();
        decrementThread.join();

        System.out.println(licznik.value());
    }
}

class Licznik {
    private int _wartosc = 0;

    public void inc() {
        _wartosc += 1;
    }

    public void dec() {
        _wartosc -= 1;
    }

    public int value() {
        return _wartosc;
    }
}

class Semafor {
    private boolean _stan = true;
    private int _czeka = 0;

    public Semafor() {
    }

    public synchronized void P() {
        try {
            if (_stan) {
                _stan = false;
            } else {
                _czeka++;
                wait();
            }
        } catch (Exception ex) {
        }
    }

    public synchronized void V() {
        if (_czeka > 0) {
            notify();
            _czeka--;
        } else {
            _stan = true;
        }
    }
}

class IThread extends Thread {
    private Semafor _semafor;
    private Licznik _licznik;

    public IThread(Licznik c, Semafor s) {
        _licznik = c;
        _semafor = s;
    }

    public void run() {
        for (int i = 0; i < 100000; ++i) {
            _semafor.P();
            _licznik.inc();
            _semafor.V();
        }
    }
}

class DThread extends Thread {
    private Semafor _semafor;
    private Licznik _licznik;

    public DThread(Licznik c, Semafor s) {
        _licznik = c;
        _semafor = s;
    }

    public void run() {
        for (int i = 0; i < 100000; ++i) {
            _semafor.P();
            _licznik.dec();
            _semafor.V();
        }
    }
}
