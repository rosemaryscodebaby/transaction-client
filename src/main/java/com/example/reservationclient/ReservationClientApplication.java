package com.example.reservationclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}

	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}

	@Bean
	RouterFunction<ServerResponse> serverResponseRouterFunction(ReservationClient rc) {
		return route()
				//HTTP Service
				.GET("/reservations/names", serverRequest -> {

					Flux<String> map = rc
							.getReservations()
							.map(Reservation::getName)
							.onErrorResume(throwable -> Flux.just("EEEEK!"))
							.retryBackoff(10, Duration.ofSeconds(1));

					return ServerResponse.ok().body(map, String.class);
				}).build();

	}
}

//HTTP CLIENT
@Component
@RequiredArgsConstructor
class ReservationClient {
	private final WebClient webClient;

	Flux<Reservation> getReservations() {
		return this.webClient
				.get()
				.uri("http://localhost:8080/reservations")
				.retrieve()
				.bodyToFlux(Reservation.class);
	}

}


@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingRequest {
	private String name;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingResponse {
	private String message;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {

	private String id;
	private String name;
}
