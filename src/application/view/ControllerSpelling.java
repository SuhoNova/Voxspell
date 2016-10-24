package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import application.VoxspellMain;
import application.VoxspellModel;
import application.model.TextToSpeech;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerSpelling {
	@FXML private Button _quit;
	@FXML private Button _enter;
	@FXML private Button _listenAgain;
	@FXML private TextField _userReply;
	@FXML private Label _accuracy;
	@FXML private Label _order;
	@FXML private Label _labelCorrect;
	@FXML private Label _categoryName;

	private VoxspellMain _main;
	private VoxspellModel _model;

	private TextToSpeech _tts;

	private ArrayList<String> _wordList;
	private ArrayList<String> _failed;
	private ArrayList<String> _mastered;
	private String _category;
	private String _currentWord;
	private boolean _secondAttempt;
	private boolean _apostropheExist;

	private int _correctOverall;
	private int _totalDone;
	private int _correctSession;
	private int _totalDoneSession;
	private int _personalBest;
	
	private HashMap<String, Integer> _stats;
	

	@FXML
	public void enter(){
		disableButtons();
		String userAns = _userReply.getText().trim();
		System.out.println("User ans: "+ userAns);
		_userReply.clear();
		
		
		// IF CORRECT ELSE INCORRECT
		/**
		 * Check if second attempt,
		 * Update what word up to ie how many correct and how many done
		 * Go to next word
		 * use tts to speak it
		 */
		if(userAns.equalsIgnoreCase(_currentWord)){
			updateStats(true);
			_stats.put(_currentWord, 1);
			_mastered.add(_currentWord.toLowerCase());
			_secondAttempt = false;
			_correctSession++;

			_totalDoneSession++;
			if(_totalDoneSession == _wordList.size()){
				_tts.Speak("Correct!");
			}else{
				_currentWord = _wordList.get(_totalDoneSession);
				this.checkApostrophe("Correct! .... Please spell .... " + _currentWord);
			}
		} else {
			if(_secondAttempt){
				updateStats(false);
				_stats.put(_currentWord, 0);
				_failed.add(_currentWord.toLowerCase());
				_secondAttempt = false;

				_totalDoneSession++;
				if(_totalDoneSession == _wordList.size()){
					_tts.Speak("Incorrrect!");
				}else{
					_currentWord = _wordList.get(_totalDoneSession);
					this.checkApostrophe("Incorrrect! .... Please spell .... " + _currentWord);
				}
			} else {
				this.checkApostrophe("Incorrect! .... Please try again .... " + _currentWord);
				_secondAttempt = true;
			}
		}
		
		
		if(_totalDoneSession == _wordList.size()){
			boolean newBest = false;
			if(_correctSession > _personalBest){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("New Personal Best!");
				alert.setHeaderText("Congratulations!");
				alert.setContentText("You have achieved a new high score");

				alert.showAndWait();
				newBest=true;
			}
			//Then go to spelling done scene
			// check if video reward and next level is in order
			// offer repeat level and back to menu regardless
			// just go back for now
			
			// update accuracy, statistics, failed
			if(!_failed.isEmpty()){
				_model.addFailedWords(_failed);
			}
			if(!_mastered.isEmpty()){
				_model.removeFailedWords(_mastered);
			}
			if(newBest){
				_model.setTotalAccuracy(_category, _totalDoneSession+_model.getTotalWordsTested(_category),
						_correctSession+_model.getTotalCorrect(_category), _correctSession);
			}else {
				_model.setTotalAccuracy(_category, _totalDoneSession+_model.getTotalWordsTested(_category),
						_correctSession+_model.getTotalCorrect(_category), _personalBest);
			}
			//stats
			_model.updateStats(_stats);
			//reward status?
			if(_correctSession>=_totalDoneSession-1){
				_model.setReward(true);
			} else {
				_model.setReward(false);
			}
			_model.setWordListAndCategory();
			spellingDone();
		}

		updateText();
		System.out.println("------------------------------");
		System.out.println("Current word: " + _currentWord);
	}
	
	@FXML
	private void backToMenu(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Back to Menu");
		alert.setHeaderText("Progress for this spelling will be lost.");
		alert.setContentText("Are you sure you want to go back to menu?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			Stage stage = (Stage) _quit.getScene().getWindow();
			Scene scene = _main.getScene();
			stage.setScene(scene);
			stage.show();
		}
	}
	public void spellingDone(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(VoxspellMain.class.getResource("view/ViewSpellingDone.fxml"));
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			@Override
			public Object call(Class<?> aClass){
				return new ControllerSpellingDone(_model, _main, getSessionAccuracy());
			}
		});
		try {
			AnchorPane spellingDone = (AnchorPane) loader.load();
			Stage stage = (Stage) _enter.getScene().getWindow();
			Scene scene = new Scene(spellingDone);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void startQuiz(){
		disableButtons();
		Scene scene = _main.getScene();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)){
					enter();
				}
			}
		});
		this.checkApostrophe("Please spell the word ...... " + _currentWord);
		//
		updateText();
		System.out.println("------------------------------");
		System.out.println("Current word: " + _currentWord);
		//
	}
	public void disableButtons(){
		_enter.setDisable(true);
		_listenAgain.setDisable(true);
		Scene scene = _enter.getScene();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
			}
		});
	}
	public void enableButtons(){
		_enter.setDisable(false);
		_listenAgain.setDisable(false);
		Scene scene = _enter.getScene();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)){
					enter();
				}
			}
		});
	}
	private void updateStats(boolean isCorrect){
		if(isCorrect){
			_stats.put(_currentWord, 1);
		}else {
			_stats.put(_currentWord, 0);
		}
	}
	private void updateText(){
		_accuracy.setText("Score: " + getSessionAccuracy() + "%");
		_labelCorrect.setText(_correctSession + " words correct!");
		_order.setText(_totalDoneSession + " out of " + _wordList.size());
	}
	private int getSessionAccuracy(){
		if(_correctSession==0||_totalDoneSession==0){
			return 0;
		}
		double value = (((double)_correctSession)/_totalDoneSession)*100;
		return (int)value;
	}
	public boolean doesApostropheExist(String word){
		if(word.contains("'")){
			return true;
		}
		return false;
	}
	@FXML
	public void listenAgain(){
		disableButtons();
		if(!_secondAttempt){
			checkApostrophe(_currentWord);
		}else{
			String temp = "";
			for(int i = 0; i < _currentWord.length(); i++){
				if(_currentWord.charAt(i) == '\''){
					temp += "apostrophe ";
				} else {
					temp += _currentWord.charAt(i) + " ... ";
				}
			}
			_tts.Speak(_currentWord + " .... " + temp);
		}
		// when tts worker is done it calls the enable buttons method
	}
	public void checkApostrophe(String word){
		if(_currentWord.contains("'")){
			_tts.Speak(word + " ... with an apostrophe.");
		}else{
			_tts.Speak(word);
		}
	}
	@FXML
	private void initialize(){
		_failed = new ArrayList<String>();
		_mastered = new ArrayList<String>();
		_stats = new HashMap<String, Integer>();
		
		_category = _model.getCategory();
		_wordList = _model.getTestWordList(_category);
		_secondAttempt = false;
		_apostropheExist = false;
		
		_correctSession = 0;
		_totalDoneSession = 0;
		
		_personalBest = _model.getPersonalBest(_category);
		_correctOverall = _model.getTotalCorrect(_category);
		_totalDone = _model.getTotalWordsTested(_category);
		
		_categoryName.setText("Category: " + _category);
		updateText();
	
		_currentWord = _wordList.get(_totalDoneSession);

	}
	public ControllerSpelling(VoxspellModel voxspellModel, VoxspellMain voxspellMain){
		_model = voxspellModel;
		_main = voxspellMain;
		_tts = new TextToSpeech(_model.getVoice(), this);
	}

}
