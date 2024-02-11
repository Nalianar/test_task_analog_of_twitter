package com.example.demo

import com.example.demo.model.Subscriber
import com.example.demo.model.User
import com.example.demo.repository.SubscriberRepository
import com.example.demo.repository.UserRepository
import com.example.demo.service.impl.SubscriberServiceImpl
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class SubscriberServiceImplTest extends Specification{

    SubscriberRepository subscriberRepository
    UserRepository userRepository

    SubscriberServiceImpl classUnderTest

    def setup(){
        subscriberRepository = Mock()
        userRepository = Mock()

        classUnderTest = new SubscriberServiceImpl(subscriberRepository, userRepository)
    }

    def "subscribeToUser: should return status ok"(){
        given:
        def firstId = "id1"
        def secondId = "id2"

        when:
        ResponseEntity response = classUnderTest.subscribeToUser(firstId, secondId)

        then:
        1 * subscriberRepository.existsBySubscriberIdAndSubscribedId(firstId, secondId) >> false
        2 * userRepository.findById(_) >> Optional.of(new User())
        1 * subscriberRepository.save(_) >> new Subscriber(id: "id", subscribedId: "id", subscriberId: "id" )
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "subscribeToUser: should return status bad request"(){
        given:
        def firstId = "id1"
        def secondId = "id2"

        when:
        ResponseEntity response = classUnderTest.subscribeToUser(firstId, secondId)

        then:
        1 * subscriberRepository.existsBySubscriberIdAndSubscribedId(firstId, secondId) >> false
        1 * userRepository.findById(_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }

    def "subscribeToUser: should return status bad request user is already subscribed"(){
        given:
        def firstId = "id1"
        def secondId = "id2"

        when:
        ResponseEntity response = classUnderTest.subscribeToUser(firstId, secondId)

        then:
        1 * subscriberRepository.existsBySubscriberIdAndSubscribedId(firstId, secondId) >> true
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
        response.getBody() == "User with id = " + firstId + " is already subscribed to user with id = " + secondId
    }

    def "unsubscribeToUser: should return status ok"(){
        given:
        def firstId = "id1"
        def secondId = "id2"

        when:
        ResponseEntity response = classUnderTest.unsubscribeFromUser(firstId, secondId)

        then:
        1 * subscriberRepository.findBySubscriberIdAndSubscribedId(_, _) >> Optional.of(new Subscriber())
        response.getStatusCode() == ResponseEntity.ok().build().getStatusCode()
    }

    def "unsubscribeToUser: should return status bad request"(){
        given:
        def firstId = "id1"
        def secondId = "id2"

        when:
        ResponseEntity response = classUnderTest.unsubscribeFromUser(firstId, secondId)

        then:
        1 * subscriberRepository.findBySubscriberIdAndSubscribedId(_,_) >> Optional.empty()
        response.getStatusCode() == ResponseEntity.badRequest().build().getStatusCode()
    }
}
