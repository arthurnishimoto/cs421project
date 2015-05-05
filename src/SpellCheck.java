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

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

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
	int subjectVerbAgreementErrors = 0;
	int verbErrors = 0;
	
	// Part 2
	int sentenceFormationErrors = 0;
	
	boolean showDetail = false;
	boolean showPOSDetail = true;
	
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
	
	public int getAgreementErrors()
	{
		return subjectVerbAgreementErrors;
	}
	
	public int getVerbErrors()
	{
		return verbErrors;
	}
	
	public int getSentenceFormationErrors()
	{
		return sentenceFormationErrors;
	}
	
	private void parseLines(List<String> essayLines) throws IOException
	{
		spellingErrors = 0;
		//System.out.println(essayLines.size());
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
		subjectVerbAgreementErrors = 0;
		verbErrors = 0;
		sentenceFormationErrors = 0;
		/*
		text = new ArrayList<String>();
		//My dog with a broken leg I not want
		text.add("My");
		text.add("dog");
		text.add("with");
		text.add("a");
		text.add("broken");
		text.add("leg");
		text.add("I");
		text.add("not");
		text.add("want");
		text.add(".");
		
		text.add("I");
		text.add("do");
		text.add("not");
		text.add("want");
		text.add("my");
		text.add("dog");
		text.add("with");
		text.add("a");
		text.add("broken");
		text.add("leg");
		
		
		text.add("Because");
		text.add("I");
		text.add("think");
		text.add("the");
		text.add("science");
		text.add("and");
		text.add("technology");
		text.add("are");
		text.add("developing");
		text.add(".");
		
		text.add("Will");
		text.add("be");
		text.add("not");
		text.add("agree");
		text.add(".");
		*/
		try {
		  modelIn = new FileInputStream("./data/en-pos-maxent.bin");
		  POSModel model = new POSModel(modelIn);
		  POSTaggerME tagger = new POSTaggerME(model);
		  
		  modelIn = new FileInputStream("./data/en-chunker.bin");
		  ChunkerModel chunkerModel = new ChunkerModel(modelIn);
		  ChunkerME chunker = new ChunkerME(chunkerModel);
		  
		  String sent[] = text.toArray(new String[text.size()]);
		  String tags[] = tagger.tag( sent );
		  
		  Sequence[] topSequences = chunker.topKSequences(sent, tags);

		  String tag[] = chunker.chunk(sent, tags);
		  //for( int i = 0; i < topSequences.length; i++ )
			//  System.out.println(topSequences[i]);
		  
		  //http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
		  
		  // Parsing 
		  // -1 = unknown, 0 = false, 1 = true
		  int subjectSingular = -1;
		  int subjectPronoun = -1;
		  int verbSingular = -1;
		  int subjectThirdPerson = -1;
		  int verbThirdPerson = -1;
		  // Used for distance
		  int subjectPos = -1;
		  int verbPos = -1;
		  
		  boolean inSentence = false;
		  int mainVerb = 0;
		  int auxVerb = 0;
		  boolean hasSubordinatingConjunction = false;
		  for(int i = 0; i < tags.length; i++)
		  {
			  if( showPOSDetail )
				  System.out.println(i+"]"+sent[i]+"|"+tags[i] + "|" + tag[i]);
			   
			  // Slightly useful (H: 0.25, M: 0.15, L: 0.35)
			  if( tag[i].contains("SBAR") )
			  {
				  if( (i+1) < tag.length && tag[i+1].contains("O") )
				  {
					  System.out.println("sentenceFormationError");
					  sentenceFormationErrors++;
				  }
			  }
			  
			  // Beginning of essay OR sentence
			  if( i == 0 || ((i-1 >= 0) && tag[i-1].contains("O") ) 
					  && !tags[i-1].contains("CC")
					  //&& !tags[i].contains("VBZ")
					  )
			  {
				  inSentence = true;
			  }
			  
			// Find main verb
			  if( tags[i].contains("VB") )
			  {
				  if( (i+1) < tags.length && tags[i+1].contains("VB") )
				  {
					  System.out.println("auxVerb");
					  auxVerb++;
				  }
				  else if( (i+1) < tags.length && !tag[i+1].contains("O") )
				  {
					  mainVerb++;
					  
					  // Main verb cannot be start of a sentence
					  if( i == 0 || ((i-1 >= 0) && tag[i-1].contains("O")) && tags[i-1].contains(".") && !tag[i].contains("O") )
					  {
						System.out.println("sentenceFormationError - Main verb starting sentence");
						sentenceFormationErrors++;
					  }
				  }
			  }
			  
			  if( tags[i].contains("IN") )// Preposition / Subordinating conjunctions
			  {
				  hasSubordinatingConjunction = true;
			  }
			  
			  // End of sentence
			  if( sent[i].contains(".") )
			  {
				  if( hasSubordinatingConjunction )
				  {
					  // Sentences with subordinating conjunction must have > 1 main verbs
					  if( mainVerb <= 1)
					  {
						  //System.out.println("SubordinatingConjunction main verb count: " + mainVerb);
						  sentenceFormationErrors++;
					  }
				  }
				  
				 // System.out.println("Aux verb count: "+auxVerb);
				  //if( auxVerb == 0 )
					//  verbErrors++;
				  
				  subjectPos = -1;
				  verbPos = -1;
				  mainVerb = 0;
				  auxVerb = 0;
				  inSentence = false;
				  hasSubordinatingConjunction = false;
			  }

			  // Find subject (nouns or pronouns)
			  //	NN 		Noun, singular or mass
			  // 	NNS 	Noun, plural
			  // 	NNP 	Proper noun, singular
			  // 	NNPS 	Proper noun, plural 
			  if( tags[i].contains("NN") || tags[i].contains("PRP") )
			  {
				  subjectPos = i;
				  subjectThirdPerson = -1;
				  subjectSingular = -1;
				  
				  // Check for plural nouns (NNS) or plural proper nouns (NNPS)
				  if( tags[i].equals("NNS") || tags[i].equals("NNPS") )
				  {
					  if( tags[i].equals("NNPS") )
						  subjectSingular = 0;
				  }
				  // Check for singular nouns (NN) or singular proper nouns (NNP)
				  else if( tags[i].equals("NN") || tags[i].equals("NNP") )
				  {
					  subjectSingular = 1;
					  if( tags[i].equals("NNP") )
						  subjectThirdPerson = 1;
				  }
				  
			  }
			  
			  // Find verbs
			  // 	VB 	Verb, base form
			  // 	VBD 	Verb, past tense
			  // 	VBG 	Verb, gerund or present participle
			  // 	VBN 	Verb, past participle
			  // 	VBP 	Verb, non-3rd person singular present
			  // 	VBZ 	Verb, 3rd person singular present
			  
			  if( sent[i].contains("to") && sent[i+1].contains("be") && !(tags[i+2].equals("VBG") || tags[i+2].equals("VBN")) )
			  {
				  verbErrors++;
			  }
			  
			  if( sent[i].contains("be") && tags[i+1].equals("RB") )
			  {
				  System.out.println("be followed by RB error");
				  verbErrors++;
			  }
			  
			  if( tags[i].contains("VB") )
			  {
				  verbThirdPerson = -1;
				  verbSingular = -1;
				  verbPos = i;
				  
				  // A subject will come before a phrase beginning with of
				  if( sent[i].equals("of") && subjectPos == -1 )
				  {
					  verbErrors++;
				  }
				  
				  if( tags[i].equals("VB") || tags[i].equals("VBD") )
				  {

				  }
				  // Check for singular verbs
				  else if( tags[i].equals("VBP") || tags[i].equals("VBZ") )
				  {
					  verbSingular = 1;
					
					if( tags[i].equals("VBZ") )
					{
						
						verbThirdPerson = 1;
					}
					else
						verbThirdPerson = 0;
					}
					// Check for plural verbs
					else if( !tags[i].equals("VBN") )
					{
						verbSingular = 0;
					}
			  }
			  			  
			  if( subjectPos != -1 && verbPos != -1 && (verbPos - subjectPos) > 0 )
			  {
				  // Rule 0 - 3rd/non-3rd person mismatch
				  if( subjectThirdPerson != -1 && verbThirdPerson != -1 && subjectThirdPerson != verbThirdPerson )
				  {
					  if( showPOSDetail )
					  {
						System.out.println("Subject-Verb Violation: 3rd/non-3rd person mismatch");
					  	System.out.println("Subject: "+sent[subjectPos]+"|"+tags[subjectPos] + " "+subjectPos);
					  	System.out.println("Verb: "+sent[verbPos]+"|"+tags[verbPos] + " "+verbPos);
					  }
					  verbErrors++;
				  }
				  // Rule 1 - plural/singular subject-verb mismatch
				  else if( subjectSingular != -1 && verbSingular != -1 && subjectSingular != verbSingular )
				  {
					  if( showPOSDetail )
					  {
						System.out.println("Subject-Verb Violation: plural/singular mismatch");
					  	System.out.println("Subject: "+sent[subjectPos]+"|"+tags[subjectPos] + " "+subjectPos);
					  	System.out.println("Verb: "+sent[verbPos]+"|"+tags[verbPos] + " "+verbPos);
					  }
					  subjectVerbAgreementErrors++;
				  }
			  }
		  }
		  System.out.println("Subject-Verb Errors: "+subjectVerbAgreementErrors);
		  System.out.println("Other Verb Errors: "+verbErrors);
		  System.out.println("Sentence Formation Errors: "+sentenceFormationErrors);
		  //System.out.println();
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
