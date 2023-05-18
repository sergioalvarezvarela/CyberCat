package es.cybercatapp.service.dto;

public class TestCheckDtoForm {

    Integer selectedOption;

    Boolean correct;

    public TestCheckDtoForm(Integer selectedOption, Boolean correct) {
        this.selectedOption = selectedOption;
        this.correct = correct;
    }

    public TestCheckDtoForm() {
    }

    public Integer getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Integer selectedOption) {
        this.selectedOption = selectedOption;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
