package kr.co.itwillbs.de.approval.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.itwillbs.de.approval.mapper.SignMapper;

@Service
public class SignService {
	@Autowired
	private SignMapper signMapper;
	
	
}
