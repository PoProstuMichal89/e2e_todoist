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

class EditProjectNameTest extends BaseTest {

    private String projectId;

    @AfterEach
    void afterEachTest(){
        ApiRequest.delete(apiContext, "projects/" + projectId);
    }

    @Test
    void should_edit_project_name_test() {
        //GIVEN
        final var projectName = StringUtils.getRandomName();
        final var response = ApiSteps.createProject(apiContext, projectName);
        projectId = ResponseUtils.apiResponseToJsonObject(response).get("id").getAsString();
        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(projectName))).isVisible();

        //WHEN
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(projectName)).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(projectName)).click();
        page.locator("input[value="+projectName+"]").fill(projectName+ " EDIT");
        page.locator("input[aria-label='Edit title']").press("Enter");



        //THEN
        PlaywrightAssertions.assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(projectName+ " EDIT"))).isVisible();
    }


}
