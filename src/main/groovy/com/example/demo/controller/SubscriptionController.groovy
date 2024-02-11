package com.example.demo.controller

import com.example.demo.service.SubscriberService
import jakarta.validation.constraints.NotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subscription")
class SubscriptionController {

    @Autowired
    private SubscriberService subscriberService


    @PostMapping("/subscribe")
    def subscribeToUser(@RequestParam @NotBlank String subscriberId, @RequestParam @NotBlank String subscribedId) {
        return subscriberService.subscribeToUser(subscriberId, subscribedId)
    }

    @DeleteMapping("/unsubscribe")
    def unsubscribeFromUser(@RequestParam @NotBlank String subscriberId, @RequestParam @NotBlank String subscribedId){
        return subscriberService.unsubscribeFromUser(subscriberId, subscribedId)
    }

}
