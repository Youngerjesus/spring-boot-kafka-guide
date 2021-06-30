package com.example.springkafkademo.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControlPlane {
    ApplicationEventPublisher applicationEventPublisher;

    public ControlPlane(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void refusingTraffic() {
        AvailabilityChangeEvent.publish(applicationEventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
    }

    public void acceptTraffic() {
        AvailabilityChangeEvent.publish(applicationEventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
    }

    @Async
    @EventListener
    public void waitForAWhileFromRefusingTraffic(AvailabilityChangeEvent<ReadinessState> readiness) throws  InterruptedException {
        if(readiness.getState() == ReadinessState.REFUSING_TRAFFIC) {
            Thread.sleep(3000L);
            acceptTraffic();
        }
    }

}
