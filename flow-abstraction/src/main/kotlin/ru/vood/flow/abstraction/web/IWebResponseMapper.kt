package ru.vood.flow.abstraction.web

import org.springframework.http.ResponseEntity

interface IWebResponseMapper<T> {

    fun map(response: ResponseEntity<String>): T
}