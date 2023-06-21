package com.cursivecharacter.configuration;

import org.springframework.context.annotation.Configuration;
import org.tensorflow.TensorFlow;

@Configuration
public class LibLoadingConfig {
	static {
		nu.pattern.OpenCV.loadShared();
		System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
	}
}
