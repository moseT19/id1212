package sockets.id1212.server.model;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Word{
    private String st;
    private String theWord;
    private ArrayList<String> wordlist = new ArrayList<>();
    private BufferedReader br;
    private File file = new File("C:\\Users\\jocke\\Hangman\\src\\sockets\\id1212\\extra\\words.txt");

    public Word(){
        try {

            br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null)
                wordlist.add(st);

            br.close();


        }catch (IOException e){
            System.out.println("File cannot be read.");
        }
    }

    public String getRandWord(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, wordlist.size() + 1);
        this.theWord  = wordlist.get(randomNum);

        return this.theWord.toLowerCase();
    }


}
