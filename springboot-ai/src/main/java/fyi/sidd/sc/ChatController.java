package fyi.sidd.sc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Simple blocking call to generate a response from the AI.
     */
    @GetMapping("/ai/promt/joke")
    public String promtJoke(
            @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.debug("Received message for generate: {}", message);
        return this.chatModel.call(message);
    }
    
    /**
     * Simple blocking call to generate a response from the AI.
     */
    @GetMapping("/ai/promt/generate")
    public String promtGeneral(
            @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.debug("Received message for generate: {}", message);
        return this.chatModel.call(message);
    }

    /**
     * Reactive streaming endpoint that returns full ChatResponse objects.
     * Useful for structured JSON consumers.
     */
    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(
            @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.debug("Received message for generateStream: {}", message);
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

    /**
     * Alternative streaming endpoint that streams plain text chunks
     * as Server-Sent Events (SSE) for browser clients.
     */
    @GetMapping(value = "/ai/generateStreamText", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateStreamText(
            @RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.debug("Received message for generateStreamText: {}", message);
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt)
                .map(resp -> resp.getResult().getOutput().getText());
    }
}
