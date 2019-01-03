package src.sockets.id1212.client.view;

class ThreadSafePrint {
    /**
     * the print methods are synchronized to make sure the the client inteface can only recieve 1 print operation.
     * @param output
     */
    synchronized void print(String output){
        System.out.print(output);
    }

    synchronized void println(String output){
        System.out.println(output);
    }
}
