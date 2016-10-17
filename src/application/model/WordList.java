package application.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class WordList {
	private ArrayList<String> _categoryList;
	private HashMap<String, ArrayList<String>> _wordList;
	private final int WORDS = 10;
	private Path _pathWordList;
	
	public WordList(Path path){
		_categoryList = new ArrayList<String>();
		_wordList = new HashMap<String, ArrayList<String>>();
		_pathWordList = Paths.get(path + "/src/application/assets/NZCER-spelling-lists.txt");
	}
	
	public ArrayList<String> getTestWordList(String category){
		ArrayList<String> testWords = new ArrayList<String>();
		ArrayList<String> temp = _wordList.get(category);
		
		if(temp.size() > WORDS){
			for(int i = 0; i < WORDS; i++){
				int num = (int)(Math.random() * temp.size());
				while(testWords.contains(temp.get(num))){
					num = (int)(Math.random() * WORDS);
				}
				testWords.add(temp.get(num));
			}
		} else {
			testWords=temp;
			long seed = System.nanoTime();
			Collections.shuffle(testWords, new Random(seed));
		}
		return testWords;
	}
	public void setWordListAndCategory(){
		_categoryList = new ArrayList<String>();
		_wordList = new HashMap<String, ArrayList<String>>();
		String category = null;
		try {
			Scanner input = new Scanner(new File(_pathWordList.toString()));
			while (input.hasNext()) {
			    String word = input.nextLine();
			    if(word.contains("%")){
			    	word = (word.substring(1)).toLowerCase().trim();
			    	_categoryList.add(word);
			    	category = word;
			    	_wordList.put(category,new ArrayList<String>());
			    } else {
			    	ArrayList<String> temp = _wordList.get(category);
			    	temp.add(word);
			    }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<String> getWordList(String category){
		return _wordList.get(category);
	}
	public ArrayList<String> getCategoryList(){
		return _categoryList;
	}
	public void setWordListPath(Path path){
		_pathWordList = path;
		setWordListAndCategory();
	}
	public Path getWordListPath(){
		return _pathWordList;
	}
	
}
