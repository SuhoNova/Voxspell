package application.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Firstly, files are checked if exist when initialized.
 * @author alex
 *
 */

public class FileManager {
	private Path _path;
	private Path _defaultWordListPath;
	private Path _pathWordList;
	//private String _pathVideo;
	private Path _pathAccuracy;
	private Path _pathStatistics;
	private Path _pathFailed;

	private WordList _wordList;
	private ArrayList<String> _voiceList;

	public FileManager(){
		this.setPath();
		// check files at startup and make it if doesn't exist
		checkFile(_pathStatistics);
		checkFailed();
		_wordList = new WordList(_path);
		_wordList.setWordListAndCategory();
		checkAccuracy();
		
		addVoiceList();
		
		// set Paths
		//this.setWordListPath(_wordList.getWordListPath());
		// check files at startup and make it if doesn't exist
	}
	/**
	 * Accuracy
	 */
	public void updateAccuracy(String category, int totalTested, int totalCorrect, int personalBest){
		boolean written = false;
		File tempFile = new File("temp.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			Scanner input = new Scanner(_pathAccuracy);
			while(input.hasNextLine()){
				String line = input.nextLine().toLowerCase().trim();
				String[] separated = line.split("_");
				//System.out.println(line + " " + category);
				if(separated[0].equalsIgnoreCase(category)){
					writer.write(category+"_"+totalTested+"_"+totalCorrect+"_"+personalBest + System.getProperty("line.separator"));
					written = true;
				} else{
					writer.write(line + System.getProperty("line.separator"));
				}
			}
			if(!written){
				writer.write(category+"_"+totalTested+"_"+totalCorrect+"_"+personalBest);// + System.getProperty("line.separator"));
			}
			writer.close();
			tempFile.renameTo(new File(_pathAccuracy.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get data from accuracy file ie total words tested, total words correct
	 * @param category
	 * @return
	 */
	public int getCategoryTotalTested(String category){
		return getFromAccuracyFile(category, 1);
	}
	public int getCategoryTotalCorrect(String category){
		return getFromAccuracyFile(category, 2);
	}
	public int getPersonalBest(String category){
		return getFromAccuracyFile(category, 3);
	}
	public int getFromAccuracyFile(String category, int pos){
		String[] separated = null;
		try { 
			BufferedReader inputFile = new BufferedReader(new FileReader(_pathAccuracy.toString()));
			String line;
			while((line = inputFile.readLine())!=null){
				separated = line.split("_");
				if((separated[0]).equals(category)){
					return Integer.parseInt(separated[pos]);
				}
			}
			inputFile.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * Checking files
	 */
	// if word exists in file return true, if not return false
	private boolean isInFile(String word, String path) {
		try {
			ArrayList<String> wordsList = new ArrayList<String>();
			BufferedReader inputFile = new BufferedReader(new FileReader(path));
			String line;
			while((line = inputFile.readLine())!=null){
				line=line.trim();
				wordsList.add(line);
			}
			inputFile.close();
			return wordsList.contains(word);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	private void checkFile(Path path){
		try {
			PrintWriter outputFile;
			outputFile = new PrintWriter(new FileWriter(path.toString(), true));
			outputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void checkFailed(){
		try {
			File f = new File(_pathFailed.toString());
			if(!f.exists()){
				PrintWriter outputFile;
				outputFile = new PrintWriter(new FileWriter(_pathFailed.toString(), true));
				outputFile.println("%review");
				outputFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// Accuracy Format: categoryName_totalTested_correct
	private void checkAccuracy(){
		try {
			File f = new File(_pathAccuracy.toString());
			if(!f.exists()){
				PrintWriter outputFile;
				outputFile = new PrintWriter(new FileWriter(_pathAccuracy.toString(), true));
				ArrayList<String> category = _wordList.getCategoryList();
				for(String s : category){
					outputFile.println(s + "_0_0_0");
				}
				outputFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get and set Statistics of user
	 */
	// For a specific category, it gets stats: words and an integer(0=fail,1=mastered)
	public HashMap<String, Integer> getStatisticsWords(String category){
		checkFile(_pathStatistics);
		boolean isStats = false;
		HashMap<String, Integer> _stats = new HashMap<String, Integer>();
		try {
			Scanner input = new Scanner(_pathStatistics);
			while(input.hasNextLine()){
				String line = input.nextLine().toLowerCase();
				if(isStats){
					if(line.contains("%")){
						break;
					}else {
						String[] temp = line.split("_");
						// 0 = failed, 1 = mastered
						int passedOrFailed = Integer.parseInt(temp[1]);
						_stats.put(temp[0], passedOrFailed);
					}
				}
				String categoryTemp = line.substring(1, line.length());
				if(line.contains("%") && categoryTemp.equalsIgnoreCase(category)){
					isStats = true;
				}
			}
		} catch (IOException e) {
			errorDialog("Error in opening statistics file");
			e.printStackTrace();
		}
		return _stats;
	}
	/**
	 * 
	 * @param stats
	 * @param category
	 */
	public void updateStatistics(HashMap<String, Integer> stats, String category){
		File tempFile = new File("temp.txt");
		boolean isStats = false;
		boolean written = false;

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			Scanner input = new Scanner(_pathStatistics);
			while(input.hasNextLine()){
				String line = input.nextLine().trim();
				// next category found while ignoring the requested category words
				if(isStats){
					if(line.contains("%")){
						isStats = false;
						ArrayList<String> temp = new ArrayList<String>();
						for(String word : stats.keySet()){
							String wordLine = word + "_" + stats.get(word);
							writer.write(wordLine + System.getProperty("line.separator"));
							temp.add(word);
						}
						for(String s : temp){
							stats.remove(s);
						}
						writer.write(line + System.getProperty("line.separator"));
					}else{
						String[] separated = line.split("_");
						if(!stats.containsKey(separated[0])){
							writer.write(line + System.getProperty("line.separator"));
						} else {
							writer.write(separated[0] +"_"+stats.get(separated[0]) + System.getProperty("line.separator"));
							stats.remove(separated[0]);
						}
					}
				}else {
					if(line.contains("%") && line.equalsIgnoreCase("%"+category)){
						writer.write(line + System.getProperty("line.separator"));
						isStats = true;
						written = true;
					} else {
						writer.write(line + System.getProperty("line.separator"));
					}
				}
			}
			if(!written){
				writer.write("%"+category + System.getProperty("line.separator"));
				for(String word : stats.keySet()){
					String wordLine = word + "_" + stats.get(word);
					writer.write(wordLine + System.getProperty("line.separator"));
				}
			}
			if(!stats.isEmpty()){
				for(String word : stats.keySet()){
					String wordLine = word + "_" + stats.get(word);
					writer.write(wordLine + System.getProperty("line.separator"));
				}
			}
			writer.close();
			tempFile.renameTo(new File(_pathStatistics.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 *Add failed words to failed file 
	 */
	public void addFailedWords(ArrayList<String> failed){
		checkFailed();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(_pathFailed.toString(), true));
			for(String s:failed){
				String word = s.trim().toLowerCase();
				if(!isInFile(s,_pathFailed.toString())){
					writer.write(s + System.getProperty("line.separator"));
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void removeWordFailed(ArrayList<String> mastered){
		File tempFile = new File("temp.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			Scanner input = new Scanner(_pathFailed);
			while(input.hasNextLine()){
				String line = input.nextLine().trim();
				if(!mastered.contains(line.toLowerCase())){
					writer.write(line + System.getProperty("line.separator"));
				}
			}
			writer.close();
			tempFile.renameTo(new File(_pathFailed.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Clearing data
	 */
	public void clearData(){
		clearFile(_pathStatistics);
		clearFailed();
		clearAccuracy();
	}
	private void clearFile(Path path){
		try{
			PrintWriter outputFile;
			outputFile = new PrintWriter(new FileWriter(path.toString(), false));
			outputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void clearAccuracy(){
		try {
			PrintWriter outputFile;
			outputFile = new PrintWriter(new FileWriter(_pathAccuracy.toString(), false));
			ArrayList<String> category = _wordList.getCategoryList();
			for(String s : category){
				outputFile.println(s + "_0_0_0");
			}
			outputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void clearFailed(){
		try {
			PrintWriter outputFile;
			outputFile = new PrintWriter(new FileWriter(_pathFailed.toString(), false));
			outputFile.println("%review");
			outputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean checkCorrectWordListFile(Path path){
		Scanner input;
		try {
			input = new Scanner(path);
			while(input.hasNextLine()){
				String line = input.nextLine().trim();
				if(line.startsWith("%")){
					return true;
				}
				return false;
			}
		} catch (IOException e) {
			errorDialog("Error in opening file");
			e.printStackTrace();
		}
		return false;
	}
	// SET PATHS ***MAY GET ERRORS IF SPACES IN PATH
	/**
	 * Getting and setting paths of files
	 */
	private void setPath(){
		_path = Paths.get(System.getProperty("user.dir"));
		_defaultWordListPath = Paths.get(_path + "/assets/NZCER-spelling-lists.txt");
		_pathAccuracy = Paths.get(_path.toString() + "/assets/.accuracy");
		_pathStatistics = Paths.get(_path.toString() + "/assets/.statistics");
		_pathFailed = Paths.get(_path.toString() + "/assets/.failed");
	}
	public Path getDefaultWordListPath(){
		return _defaultWordListPath;
	}
	/**
	 * Check if valid
	 */
	public void setWordListPath(Path path){
		_pathWordList = path;
		_wordList.setWordListPath(path);
	}
	public Path getWordListPath(){
		return _wordList.getWordListPath();
	}
	public ArrayList<String> getFailedList(){
		ArrayList<String> failedList = new ArrayList<String>();
		try {
			Scanner input = new Scanner(_pathFailed);
			while(input.hasNextLine()){
				String line = input.nextLine();
				if(!line.equalsIgnoreCase("%review")){
					failedList.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return failedList;
	}
	private void addVoiceList(){
		_voiceList = new ArrayList<String>();
		_voiceList.add("american");
		_voiceList.add("new zealander");
	}
	public ArrayList<String> getVoiceList(){
		return _voiceList;
	}
	public ArrayList<String> getCategoryList(){
		return _wordList.getCategoryList();
	}
	public ArrayList<String> getTestWordList(String category){
		return _wordList.getTestWordList(category);
	}
	public void setWordListAndCategory(){
		_wordList.setWordListAndCategory();
	}
	/**
	 * Error and warning message template
	 * @param content
	 */
	private void errorDialog(String content){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText(content);

		alert.showAndWait();
	}
	private void warningDialog(String content){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText("Warning");
		alert.setContentText(content);

		alert.showAndWait();
	}
}
