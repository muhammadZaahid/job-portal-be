package com.lawencon.candidate.service;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawencon.base.ConnHandler;
import com.lawencon.candidate.dao.CompanyBannerDao;
import com.lawencon.candidate.dao.CompanyDao;
import com.lawencon.candidate.dao.CompanyLogoDao;
import com.lawencon.candidate.dao.FileDao;
import com.lawencon.candidate.dto.InsertResDto;
import com.lawencon.candidate.dto.UpdateResDto;
import com.lawencon.candidate.dto.company.CompanyInsertReqDto;
import com.lawencon.candidate.dto.company.CompanyUpdateReqDto;
import com.lawencon.candidate.model.Company;
import com.lawencon.candidate.model.CompanyBanner;
import com.lawencon.candidate.model.CompanyLogo;
import com.lawencon.candidate.model.File;

@Service
public class CompanyService {

    @Autowired
    CompanyDao companyDao;
    @Autowired
    FileDao fileDao;
    @Autowired
    CompanyLogoDao companyLogoDao;
    @Autowired
    CompanyBannerDao companyBannerDao;

    public InsertResDto createCompany(CompanyInsertReqDto request) {
        ConnHandler.begin();
        System.out.println(request.toString());
        InsertResDto response = new InsertResDto();

        Supplier<String> supplier = () -> "System";

        CompanyLogo companyLogo = new CompanyLogo();
        CompanyBanner companyBanner = new CompanyBanner();

        Company company = new Company();
        company.setCompanyCode(request.getCompanyCode());
        company.setCompanyName(request.getCompanyName());
        company.setCompanyTaxNumber(request.getCompanyTaxNumber());
        company.setCompanyDesc(request.getCompanyDesc());

        if (request.getCompanyLogo().getFiles() != null) {
            File fileLogo = new File();
            fileLogo.setFiles(request.getCompanyLogo().getFiles());
            fileLogo.setFileFormat(request.getCompanyLogo().getFileFormat());
            fileDao.saveNoLogin(fileLogo, supplier);
            company.setCompanyLogo(fileLogo);
            companyLogo.setFile(fileLogo);
        }

        if (request.getCompanyBanner().getFiles() != null) {
            File fileBanner = new File();
            fileBanner.setFiles(request.getCompanyBanner().getFiles());
            fileBanner.setFileFormat(request.getCompanyBanner().getFileFormat());
            fileDao.saveNoLogin(fileBanner, supplier);
            company.setCompanyBanner(fileBanner);
            companyBanner.setFile(fileBanner);
        }

        Company createdCompany = companyDao.saveNoLogin(company,supplier);

        if (createdCompany != null) {
            if (request.getCompanyLogo().getFiles() != null) {
                companyLogo.setCompany(createdCompany);
                companyLogoDao.saveNoLogin(companyLogo, supplier);
            }

            if (request.getCompanyBanner().getFiles() != null) {
                companyBanner.setCompany(createdCompany);
                companyBannerDao.saveNoLogin(companyBanner, supplier);
            }

            ConnHandler.commit();
            response.setId(createdCompany.getId());
            response.setMessage("Company Created Successfully!");
        } else {
            ConnHandler.rollback();
        }

        return response;
    }
    
    public UpdateResDto updateCompany(CompanyUpdateReqDto request) {
    	
    	ConnHandler.begin();
    	
    	final UpdateResDto updateResDto = new UpdateResDto();
    	
    	Supplier<String> supplier = () -> "System";
    	
    	Company company = companyDao.getCompanyByCode(request.getCompanyCode());
        	    
    	company.setCompanyName(request.getCompanyName());
    	company.setCompanyDesc(request.getCompanyDesc());
    	company.setCompanyTaxNumber(request.getCompanyTaxNumber());
    	
    	if (request.getCompanyLogo().getFiles() != null) {
    		CompanyLogo companyLogo = companyLogoDao.getByCompanyId(company.getId());
			File fileLogo = fileDao.getById(File.class, companyLogo.getFile().getId());
			fileLogo.setFiles(request.getCompanyLogo().getFiles());
			fileLogo.setFileFormat(request.getCompanyLogo().getFileFormat());
			fileDao.saveNoLogin(fileLogo, supplier);
			company.setCompanyLogo(fileLogo);
			companyLogo.setFile(fileLogo);
			companyLogoDao.saveNoLogin(companyLogo, supplier);
		}

		if (request.getCompanyBanner().getFiles() != null) {
			CompanyBanner companyBanner = companyBannerDao.getByCompanyId(company.getId());
			File fileBanner = fileDao.getById(File.class, companyBanner.getFile().getId());
			fileBanner.setFiles(request.getCompanyBanner().getFiles());
			fileBanner.setFileFormat(request.getCompanyBanner().getFileFormat());
			fileDao.saveNoLogin(fileBanner, supplier);
			company.setCompanyBanner(fileBanner);
			companyBanner.setFile(fileBanner);
			companyBannerDao.saveNoLogin(companyBanner, supplier);
		}
		
		company.setVersion(company.getVersion() + 1);
		
		Company updatedCompany = companyDao.saveNoLogin(company, supplier);
		
		if(updatedCompany !=null) {
			
			ConnHandler.commit();
			updateResDto.setVer(updatedCompany.getVersion());
			updateResDto.setMessage("Company Updated Successfully");
		}
    	
    	return updateResDto;
    }
}
