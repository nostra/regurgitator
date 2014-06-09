package no.api.proxyrecorder.health;

import com.codahale.metrics.health.HealthCheck;

/**
 *
 */
public class AlwaysGood extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
