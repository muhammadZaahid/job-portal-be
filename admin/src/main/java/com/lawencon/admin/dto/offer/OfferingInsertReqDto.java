package com.lawencon.admin.dto.offer;

public class OfferingInsertReqDto {
    private String applicantId;
    private Double offerSalary;
    
    public String getApplicantId() {
        return applicantId;
    }
    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
    public Double getOfferSalary() {
        return offerSalary;
    }
    public void setOfferSalary(Double offerSalary) {
        this.offerSalary = offerSalary;
    }    
}
