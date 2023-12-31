package com.lawencon.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lawencon.admin.constant.ApiList;
import com.lawencon.admin.dao.ApplicantDao;
import com.lawencon.admin.dao.ApplicationDao;
import com.lawencon.admin.dao.AssessmentDao;
import com.lawencon.admin.dao.CandidateDao;
import com.lawencon.admin.dao.ConstantDao;
import com.lawencon.admin.dao.EmployeeDao;
import com.lawencon.admin.dao.InterviewDao;
import com.lawencon.admin.dao.JobVacancyDao;
import com.lawencon.admin.dao.MedicalDao;
import com.lawencon.admin.dao.UserDao;
import com.lawencon.admin.dto.CountResDto;
import com.lawencon.admin.dto.InsertResDto;
import com.lawencon.admin.dto.UpdateResDto;
import com.lawencon.admin.dto.applicant.ApplicantDetailResDto;
import com.lawencon.admin.dto.applicant.ApplicantInsertAdminReqDto;
import com.lawencon.admin.dto.applicant.ApplicantInsertReqDto;
import com.lawencon.admin.dto.applicant.ApplicantsResDto;
import com.lawencon.admin.model.Applicant;
import com.lawencon.admin.model.Application;
import com.lawencon.admin.model.Assessment;
import com.lawencon.admin.model.Candidate;
import com.lawencon.admin.model.Email;
import com.lawencon.admin.model.Employee;
import com.lawencon.admin.model.Interview;
import com.lawencon.admin.model.JobVacancy;
import com.lawencon.admin.model.User;
import com.lawencon.base.ConnHandler;
import com.lawencon.util.GeneratorUtil;

@Service
public class ApplicantService {

	@Autowired
	ApplicantDao applicantDao;
	@Autowired
	CandidateDao candidateDao;
	@Autowired
	JobVacancyDao jobVacancyDao;
	@Autowired
	ApplicationDao applicationDao;
	@Autowired
	AssessmentDao assessmentDao;
	@Autowired
	InterviewDao interviewDao;
	@Autowired
	MedicalDao medicalDao;
	@Autowired
	UserDao userDao;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	ConstantDao constantDao;
	@Autowired
	BlacklistService blacklistService;
	@Autowired
	EmailService emailService;
	@Autowired
	RestTemplate restTemplate;

	@Value("${spring.mail.username}")
	private String emailSender;

	public InsertResDto createApplicant(ApplicantInsertReqDto request) {
		ConnHandler.begin();
		Boolean hasApplied = applicantDao.checkHasApplied(request.getCandidateId(), request.getJobVacancyId());
		if (hasApplied) {
			throw new RuntimeException("This Candidate has already applied to this job!");
		}
		InsertResDto response = new InsertResDto();
		Applicant applicant = new Applicant();
		Candidate candidate = candidateDao.getById(Candidate.class, request.getCandidateId());
		applicant.setCandidate(candidate);
		JobVacancy jobVacancy = jobVacancyDao.getById(JobVacancy.class, request.getJobVacancyId());
		blacklistService.checkBlacklist(request.getCandidateId(), jobVacancy.getCompany().getId());
		applicant.setJobVacancy(jobVacancy);
		applicant.setCurrentStage("application");
		applicant.setStgApplication(true);
		applicant.setAppliedDate(LocalDate.now());
		applicant.setApplicantCode(GeneratorUtil.generateCode());

		Applicant createdApplicant = applicantDao.save(applicant);

		if (createdApplicant != null) {

			Application application = new Application();
			application.setApplicant(createdApplicant);
			applicationDao.save(application);
			ConnHandler.commit();
			response.setId(createdApplicant.getId());
			response.setMessage("Applicant Created Successfully");
		} else {
			ConnHandler.rollback();
		}

		return response;

	}

