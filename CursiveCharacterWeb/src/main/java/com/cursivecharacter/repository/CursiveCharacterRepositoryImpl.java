package com.cursivecharacter.repository;

import java.io.File;

import org.springframework.stereotype.Repository;

import com.cursivecharacter.domain.CursiveCharacter;


@Repository
public class CursiveCharacterRepositoryImpl implements CursiveCharacterRepository {
			
	public String[] getAllCursiveCharacterList() {
		String data_dir = "c:/Users/jongoan/git/CursiveCharacterPredictionWeb/CursiveCharacterWeb/src/main/webapp/resources/images/original (copy)/";
		File dir = new File(data_dir);
		String[] labelNames = dir.list();
		return labelNames;
	}
	
	public void setNewCC(CursiveCharacter cc) {
		cc = new CursiveCharacter();
	}
		
}
