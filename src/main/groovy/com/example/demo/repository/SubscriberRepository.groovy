package com.example.demo.repository

import com.example.demo.model.Subscriber
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriberRepository extends MongoRepository<Subscriber, String> {

    boolean existsBySubscriberIdAndSubscribedId(String s1, String s2)

    Optional<Subscriber> findBySubscriberIdAndSubscribedId(String s1, String s2)

    List<Subscriber> findBySubscriberId(String userId)
}