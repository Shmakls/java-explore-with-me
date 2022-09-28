package ru.andrianov.emw.requests.service;

import ru.andrianov.emw.requests.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Request addNewRequest(Request request);

    List<Request> getRequestsByEventId(Long eventId);

    Request getRequestById(Long requestId);

    Request updateRequest(Request request);

    List<Request> getRequestsWithPendingStatus(Long eventId);

    boolean existById(Long requestID);

    List<Request> getRequestsByUserId(Long userId);

    Optional<Request> getRequestByUserIdAndEventId(Long userId, Long eventId);
}
