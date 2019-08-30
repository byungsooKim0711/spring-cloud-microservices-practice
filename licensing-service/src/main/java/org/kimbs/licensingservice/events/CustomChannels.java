package org.kimbs.licensingservice.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {

    @Input("inBoundOrganizationChanges")
    SubscribableChannel organizations();
}
