package com.chan.api_ex.controller;

import com.chan.api_ex.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ExController {

    @PostMapping("/api/post/{id}")
    public void post(@RequestBody Address body, @PathVariable int id, @RequestParam String sort) {
        log.info("body : {}, id : {}, sort : {}", body, id, sort);
    }
}
