package no.api.regurgitator.health;

import com.codahale.metrics.health.HealthCheck;
import io.netty.handler.codec.http.HttpHeaders;

public class AlwaysGood extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        HttpHeaders headers;

        return Result.healthy();
    }
}
