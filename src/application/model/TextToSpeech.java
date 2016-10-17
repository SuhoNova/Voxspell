package application.model;

import java.util.ArrayList;

import application.view.ControllerSpelling;
import javafx.beans.property.StringProperty;

public class TextToSpeech{
	private String _voice;
	private ControllerSpelling _spelling;
	private boolean _isSpelling = false;
	
	public TextToSpeech(){
		changeVoice("american");
	}
	public TextToSpeech(String voice){
		changeVoice(voice);
	}
	public TextToSpeech(String voice, ControllerSpelling spelling){
		changeVoice(voice);
		_spelling = spelling;
		_isSpelling = true;
	}
	public void enableSpellingButtons(){
		if(_isSpelling){
			_spelling.enableButtons();
		}
	}
	
	public void Speak(String sentence){
		TextToSpeechWork ttsWork = new TextToSpeechWork(_voice,sentence, this);
		try {
			ttsWork.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeVoice(String voice){
		if(voice.equalsIgnoreCase("american")){
			_voice = "kal_diphone";
		}else if(voice.equalsIgnoreCase("new zealander")){
			_voice = "akl_nz_jdt_diphone";
		}else{
			_voice = "kal_diphone";
		}
	}
	public String getVoice(){
		if(_voice.equalsIgnoreCase("kal_diphone")){
			return "american";
		} else if(_voice.equalsIgnoreCase("akl_nz_jdt_diphone")){
			return "new zealander";
		}else{
			return "american";
		}
	}
}

