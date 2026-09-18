package com.hexin.cbas.spider.application.manager;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 指标注册操作类
 *
 * @author huangjiarong@myhexin.com
 * @date 2024/10/29 15:41
 **/
@Component
@SuppressWarnings("all")
public class MetricManager implements InitializingBean {

    public static MetricManager INSTANCE;
    private final MeterRegistry registry;
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();
    private final Map<String, Gauge> gaugeCache = new ConcurrentHashMap<>();
    private final Map<String, AtomicNumber> gaugeNumberCache = new ConcurrentHashMap<>();

    public MetricManager(MeterRegistry registry) {
        this.registry = registry;
    }

    public void counter(String metricKey) {
        if (!StringUtils.hasText(metricKey)) {
            return;
        }
        Counter counter = counterCache.computeIfAbsent(metricKey, r -> Counter.builder(metricKey).register(registry));
        counter.increment();
    }


    public void gauge(String metricKey, String tagKey, String tagValue, Number value) {
        if (!StringUtils.hasText(metricKey)) {
            return;
        }
        String cacheKey = metricKey + "&" + tagValue;
        AtomicNumber atomicNumber = gaugeNumberCache.computeIfAbsent(cacheKey, r -> new AtomicNumber(0));
        atomicNumber.set(value);
        gaugeCache.computeIfAbsent(cacheKey, r -> Gauge.builder(metricKey, new Supplier<Number>() {
                    @Override
                    public Number get() {
                        return atomicNumber;
                    }
                })
                .tag(tagKey, tagValue)
                .register(registry));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        INSTANCE = this;
    }


    static class AtomicNumber extends Number {
        private AtomicReference<Number> valueHolder;

        public AtomicNumber(Number value) {
            valueHolder = new AtomicReference<>(value);
        }

        public Number get() {
            return valueHolder.get();
        }

        public void set(Number newValue) {
            valueHolder.set(newValue);
        }

        @Override
        public int intValue() {
            return valueHolder.get().intValue();
        }

        @Override
        public long longValue() {
            return valueHolder.get().longValue();
        }

        @Override
        public float floatValue() {
            return valueHolder.get().floatValue();
        }

        @Override
        public double doubleValue() {
            return valueHolder.get().doubleValue();
        }
    }
}
