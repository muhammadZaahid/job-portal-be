package com.lawencon.admin.dto.jobvacancy;

public class JobVacancyUpdateSeekerReqDto {

	private String jobVacancyCode;
	private String title;
    private String jobLevelId;
    private String location;
    private String benefitDesc;
	private String jobDesc;
    private Long salaryFrom;
    private Long salaryTo;
    private Boolean salaryPublish;
    private String startDate;
    private String endDate;
    

	
	public JobVacancyUpdateSeekerReqDto(String jobVacancyCode, String title, String jobLevelId, String location,
			String benefitDesc, String jobDesc, Long salaryFrom, Long salaryTo, Boolean salaryPublish, String startDate,
			String endDate) {
		super();
		this.jobVacancyCode = jobVacancyCode;
		this.title = title;
		this.jobLevelId = jobLevelId;
		this.location = location;
		this.benefitDesc = benefitDesc;
		this.jobDesc = jobDesc;
		this.salaryFrom = salaryFrom;
		this.salaryTo = salaryTo;
		this.salaryPublish = salaryPublish;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	public String getJobVacancyCode() {
		return jobVacancyCode;
	}
	public void setJobVacancyCode(String jobVacancyCode) {
		this.jobVacancyCode = jobVacancyCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJobLevelId() {
		return jobLevelId;
	}
	public void setJobLevelId(String jobLevelId) {
		this.jobLevelId = jobLevelId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBenefitDesc() {
		return benefitDesc;
	}
	public void setBenefitDesc(String benefitDesc) {
		this.benefitDesc = benefitDesc;
	}
	public Long getSalaryFrom() {
		return salaryFrom;
	}
	public void setSalaryFrom(Long salaryFrom) {
		this.salaryFrom = salaryFrom;
	}
	public Long getSalaryTo() {
		return salaryTo;
	}
	public void setSalaryTo(Long salaryTo) {
		this.salaryTo = salaryTo;
	}
	public Boolean getSalaryPublish() {
		return salaryPublish;
	}
	public void setSalaryPublish(Boolean salaryPublish) {
		this.salaryPublish = salaryPublish;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
    
    
}
