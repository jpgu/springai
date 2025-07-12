package com.example.openaiImage.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ImageTextGenService {

    private final ChatModel chatModel;

    @Value("classpath:/system.message")
    private Resource defaultSystemMessage;

    @Autowired
    private MessageSource messageSource;

    public ImageTextGenService(ChatModel chatModel){
        this.chatModel = chatModel;
    }

    public String analyzeImage(MultipartFile imageFile, String message) {

        String contentType = imageFile.getContentType();
        if(!MimeTypeUtils.IMAGE_PNG_VALUE.equals(contentType) &&
            !MimeTypeUtils.IMAGE_JPEG_VALUE.equals(contentType)) {
            throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
        }
        try{
            var media = new Media(MimeType.valueOf(contentType), imageFile.getResource());
            
            // 사용자 메세지
            var userMessage = new UserMessage(message, media);
            
            // 시스템 메세지. => 역할 부여 등
//            var systemMessage = new SystemMessage(defaultSystemMessage);
              var systemMessage = new SystemMessage(messageSource.getMessage("image.analyze", null, Locale.of("en")));

            return chatModel.call(userMessage, systemMessage);
        }
        catch(Exception e){
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.", e);
        }
    }

    public List<String> searchYouTubeVideos(String query) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=EBS " +
                query + "&order=relevance&key=AIzaSyC7zZEQ1NwSClmPhz_c73PbZXwHTtgnl0w";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());

        List<String> videoUrls = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray items = jsonResponse.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String videoId = item.getJSONObject("id").getString("videoId");
            videoUrls.add(videoId);
        }
        return videoUrls;
    }

    public String extractKeyYouTubeSearch(String analysisText) {
        String keyword=null;
        if(analysisText.indexOf("핵심 키워드:")!=-1){
            //                                                                    핵심 키워드: 세제곱근, 제곱근, 곱셈
            keyword=analysisText.substring(analysisText.indexOf("핵심 키워드:")).split(":")[1].trim();
        }
        //          세제곱근, 제곱근, 곱셈
        return keyword;
    }

    public String analyzeMathImage(MultipartFile imageFile, String message) {

        String contentType = imageFile.getContentType();
        if (!MimeTypeUtils.IMAGE_PNG_VALUE.equals(contentType) &&
                !MimeTypeUtils.IMAGE_JPEG_VALUE.equals(contentType)) {
            throw new IllegalArgumentException("지원되지 않는 이미지 형식입니다.");
        }

        var media = new Media(MimeType.valueOf(contentType), imageFile.getResource());
        var userMessage = new UserMessage(message, media);
        var systemMessage = new SystemMessage(messageSource.getMessage("image.math", null, Locale.getDefault()));
        return chatModel.call(userMessage, systemMessage);
    }
}
