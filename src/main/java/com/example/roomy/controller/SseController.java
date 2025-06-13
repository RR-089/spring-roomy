package com.example.roomy.controller;

import com.example.roomy.util.SseManagerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {
    private final SseManagerUtil sseManagerUtil;

    @GetMapping(value = "{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectSse(@PathVariable("userId") Long userId) {
        return sseManagerUtil.register(userId);
    }

    @PostMapping(value = "/send/{userId}")
    public void sendMessage(@PathVariable("userId") Long userId,
                            @RequestBody String message) {

        sseManagerUtil.sendToUser(userId, "message", message);
    }

    @PostMapping(value = "/broadcast")
    public void sendBroadcastMessage(@RequestBody String message) {
        sseManagerUtil.broadcast("message", message);
    }

}
