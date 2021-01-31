package com.okta.dev.oktar2dbc.controller;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import com.okta.dev.oktar2dbc.database.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalTime;

//@RestController
//@RequestMapping(value = "/heartbeat")
public class HeartbeatController {

    private final HeartbeatRepository heartbeatRepository;
    private final Flux<HeartbeatEntity> heartbeatStream;

    static class HeartbeatData {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @Autowired
    public HeartbeatController(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
        this.heartbeatStream = heartbeatRepository.findAll().share();
    }

    @PostMapping
    public Mono<ResponseEntity<Long>> heartbeat(Principal principal, @RequestBody HeartbeatData heartbeatData) {
        String email = principal.getName();

        HeartbeatEntity heartbeatEntity = new HeartbeatEntity();
        heartbeatEntity.setTimestamp(System.currentTimeMillis());
        heartbeatEntity.setUsername(email);
        heartbeatEntity.setText(heartbeatData.getText());

        return heartbeatRepository
                .save(heartbeatEntity)
                .map(e -> ResponseEntity.ok(e.getId()))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(sequence -> "Flux - " + LocalTime.now().toString());
    }

//    @RequestMapping(
//            method = RequestMethod.GET,
//            value = "/stream",
//            produces = "text/event-stream;charset=UTF-8")
//    public Flux<HeartbeatEntity> heartbeats() {
//        return heartbeatStream;
//    }
}
