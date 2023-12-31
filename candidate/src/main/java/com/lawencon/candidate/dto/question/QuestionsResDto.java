package com.lawencon.candidate.dto.question;

import java.util.List;

public class QuestionsResDto {
	private String applicantId;
    private String questionDesc;
    private String questionId;
    private List<QuestionAnswerResDto> options;
    
    public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getQuestionDesc() {
        return questionDesc;
    }
    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }
    public List<QuestionAnswerResDto> getOptions() {
        return options;
    }
    public void setOptions(List<QuestionAnswerResDto> options) {
        this.options = options;
    }
    public String getQuestionId() {
        return questionId;
    }
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
    
}
