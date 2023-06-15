package com.cursivecharacter.controller;

import java.io.File;
import java.util.Arrays;
import java.util.TreeMap;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.tensorflow.Signature;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TInt32;

import com.cursivecharacter.domain.CursiveCharacter;
import com.cursivecharacter.service.CursiveCharacterService;


@Controller
public class HomeController {
	
	@Autowired
	private CursiveCharacterService ccService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home (CursiveCharacter cc, Model model) {
		ccService.setNewCC(cc);
		model.addAttribute("predictImg",cc);
		return "home";
	}
	
	@RequestMapping(value="/predict", method=RequestMethod.POST)
	public String predictImg(@ModelAttribute("predictImg") CursiveCharacter cc, Model model, BindingResult result) {
		if(result.hasErrors()) {
			System.out.println("Error!!!");
			return "home";
		}
		
		String[] labelNames = ccService.getAllCursiveCharacterList();
		String data_dir = "c:/Users/jongoan/git/CursiveCharacterPredictionWeb/CursiveCharacterWeb/src/main/webapp/resources/images/original (copy)/";
		Arrays.parallelSort(labelNames);
		
		int labels = labelNames.length;
		
		String outputImagePath = "";
		String modelPath = "c:/Users/jongoan/git/CursiveCharacterPredictionWeb/CursiveCharacterWeb/src/main/webapp/resources/model/densenet_201.h5";

		TreeMap<Integer, String> imgTreeMap = new TreeMap<>();
		int imgCount = 1;
		
		for (String label : labelNames) {
			imgTreeMap.put(imgCount, label);
			imgCount++;
		}
		
//		사용자 이미지 데이터 저장하기 위한 File 객체
		MultipartFile ccImage = cc.getCcImage();
		String save_dir = "c:/Users/jongoan/git/CursiveCharacterPredictionWeb/CursiveCharacterWeb/src/main/webapp/resources/userSavedFiles/";
		String saveName = ccImage.getOriginalFilename();
		File saveFile = new File(save_dir, saveName);
		
//		userSavedFiles에 사용자 이미지 저장
		if (ccImage != null && !ccImage.isEmpty()) {
			try {
				ccImage.transferTo(saveFile);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Data Preprocessing
		//https://d2.naver.com/helloworld/8344782?refer=%EA%B0%9C%EB%B0%9C%EC%9E%90%EC%8A%A4%EB%9F%BD%EB%8B%A4
		
		
		try {
			String locCC = save_dir + saveName;
			
			Mat imageCC = Imgcodecs.imread(locCC);
			Mat imageGrayCC = new Mat();
			Imgproc.cvtColor(imageCC, imageGrayCC, Imgproc.COLOR_RGB2GRAY);
			
			Mat imageResizeCC = new Mat();
			Size sizeCC = new Size(224, 224);
			Imgproc.resize(imageGrayCC, imageResizeCC, sizeCC);
			
			Mat imageBinarizationGrayCC = new Mat();
			Imgproc.threshold(imageGrayCC, imageBinarizationGrayCC, 0, 255, Imgproc.THRESH_BINARY);
			Imgcodecs.imwrite("c:/Users/jongoan/git/CursiveCharacterPredictionWeb/CursiveCharacterWeb/src/main/webapp/resources/images/savedPreprocessingImg/"+saveName, imageBinarizationGrayCC);
		} catch(Exception e) {
			e.printStackTrace();
		}

		//Prediction
		
//		SavedModelBundle models = SavedModelBundle.load(modelPath, "serve");
		
		
		
		
		
//		결과 값을 가져오기
		String predictionResult = "";
		int predictionNum = 1;
		for (Integer i : imgTreeMap.keySet()) {
			if (i == predictionNum) {
				predictionResult = imgTreeMap.get(i);
			}
		}
		
		
		
		
		
		//예측 결과 값을 받은 후
		String predictedDir = data_dir + predictionResult + "/";
		File dir = new File(predictedDir);
		String[] names = dir.list();
		String[] s = predictedDir.split("/");
		model.addAttribute("predictedDir", predictionResult + "/" + names[0]);
		model.addAttribute("savedImageDir", saveName);
		
		
		
		return "home";
	}
	
//	@ModelAttribute
//	public void predictCC(Model model) {
//		model.addAttribute("predictImg","predictImg");
//	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("ccImage");
	}
	
	private static Signature dbl(Ops tf) {
	    Placeholder<TInt32> x = tf.placeholder(TInt32.class);
	    Add<TInt32> dblX = tf.math.add(x, x);
	    return Signature.builder().input("x", x).output("dbl", dblX).build();
	}
	
	
	/*
	 * 
	 * 
	 * 
	 * */
	
}
