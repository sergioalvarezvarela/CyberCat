package es.cybercatapp.model.entities;

public class PuzzleCheckReturn {

    private boolean isCorrect;
    private String formatedSentence;

    public PuzzleCheckReturn(boolean isCorrect, String formatedSentence) {
        this.isCorrect = isCorrect;
        this.formatedSentence = formatedSentence;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getFormatedSentence() {
        return formatedSentence;
    }

    public void setFormatedSentence(String formatedSentence) {
        this.formatedSentence = formatedSentence;
    }
}
