package com.example.demo.service.impl

import com.example.demo.dto.SubscriberDTO
import com.example.demo.exception.EntityNotFoundException
import com.example.demo.model.Subscriber
import com.example.demo.repository.SubscriberRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.SubscriberService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Slf4j
class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private final SubscriberRepository subscriberRepository

    @Autowired
    private final UserRepository userRepository

    SubscriberServiceImpl(SubscriberRepository subscriberRepository, UserRepository userRepository){
        this.subscriberRepository = subscriberRepository
        this.userRepository = userRepository
    }


    @Override
    def subscribeToUser(String subscriberId, String subscribedId) {
        try {
            if(subscriberRepository.existsBySubscriberIdAndSubscribedId(subscriberId, subscribedId)) {
                return ResponseEntity.badRequest().body("User with id = " + subscriberId + " is already subscribed to user with id = " + subscribedId)
            }
            userRepository.findById(subscriberId).orElseThrow({ new EntityNotFoundException("User with id: " + subscriberId + " does not exist") })

            userRepository.findById(subscribedId).orElseThrow({ new EntityNotFoundException("User with id: " + subscribedId + " does not exist") })

            Subscriber subscriber = new Subscriber(
                    subscriberId: subscriberId,
                    subscribedId: subscribedId
            )
            subscriber = subscriberRepository.save(subscriber)
            return ResponseEntity.ok().body(SubscriberDTO.builder()
                    .id(subscriber.id)
                    .subscribedId(subscriber.subscribedId)
                    .subscriberId(subscriber.subscriberId)
                    .build())
        } catch (Exception ex){
            log.error(":subscribeToUser: cannot subscribe to user with this parameters: subscriberId = " + subscriberId + " subscribedId = " + subscribedId, ex)
            return ResponseEntity.badRequest().body("Cannot subscribe to user with this parameters: subscriberId = " + subscriberId + " subscribedId = " + subscribedId)
        }
    }

    @Override
    def unsubscribeFromUser(String subscriberId, String subscribedId) {
        try {
            Subscriber subscriber = subscriberRepository.findBySubscriberIdAndSubscribedId(subscriberId, subscriberId).orElseThrow({ new EntityNotFoundException("Subscriber with id: " + subscriberId + " does not subscribed to user: " + subscribedId) })

            subscriberRepository.delete(subscriber)
            return ResponseEntity.ok().body("User " + subscriberId + " is unsubscribed from user " + subscribedId)
        } catch (Exception ex){
            log.error(":unsubscribeFromUser: cannot unsubscribe from user with this parameters: subscriberId = " + subscriberId + " subscribedId = " + subscribedId, ex)
            return ResponseEntity.badRequest().body("Cannot unsubscribe from user with this parameters: subscriberId = " + subscriberId + " subscribedId = " + subscribedId)

        }
    }
}
