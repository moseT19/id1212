package sockets.id1212.client.view;

class ThreadSafePrint {

    synchronized void print(String output){
        System.out.print(output);
    }

    synchronized void println(String output){
        System.out.println(output);
    }
}
