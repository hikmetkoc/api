package tr.com.meteor.crm.service.dto;

import tr.com.meteor.crm.domain.SurveyAnswer;
import tr.com.meteor.crm.domain.SurveyQuestion;

import java.util.List;

public class CustomerSurveyDTO {

    private SurveyQuestion surveyQuestion;
    private List<SurveyAnswer> surveyAnswers;
    private SurveyAnswer customerSurveyAnswer;

    public SurveyQuestion getSurveyQuestion() {
        return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
        this.surveyQuestion = surveyQuestion;
    }

    public List<SurveyAnswer> getSurveyAnswers() {
        return surveyAnswers;
    }

    public void setSurveyAnswers(List<SurveyAnswer> surveyAnswers) {
        this.surveyAnswers = surveyAnswers;
    }

    public SurveyAnswer getCustomerSurveyAnswer() {
        return customerSurveyAnswer;
    }

    public void setCustomerSurveyAnswer(SurveyAnswer customerSurveyAnswer) {
        this.customerSurveyAnswer = customerSurveyAnswer;
    }
}
