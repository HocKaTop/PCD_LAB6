import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        ExecutorService producers = Executors.newFixedThreadPool(3);
        ExecutorService consumers = Executors.newFixedThreadPool(4);
        consumers.execute(new Consumer(store));
        consumers.execute(new Consumer(store));
        consumers.execute(new Consumer(store));
        consumers.execute(new Consumer(store));
        producers.execute(new Producer(store));
        producers.execute(new Producer(store));
        producers.execute(new Producer(store));

        producers.shutdown();
        consumers.shutdown();
    }
}
