package com.TempoLux.api.controller;

import com.TempoLux.domain.dto.request.ChatRequestDto;
import com.TempoLux.domain.dto.response.ChatResponseDto;
import com.TempoLux.application.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Endpoint reactivo del asistente virtual LuxBot (integración con Groq API + tool calling).
 */
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Mono<ChatResponseDto> chat(@Valid @RequestBody ChatRequestDto request) {
        return chatService.chat(request);
    }
}
