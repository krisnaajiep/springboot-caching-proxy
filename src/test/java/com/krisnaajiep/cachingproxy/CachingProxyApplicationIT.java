package com.krisnaajiep.cachingproxy;

import com.krisnaajiep.cachingproxy.config.ITConfig;
import com.krisnaajiep.cachingproxy.config.ServerProperties;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.shell.test.ShellAssertions;
import org.springframework.shell.test.ShellScreen;
import org.springframework.shell.test.ShellTestClient;
import org.springframework.shell.test.autoconfigure.ShellTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(ITConfig.class)
class CachingProxyApplicationIT {
    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    @Qualifier("setServerOrigin")
    private ApplicationRunner serverOriginRunner;

    @Test
    void contextLoads() {
    }

    @Test
    void run_withEmptyServerOriginValues_shouldThrowIllegalArgumentException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> serverOriginRunner.run(new DefaultApplicationArguments("--server.origin="))
        );
    }

    @Test
    void run_withServerOriginValue_shouldSetProperty() throws Exception {
        String origin = "https://origintest.com";
        serverOriginRunner.run(new DefaultApplicationArguments("--server.origin=" + origin));
        assertEquals(origin, serverProperties.getOrigin());
    }

    @Nested
    @ShellTest
    class ShellCommandIT {
        @Autowired
        private ShellTestClient shellTestClient;

        @Test
        void execute_withClearCacheOption_shouldClearCache() throws Exception {
            ShellScreen shellScreen = shellTestClient.sendCommand("clear-cache");

            ShellAssertions.assertThat(shellScreen)
                    .containsText("Clearing all cache data...")
                    .containsText("Cache cleared successfully.");
        }
    }

}
