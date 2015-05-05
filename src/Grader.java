import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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
	private static int sentenceFormationErrors;
	private static int wordCount;
	private static int sentenceCount;
	private static int finalScore;
	private static String finalRating;
	
	enum ScoreType { Low, Medium, High };
	static ScoreType currentScoreType = ScoreType.Low;
	
	static SpellCheck spellChecker;
	
	// [0] min, [1] count, [2] max
	private static int[] totalSpellingErrors = {9999,0,0};
	private static int[] totalAgreementErrors = {9999,0,0};
	private static int[] totalVerbTenseErrors = {9999,0,0};
	private static int[] totalSentenceFormationErrorsLow = {9999,0,0};
	private static int[] totalSentenceFormationErrorsMed = {9999,0,0};
	private static int[] totalSentenceFormationErrorsHigh = {9999,0,0};
	
	public static void main(String[] args) throws IOException {
		String inputFile;
		
		spellChecker = new SpellCheck("./data/fulldictionary01.txt");
		
		// Setup output file
		PrintStream out = new PrintStream(new FileOutputStream("./output/results.txt"));
					
		// No arguments. Uses following variables to search/grade directories
		if( args.length == 1) // Single argument: specific file to grade
		{
			inputFile = args[0];

			System.out.println("Reading: "+inputFile);
			gradeFile(inputFile, out);
		}
		else
		{
			boolean useTrainingSet = true;
			boolean useTokenized = true;
			boolean automatedFullTraining = true;
			ScoreType scoreType = ScoreType.High;
			int essayLimit = 100;
			
			if( automatedFullTraining )
			{
				for( int x = 0; x < 3; x++ )
				{
					// Setup data paths --------------------------------
					String essayPath = "./input/";
					if(useTrainingSet)
						essayPath += "training/";
					else
						essayPath += "test/";
					if(useTokenized)
						essayPath += "tokenized/";
					else
						essayPath += "original/";
					
					if(useTrainingSet)
					{
						switch(x)
						{
							case 0:	essayPath += "high/";	currentScoreType = ScoreType.High; break;
							case 1:essayPath += "medium/";	currentScoreType = ScoreType.Medium; break;
							case 2:	essayPath += "low/";	currentScoreType = ScoreType.Low; break;
							default:	essayPath += "high/";	break;
						}
					}
					// Read the file
					File dir = new File(essayPath);
					File[] filesList = dir.listFiles();
					
					for( int i = 0; i < filesList.length; i++ )
					{
						inputFile = filesList[i].getPath();
						System.out.println("Reading: "+inputFile);
						
						if(useTrainingSet)
							out.print(inputFile+"\t");
						else
							out.print(filesList[i].getName()+"\t");
						
						gradeFile(inputFile,out);
						if(i >= essayLimit - 1)
							break;
					}
					
				}
			}
			
			//System.out.println("Essays parsed: "+filesList.length);
			//System.out.println("totalSpellingErrors: "+totalSpellingErrors[0]+" "+totalSpellingErrors[1]/(float)filesList.length+" "+totalSpellingErrors[2]);
			//System.out.println("totalAgreementErrors: "+totalAgreementErrors[0]+" "+totalAgreementErrors[1]/(float)filesList.length+" "+totalAgreementErrors[2]);
			//System.out.println("totalVerbTenseErrors: "+totalVerbTenseErrors[0]+" "+totalVerbTenseErrors[1]/(float)filesList.length+" "+totalVerbTenseErrors[2]);
			// -------------------------------------------------
		}
		
		System.out.println("Sentence Formation Error (max|avg|min)");
		System.out.println(" High Score: "+ totalSentenceFormationErrorsHigh[0] +"|"+ (totalSentenceFormationErrorsHigh[1]/20.0) +"|"+totalSentenceFormationErrorsHigh[2]);
		System.out.println(" Medium Score: "+ totalSentenceFormationErrorsMed[0] +"|"+ (totalSentenceFormationErrorsMed[1]/20.0)+"|"+totalSentenceFormationErrorsMed[2]);
		System.out.println(" Low Score: "+ totalSentenceFormationErrorsLow[0] +"|"+ (totalSentenceFormationErrorsLow[1]/20.0)+"|"+totalSentenceFormationErrorsLow[2]);
		
		System.out.println("Finished");
		out.close();
	}

	static void gradeFile(String inputFile, PrintStream out) throws IOException
	{
		Parser parser = new Parser();
		parser.parseFile(inputFile); 
		spellChecker.parseFile(inputFile);
		
		
		spellingErrors = spellChecker.getSpellingErrors();
		agreementErrors = spellChecker.getAgreementErrors();
		verbTenseErrors = spellChecker.getVerbErrors();
		wordCount = parser.getWordCount();
		sentenceCount = parser.getSentenceCount();
		
		// Part 2
		sentenceFormationErrors = spellChecker.getSentenceFormationErrors();

		Map scores = new Map();
		scores.mapScores(spellingErrors, agreementErrors, verbTenseErrors, sentenceFormationErrors,
				wordCount, sentenceCount, out);
		
		finalScore = scores.getFinalScore();
		System.out.println("Score: " + finalScore);
		
		finalRating = scores.getFinalRating();
		System.out.println("Rating: " + finalRating);
		
		totalSpellingErrors[1] += spellingErrors;
		totalAgreementErrors[1] += agreementErrors;
		totalVerbTenseErrors[1] += verbTenseErrors;
		
		if( currentScoreType == ScoreType.High )
		{
			totalSentenceFormationErrorsHigh[1] += sentenceFormationErrors;
			
			if( totalSentenceFormationErrorsHigh[0] > sentenceFormationErrors )
				totalSentenceFormationErrorsHigh[0] = sentenceFormationErrors;
			if( totalSentenceFormationErrorsHigh[2] < sentenceFormationErrors )
				totalSentenceFormationErrorsHigh[2] = sentenceFormationErrors;
		}
		else if( currentScoreType == ScoreType.Medium )
		{
			totalSentenceFormationErrorsMed[1] += sentenceFormationErrors;
			
			if( totalSentenceFormationErrorsMed[0] > sentenceFormationErrors )
				totalSentenceFormationErrorsMed[0] = sentenceFormationErrors;
			if( totalSentenceFormationErrorsMed[2] < sentenceFormationErrors )
				totalSentenceFormationErrorsMed[2] = sentenceFormationErrors;
		}
		else if( currentScoreType == ScoreType.Low )
		{
			totalSentenceFormationErrorsLow[1] += sentenceFormationErrors;
			
			if( totalSentenceFormationErrorsLow[0] > sentenceFormationErrors )
				totalSentenceFormationErrorsLow[0] = sentenceFormationErrors;
			if( totalSentenceFormationErrorsLow[2] < sentenceFormationErrors )
				totalSentenceFormationErrorsLow[2] = sentenceFormationErrors;
		}
		
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
