import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Scanner scan =  new Scanner(System.in);
        System.out.println("Выберите вариант: 1) Слободенюк 2) Кравченко");
        int var =  scan.nextInt();
        switch(var){
            case 1:
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
                break;
            case 2:
                CravStore crav = new CravStore();
                ExecutorService crav_producers = Executors.newFixedThreadPool(4);
                ExecutorService crav_consumers = Executors.newFixedThreadPool(3);

                crav_consumers.execute(crav.new cConsumer(crav));
                crav_consumers.execute(crav.new cConsumer(crav));
                crav_consumers.execute(crav.new cConsumer(crav));

                crav_producers.execute(crav.new cProducer(crav));
                crav_producers.execute(crav.new cProducer(crav));
                crav_producers.execute(crav.new cProducer(crav));
                crav_producers.execute(crav.new cProducer(crav));
                crav_producers.shutdown();
                crav_consumers.shutdown();
                break;
        }

    }
}
