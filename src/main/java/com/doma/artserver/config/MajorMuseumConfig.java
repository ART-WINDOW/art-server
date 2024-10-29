package com.doma.artserver.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "major-museums")
@Getter
public class MajorMuseumConfig implements ApplicationEventPublisherAware {

    private List<String> names;
    private ApplicationEventPublisher eventPublisher;

    public void setNames(List<String> names) {
        this.names = names;
        eventPublisher.publishEvent(new MajorMuseumNamesUpdatedEvent(this, names));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public static class MajorMuseumNamesUpdatedEvent {
        private final Object source;
        private final List<String> names;

        public MajorMuseumNamesUpdatedEvent(Object source, List<String> names) {
            this.source = source;
            this.names = names;
        }

        public Object getSource() {
            return source;
        }

        public List<String> getNames() {
            return names;
        }
    }
}