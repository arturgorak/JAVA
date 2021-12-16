package uj.java.kindergarten;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Kindergarten {
    static int quantity = 0; //ilość dzieci i widelców
    static ChildImpl[] children; //tablica dzieci
    static Fork[] forks; //tablica widelców

    public static void main(String[] args) throws IOException, InterruptedException {
        init();

        final var fileName = args[0];
        System.out.println("File name: " + fileName);

        readChildrenFromFile(fileName);
        saveFromHunger();
    }

    private static void readChildrenFromFile(String fileName){

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            quantity = Integer.parseInt(reader.readLine());
            createForks();

            children = new ChildImpl[quantity];
            for (int i = 0; i < quantity - 1; i++) {
                String line= reader.readLine();
                String[] words = line.split(" "); //tablica słów w linii
                int hungerSpeedMs = Integer.parseInt(words[1]); //czas głodzenia jest intem

                children[i] = new ChildImpl(words[0],hungerSpeedMs, forks[i], forks[(i + 1) % quantity]); //dodaję dziecko do tablicy i przydzielam mu odpowiednie widelce
            }

            String line= reader.readLine();
            String[] words = line.split(" ");
            int hungerSpeedMs = Integer.parseInt(words[1]);

            children[quantity - 1] = new ChildImpl(words[0],hungerSpeedMs, forks[0], forks[quantity - 1]);
            //chcemy aby ziecko zawsze jako pierwsze brało widelec o mniejszym numerze (lewy widelec)
            // u każdego dziecka z wyjątkiem ostatniego jest to lewy widelec
            // ,więc zamieniamy miejscami widelce u tego dziecka



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createForks(){
        forks = new Fork[quantity];
        for (int i = 0; i < quantity; i++) { //dodawanie do tablicy widelców kolejnych widelców
            forks[i] = new Fork();
        }
    }

    private static void saveFromHunger() throws InterruptedException {
        while(true){
            PriorityBlockingQueue<ChildImpl> childPriorityBlockingQueue = new PriorityBlockingQueue<>();
            //tworzymy PriorityBlockingQueue, ponieważ elementy w tej kolejce są uporządkowane wedle kolejności ustatolenj przy pomocy compareTo
            // więc na początku będą najbardziej głodne dzieci
            childPriorityBlockingQueue.addAll(Arrays.asList(children).subList(0, quantity));

            for(int i = 0; i < quantity; i++){
                ChildImpl tmp = childPriorityBlockingQueue.poll();
                tmp.startThread();
                Thread.sleep(tmp.hungerSpeed());
                //po kolei karmimy dzieci zaczynając od tych najgłodniejszych
            }

        }
    }

    private static void init() throws IOException {
        Files.deleteIfExists(Path.of("out.txt"));
        System.setErr(new PrintStream(new FileOutputStream("out.txt")));
        new Thread(Kindergarten::runKindergarden).start();
    }

    private static void runKindergarden() {
        try {
            Thread.sleep(10100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            List<String> errLines = Files.readAllLines(Path.of("out.txt"));
            System.out.println("Children cries count: " + errLines.size());
            errLines.forEach(System.out::println);
            System.exit(errLines.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
