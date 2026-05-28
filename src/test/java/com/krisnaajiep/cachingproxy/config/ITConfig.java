package com.krisnaajiep.cachingproxy.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(ITContainers.class)
public class ITConfig {
}
