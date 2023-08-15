package es.cybercatapp.web.dto;

public class PuzzleCheckDtoForm {

    String words;

    Boolean correct;

    public PuzzleCheckDtoForm(String words, Boolean correct) {
        this.words = words;
        this.correct = correct;
    }

    public PuzzleCheckDtoForm() {
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