	public InsertResDto createApplicantNoLogin(ApplicantInsertAdminReqDto request) {
		ConnHandler.begin();
		Supplier<String> supplier = () -> "System";
		InsertResDto response = new InsertResDto();

		try {
			Applicant applicant = new Applicant();
			Candidate candidate = candidateDao.getByCode(request.getCandidateCode());
			applicant.setCandidate(candidate);
			JobVacancy jobVacancy = jobVacancyDao.getByCode(request.getJobVacancyCode());
			blacklistService.checkBlacklist(candidate.getId(), jobVacancy.getCompany().getId());
			applicant.setJobVacancy(jobVacancy);
			applicant.setCurrentStage("application");
			applicant.setStgApplication(true);
			applicant.setAppliedDate(LocalDate.now());
			applicant.setApplicantCode(request.getApplicantCode());

			Applicant createdApplicant = applicantDao.saveNoLogin(applicant, supplier);

			if (createdApplicant != null) {

				Application application = new Application();
				application.setApplicant(createdApplicant);
				applicationDao.saveNoLogin(application, supplier);
				ConnHandler.commit();
				response.setId(createdApplicant.getId());
				response.setMessage("Applicant Created Successfully");

				try {
					Map<String, Object> properties = new HashMap<>();
					properties.put("name", candidate.getName());
					properties.put("jobName", jobVacancy.getTitle());
					properties.put("companyName", jobVacancy.getCompany().getCompanyName());
					Email email = new Email();
					email.setSubject("Success! You have applied for " + jobVacancy.getCompany().getCompanyName());
					email.setRecipientEmail(candidate.getEmail());
					email.setRecipientName(candidate.getName());
					email.setSenderEmail(emailSender);
					email.setProperties(properties);
					email.setTemplate("template-after-apply");
					emailService.sendHtmlMessage(email);
				} catch (MessagingException exception) {
					exception.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			ConnHandler.rollback();
			throw new RuntimeException(e.getMessage());
		}

		return response;

	}

	public UpdateResDto updateApplicant(String data) {
		ConnHandler.begin();
		final UpdateResDto response = new UpdateResDto();
		Applicant applicant = applicantDao.getById(Applicant.class, data);
		JobVacancy job = jobVacancyDao.getById(JobVacancy.class, applicant.getJobVacancy().getId());

		User userpic = userDao.getById(User.class, job.getUser().getId());

		try {
			ResponseEntity<UpdateResDto> res = restTemplate.exchange(
					ApiList.ApiApplicant.apiUrl + applicant.getApplicantCode(),
					HttpMethod.PATCH, null, UpdateResDto.class);
			if (res.getStatusCode().equals(HttpStatus.OK)) {
				if (applicant.getCurrentStage().equals("application")) {
					applicant.setCurrentStage("assessment");
					applicant.setStgAssessment(true);
					applicant.setVersion(applicant.getVersion() + 1);
					applicantDao.save(applicant);
					Assessment assessment = new Assessment();
					assessment.setApplicantId(applicant);
					assessment.setAssessmentLastEmailSend(LocalDateTime.now().toString());
					assessment.setAssessmentPic(userpic);
					assessmentDao.save(assessment);
					Application application = applicationDao.getByApplicant(data);
					application.setIsAccepted(true);
					applicationDao.save(application);
					try {
						Map<String, Object> properties = new HashMap<>();
						properties.put("name", applicant.getCandidate().getName());
						properties.put("jobName", applicant.getJobVacancy().getTitle());
						properties.put("companyName", applicant.getJobVacancy().getCompany().getCompanyName());
						Email email = new Email();
						email.setSubject("Your application for " + applicant.getJobVacancy().getTitle()
								+ " has reached Assessment stage!");
						email.setRecipientEmail(applicant.getCandidate().getEmail());
						email.setRecipientName(applicant.getCandidate().getName());
						email.setSenderEmail(emailSender);
						email.setProperties(properties);
						email.setTemplate("template-assessment");
						emailService.sendHtmlMessage(email);
					} catch (MessagingException exception) {
						exception.printStackTrace();
					}
				} else if (applicant.getCurrentStage().equals("assessment")) {
					applicant.setCurrentStage("interview");
					applicant.setStgInterview(true);
					applicant.setVersion(applicant.getVersion() + 1);
					applicantDao.save(applicant);
					Assessment assessment = assessmentDao.getByApplicantId(data);
					assessment.setIsAccepted(true);
					assessmentDao.saveAndFlush(assessment);

				} else if (applicant.getCurrentStage().equals("interview")) {
					applicant.setCurrentStage("mcu");
					applicant.setStgMcu(true);
					applicant.setVersion(applicant.getVersion() + 1);
					applicantDao.save(applicant);
					Interview interview = interviewDao.getByApplicantId(data);
					interview.setIsAccepted(true);
					assessmentDao.saveAndFlush(interview);
					try {
						Map<String, Object> properties = new HashMap<>();
						properties.put("name", applicant.getCandidate().getName());
						properties.put("jobName", applicant.getJobVacancy().getTitle());
						properties.put("companyName", applicant.getJobVacancy().getCompany().getCompanyName());
						Email email = new Email();
						email.setSubject("Your application for " + applicant.getJobVacancy().getTitle()
								+ " has reached Medical stage!");
						email.setRecipientEmail(applicant.getCandidate().getEmail());
						email.setRecipientName(applicant.getCandidate().getName());
						email.setSenderEmail(emailSender);
						email.setProperties(properties);
						email.setTemplate("template-medical");
						emailService.sendHtmlMessage(email);
					} catch (MessagingException exception) {
						exception.printStackTrace();
					}
				} else if (applicant.getCurrentStage().equals("mcu")) {
					applicant.setCurrentStage("offer");
					applicant.setStgOffer(true);
					applicant.setVersion(applicant.getVersion() + 1);
					applicantDao.save(applicant);
				} else if (applicant.getCurrentStage().equals("offer")) {
					applicant.setCurrentStage("hired");
					applicantDao.saveAndFlush(applicant);
					Employee employee = new Employee();
					employee.setCandidate(applicant.getCandidate());
					employee.setCompany(applicant.getJobVacancy().getCompany());
					employeeDao.save(employee);
					try {
						Map<String, Object> properties = new HashMap<>();
						properties.put("name", applicant.getCandidate().getName());
						properties.put("jobName", applicant.getJobVacancy().getTitle());
						properties.put("companyName", applicant.getJobVacancy().getCompany().getCompanyName());
						Email email = new Email();
						email.setSubject("You are now hired as " + applicant.getJobVacancy().getTitle());
						email.setRecipientEmail(applicant.getCandidate().getEmail());
						email.setRecipientName(applicant.getCandidate().getName());
						email.setSenderEmail(emailSender);
						email.setProperties(properties);
						email.setTemplate("template-hired");
						emailService.sendHtmlMessage(email);
					} catch (MessagingException exception) {
						exception.printStackTrace();
					}
				}
				ConnHandler.commit();
				response.setVer(applicant.getVersion());
				response.setMessage("Success update status applicant");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ConnHandler.rollback();
			throw new RuntimeException();
		}

		return response;
	}

	public List<ApplicantsResDto> getAllByVacancy(String jobVacancyId) {
		List<ApplicantsResDto> responses = new ArrayList<>();

		applicantDao.getAllByVacancy(jobVacancyId).forEach(a -> {
			ApplicantsResDto response = new ApplicantsResDto();
			response.setApplicantId(a.getId());
			response.setCandidateNik(a.getCandidate().getNik());
			response.setCandidateName(a.getCandidate().getName());
			response.setCandidateEmail(a.getCandidate().getEmail());
			response.setCurrentStage(a.getCurrentStage());
			response.setJobTitle(a.getJobVacancy().getTitle());
			response.setAppliedDate(a.getAppliedDate().toString());
			responses.add(response);
		});

		return responses;
	}

	public List<ApplicantsResDto> getAllApplicants(Integer page, Integer limit) {
		List<ApplicantsResDto> responses = new ArrayList<>();

		List<Applicant> a = applicantDao.getAllPaged(page, limit);
		for (int i = 0; i < a.size(); i++) {
			ApplicantsResDto response = new ApplicantsResDto();
			response.setApplicantId(a.get(i).getId());
			response.setCandidateNik(a.get(i).getCandidate().getNik());
			response.setCandidateName(a.get(i).getCandidate().getName());
			response.setCandidateEmail(a.get(i).getCandidate().getEmail());
			response.setCurrentStage(a.get(i).getCurrentStage());
			response.setJobTitle(a.get(i).getJobVacancy().getTitle());
			response.setAppliedDate(a.get(i).getAppliedDate().toString());
			responses.add(response);
		}
		;

		return responses;
	}

	public ApplicantDetailResDto getById(String applicantId) {
		final ApplicantDetailResDto response = new ApplicantDetailResDto();
		Applicant applicant = applicantDao.getById(Applicant.class, applicantId);
		response.setCandidateId(applicant.getCandidate().getId());
		response.setCurrentStage(applicant.getCurrentStage());
		response.setApplication(applicant.isStgApplication());
		response.setAssessment(applicant.isStgAssessment());
		response.setInterview(applicant.isStgInterview());
		response.setMcu(applicant.isStgMcu());
		response.setOffer(applicant.isStgOffer());
		response.setAppliedDate(applicant.getAppliedDate().toString());
		response.setJobVacancyId(applicant.getJobVacancy().getId());
		try {
			if (medicalDao.getByApplicantId(applicantId).getId() != null) {
				response.setHasMedicalFile(true);
			}
		} catch (NullPointerException ex) {
			response.setHasMedicalFile(false);
		}
		return response;
	}
	
	public CountResDto getTotalApplicant() {
		
		CountResDto response = new CountResDto();
		String total = constantDao.getTotal("t_applicant");
		response.setTotal(total);
		
		return response;
	}

}
