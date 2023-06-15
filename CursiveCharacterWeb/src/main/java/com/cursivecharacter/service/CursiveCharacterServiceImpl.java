package com.cursivecharacter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursivecharacter.domain.CursiveCharacter;
import com.cursivecharacter.repository.CursiveCharacterRepository;

@Service
public class CursiveCharacterServiceImpl implements CursiveCharacterService {
	
	@Autowired
	private CursiveCharacterRepository ccRepository;
	
	public String[] getAllCursiveCharacterList() {
		return ccRepository.getAllCursiveCharacterList();
	}
	
	public void setNewCC(CursiveCharacter cc) {
		ccRepository.setNewCC(cc);
	}
}
