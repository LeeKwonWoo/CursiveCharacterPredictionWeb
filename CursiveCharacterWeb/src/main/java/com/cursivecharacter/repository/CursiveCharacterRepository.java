package com.cursivecharacter.repository;

import com.cursivecharacter.domain.CursiveCharacter;

public interface CursiveCharacterRepository {
	String[] getAllCursiveCharacterList();
	void setNewCC(CursiveCharacter cc);
}
