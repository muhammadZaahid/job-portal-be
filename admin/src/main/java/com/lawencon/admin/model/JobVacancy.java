package com.lawencon.admin.model;

import com.lawencon.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "t_job_vacancy",uniqueConstraints = {@UniqueConstraint(columnNames = {"job_vacancy_code"})})
public class JobVacancy extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "pic_id", nullable = false)
    private User user;

    @Column(name = "job_vacancy_code", nullable = false)
    private String JobVacancyCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "location")
    private String location;

    @OneToOne
    @JoinColumn(name = "job_level_id")
    private JobLevel jobLevel;

    @Column(name = "benefit_desc")
    private String benefitDesc;

    @Column(name = "job_desc")
    private String jobDesc;

    @Column(name = "salary_from", nullable = false)
    private Long salaryFrom;

    @Column(name = "salary_to", nullable = false)
    private Long salaryTo;

    @Column(name = "salary_publish", nullable = false)
    private boolean SalaryPublish;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJobVacancyCode() {
        return JobVacancyCode;
    }

    public void setJobVacancyCode(String jobVacancyCode) {
        JobVacancyCode = jobVacancyCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isSalaryPublish() {
        return SalaryPublish;
    }

    public void setSalaryPublish(boolean salaryPublish) {
        SalaryPublish = salaryPublish;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobLevel getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(JobLevel jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getBenefitDesc() {
        return benefitDesc;
    }

    public void setBenefitDesc(String benefitDesc) {
        this.benefitDesc = benefitDesc;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }
    
}
