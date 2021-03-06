import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ---------------------------------------------
 * Parser.java
 * Description: Parser that also counts amount of words and sentences
 *
 * Class: CS 421 - Spring 2015
 * System: Eclipse Luna, Mac Mavericks OSX
 * Author: Jenny Sum (sum1)
 * Version: 1.0
 *
 * ---------------------------------------------
 */

public class Parser {
	
	private ArrayList<String> wordList;
	private int wordCount;
	private int sentenceCount;

	public void parseFile(String inputFile) throws IOException {
		ArrayList<String> inputList = parseText(inputFile);
		wordList = inputList;
		
		int words = countWords(wordList);
		wordCount = words;
		
		int sentences = countSentences(inputFile);
		sentenceCount = sentences;
	}
	
	public ArrayList<String> getWordList() {
		return wordList;
	}
	
	public int getWordCount() {
		return wordCount;
	}
	
	public int getSentenceCount() {
		return sentenceCount;
	}
	
	private ArrayList<String> parseText(String inputFile) throws IOException {
		ArrayList<String> inputList = new ArrayList<String>();
		Scanner scanInput = new Scanner(new FileReader(inputFile));
		
		while(scanInput.hasNext()){
			String next = scanInput.next();
			inputList.add(next);
		}
		scanInput.close();
		return inputList;
	}
	
	private int countWords(ArrayList<String> wordList) {
		int words = 0;
		
		for (String word : wordList) {
			words++;
		}
		return words;
	}
	
	private int countSentences(String inputFile) throws IOException {
		int sentenceEnder = 0;
		
		BufferedReader reader = new BufferedReader(
			    new InputStreamReader( new FileInputStream(inputFile),
			        Charset.forName("UTF-8")));
			int c;
			while((c = reader.read()) != -1) {
			  char character = (char) c;
			  if (character == '.' || character == '!' || character == '?') {
				  sentenceEnder++;
			  }
			}
			reader.close();
		return sentenceEnder;
	}
}