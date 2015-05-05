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
		
		System.out.println("Word Count (max|avg|min)");
		System.out.println(" High Score: "+ wordCountHigh[0] +"|"+ (wordCountHigh[1]/20.0) +"|"+wordCountHigh[2]);
		System.out.println(" Medium Score: "+ wordCountMed[0] +"|"+ (wordCountMed[1]/20.0)+"|"+wordCountMed[2]);
		System.out.println(" Low Score: "+ wordCountLow[0] +"|"+ (wordCountLow[1]/20.0)+"|"+wordCountLow[2]);
		
		System.out.println("Sentence Count (max|avg|min)");
		System.out.println(" High Score: "+ sentenceCountHigh[0] +"|"+ (sentenceCountHigh[1]/20.0) +"|"+sentenceCountHigh[2]);
		System.out.println(" Medium Score: "+ sentenceCountMed[0] +"|"+ (sentenceCountMed[1]/20.0)+"|"+sentenceCountMed[2]);
		System.out.println(" Low Score: "+ sentenceCountLow[0] +"|"+ (sentenceCountLow[1]/20.0)+"|"+sentenceCountLow[2]);
		
		System.out.println("Verb Tense Error (max|avg|min)");
		System.out.println(" High Score: "+ totalVerbTenseErrorsHigh[0] +"|"+ (totalVerbTenseErrorsHigh[1]/20.0) +"|"+totalVerbTenseErrorsHigh[2]);
		System.out.println(" Medium Score: "+ totalVerbTenseErrorsMed[0] +"|"+ (totalVerbTenseErrorsMed[1]/20.0)+"|"+totalVerbTenseErrorsMed[2]);
		System.out.println(" Low Score: "+ totalVerbTenseErrorsLow[0] +"|"+ (totalVerbTenseErrorsLow[1]/20.0)+"|"+totalVerbTenseErrorsLow[2]);
		
		
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
		
		if( currentScoreType == ScoreType.High )
		{
			sentenceCountHigh[1] += sentenceCount;
			totalSentenceFormationErrorsHigh[1] += sentenceFormationErrors;
			totalVerbTenseErrorsHigh[1] += verbTenseErrors;
			
			if( sentenceCountHigh[0] > sentenceCount )
				sentenceCountHigh[0] = sentenceCount;
			if( sentenceCountHigh[2] < sentenceCount )
				sentenceCountHigh[2] = sentenceCount;
			
			wordCountHigh[1] += wordCount;
			if( wordCountHigh[0] > wordCount )
				wordCountHigh[0] = wordCount;
			if( wordCountHigh[2] < wordCount )
				wordCountHigh[2] = wordCount;
			
			if( totalSentenceFormationErrorsHigh[0] > sentenceFormationErrors )
				totalSentenceFormationErrorsHigh[0] = sentenceFormationErrors;
			if( totalSentenceFormationErrorsHigh[2] < sentenceFormationErrors )
				totalSentenceFormationErrorsHigh[2] = sentenceFormationErrors;
			
			if( totalVerbTenseErrorsHigh[0] > verbTenseErrors )
				totalVerbTenseErrorsHigh[0] = verbTenseErrors;
			if( totalVerbTenseErrorsHigh[2] < verbTenseErrors )
				totalVerbTenseErrorsHigh[2] = verbTenseErrors;
		}
		else if( currentScoreType == ScoreType.Medium )
		{
			
			totalSentenceFormationErrorsMed[1] += sentenceFormationErrors;
			totalVerbTenseErrorsMed[1] += verbTenseErrors;
			
			sentenceCountMed[1] += sentenceCount;
			if( sentenceCountMed[0] > sentenceCount )
				sentenceCountMed[0] = sentenceCount;
			if( sentenceCountMed[2] < sentenceCount )
				sentenceCountMed[2] = sentenceCount;
			
			wordCountMed[1] += wordCount;
			if( wordCountMed[0] > wordCount )
				wordCountMed[0] = wordCount;
			if( wordCountMed[2] < wordCount )
				wordCountMed[2] = wordCount;
			
			if( totalSentenceFormationErrorsMed[0] > sentenceFormationErrors )
				totalSentenceFormationErrorsMed[0] = sentenceFormationErrors;
			if( totalSentenceFormationErrorsMed[2] < sentenceFormationErrors )
				totalSentenceFormationErrorsMed[2] = sentenceFormationErrors;
			
			if( totalVerbTenseErrorsMed[0] > verbTenseErrors )
				totalVerbTenseErrorsMed[0] = verbTenseErrors;
			if( totalVerbTenseErrorsMed[2] < verbTenseErrors )
				totalVerbTenseErrorsMed[2] = verbTenseErrors;
		}
		else if( currentScoreType == ScoreType.Low )
		{
			totalSentenceFormationErrorsLow[1] += sentenceFormationErrors;
			totalVerbTenseErrorsLow[1] += verbTenseErrors;
			
			sentenceCountLow[1] += sentenceCount;
			if( sentenceCountLow[0] > sentenceCount )
				sentenceCountLow[0] = sentenceCount;
			if( sentenceCountLow[2] < sentenceCount )
				sentenceCountLow[2] = sentenceCount;
			
			wordCountLow[1] += wordCount;
			if( wordCountLow[0] > wordCount )
				wordCountLow[0] = wordCount;
			if( wordCountLow[2] < wordCount )
				wordCountLow[2] = wordCount;
			
			if( totalSentenceFormationErrorsLow[0] > sentenceFormationErrors )
				totalSentenceFormationErrorsLow[0] = sentenceFormationErrors;
			if( totalSentenceFormationErrorsLow[2] < sentenceFormationErrors )
				totalSentenceFormationErrorsLow[2] = sentenceFormationErrors;
			
			if( totalVerbTenseErrorsLow[0] > verbTenseErrors )
				totalVerbTenseErrorsLow[0] = verbTenseErrors;
			if( totalVerbTenseErrorsLow[2] < verbTenseErrors )
				totalVerbTenseErrorsLow[2] = verbTenseErrors;
		}
		
		if( totalSpellingErrors[0] > spellingErrors )
			totalSpellingErrors[0] = spellingErrors;
		if( totalSpellingErrors[2] < spellingErrors )
			totalSpellingErrors[2] = spellingErrors;
		if( totalAgreementErrors[0] > agreementErrors )
			totalAgreementErrors[0] = agreementErrors;
		if( totalAgreementErrors[2] < agreementErrors )
			totalAgreementErrors[2] = agreementErrors;
	}
}
