package com.example.openaiImage.service;

import com.example.openaiImage.entity.ImageRequestDTO;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final OpenAiImageModel openAiImageModel;

    public ImageService(OpenAiImageModel openAiImageModel){

        this.openAiImageModel = openAiImageModel;
    }

    public ImageResponse getImageGen(ImageRequestDTO request){

        ImageResponse imageResponse = openAiImageModel
                .call(new ImagePrompt(request.getMessage()
                    ,OpenAiImageOptions.builder()
                        .withModel(request.getModel())
                        .withN(request.getN())
                        .withQuality("hd")
                        .withWidth(1024)
                        .withHeight(1024)
                        .build()
                )
            );
        return imageResponse;
    }
}
