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
	private static int[] totalSpellingErrorsLow = {9999,0,0};
	private static int[] totalSpellingErrorsMed = {9999,0,0};
	private static int[] totalSpellingErrorsHigh = {9999,0,0};
	private static int[] totalAgreementErrorsLow = {9999,0,0};
	private static int[] totalAgreementErrorsMed = {9999,0,0};
	private static int[] totalAgreementErrorsHigh = {9999,0,0};
	private static int[] totalVerbTenseErrorsLow = {9999,0,0};
	private static int[] totalVerbTenseErrorsMed = {9999,0,0};
	private static int[] totalVerbTenseErrorsHigh = {9999,0,0};
	private static int[] totalSentenceFormationErrorsLow = {9999,0,0};
	private static int[] totalSentenceFormationErrorsMed = {9999,0,0};
	private static int[] totalSentenceFormationErrorsHigh = {9999,0,0};
	
	private static int[] wordCountLow = {9999,0,0};
	private static int[] wordCountMed = {9999,0,0};
	private static int[] wordCountHigh = {9999,0,0};
	
	private static int[] sentenceCountLow = {9999,0,0};
	private static int[] sentenceCountMed = {9999,0,0};
	private static int[] sentenceCountHigh = {9999,0,0};
	
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
		
		float essayCount = 30.0f;
		System.out.println("Spelling Error (min|avg|max)");
		System.out.println(" High Score: "+ totalSpellingErrorsHigh[0] +"|"+ (totalSpellingErrorsHigh[1]/essayCount) +"|"+totalSpellingErrorsHigh[2]);
		System.out.println(" Medium Score: "+ totalSpellingErrorsMed[0] +"|"+ (totalSpellingErrorsMed[1]/essayCount)+"|"+totalSpellingErrorsMed[2]);
		System.out.println(" Low Score: "+ totalSpellingErrorsLow[0] +"|"+ (totalSpellingErrorsLow[1]/essayCount)+"|"+totalSpellingErrorsLow[2]);
		
		
		System.out.println("Subject-Verb Agreement Error (min|avg|max)");
		System.out.println(" High Score: "+ totalAgreementErrorsHigh[0] +"|"+ (totalAgreementErrorsHigh[1]/essayCount) +"|"+totalAgreementErrorsHigh[2]);
		System.out.println(" Medium Score: "+ totalAgreementErrorsMed[0] +"|"+ (totalAgreementErrorsMed[1]/essayCount)+"|"+totalAgreementErrorsMed[2]);
		System.out.println(" Low Score: "+ totalAgreementErrorsLow[0] +"|"+ (totalAgreementErrorsLow[1]/essayCount)+"|"+totalAgreementErrorsLow[2]);
		
		
		System.out.println("Verb Tense Error (min|avg|max)");
		System.out.println(" High Score: "+ totalVerbTenseErrorsHigh[0] +"|"+ (totalVerbTenseErrorsHigh[1]/essayCount) +"|"+totalVerbTenseErrorsHigh[2]);
		System.out.println(" Medium Score: "+ totalVerbTenseErrorsMed[0] +"|"+ (totalVerbTenseErrorsMed[1]/essayCount)+"|"+totalVerbTenseErrorsMed[2]);
		System.out.println(" Low Score: "+ totalVerbTenseErrorsLow[0] +"|"+ (totalVerbTenseErrorsLow[1]/essayCount)+"|"+totalVerbTenseErrorsLow[2]);
		
		System.out.println("Sentence Formation Error (min|avg|max)");
		System.out.println(" High Score: "+ totalSentenceFormationErrorsHigh[0] +"|"+ (totalSentenceFormationErrorsHigh[1]/essayCount) +"|"+totalSentenceFormationErrorsHigh[2]);
		System.out.println(" Medium Score: "+ totalSentenceFormationErrorsMed[0] +"|"+ (totalSentenceFormationErrorsMed[1]/essayCount)+"|"+totalSentenceFormationErrorsMed[2]);
		System.out.println(" Low Score: "+ totalSentenceFormationErrorsLow[0] +"|"+ (totalSentenceFormationErrorsLow[1]/essayCount)+"|"+totalSentenceFormationErrorsLow[2]);
		
		System.out.println("Word Count (min|avg|max)");
		System.out.println(" High Score: "+ wordCountHigh[0] +"|"+ (wordCountHigh[1]/essayCount) +"|"+wordCountHigh[2]);
		System.out.println(" Medium Score: "+ wordCountMed[0] +"|"+ (wordCountMed[1]/essayCount)+"|"+wordCountMed[2]);
		System.out.println(" Low Score: "+ wordCountLow[0] +"|"+ (wordCountLow[1]/essayCount)+"|"+wordCountLow[2]);
		
		System.out.println("Sentence Count (min|avg|max)");
		System.out.println(" High Score: "+ sentenceCountHigh[0] +"|"+ (sentenceCountHigh[1]/essayCount) +"|"+sentenceCountHigh[2]);
		System.out.println(" Medium Score: "+ sentenceCountMed[0] +"|"+ (sentenceCountMed[1]/essayCount)+"|"+sentenceCountMed[2]);
		System.out.println(" Low Score: "+ sentenceCountLow[0] +"|"+ (sentenceCountLow[1]/essayCount)+"|"+sentenceCountLow[2]);
		
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
		
		if( currentScoreType == ScoreType.High )
		{
			CalculateErrorStats(totalSpellingErrorsHigh, spellingErrors);
			CalculateErrorStats(totalAgreementErrorsHigh, agreementErrors);
			CalculateErrorStats(totalVerbTenseErrorsHigh, verbTenseErrors);
			CalculateErrorStats(totalSentenceFormationErrorsHigh, sentenceFormationErrors);
			CalculateErrorStats(sentenceCountHigh, sentenceCount);
			CalculateErrorStats(wordCountHigh, wordCount);
		}
		else if( currentScoreType == ScoreType.Medium )
		{
			
			CalculateErrorStats(totalSpellingErrorsMed, spellingErrors);
			CalculateErrorStats(totalAgreementErrorsMed, agreementErrors);
			CalculateErrorStats(totalVerbTenseErrorsMed, verbTenseErrors);
			CalculateErrorStats(totalSentenceFormationErrorsMed, sentenceFormationErrors);
			CalculateErrorStats(sentenceCountMed, sentenceCount);
			CalculateErrorStats(wordCountMed, wordCount);
		}
		else if( currentScoreType == ScoreType.Low )
		{
			CalculateErrorStats(totalSpellingErrorsLow, spellingErrors);
			CalculateErrorStats(totalAgreementErrorsLow, agreementErrors);
			CalculateErrorStats(totalVerbTenseErrorsLow, verbTenseErrors);
			CalculateErrorStats(totalSentenceFormationErrorsLow, sentenceFormationErrors);
			CalculateErrorStats(sentenceCountLow, sentenceCount);
			CalculateErrorStats(wordCountLow, wordCount);
		}
	}
	
	 static void CalculateErrorStats( int[] dataArray, int errorCount )
	 {
		 dataArray[1] += errorCount;
		 
		if( dataArray[0] > errorCount )
			dataArray[0] = errorCount;
		if( dataArray[2] < errorCount )
			dataArray[2] = errorCount;	 
	 }
}