package ru.andrianov.emw.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.andrianov.emw.requests.repository.RequestRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl {

    private final RequestRepository requestRepository;



}
