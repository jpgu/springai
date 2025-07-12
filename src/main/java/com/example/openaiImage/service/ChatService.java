package com.example.openaiImage.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatModel chatModel;
    public ChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getResponse(String message){
         return chatModel.call(message);
    }

    public String getResponseOptions(String message){
        ChatResponse response = chatModel.call(
                new Prompt(
                    message,
                    OpenAiChatOptions.builder()
                        .withModel("gpt-4o")
                        .withTemperature(0.4)
                            // 높을수록 범용적이다. 기본값은 0.8 이다.
                        .build()
                ));
          return response.getResult().getOutput().getContent();
    }
}
