package com.krisnaajiep.cachingproxy;

import com.krisnaajiep.cachingproxy.config.ITConfig;
import com.krisnaajiep.cachingproxy.config.ServerProperties;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String ORIGIN = "https://origintest.com";

    @Test
    void contextLoads() {
    }

    @Nested
    @SpringBootTest(args = {"--server.origin=" + ORIGIN})
    class ServerOriginArgument {
        @Autowired
        private ServerProperties serverProperties;

        @Test
        void run_withServerOriginArgument_shouldSetProperty() {
            assertEquals(ORIGIN, serverProperties.getOrigin());
        }
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
