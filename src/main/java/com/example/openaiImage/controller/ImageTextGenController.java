package com.example.openaiImage.controller;

import com.example.openaiImage.entity.ImageAnalysisVO;
import com.example.openaiImage.service.ImageTextGenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;


@RestController
@RequestMapping("/image-text")
public class ImageTextGenController {

    private final ImageTextGenService imageTextGenService;

    @Value("${upload.path}")
    private String uploadPath;

    public ImageTextGenController(ImageTextGenService imageTextGenService){
        this.imageTextGenService = imageTextGenService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ImageAnalysisVO> getMultimodalResponse(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(defaultValue = "이 이미지에 무엇이 있나요?") String message) throws IOException {

        try {
            File uploadDirectory = new File(uploadPath);

            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdir();
                System.out.println(uploadPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String filename = imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, filename);
        Files.write(filePath, imageFile.getBytes());

        String analysisText = imageTextGenService.analyzeImage(imageFile, message);
        String imageUrl = "/uploads/" + filename;

        ImageAnalysisVO response = new ImageAnalysisVO(imageUrl, analysisText, null);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/analyzemath")
    public ResponseEntity<ImageAnalysisVO> getMultimodalMathResponse(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(defaultValue = "이 이미지에 무엇이 있나요?") String message)
            throws IOException {

        // Ensure the upload directory exists
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Save the uploaded file to the specified upload path
        String filename = imageFile.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, filename);
        Files.write(filePath, imageFile.getBytes());

        // Analyze the image
        String analysisText = imageTextGenService.analyzeMathImage(imageFile, message);
        // 세제곱근, 제곱근, 곱셈
        String searchKeyword = imageTextGenService.extractKeyYouTubeSearch(analysisText);
        List<String> youtubeUrls = imageTextGenService.searchYouTubeVideos(searchKeyword);

        String imageUrl = "/uploads/" + filename;

        ImageAnalysisVO response = new ImageAnalysisVO(imageUrl, analysisText, youtubeUrls);
        return ResponseEntity.ok(response);

    }

}