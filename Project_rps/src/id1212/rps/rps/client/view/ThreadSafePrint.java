package id1212.rps.rps.client.view;

public class ThreadSafePrint {

    synchronized void print(String toPrint){
        System.out.print(toPrint);
    }

    synchronized void println(String toPrint){
        System.out.println(toPrint);
    }
}
