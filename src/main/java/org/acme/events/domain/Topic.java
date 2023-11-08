package org.acme.events.domain;

public enum Topic {
    TOPIC_A("topic_a"),
    TOPIC_B("topic_b");

    private final String topicName;

    private Topic(String name) {
        topicName = name;
    }

    public String getTopicName() {
        return topicName;
    }
}
