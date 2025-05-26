package com.david.demoSpringAI;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompts")
public class Youtube {

    private final ChatClient chatClient;
    private final ChatModel chatModel;

    public Youtube(ChatClient.Builder chatClientBuilder, ChatModel chatModel){
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
    }

    //http://localhost:8080/prompts/popular?genre=technology
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

    //http://localhost:8080/prompts/author/craig%20walls
    @GetMapping("/author/{author}")
    public Map<String, Object> getAuthorsSocialLinks(@PathVariable String author){

            Map<String, Object> result = ChatClient.create(chatModel).prompt()
        .user(u -> u.text("Generate a list of links for the author {author}")
                    .param("author", "Include the authors name as the key and any social network\r\n" + //
                                                "            links as the object {format} 'numbers'"))
        .call()
        .entity(new ParameterizedTypeReference<Map<String, Object>>() {});

        return result;
        

    }

    //http://localhost:8080/prompts/by-author
    @GetMapping("/by-author")
    public Author getBooksByAuthor(@RequestParam(value = "author", defaultValue = "Stephen King") String author){

        String message1 = """
                Generate a list of books written by the author {author}. If you aren't positive that a book
            belongs to this author please don't include it
                """;

        var authorRet= ChatClient.create(this.chatModel).prompt()
        .user(message1)
        .call()
        .entity(Author.class);
        return authorRet;
    
    }
}
