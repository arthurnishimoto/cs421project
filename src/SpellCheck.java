import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

// Uses Apache Lucene Core
// https://lucene.apache.org/core/
/*
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.*; // lucene-suggest-5.1.0.jar
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
*/

/**
 * ---------------------------------------------
 * SpellCheck.java
 * Description: Spell checker for CS 421 Project
 *
 * Class: CS 421 - Spring 2015
 * System: Eclipse Luna, Windows 7 x64
 * Author: Arthur Nishimoto (anishi2)
 * Version: 1.0
 *
 * ---------------------------------------------
 */

public class SpellCheck
{
	public enum ScoreType { Low, Medium, High };
	
	//SpellChecker spellchecker;
	int spellingErrors = 0;
	
	boolean showDetail = true;
	
	/*
	public static void main(String[] args)
	{
		boolean useTokenized = false;
		ScoreType scoreType = ScoreType.High;
		
		// Setup data paths
		String essayPath = "./data/";
		if(useTokenized)
			essayPath += "P5-tokenized/";
		else
			essayPath += "P5-original/";
		
		switch(scoreType)
		{
			case High:	essayPath += "high/";	break;
			case Medium:essayPath += "medium/";	break;
			case Low:	essayPath += "low/";	break;
			default:	essayPath += "high/";	break;
		}
		
		// Read the file
		File dir = new File(essayPath);
		File[] filesList = dir.listFiles();
		//for (File file : filesList) {
		//    if (file.isFile()) {
		//        System.out.println(file.getName());
		//    }
		//}
		
		// Initialize the spell checker
		InitializeSpellCheck("./data/fulldictionary00.txt");
		
		essayParser(filesList[0]);
	}
	*/
	
	HashSet<String> wordList;
	
	// Takes in a filename
	public void parseFile(String filePath) throws IOException
	{
		List<String> parsedText = parseText(filePath);
		checkPOS( parsedText );
		parseLines( parsedText );
	}
	
