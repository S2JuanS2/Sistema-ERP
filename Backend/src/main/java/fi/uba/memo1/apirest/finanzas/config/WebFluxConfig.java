package fi.uba.memo1.apirest.finanzas.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Value("${recursos.api.base-url}")
    private String baseUrlRecursos;

    @Value("${tareas.api.base-url}")
    private String baseUrlTareas;

    @Value("${roles.api.base-url}")
    private String baseUrlRoles;

    @Value("${proyectos.api.base-url}")
    private String baseUrlProyectos;

    @Value("${horas.api.base-url}")
    private String baseUrlHoras;

    @Bean(name = "recursosWebClient")
    public WebClient getWebClientRecursos() {
        return getWebClient(baseUrlRecursos);
    }

    @Bean(name = "tareasWebClient")
    public WebClient getWebClientTareas() {
        return getWebClient(baseUrlTareas);
    }

    @Bean(name = "rolesWebClient")
    public WebClient getWebClientRoles() {
        return getWebClient(baseUrlRoles);
    }

    @Bean(name = "proyectosWebClient")
    public WebClient getWebClientProyectos() {
        return getWebClient(baseUrlProyectos);
    }

    @Bean(name = "HorasWebClient")
    public WebClient getWebClientSquad9() {
        return getWebClient(baseUrlHoras);
    }

    private WebClient getWebClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                        .doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(10))
                                .addHandlerLast(new WriteTimeoutHandler(10))));

        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(connector)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}