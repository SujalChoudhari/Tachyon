package com.sujal.tachyon;

import java.util.ArrayList;
import java.util.Arrays;

public class Command {
    public static final String[] FORWARD = {"go","front","forward","f","start"};
    public static final String[] REVERSE = {"back","reverse","b","r"};
    public static final String[] LEFT = {"left","l"};
    public static final String[] RIGHT = {"right","r"};
    public static final String[] STOP = {"stop","halt","s"};


    public static ArrayList<Character> processRawInput(String rawInput){

        ArrayList<Character> outputCharacters = new ArrayList<>();

        ArrayList<String> splitRawInput = new ArrayList<>(Arrays.asList(rawInput.split("\\s+")));

        for(String word: splitRawInput){
            word = word.toLowerCase();
            if(Arrays.asList(FORWARD).contains(word)){
                outputCharacters.add('w');
            }
            else if(Arrays.asList(REVERSE).contains(word)){
                outputCharacters.add('s');
            }
            else if(Arrays.asList(LEFT).contains(word)){
                outputCharacters.add('a');
            }
            else if(Arrays.asList(RIGHT).contains(word)){
                outputCharacters.add('d');
            }
            else if(Arrays.asList(STOP).contains(word)){
                outputCharacters.add('o');
            }
        }

        return outputCharacters;
    }
}
