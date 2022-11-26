package ru.practicum.ewm.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.util.BaseClient;
import ru.practicum.ewm.util.EndpointHit;

import javax.servlet.http.HttpServletRequest;

@Service
public class EventClient extends BaseClient {
    @Autowired
    public EventClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .build()
        );
    }

    public Object getViews(String uri) {
        return get("/hit?uri=" + uri).getBody();
    }

    public void addRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        post(new EndpointHit("main-service", uri, ip));
    }
}
