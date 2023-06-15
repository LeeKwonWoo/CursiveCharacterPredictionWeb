package com.cursivecharacter.service;

import com.cursivecharacter.domain.CursiveCharacter;

public interface CursiveCharacterService {
	String[] getAllCursiveCharacterList();
	void setNewCC(CursiveCharacter cc);
}
