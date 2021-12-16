package uj.java.kindergarten;

public final class ChildImpl extends Child implements Comparable<ChildImpl>{
    //aby skorzystać z PriorityBlockingQueue musiałem sprawić aby było możliwe porównywanie 2 instancji danej klasy


    public Fork left; //lewy widelec
    public Fork right; //prawy widelec
    Thread thread; //jako że mogę dziedziczyć tylko po 1 klasie, więc dziedziczenie po klasie Thread odpada. Zatem zrobiłem z niej klasę wewnętrzną

    public ChildImpl(String name, int hungerSpeedMs, Fork left, Fork right){
        super(name, hungerSpeedMs);
        this.left = left;
        this.right = right;
    }

    void startThread() throws InterruptedException {
        thread = new Thread(this::meal);
        thread.start();
        Thread.sleep(hungerSpeed());
    }

    void meal(){
        try {
            left.grab();
            right.grab();
            eat();
            left.release();
            right.release();
            //podniesienie 2 widelców zjedzenie posiłku i odłożenie widelców
            Thread.sleep(hungerSpeed());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public int compareTo(ChildImpl o) {
        return this.hungerSpeed() * this.happiness() - o.hungerSpeed() * o.happiness();
        //dzieci porównuję na podstawie tego ile czasu jeszcze im zostało do momentu gdy zaczną płakać
    }
}
