public class PKmon {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Buffer buffer = new Buffer(bufferSize);
       …
        long time = System.currentTimeMillis() - start;
        System.out.println("czas: " + time);
    }
}

class Buffer {
    private int _size;

    public Buffer(int size) {
        this._size = size;
    }

    public synchronized void put(int i) {
        while (_buf.size() == _size) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
       …
    }

    public synchronized int get() {
        while (_buf.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
       	…
        return value;
    }
}

class Consumer extends Thread {
    private Buffer _buf;

    public Consumer(Buffer buffer, …) {
        this._buf = buffer;
       …
    }

    public void run() {
        _buf.get();
    }
}

class Converter extends Thread {
    private Buffer previous;
    private Buffer next;
       …

    public Converter(Buffer previous, Buffer next, …) {
        this.previous = previous;
        this.next = next;
       …
    }

    public void run() {
       …
        int tmp = previous.get();
        try {
            sleep(10);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        next.put(tmp);
    }
}

class Producer extends Thread {
    private Buffer _buf;
    private int iter;

    public Producer(Buffer buffer, int iterations) {
        this._buf = buffer;
       …
    }

    public void run() {
       …
        _buf.put(i);
    }
}
