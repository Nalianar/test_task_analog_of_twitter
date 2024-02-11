package com.example.demo.service

interface SubscriberService {

    def subscribeToUser(String subscriberId, String subscribedId)

    def unsubscribeFromUser(String subscriberId, String subscribedId)
}
