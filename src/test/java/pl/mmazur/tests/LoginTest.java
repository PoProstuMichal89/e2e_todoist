package pl.mmazur.tests;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;
import pl.mmazur.utils.Properties;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {
    @Test
    void should_login_todoist_app_test(){
        page.waitForCondition(()-> page.locator("#todoist_app #loading").isHidden(), new Page.WaitForConditionOptions().setTimeout(30000));
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Logowanie"))).isVisible();

        page.getByPlaceholder("Wpisz swój e-mail...").fill(Properties.getProperty("app.ui.username"));
        page.getByPlaceholder("Wpisz hasło...").fill(Properties.getProperty("app.ui.password"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logowanie")).click();

        page.waitForTimeout(3000);

        uiContext.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get(Properties.getProperty("app.login.storage.file.path"))));
    }

    @Test
    void should_open_app_as_logged_user_test(){
        page.waitForCondition(()-> page.locator("todoist_app #loading").isHidden(), new Page.WaitForConditionOptions().setTimeout(30000));
        page.waitForTimeout(5000);
    }

}