	// Note: This is a copy of Jenny's Parser.java parseText()
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
	
	
	// Takes in a file object (original version)
	public void fileParser(File essay)
	{
		if (essay.isFile()) {
	        System.out.println(essay.getName());
	        
	        List<String> essayLines;
	        try
			{
	        	essayLines = Files.readAllLines(essay.toPath());
	        	
	        	parseLines(essayLines);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
	    }
	}
	
	public int getSpellingErrors()
	{
		return spellingErrors;
	}
	
	private void parseLines(List<String> essayLines) throws IOException
	{
		spellingErrors = 0;
		System.out.println(essayLines.size());
        for( int i = 0; i < essayLines.size(); i++ )
        {
        	String line = essayLines.get(i);
        	//if( line.length() > 0 )
        	//	System.out.println("Line "+i+"] "+line);
        	String[] words = line.split("[ -;!?:,\".\t//()]");
        	
        	//if(showDetail)
			//	System.out.println("Misspelled words:");
        	for( int j = 0; j < words.length; j++ )
	        {
        		if( words[j].length() > 0 )
        		{
        			String word = words[j];
        			
        			if( !CheckSpelling(word) )
        			{
        				if(showDetail)
        					System.out.println(" '"+word+"'*");
        				spellingErrors++;
        			}
        			/*// If we cared about correcting/suggestions
        			String[] suggestions = CheckSpelling(word, 3);
        			if( suggestions.length == 0 )
        			{
        				//System.out.println(" "+j+"] '"+words[j]+"'");
        			}
        			else
        			{
        				spellingErrors++;
        				if(showDetail)
        				{
        					System.out.println(" "+j+"] '"+words[j] + "' *");
	        				for( int k = 0; k < suggestions.length; k++ )
	        		        {
	        					System.out.println("    *"+k+"] '"+suggestions[k] + "'");
	        		        }
        				}
        			}
        			*/
        		}
	        }
        }
        
        System.out.println("Spelling errors: "+spellingErrors);
	}
	
	public SpellCheck(String dictionaryPath)
	{
		wordList = new HashSet<String>();

		Scanner scanInput = null;
		try
		{		
			scanInput = new Scanner(new FileReader(dictionaryPath));
			while(scanInput.hasNext()){
				String next = scanInput.next();
				wordList.add(next);
			}
			
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchElementException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanInput.close();
		
		System.out.println(wordList.size()+" words added to the dictionary");
	}
	
	private void checkPOS(List<String> text)
	{
		InputStream modelIn = null;

		try {
		  modelIn = new FileInputStream("./data/en-pos-maxent.bin");
		  POSModel model = new POSModel(modelIn);
		  
		  POSTaggerME tagger = new POSTaggerME(model);
		  
		  String sent[] = text.toArray(new String[text.size()]);
		  String tags[] = tagger.tag( sent );
		  
		  // Parsing 
		  for(int i = 0; i < tags.length; i++)
		  {
			  System.out.print(sent[i]+"|"+tags[i] + " ");
			  
		  }
		  System.out.println();
		}
		catch (IOException e) {
		  // Model loading failed, handle the error
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
		
	}
	
	private boolean CheckSpelling(String word)
	{
		boolean validWord = true;
		
		// Catch proper nouns before ignoring case (for sentence starts)
		if( wordList.contains(word) ) 
			return true;
		
		// Check for numbers
		try
		{
			if( Float.valueOf(word) != null )
				return true;
		}
		catch(NumberFormatException e)
		{
		}
		
		word = word.toLowerCase();
		
		// Check for root word (cases the dictionary tends to fail even if these words are there)
		if( wordList.contains( modifyWordEnding(word, "s", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "d", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "ed", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "ied", "y") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "es", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "er", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "ing", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "est", "") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "ing", "e") ) )
		{

		}
		else if( wordList.contains( modifyWordEnding(word, "ies", "y") ) )
		{
			
		}
		else if( wordList.contains(word+"s") )
		{

		}
		else if( !wordList.contains(word) ) 
			validWord = false;
		
		return validWord;
	}
	
	private String modifyWordEnding(String word, String oldEnding, String newEnding)
	{
		if( word.endsWith(oldEnding) )
			return word.substring(0, word.length()-oldEnding.length())+newEnding;
		else
			return word;
	}
	
	// lucene spell check
	/*
	private String[] CheckSpelling(String word, int suggestionCount) throws IOException
	{
		word = word.toLowerCase();
		String[] suggestions = {};
		
		boolean validWord = true;
		
		// handle words with apostrophes
		if( word.contains("\'") )
		{
			String[] splitWord = word.split("\'");
			word = "";
			for( int i = 0; i < splitWord.length; i++)
			{
				word += splitWord[i];
			}
		}
		
		// Check for root word (cases the dictionary tends to fail even if these words are there)
		if( word.endsWith("s") && spellchecker.exist(word.substring(0, word.length()-1 )))
		{

		}
		else if( word.endsWith("d") && spellchecker.exist(word.substring(0, word.length()-1 )))
		{

		}
		else if( word.endsWith("ed") && spellchecker.exist(word.substring(0, word.length()-2 )))
		{

		}
		else if( word.endsWith("es") && spellchecker.exist(word.substring(0, word.length()-2 )))
		{

		}
		else if( word.endsWith("er") && spellchecker.exist(word.substring(0, word.length()-2 )))
		{

		}
		else if( word.endsWith("ing") && spellchecker.exist(word.substring(0, word.length()-3 )))
		{

		}
		else if( word.endsWith("est") && spellchecker.exist(word.substring(0, word.length()-3 )))
		{

		}
		else if( word.endsWith("ing") && spellchecker.exist(word.substring(0, word.length()-3)+"e"))
		{

		}
		else if( word.endsWith("ies") && spellchecker.exist(word.substring(0, word.length()-3)+"y"))
		{

		}
		else if( spellchecker.exist(word+"s") )
		{

		}
		// Other common cases causing false positives (word too short?)
		else if( word.equals("we") ||
				word.equals("to") ||
				word.equals("in") ||
				word.equals("be") ||
				word.equals("i") ||
				word.equals("of") ||
				word.equals("has") ||
				word.equals("as") ||
				word.equals("do") ||
				word.equals("is") ||
				word.equals("go") ||
				word.equals("on") ||
				word.equals("if") ||
				word.equals("a") ||
				word.equals("or") ||
				word.equals("an") ||
				word.equals("by") ||
				word.equals("no") ||
				word.equals("up") ||
				word.equals("so") ||
				word.equals("he") ||
				word.equals("my") ||
				word.equals("at") ||
				word.equals("it")
				)
		{

		}
		else if( !spellchecker.exist(word) ) 
			validWord = false;
		
		if( !validWord )
		{
			//System.out.println(word);
			suggestions = spellchecker.suggestSimilar(word, suggestionCount);
		}
		
		return suggestions;
	}
	*/
}
