/**
 * ---------------------------------------------
 * Map.java
 * Description: Maps scores to different parts of grader
 *
 * Class: CS 421 - Spring 2015
 * System: Eclipse Luna, Mac Mavericks OSX
 * Author: Jenny Sum (sum1)
 * Version: 1.0
 *
 * ---------------------------------------------
 */

public class Map {
	private int _1a;
	private int _1b;
	private int _1c;
	private int _3a;
	private int finalScore;
	private String finalRating;

	public void mapScores(int spellingErrors, int agreementErrors,
			int verbTenseErrors, int wordCount, int sentenceCount) {
		_1a = score_1a(spellingErrors);
		_1b = score_1b(agreementErrors);
		_1c = score_1c(verbTenseErrors);
		_3a = score_3a(wordCount, sentenceCount);
		finalScore = (_1a + _1b + _1c + _3a)/4;
		finalRating = mapRating(finalScore);
	}
	
	//getter for overall int score
	public int getFinalScore() {
		return finalScore;
	}
	
	//getter for overall rating
	public String getFinalRating() {
		return finalRating;
	}
	
	//map the score to the 3 ratings
	private String mapRating (int finalScore) {
		String rating;
		
		switch(finalScore)
		{
			case 1:	rating = "low"; break;	 
			case 2: rating = "low"; break;	 
			case 3:	rating = "medium"; break; 
			case 4: rating = "medium"; break;
			case 5: rating = "high"; break;
			default: rating = "Error has occurred while mapping scores."; break;
		}
		
		return rating;
	}
	
	//rates essay 1-5 based on amount of spelling errors
	private int score_1a (int spellingErrors) {
		if (spellingErrors < 10) {
			return 5;
		}
		else if (spellingErrors < 20 && spellingErrors > 10) {
			return 4;
		}
		else if (spellingErrors < 30 && spellingErrors > 20) {
			return 3;
		}
		else if (spellingErrors < 40 && spellingErrors > 30) {
			return 2;
		}
		else {
			return 1;
		}
	}
	
	//rates essay 1-5 based on agreement errors
	private int score_1b (int agreementErrors) {
		if (agreementErrors < 10) {
			return 5;
		}
		else if (agreementErrors < 20 && agreementErrors > 10) {
			return 4;
		}
		else if (agreementErrors < 30 && agreementErrors > 20) {
			return 3;
		}
		else if (agreementErrors < 40 && agreementErrors > 30) {
			return 2;
		}
		else {
			return 1;
		}
	}
	
	//rates scores 1-5 based on verb tense errors
	private int score_1c (int verbTenseErrors) {
		if (verbTenseErrors < 10) {
			return 5;
		}
		else if (verbTenseErrors < 20 && verbTenseErrors > 10) {
			return 4;
		}
		else if (verbTenseErrors < 30 && verbTenseErrors > 20) {
			return 3;
		}
		else if (verbTenseErrors < 40 && verbTenseErrors > 30) {
			return 2;
		}
		else {
			return 1;
		}
	}
	
	//rates 1-5 based on amount of words and amount of sentences
	private int score_3a (int wordCount, int sentenceCount) {
		if (wordCount > 370 && sentenceCount > 20) {
			return 5;
		}
		else if (wordCount < 370 && wordCount > 310 
				&& sentenceCount < 20 && sentenceCount > 17) {
			return 4;
		}
		else if (wordCount < 310 && wordCount > 250 
				&& sentenceCount < 17 && sentenceCount > 14) {
			return 3;
		}
		else if (wordCount < 250 && wordCount > 190 
				&& sentenceCount < 14 && sentenceCount > 11) {
			return 2;
		}
		else {
			return 1;
		}
	}
}
