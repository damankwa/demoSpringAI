package com.david.demoSpringAI;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/youtube")
public class Youtube {

    private final ChatClient chatClient;
    private final ChatModel chatModel;

    public Youtube(ChatClient.Builder chatClientBuilder, ChatModel chatModel){
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
    }

    //http://localhost:8080/youtube/popular?genre=technology
    @GetMapping("/popular")
    public String findPopularYouTubersByGenre(@RequestParam(value="genre", defaultValue = "Why is the sky blue?") String genre){

//PromptTemplate promptTemplate = PromptTemplate.builder()
    //.renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
    //.template("""
            //List 10 of the most popular Youtubers in <genre> along with their subscriber counts. IF you don't know
            // the answer, just say I don't know
        //    """)
    //.build();

        String message1 = """
            List 10 of the most popular Youtubers in {genre} along with their subscriber counts. IF you don't know
             the answer, just say "I don't know". Please format in table form
            """;

        PromptTemplate promptTemplate = new PromptTemplate(message1);

        String prompt = promptTemplate.render(Map.of("genre", genre));
        return this.chatModel.call(prompt);

    }
}
