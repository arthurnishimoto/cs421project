package cs421.project;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.*; // lucene-suggest-5.1.0.jar
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class NishimotoA_projectP1
{
	public enum ScoreType { Low, Medium, High };
	
	public static void main(String[] args)
	{
		boolean useTokenized = true;
		ScoreType scoreType = ScoreType.High;
		
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
		
		File dir = new File(essayPath);
		File[] filesList = dir.listFiles();
		//for (File file : filesList) {
		//    if (file.isFile()) {
		//        System.out.println(file.getName());
		//    }
		//}
		
		essayParser(filesList[0]);
	}
	
	static void essayParser(File essay)
	{
		if (essay.isFile()) {
	        System.out.println(essay.getName());
	        
	        List<String> essayLines;
	        try
			{
	        	essayLines = Files.readAllLines(essay.toPath());
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
	        
	        System.out.println(essayLines.size());
	        SpellCheck();
	    }
	}
	
	static void SpellCheck()
	{
		File dir = new File("./spellcheck");
		Path path = dir.toPath();
		

		SpellChecker spellchecker;
		try
		{
			Directory spellIndexDirectory = FSDirectory.open(path);
			spellchecker = new SpellChecker(spellIndexDirectory);
			// To index a field of a user index:
			//spellchecker.indexDictionary(new LuceneDictionary(my_lucene_reader, a_field));
			// To index a file containing words:
			Analyzer analyzer = new SimpleAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			spellchecker.indexDictionary(new PlainTextDictionary(new File("./data/fulldictionary00.txt").toPath()), config, false);

			String[] suggestions = spellchecker.suggestSimilar("misspelt", 5);
			System.out.println(suggestions.length);
			for( int  i = 0; i < suggestions.length; i++ )
			{
				System.out.println(i+"] "+suggestions[i]);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
