package application;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import application.model.BackgroundMusic;
import application.model.FileManager;

/**
 * This is the main model of the Voxspell program.
 * It tells other models what to do, and communicates with views and controllers.
 * 
 * @author syu680, Alex (Suho) Yu
 *
 */

public class VoxspellModel {
	private FileManager _fm;
	private BackgroundMusic _bm;
	
	private String _voice;
	private ArrayList<String> _voiceList;
	private String _category;
	private ArrayList<String> _categoryList;
	
	private ArrayList<String> _wordTestList;
	private int _wordIndex;
	
	private int _accuracyTotal;
	private int _accuracyTotalCorrect;
	private int _accuracy;
	private int _personalBest;
	
	private Path _wordFile;
	
	private boolean _reward;
	
	public VoxspellModel(){
		_fm = new FileManager();
		_bm = new BackgroundMusic();

		_categoryList = _fm.getCategoryList();
		_category = _categoryList.get(1);
		
		_voice = "american";
		_voiceList = _fm.getVoiceList();
		
		_accuracyTotal = getTotalWordsTested(_category);
		_accuracyTotalCorrect = getTotalCorrect(_category);
		_personalBest = getPersonalBest(_category);
		_accuracy = 0;
		
		_wordFile = getWordListPath();
	}
	/**
	 * Setting and getting category
	 * When category is set, the info of the current category is updated using setWordListAndCategory method
	 */
	public String getCategory(){
		return _category;
	}
	public void setCategory(String category){
		_category = category.toLowerCase();
		this.setWordListAndCategory();
	}
	public void setWordListAndCategory(){
		_fm.setWordListAndCategory();
	}
	/**
	 *  Setting and getting accuracy of a category: total, tested, personal best, calculation
	 */
	public int getTotalWordsTested(String category){
		return _fm.getCategoryTotalTested(category);
	}
	public int getTotalCorrect(String category){
		return _fm.getCategoryTotalCorrect(category);
	}
	public int getTotalAccuracy(String category){
		return calculateAccuracy(_fm.getCategoryTotalTested(category),_fm.getCategoryTotalCorrect(category));
	}
	public int getPersonalBest(String category){
		return _fm.getPersonalBest(category);
	}
	public void setTotalAccuracy(String category, int totalTested,  int totalCorrect, int personalBest){
		_fm.updateAccuracy(category, totalTested, totalCorrect, personalBest);
	}
	public void setAccuracy(int tested, int correct){
		_accuracy = calculateAccuracy(tested,correct);
	}
	private int calculateAccuracy(int tested, int correct){
		double value = (((double)correct)/tested) * 100;
		return (int)value;
	}
	
	public void clearData(){
		_fm.clearData();
	}
	public String getVoice(){
		return _voice;
	}
	public void setVoice(String voice){
		_voice = voice;
	}
	/**
	 * Gets data such as category list, voice list
	 * 
	 */
	public ArrayList<String> getCategoryList(){
		return _fm.getCategoryList();
	}
	public ArrayList<String> getVoiceList(){
		return _fm.getVoiceList();
	}
	/**
	 * This method checks if the user input word list is in usable format 
	 */
	public boolean checkCorrectWordListFile(Path p){
		return _fm.checkCorrectWordListFile(p);
	}
	/**
	 * Option to go back to original word list
	 * 
	 */
	public Path getDefaultWordListPath(){
		return _fm.getDefaultWordListPath();
	}
	/**
	 * Gets and sets Statistics
	 */
	public void updateStats(HashMap<String, Integer> stats){
		_fm.updateStatistics(stats, _category);
	}
	public HashMap<String, Integer> getStats(String category){
		return _fm.getStatisticsWords(category);
	}
	/**
	 * Info on whether user should recieve Video Reward or not
	 *
	 */
	public void setReward(boolean giveReward){
		_reward = giveReward;
	}
	public boolean getReward(){
		return _reward;
	}
	/**
	 * Removing words from failed list
	 */
	public ArrayList<String> getFailedWordList(){
		return _fm.getFailedList();
	}
	public void removeWordFailed(String word){
		removeWordFailed(word);
	}
	/**
	 * WordList
	 */
	public Path getWordListPath(){
		return _fm.getWordListPath();
	}
	public void setWordListPath(Path path){
		_wordFile = path;
		_fm.setWordListPath(path);
		
		_categoryList = _fm.getCategoryList();
		_category = _categoryList.get(0);
		
		_wordTestList = _fm.getTestWordList(_category);
	}
	public ArrayList<String> getTestWordList(String category){
		_wordTestList = _fm.getTestWordList(category);
		return _wordTestList;
	}
	public void addFailedWords(ArrayList<String> failed){
		_fm.addFailedWords(failed);
	}
	public void removeFailedWords(ArrayList<String> mastered){
		_fm.removeWordFailed(mastered);
	}
	/**
	 * Background Music
	 */
	public void musicPlay(){
		_bm.play();
	}
	public void musicPause(){
		_bm.pause();
	}
	public void setBGMVolume(double volume){
		_bm.setVolume(volume);
	}
}
