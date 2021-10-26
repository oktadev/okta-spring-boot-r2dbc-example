package com.okta.dev.oktar2dbc;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import com.okta.dev.oktar2dbc.database.HeartbeatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
public class ApplicationRouter {

    @Value("classpath:pages/index.html")
    private Resource indexHtml;

    @Value("classpath:pages/protected.html")
    private Resource protectedHtml;

    private final HeartbeatRepository heartbeatRepository;

    public ApplicationRouter(HeartbeatRepository heartbeatRepository) {
        this.heartbeatRepository = heartbeatRepository;
    }

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
            .route(RequestPredicates.GET("/index"), request -> pageResponse(indexHtml))
            .andRoute(RequestPredicates.GET("/"), request -> pageResponse(indexHtml))
            .andRoute(RequestPredicates.GET("/protected"), request -> pageResponse(protectedHtml))
            .andRoute(RequestPredicates.GET("/heartbeats"), request -> {
                Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
                Flux<HeartbeatEntity> heartbeatEntityFlux = heartbeatRepository.findAll();
                Flux<HeartbeatEntity> zipped = Flux.zip(heartbeatEntityFlux, interval, (key, value) -> key);

                return ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(zipped, HeartbeatEntity.class);
            });
    }

    private static Mono<ServerResponse> pageResponse(Resource page) {
        return ServerResponse
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(DataBufferUtils.read(page, new DefaultDataBufferFactory(), 4096), DataBuffer.class);
    }
}
