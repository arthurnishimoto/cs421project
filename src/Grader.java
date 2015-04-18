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
	
	static SpellCheck spellChecker;
	
	// [0] min, [1] count, [2] max
	private static int[] totalSpellingErrors = {9999,0,0};
	private static int[] totalAgreementErrors = {9999,0,0};
	private static int[] totalVerbTenseErrors = {9999,0,0};

	public static void main(String[] args) throws IOException {
		String inputFile;
		
		spellChecker = new SpellCheck("./data/fulldictionary01.txt");
		
		// No arguments. Uses following variables to search/grade directories
		if( args.length == 0 )
		{
			boolean useTokenized = true;
			ScoreType scoreType = ScoreType.Low;
			int essayLimit = 100;
			
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

			// Read the file
			File dir = new File(essayPath);
			File[] filesList = dir.listFiles();
			for( int i = 0; i < filesList.length; i++ )
			{
				inputFile = filesList[i].getPath();
				System.out.println("Reading: "+inputFile);
				gradeFile(inputFile);
				
				if(i >= essayLimit - 1)
					break;
			}
			
			System.out.println("Essays parsed: "+filesList.length);
			System.out.println("totalSpellingErrors: "+totalSpellingErrors[0]+" "+totalSpellingErrors[1]/(float)filesList.length+" "+totalSpellingErrors[2]);
			System.out.println("totalAgreementErrors: "+totalAgreementErrors[0]+" "+totalAgreementErrors[1]/(float)filesList.length+" "+totalAgreementErrors[2]);
			System.out.println("totalVerbTenseErrors: "+totalVerbTenseErrors[0]+" "+totalVerbTenseErrors[1]/(float)filesList.length+" "+totalVerbTenseErrors[2]);
			// -------------------------------------------------
		}
		else // Single argument: specific file to grade
		{
			inputFile = args[0];

			System.out.println("Reading: "+inputFile);
			gradeFile(inputFile);
		}
	}
	
	static void gradeFile(String inputFile) throws IOException
	{
		Parser parser = new Parser();
		parser.parseFile(inputFile); 
		spellChecker.parseFile(inputFile);
		
		spellingErrors = spellChecker.getSpellingErrors();
		agreementErrors = spellChecker.getAgreementErrors();
		verbTenseErrors = spellChecker.getVerbErrors();
		wordCount = parser.getWordCount();
		sentenceCount = parser.getSentenceCount();
		
		Map scores = new Map();
		scores.mapScores(spellingErrors, agreementErrors, verbTenseErrors,
				wordCount, sentenceCount);
		
		finalScore = scores.getFinalScore();
		System.out.println("Score: " + finalScore);
		
		finalRating = scores.getFinalRating();
		System.out.println("Rating: " + finalRating);
		
		totalSpellingErrors[1] += spellingErrors;
		totalAgreementErrors[1] += agreementErrors;
		totalVerbTenseErrors[1] += verbTenseErrors;
		
		if( totalSpellingErrors[0] > spellingErrors )
			totalSpellingErrors[0] = spellingErrors;
		if( totalSpellingErrors[2] < spellingErrors )
			totalSpellingErrors[2] = spellingErrors;
		if( totalAgreementErrors[0] > agreementErrors )
			totalAgreementErrors[0] = agreementErrors;
		if( totalAgreementErrors[2] < agreementErrors )
			totalAgreementErrors[2] = agreementErrors;
		if( totalVerbTenseErrors[0] > verbTenseErrors )
			totalVerbTenseErrors[0] = verbTenseErrors;
		if( totalVerbTenseErrors[2] < verbTenseErrors )
			totalVerbTenseErrors[2] = verbTenseErrors;
	}
}
