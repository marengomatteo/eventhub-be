package com.eventhub.event_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eventhub.event_service.entities.Event;

public interface EventRepository extends MongoRepository<Event, String> {

}
