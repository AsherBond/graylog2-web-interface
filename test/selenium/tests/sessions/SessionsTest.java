/**
 * Copyright 2013 Lennart Koopmann <lennart@torch.sh>
 *
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package selenium.tests.sessions;

import com.google.common.collect.Maps;
import org.fluentlenium.adapter.FluentTest;
import org.fluentlenium.adapter.util.SharedDriver;
import org.fluentlenium.core.annotation.Page;
import org.junit.Test;
import selenium.pages.DashboardPage;
import selenium.pages.LoginPage;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.fluentlenium.FluentLeniumAssertions.assertThat;
import static play.test.Helpers.*;

/**
 * @author Lennart Koopmann <lennart@torch.sh>
 */
@SharedDriver(type = SharedDriver.SharedType.PER_CLASS)
public class SessionsTest extends FluentTest {
    public static final int WEB_PORT = 9999;

    @Page
    public LoginPage loginPage;

    @Override
    public String getDefaultBaseUrl() {
        return "http://localhost:" + WEB_PORT + "/";
    }

    private Map<String, ? extends Object> getApplicationConfig() {
        final HashMap map = Maps.newHashMap();
        map.put("application.secret", "qwertyqwertyqwertyqwerty");
        map.put("graylog2-server.uris", "http://localhost:12900");
        return map;
    }

    @Test
    public void login() {
        running(testServer(WEB_PORT, fakeApplication(getApplicationConfig())), new Runnable() {
            @Override
            public void run() {
                loginPage.go();
                final DashboardPage dashboardPage = loginPage.loginAs("admin", "123123123");
                assertThat(dashboardPage).isAt();
            }
        });
    }

    @Test
    public void loginErrorNoUser() {
        running(testServer(WEB_PORT, fakeApplication(getApplicationConfig())), new Runnable() {
            @Override
            public void run() {
                loginPage.go();
                final LoginPage loginPage1 = loginPage.loginWithError("admin", "");
                assertThat(loginPage1).isAt();
            }
        });
    }
}
