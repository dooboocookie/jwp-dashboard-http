package org.apache.coyote.http11.request;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private final String id;
    private final Map<String, Object> value = new ConcurrentHashMap<>();
    private final LocalDateTime expiredAt;

    public Session() {
        this(UUID.randomUUID().toString(), LocalDateTime.now().plusDays(1));
    }

    public Session(final String id,
                   final LocalDateTime expiredAt) {
        this.id = id;
        this.expiredAt = expiredAt;
    }


    public Object getAttribute(final String name) {
        validate();
        return value.get(name);
    }

    public void setAttribute(final String name, final Object value) {
        validate();
        this.value.put(name, value);
    }

    private void validate() {
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("유효기간이 지난 세션입니다");
        }
    }

    public boolean isAvailable() {
        return !value.isEmpty() &&
                expiredAt.isAfter(LocalDateTime.now());
    }

    public String getId() {
        return id;
    }
}
