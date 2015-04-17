import java.io.File;
import java.io.IOException;

/**
 * ---------------------------------------------
 * Grader.java
 * Description: Main function
 *
 * Class: CS 421 - Spring 2015
 * System: Eclipse Luna, Mac Mavericks OSX
 * Author: Arthur Nishimoto (anishi2) and Jenny Sum (sum1)
 * Version: 1.0
 *
 * ---------------------------------------------
 */

public class Grader {
	private static int spellingErrors;
	private static int agreementErrors;
	private static int verbTenseErrors;
	private static int wordCount;
	private static int sentenceCount;
	private static int finalScore;
	private static String finalRating;
	
	enum ScoreType { Low, Medium, High };
	
	public static void main(String[] args) throws IOException {
		String inputFile = args[0];
		
		boolean useTokenized = false;
		ScoreType scoreType = ScoreType.High;
		
		// Setup data paths --------------------------------
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
		
		SpellCheck spellChecker = new SpellCheck("./data/fulldictionary01.txt");
		
		// Read the file
		File dir = new File(essayPath);
		File[] filesList = dir.listFiles();
		for( int i = 0; i < filesList.length; i++ )
		{
			inputFile = filesList[i].getPath();
			spellChecker.parseFile(inputFile);
		}
		// -------------------------------------------------
		
		Parser parser = new Parser();
		parser.parseFile(inputFile); 

		spellingErrors = spellChecker.getSpellingErrors();
		wordCount = parser.getWordCount();
		sentenceCount = parser.getSentenceCount();
		
		Map scores = new Map();
		scores.mapScores(spellingErrors, agreementErrors, verbTenseErrors,
				wordCount, sentenceCount);
		
		finalScore = scores.getFinalScore();
		System.out.println("Score: " + finalScore);
		
		finalRating = scores.getFinalRating();
		System.out.println("Rating: " + finalRating);
	}

}
