package application.model;

public class WordMastery {
	private String _word;
	private String _mastery;
	public WordMastery(String word, int mastery){
		_word = word;
		if(mastery == 1){
			_mastery = "mastered";
		} else {
			_mastery = "failed";
		}
	}
	public String getMastery(){
		return _mastery;
	}
	public String getWord(){
		return _word;
	}
}
