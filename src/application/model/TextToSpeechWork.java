package application.model;



import java.io.OutputStream;

import javax.swing.SwingWorker;

public class TextToSpeechWork extends SwingWorker<Void,Void> {
	private String _word;
	private String _voice;
	private TextToSpeech _tts;
	
	public TextToSpeechWork(String voice, String sentence, TextToSpeech tts){
		_word = sentence;
		_voice = voice;
		_tts = tts;
	}
	/**
	 * Sets word, voice, and rehear option
	 * @param word
	 * @param voice
	 * @param rehear
	 */
	public void setWordAndVoice(String sentence, String voice){
		_word = sentence;
		_voice = voice;
	}
	
	/**
	 * If rehear is true, thread does not sleep at all
	 * If it is correct or incorrect or incorrect, try once more thread does not sleep
	 * If it is Please spell the word it sleeps only 1000ms
	 * if it is anything else it sleeps 2200ms 
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
	@Override
	protected void done(){
		_tts.enableSpellingButtons();
	}
	
}
