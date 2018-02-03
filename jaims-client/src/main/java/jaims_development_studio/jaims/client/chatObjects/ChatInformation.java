package jaims_development_studio.jaims.client.chatObjects;

public class ChatInformation {

	private int totalNumberMessages = 0, numberOwnMessages = 0, numberContactMessages = 0, numberTotalTextMessages = 0,
			numberOwnTextMessages = 0, numberContactTextMessages = 0, numberWordsUsed, numberOwnWordsUsed = 0,
			numberContactWordsUsed = 0, numberTotalVoiceMessages = 0, numberOwnVoiceMessages = 0,
			numberContactVoiceMessages = 0;

	public void addTotalNumberMessages() {
		totalNumberMessages++;
	}

	public void addNumberOwnMessages() {
		numberOwnMessages++;
	}

	public void addNumberContactMessages() {
		numberContactMessages++;
	}

	public void addNumberTotalTextMessages() {
		numberTotalTextMessages++;
	}

	public void addNumberOwnTextMessages() {
		numberOwnTextMessages++;
	}

	public void addNumberContactTextMessages() {
		numberContactMessages++;
	}

	public void addNumberWordsUsed() {
		numberWordsUsed++;
	}

	public void addNumberOwnWordsUsed() {
		numberOwnWordsUsed++;
	}

	public void addNumberContactsWordsUsed() {
		numberContactWordsUsed++;
	}

	public void addNumerTotalVoiceMessages() {
		numberTotalVoiceMessages++;
	}

	public void addNumberOwnVoiceMessages() {
		numberOwnVoiceMessages++;
	}

	public void addNumberContactVoiceMessages() {
		numberContactVoiceMessages++;
	}

	public int getTotalNumberMessages() {

		return totalNumberMessages;
	}

	public void setTotalNumberMessages(int totalNumberMessages) {

		this.totalNumberMessages = totalNumberMessages;
	}

	public int getNumberOwnMessages() {

		return numberOwnMessages;
	}

	public void setNumberOwnMessages(int numberOwnMessages) {

		this.numberOwnMessages = numberOwnMessages;
	}

	public int getNumberContactMessages() {

		return numberContactMessages;
	}

	public void setNumberContactMessages(int numberContactMessages) {

		this.numberContactMessages = numberContactMessages;
	}

	public int getNumberTotalTextMessages() {

		return numberTotalTextMessages;
	}

	public void setNumberTotalTextMessages(int numberTotalTextMessages) {

		this.numberTotalTextMessages = numberTotalTextMessages;
	}

	public int getNumberOwnTextMessages() {

		return numberOwnTextMessages;
	}

	public void setNumberOwnTextMessages(int numberOwnTextMessages) {

		this.numberOwnTextMessages = numberOwnTextMessages;
	}

	public int getNumberContactTextMessages() {

		return numberContactTextMessages;
	}

	public void setNumberContactTextMessages(int numberContactTextMessages) {

		this.numberContactTextMessages = numberContactTextMessages;
	}

	public int getNumberWordsUsed() {

		return numberWordsUsed;
	}

	public void setNumberWordsUsed(int numberWordsUsed) {

		this.numberWordsUsed += numberWordsUsed;
	}

	public int getNumberOwnWordsUsed() {

		return numberOwnWordsUsed;
	}

	public void setNumberOwnWordsUsed(int numberOwnWordsUsed) {

		this.numberOwnWordsUsed += numberOwnWordsUsed;
	}

	public int getNumberContactWordsUsed() {

		return numberContactWordsUsed;
	}

	public void setNumberContactWordsUsed(int numberContactWordsUsed) {

		this.numberContactWordsUsed += numberContactWordsUsed;
	}

	public int getNumberTotalVoiceMessages() {

		return numberTotalVoiceMessages;
	}

	public void setNumberTotalVoiceMessages(int numberTotalVoiceMessages) {

		this.numberTotalVoiceMessages = numberTotalVoiceMessages;
	}

	public int getNumberOwnVoiceMessages() {

		return numberOwnVoiceMessages;
	}

	public void setNumberOwnVoiceMessages(int numberOwnVoiceMessages) {

		this.numberOwnVoiceMessages = numberOwnVoiceMessages;
	}

	public int getNumberContactVoiceMessages() {

		return numberContactVoiceMessages;
	}

	public void setNumberContactVoiceMessages(int numberContactVoiceMessages) {

		this.numberContactVoiceMessages = numberContactVoiceMessages;
	}

}
