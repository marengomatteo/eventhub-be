package com.eventhub.event_service.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eventhub.event_service.entities.Event;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findByUserId(String userId);
}
