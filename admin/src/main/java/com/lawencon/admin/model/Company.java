package com.lawencon.admin.model;

import javax.persistence.*;

import com.lawencon.base.BaseEntity;

@Entity
@Table(name = "t_company",uniqueConstraints = {@UniqueConstraint(columnNames = {"company_code"}),@UniqueConstraint(columnNames = {"company_tax_number"})})
public class Company extends BaseEntity {

    @Column(name = "company_code", nullable = false)
    private String companyCode;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_desc", nullable = false)
    private String companyDesc;

    @Column(name = "company_tax_number", nullable = false)
    private String companyTaxNumber;

    @OneToOne
    @JoinColumn(name = "company_logo")
    private File companyLogo;

    @OneToOne
    @JoinColumn(name = "company_banner")
    private File companyBanner;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public String getCompanyTaxNumber() {
        return companyTaxNumber;
    }

    public void setCompanyTaxNumber(String companyTaxNumber) {
        this.companyTaxNumber = companyTaxNumber;
    }

    public File getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(File companyLogo) {
        this.companyLogo = companyLogo;
    }

    public File getCompanyBanner() {
        return companyBanner;
    }

    public void setCompanyBanner(File companyBanner) {
        this.companyBanner = companyBanner;
    }

}

   
