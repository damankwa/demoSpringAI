package com.david.demoSpringAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }
    
    @GetMapping("/dad-joke")
    public String generate(@RequestParam(value="message", defaultValue = "Why is the sky blue?") String message){
        return this.chatClient.prompt()
            .user(message)
            .call()
            .content();
    }

}
