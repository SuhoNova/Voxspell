package application.model;

import java.io.OutputStream;
import javax.swing.SwingWorker;

/**
 * This class deals with using festival on linux
 * 
 * @author syu680, Alex (Suho) Yu
 *
 */

public class TextToSpeechWork extends SwingWorker<Void,Void> {
	private String _word;
	private String _voice;
	private TextToSpeech _tts;
	
	public TextToSpeechWork(String voice, String sentence, TextToSpeech tts){
		_word = sentence;
		_voice = voice;
		_tts = tts;
	}
	public void setWordAndVoice(String sentence, String voice){
		_word = sentence;
		_voice = voice;
	}
	
	/**
	 * Use festival to process text to speech
	 */
	@Override
	protected Void doInBackground() throws Exception {
		// pipes into festival
		Runtime rt = Runtime.getRuntime();
		Process process = rt.exec("festival --pipe");
		OutputStream output = process.getOutputStream();
		
		output.write(("(voice_"+_voice+")").getBytes());
		output.flush();
		output.write(("(SayText \""+_word+"\")").getBytes());
		output.flush();
		output.close();
		// waits until the process is terminated
		process.waitFor();
		return null;
	}
	/**
	 * After festival finishes speaking, it enables the buttons on gui
	 */
	@Override
	protected void done(){
		_tts.enableSpellingButtons();
	}
	
}
