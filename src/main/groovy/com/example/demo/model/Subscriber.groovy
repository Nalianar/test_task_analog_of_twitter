package com.example.demo.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "subscribers")
class Subscriber {

    @Id
    String id
    String subscriberId
    String subscribedId
}
