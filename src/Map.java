
public class Map {
	private int _1a;
	private int _1b;
	private int _1c;
	private int _3a;
	private int finalScore;

	public void mapScores(int spellingErrors, int agreementErrors,
			int verbTenseErrors, int wordCount, int sentenceCount) {
		_1a = score_1a(spellingErrors);
		_1b = score_1b(agreementErrors);
		_1c = score_1c(verbTenseErrors);
		_3a = score_3a(wordCount, sentenceCount);
		finalScore = (_1a + _1b + _1c + _3a)/4;
	}
	
	public int getFinalScore() {
		return finalScore;
	}
	
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
