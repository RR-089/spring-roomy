package com.example.roomy.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseManagerUtil {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();


    public SseEmitter register(Long userId) {
        SseEmitter emitter = new SseEmitter(0L);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((ex) -> emitters.remove(userId));

        return emitter;
    }

    public void sendToUser(Long userId, String eventName, Object message) {
        SseEmitter emitter = emitters.get(userId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(message));
            } catch (IOException ex) {
                emitters.remove(userId);
            }
        } else {
            System.out.println("Emitter not found for userId: " + userId);
        }
    }

    public void broadcast(String eventName, Object message) {
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(message));
            } catch (IOException ex) {
                emitters.remove(userId);
            }

        });
    }

}
