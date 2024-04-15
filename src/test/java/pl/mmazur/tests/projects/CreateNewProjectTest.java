package pl.mmazur.tests.projects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.mmazur.tests.BaseTest;
import pl.mmazur.api.request.ApiRequest;
import pl.mmazur.api.steps.ApiSteps;
import pl.mmazur.utils.ResponseUtils;
import pl.mmazur.utils.StringUtils;

public class CreateNewProjectTest extends BaseTest {
    private String projectId;

    @AfterEach
    void afterEachTest(){
        ApiRequest.delete(apiContext, "projects/" + projectId);
    }

    @Test
    void should_create_new_project_test() {
        //GIVEN
        final var projectName = StringUtils.getRandomName();

        //WHEN
        final var response = ApiSteps.createProject(apiContext, projectName);
        projectId = ResponseUtils.apiResponseToJsonObject(response).get("id").getAsString();

        //THEN
        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(projectName))).isVisible();

        page.waitForTimeout(10000);
    }
}
