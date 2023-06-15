package com.cursivecharacter.domain;

import org.springframework.web.multipart.MultipartFile;

public class CursiveCharacter {
	
	private MultipartFile ccImage;
		
	public CursiveCharacter() {
		super();
	}
	
	public MultipartFile getCcImage() {
		return ccImage;
	}
	public void setCcImage(MultipartFile ccImage) {
		this.ccImage = ccImage;
	}
	
}
