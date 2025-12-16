package com.example.demo.service;

import com.example.demo.model.event.RuleHitEvent;

public interface RuleHitProducer {

    void send(RuleHitEvent event);

}
