package com.eventhub.agenda_service.service;

import java.util.List;

import org.springframework.stereotype.Service;


import com.eventhub.agenda_service.dto.AgendaResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {
    

    public AgendaResponse getAgendaByEvent() {
      return new AgendaResponse();
    }

    public void createAgenda() {

    }

    public void updateAgenda(String id) {
        // Logic to update the agenda
    }

    public void deleteAgenda(String id) {
        // Logic to delete the agenda
    }
}

