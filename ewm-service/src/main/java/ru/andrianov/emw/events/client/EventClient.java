package ru.andrianov.emw.events.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.andrianov.emw.business.clients.BaseClient;
import ru.andrianov.emw.events.model.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        EndpointHitDto endpointHitDto = new EndpointHitDto();

        endpointHitDto.setApp(app);
        endpointHitDto.setIp(ip);
        endpointHitDto.setUri(uri);
        endpointHitDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return post("/hit", endpointHitDto);

    }

    public ResponseEntity<Object> getStat(String path, Map<String, Object> params) {

        return get("" + path + "?start={start}&end={end}&uris={uris}", params);

    }

}
