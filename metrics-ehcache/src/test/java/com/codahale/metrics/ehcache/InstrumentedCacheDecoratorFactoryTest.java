package com.codahale.metrics.ehcache;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codahale.metrics.MetricRegistry.name;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class InstrumentedCacheDecoratorFactoryTest {
    private static final CacheManager MANAGER = CacheManager.create();

    private MetricRegistry registry;
    private Ehcache cache;

    @BeforeEach
    public void setUp() {
        this.cache = MANAGER.getEhcache("test-config");
        assumeTrue(cache != null);

        this.registry = SharedMetricRegistries.getOrCreate("cache-metrics");
    }

    @Test
    public void measuresGets() {
        cache.get("woo");

        assertThat(registry.timer(name(Cache.class, "test-config", "gets")).getCount())
                .isEqualTo(1);

    }

    @Test
    public void measuresPuts() {
        cache.put(new Element("woo", "whee"));

        assertThat(registry.timer(name(Cache.class, "test-config", "puts")).getCount())
                .isEqualTo(1);
    }
}
