package uj.java.kindergarten;

import java.util.concurrent.Semaphore;

public class Fork {
    public Semaphore sem = new Semaphore(1);
    // jako narzędzia do synchronizacji wybrałem semafory, widelec może trzymać tylko dziecko więc do konretnego zasobu może mieć dostęp tylko 1 wątek

    void grab() { // zdobycie dostępu do widelca
        try {
            sem.acquire();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    void release() { // oddanie dostępu do widelca
        sem.release();
    }

}
