import java.io.File;
import java.io.IOException;


public class Grader {
	private static int spellingErrors;
	private static int agreementErrors;
	private static int verbTenseErrors;
	private static int wordCount;
	private static int sentenceCount;
	private static int finalScore;
	
	enum ScoreType { Low, Medium, High };
	
	public static void main(String[] args) throws IOException {
		String inputFile = args[0];
		
		boolean useTokenized = false;
		ScoreType scoreType = ScoreType.Medium;
		
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
		
		inputFile = filesList[8].getPath();
		// -------------------------------------------------
		
		Parser parser = new Parser();
		parser.parseFile(inputFile); 
		
		SpellCheck spellChecker = new SpellCheck("./data/fulldictionary01.txt");
		spellChecker.parseFile(inputFile);
		
		spellingErrors = spellChecker.getSpellingErrors();
		wordCount = parser.getWordCount();
		sentenceCount = parser.getSentenceCount();
		
		Map scores = new Map();
		scores.mapScores(spellingErrors, agreementErrors, verbTenseErrors,
				wordCount, sentenceCount);
		
		finalScore = scores.getFinalScore();
		System.out.println("Rating: " + finalScore);
	}

}
