package com.example.openaiImage.controller;

import com.example.openaiImage.entity.ImageRequestDTO;
import com.example.openaiImage.service.ImageService;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ImageGenerationController {

    private final ImageService imageService;

    public ImageGenerationController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/image", consumes = "application/json; charset=UTF-8")
    public List<String> image(@RequestBody ImageRequestDTO request) throws IOException {
        //String message = request.get("message"); // Map에서 "message" 키의 값을 가져옴
        ImageResponse imageResponse=imageService.getImageGen(request);
        //String imageUrl= imageResponse.getResult().getOutput().getUrl();
        //response.sendRedirect(imageUrl);
        List<String> imageUrls=imageResponse.getResults().stream()
                .map(result->result.getOutput().getUrl())
                .toList();
        return imageUrls;
    }

}
