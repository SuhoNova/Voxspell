package application.model;

import application.view.ControllerSpelling;

/**
 * This class handles text to speech data
 * It then sends it off to festival swing worker
 * @author syu680, Alex (Suho) Yu
 *
 */

public class TextToSpeech{
	private String _voice;
	private ControllerSpelling _spelling;
	private boolean _isSpelling = false;
	
	// Default voice is american
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
	/**
	 * This method enables the buttons that were disabled during text to speech
	 */
	public void enableSpellingButtons(){
		if(_isSpelling){
			_spelling.enableButtons();
		}
	}
	/**
	 * Sends the sentence to swing worker which processes it
	 * @param sentence
	 */
	public void Speak(String sentence){
		TextToSpeechWork ttsWork = new TextToSpeechWork(_voice,sentence, this);
		try {
			ttsWork.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Changes the voice to a format festival understands
	 * @param voice
	 */
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

