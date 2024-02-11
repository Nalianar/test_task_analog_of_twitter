package com.example.demo.exception

class EntityNotFoundException extends RuntimeException{

    EntityNotFoundException(String message) {
        super(message)
    }
}
