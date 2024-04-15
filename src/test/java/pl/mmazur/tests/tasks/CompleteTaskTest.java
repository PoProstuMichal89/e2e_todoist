package pl.mmazur.tests.tasks;

import com.google.gson.JsonObject;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.mmazur.api.request.ApiRequest;
import pl.mmazur.api.steps.ApiSteps;
import pl.mmazur.tests.BaseTest;
import pl.mmazur.utils.ResponseUtils;
import pl.mmazur.utils.StringUtils;

class CompleteTaskTest extends BaseTest {
    private String projectId;

    @AfterEach
    void afterEachTest() {
        ApiRequest.delete(apiContext, "projects/" + projectId);
    }

    @Test
    void should_be_able_to_complete_task_test() {
        //GIVEN
        final String projectName = StringUtils.getRandomName();
        final String taskName = "Napisać testy do UI i API";

        final var response = ApiSteps.createProject(apiContext, projectName);
        projectId = ResponseUtils.apiResponseToJsonObject(response).get("id").getAsString();
        log.info("Vreate projcet with id {}", projectId);

        final var taskResponse = ApiSteps.createTask(apiContext, taskName, projectId);
        String taskId = ResponseUtils.apiResponseToJsonObject(taskResponse).get("id").getAsString();
        log.info("Create task with id {}", taskId);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(projectName)).click();

        //WHEN
        PlaywrightAssertions.assertThat(page.locator("div[class=task_content]:has-text(\"" + taskName + "\")")).isVisible();
        page.locator("button:left-of(:text(\""+taskName+"\"))").first().click();

        //THEN
        PlaywrightAssertions.assertThat(page.locator("div[class=task_content]:has-text(\"" + taskName + "\")")).not().isVisible();

    }
}
