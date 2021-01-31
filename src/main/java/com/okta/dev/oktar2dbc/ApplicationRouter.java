package com.okta.dev.oktar2dbc;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import com.okta.dev.oktar2dbc.database.HeartbeatRepository;
import com.okta.dev.oktar2dbc.service.HeartbeatService;
import com.okta.dev.oktar2dbc.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;

@Configuration
public class ApplicationRouter {

    @Bean
    public RouterFunction<ServerResponse> route(HeartbeatService heartbeatService, WeatherService weatherService) {
        return RouterFunctions
                .route(RequestPredicates.GET("/heartbeat/stream1")
                                .and(RequestPredicates.contentType(MediaType.TEXT_EVENT_STREAM)),
                        this::hurr)
                .andRoute(RequestPredicates.GET("/heartbeat/stream2")
                                .and(RequestPredicates.contentType(MediaType.TEXT_EVENT_STREAM)),
                        heartbeatService::getAllHeartbeats)
                .andRoute(RequestPredicates.GET("/weather/stream").and(RequestPredicates.contentType(MediaType.TEXT_EVENT_STREAM)),
                        weatherService::getWeatherStream);
    }

    private Mono<ServerResponse> fakeStream(ServerRequest serverRequest) {
        Flux<String> flux = Flux.interval(Duration.ofSeconds(2))
                .map(sequence -> "Flux - " + LocalTime.now().toString());

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(flux, String.class);
    }
}
