package com.okta.dev.oktar2dbc.service;

import com.okta.dev.oktar2dbc.database.HeartbeatEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class WeatherService {
    public Mono<ServerResponse> getWeatherStream(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(streamWeather(), WeatherEvent.class);
    }

    public Flux<WeatherEvent> streamWeather() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<WeatherEvent> events =
                Flux
                        .fromStream(Stream.generate(
                                ()->new WeatherEvent(
                                        new Weather(getTemprature(),
                                                getHumidity(),
                                                getWindSpeed()),
                                        LocalDateTime
                                                .now())));
        return Flux.zip(events, interval, (key, value) -> key);
    }
    private String getWindSpeed() {
        String[] windSpeeds = "100 km/h,101 km/h, 102 km/h,103 km/h, 104 km/h".split(",");
        return windSpeeds[new Random().nextInt(windSpeeds.length)];
    }
    private String getHumidity() {
        String[] humidity = "40%,41%, 42%,42%,44%,45%,46%".split(",");
        return humidity[new Random().nextInt(humidity.length)];
    }
    private String getTemprature() {
        String[] tempratures = "19C,19.5C,20C,20.5C, 21C,21.5 C,22C,22.5C,23C,23.5C,24 C"
                .split(",");
        return tempratures[new Random()
                .nextInt(tempratures.length)];
    }

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    public class Weather {
        private String temprature;
        private String humidity;
        private String windSpeed;
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class WeatherEvent {
        private Weather weather;
        private LocalDateTime date;
    }
}
