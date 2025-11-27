import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class CravStore {
    ArrayList<Character> stockList = new ArrayList<>();

    ReentrantLock lock = new ReentrantLock();
    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    private volatile int consumeralive = 0;

    public void consumer_started() {
        lock.lock();
        try { consumeralive++; }
        finally { lock.unlock(); }
    }

    public void consumer_finished() {
        lock.lock();
        try {
            consumeralive--;
            if (consumeralive == 0) {
                notEmpty.signalAll();
                notFull.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public int getConsumeralive() {
        return consumeralive;
    }

    public void put(String str, char... values) {
        lock.lock();
        try {
            for (char v : values) {

                while (stockList.size() == 10) {
                    notFull.await();
                }

                if (consumeralive == 0) return;

                stockList.add(v);
                System.out.println(str + " поместил: " + v + " -> " + stockList);

                if (stockList.size() == 10) {
                    System.out.println(">>> Склад заполнен!");
                    notEmpty.signalAll();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void get(String str) {
        lock.lock();
        try {
            while (stockList.size() < 10 && consumeralive > 0) {
                notEmpty.await();
            }

            if (consumeralive == 0 && stockList.isEmpty()) return;

            while (!stockList.isEmpty()) {
                char val = stockList.remove(stockList.size() - 1);
                System.out.println(str + " взял букву " + val);
            }

            System.out.println("<<< Склад пуст!");
            notFull.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    class cProducer implements Runnable {
        CravStore store;

        public cProducer(CravStore store) {
            this.store = store;
        }

        public void run() {

            char[] vowels = {'а','е','ё','и','о','у','ы','э','ю','я'};

            while (store.getConsumeralive() > 0) {

                int iterations = 1 + (int)(Math.random() * 5);

                for (int i = 0; i < iterations; i++) {

                    int count = 1 + (int)(Math.random() * 5); // от 1 до 5 букв
                    char[] arr = new char[count];

                    for (int j = 0; j < count; j++) {
                        arr[j] = vowels[(int)(Math.random() * vowels.length)];
                    }

                    store.put(Thread.currentThread().getName(), arr);
                }
            }
        }
    }

    class cConsumer implements Runnable {
        CravStore store;

        public cConsumer(CravStore store) {
            this.store = store;
        }

        public void run() {
            store.consumer_started();

            for (int i = 0; i < 50; i++) {
                store.get(Thread.currentThread().getName());
            }

            System.out.println("!!!!!!!!!!!!!!!!!!!!!! " + Thread.currentThread().getName() + " НАЕЛСЯ БУКВ И УМЕР");

            store.consumer_finished();
        }
    }
}
