package ru.andrianov.emw.events.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.andrianov.emw.business.clients.BaseClient;
import ru.andrianov.emw.events.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EventClient extends BaseClient {

    public EventClient() {
        super(new RestTemplate());
    }

    public ResponseEntity<Object> saveStat(String app, String uri, String ip) {

        EndpointHit endpointHit = new EndpointHit();

        endpointHit.setApp(app);
        endpointHit.setIp(ip);
        endpointHit.setUri(uri);
        endpointHit.setTimestamp(LocalDateTime.now());

        return post("http://localhost:9090/hit", endpointHit);

    }

    public ResponseEntity<Object> getStat(String path, Map<String, Object> params) {

        return get("http://localhost:9090" + path, params);

    }

}
