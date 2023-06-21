package com.cursivecharacter.controller;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.TreeMap;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.types.TFloat32;

import com.cursivecharacter.domain.CursiveCharacter;
import com.cursivecharacter.service.CursiveCharacterService;


@Controller
public class HomeController {
	
	
	@Autowired
	private CursiveCharacterService ccService;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home (CursiveCharacter cc, Model model) {
		System.out.println("Hello TensorFlow " + TensorFlow.version());
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
		String preprocessingImg = "c:/Users/jongoan/git/CursiveCharacterPredictionWeb/CursiveCharacterWeb/src/main/webapp/resources/images/savedPreprocessingImg/" + saveName;
		String locCC = save_dir + saveName;
		
		try {
			Mat imageCC = Imgcodecs.imread(locCC);
			Mat imageGrayCC = new Mat();
			Imgproc.cvtColor(imageCC, imageGrayCC, Imgproc.COLOR_RGB2GRAY);

			Mat imageResizeCC = new Mat();
			Size sizeCC = new Size(224, 224);
			Imgproc.resize(imageGrayCC, imageResizeCC, sizeCC);
			
			Mat imageBinarizationGrayCC = new Mat();
			Imgproc.threshold(imageGrayCC, imageBinarizationGrayCC, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
			Imgcodecs.imwrite(preprocessingImg, imageBinarizationGrayCC);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		// Load the TensorFlow model

		// Load and preprocess the input image using OpenCV
        String imagePath = locCC;
        Mat image = Imgcodecs.imread(imagePath, Imgproc.COLOR_RGB2GRAY);
        Mat resizedImage = new Mat();
        Imgproc.resize(image, resizedImage, new Size(224, 224));
        resizedImage.convertTo(resizedImage, CvType.CV_32F, 1.0 / 255.0);
        
        
        // Tensorflow 1.x version
//		String modelPath = "c:/Users/jongoan/Desktop/Cursive_Character_Prediction_using_a_Deep_Learning/Task/saved_model.pb";

//        byte[] graphBytes;
//        try {
//            graphBytes = Files.readAllBytes(Paths.get(modelPath));
//        } catch (Exception e) {
//            System.err.println("Failed to read the model file.");
//            
//        }
//        Graph graph = new Graph();
//        GraphDef graphDef = GraphDef.parseFrom(graphBytes);
//        graph.importGraphDef(graphDef);
//        Iterator<Operation> iter = graph.operations();
//        while (iter.hasNext()) {
//        	Operation operation = iter.next();
//        	System.out.println(operation.name());
//        }
        
        // Tensorflow 2.x version
		String modelPath = "c:/Users/jongoan/Desktop/Cursive_Character_Prediction_using_a_Deep_Learning/Task/densenet201.h5";
		String input_1 = "input_1";
		String predictions = "predictions";
		
		try (SavedModelBundle b = SavedModelBundle.load(modelPath, "serve")) {
			// Create the input tensor
	        float[][] inputArray = new float[resizedImage.rows()][resizedImage.cols()];
	        for (int i = 0; i < resizedImage.rows(); i++) {
	            for (int j = 0; j < resizedImage.cols(); j++) {
	                inputArray[i][j] = (float) resizedImage.get(i, j)[0];
	            }
	        }
	        int batchSize = 1;
	        long[] inputShape = new long[] {1, resizedImage.rows(), resizedImage.cols(), 1};
	        FloatBuffer inputData = FloatBuffer.allocate(inputArray.length * inputArray[0].length);
	        for (float[] row : inputArray) {
	            inputData.put(row);
	        }
	        inputData.rewind();
	        
	        Tensor<TFloat32> inputTensor = Tensor.create(inputShape, inputData);
	        
	        Session s = b.session();
	        // Run the inference
	        try {
	        	
	        	Tensor<?> result2 = s.runner()
	                    .feed(input_1, inputTensor)
	                    .fetch(predictions)
	                    .run()
	                    .get(0);
	
	            // Process the prediction result
//	        	float[] predictions = new float[(int) result.shape()[1]];
	            float[][] output = new float[1][331];
	            result2.copyTo(output);
	
	            // Print the prediction result
	            System.out.println("Prediction: " + output[0][0]);
	        
	        } catch (Exception e) {
	        	e.printStackTrace();
	        } 
	    } catch (Exception e1) {
			System.err.println("Failed to read the model file.");
			
	    }
		
		
       
        //Prediction
//        float[][] inputArray = new float[resizedImage.rows()][resizedImage.cols()];
//        for (int i = 0; i < resizedImage.rows(); i++) {
//            for (int j = 0; j < resizedImage.cols(); j++) {
//                inputArray[i][j] = (float) resizedImage.get(i, j)[0];
//            }
//        }
//        int batchSize = 1;
//        long[] inputShape = new long[] {1, resizedImage.rows(), resizedImage.cols(), 1};
//        FloatBuffer inputData = FloatBuffer.allocate(inputArray.length * inputArray[0].length);
//        for (float[] row : inputArray) {
//            inputData.put(row);
//        }
//        inputData.rewind();
//        Tensor<Float> inputTensor = Tensor.create(inputShape, inputData);
//        // Run the inference
//        try (Session session = new Session(graph)) {
//        	Tensor<?> result2 = session.runner()
//                    .feed("input", inputTensor)
//                    .fetch("output")
//                    .run()
//                    .get(0);
//
//            // Process the prediction result
////        	float[] predictions = new float[(int) result.shape()[1]];
//            float[][] output = new float[1][331];
//            result2.copyTo(output);
//
//            // Print the prediction result
//            System.out.println("Prediction: " + output[0][0]);
//        } catch(Exception e) {
//        	System.err.println("Failed to run the prediction.");
//        	e.printStackTrace();
//        }
    
		
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
		
}
