package com.lawencon.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lawencon.admin.dto.InsertResDto;
import com.lawencon.admin.dto.candidate.CandidateInsertReqDto;
import com.lawencon.admin.dto.candidate.CandidateResDto;
import com.lawencon.admin.service.CandidateService;
import com.lawencon.base.ConnHandler;

@RestController
@RequestMapping("admin/candidate")
public class CandidateController {
	
	@Autowired
	CandidateService candidateService;
	
	@PostMapping
	public ResponseEntity<InsertResDto> createCandidate(@RequestBody CandidateInsertReqDto request){
		
		ConnHandler.begin();
		InsertResDto response = candidateService.createCandidate(request);
		ConnHandler.commit();
		
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}
	
	@GetMapping
	public ResponseEntity<List<CandidateResDto>> getAllCandidates(){
		
		List<CandidateResDto> response = candidateService.getAllCandidate();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
