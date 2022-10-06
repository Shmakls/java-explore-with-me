package ru.andrianov.emw.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.requests.model.Request;
import ru.andrianov.emw.requests.model.RequestStatus;
import ru.andrianov.emw.requests.repository.RequestRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    @Override
    public Request addNewRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getRequestsByEventId(Long eventId) {
        return requestRepository.getRequestsByEvent(eventId);
    }

    @Override
    public Request getRequestById(Long requestId) {
        return requestRepository.getReferenceById(requestId);
    }

    @Override
    public Request updateRequest(Request request) {
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getRequestsWithPendingStatus(Long eventId) {
        return requestRepository.getRequestsByStatusAndEvent(RequestStatus.PENDING, eventId);
    }

    @Override
    public List<Request> getRequestsByUserId(Long userId) {
        return requestRepository.getRequestsByRequester(userId);
    }

    @Override
    public Optional<Request> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        return requestRepository.getFirstByRequesterAndEvent(userId, eventId);
    }

    @Override
    public boolean existById(Long requestID) {
        return requestRepository.existsById(requestID);
    }
}
