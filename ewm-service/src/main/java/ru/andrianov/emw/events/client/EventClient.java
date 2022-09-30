package ru.andrianov.emw.events.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.andrianov.emw.business.clients.BaseClient;
import ru.andrianov.emw.events.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EventClient extends BaseClient {

    @Autowired
    public EventClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> saveStat(String app, String uri, String ip) {

        EndpointHit endpointHit = new EndpointHit();

        endpointHit.setApp(app);
        endpointHit.setIp(ip);
        endpointHit.setUri(uri);
        endpointHit.setTimestamp(LocalDateTime.now());

        return post("/hit", endpointHit);

    }

    public ResponseEntity<Object> getStat(String path, Map<String, Object> params) {

        return get("" + path, params);

    }

}
