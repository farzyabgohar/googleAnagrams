/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;
import java.util.Arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private int difficulty = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordList = new ArrayList<String>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)){
                lettersToWord.get(sortedWord).add(word);

            } else {
                ArrayList<String> values = new ArrayList<String>();
                values.add(word);
                lettersToWord.put(sortedWord,values);

            }

            if (sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            } else{
                ArrayList<String> wordsOfSameLength = new ArrayList<>();
                wordsOfSameLength.add(word);
                sizeToWords.put(word.length(), wordsOfSameLength);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {

    if (!(wordSet.contains(word))){
    return false;
    } else if (!(word.indexOf(base) == -1)) {
    return false;
    }
    return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> allAnagrams = new ArrayList<String>();
        for (String s: wordList){
            if (sortLetters(targetWord).equals(sortLetters(s))){
                allAnagrams.add(s);
            }
        }

        return allAnagrams;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> newAnagrams = new ArrayList<String>();
        String newSorted;
        for (char i = 'a'; i <= 'z'; i++) {
            newSorted = sortLetters(word + i);
            //check if key exists
            if (lettersToWord.containsKey(newSorted)) {
                //find all good words
                for (String s : lettersToWord.get(newSorted)) {
                    if (isGoodWord(s, word)) {
                        newAnagrams.add(s);
                    }
                }
            }
        }
        return newAnagrams;
    }
    public String sortLetters(String unsorted){
        char[] charArray = unsorted.toCharArray();
        Arrays.sort(charArray);
        String sortedString = new String(charArray);
        return sortedString;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> difficultySet = sizeToWords.get(difficulty);
        String startingWord = "";

        int randomIndex = random.nextInt(difficultySet.size());
        String wordAtIndex = difficultySet.get(randomIndex);
        boolean wordFound = false;
        while (wordFound == false){
            if (NumAnagrams(wordAtIndex)>= MIN_NUM_ANAGRAMS){
                wordFound = true;
                startingWord = wordAtIndex;
                difficulty = difficulty + 1;
            } else {
                randomIndex = random.nextInt(difficultySet.size());
                wordAtIndex = difficultySet.get(randomIndex);
            }
        }
        return startingWord;
    }

    public int NumAnagrams(String s){
        //System.out.println("annagram size" + getAnagrams(s).size());
        return getAnagramsWithOneMoreLetter(s).size();
    }
}
