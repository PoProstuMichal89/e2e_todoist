package pl.mmazur.tests.tasks;

import com.google.gson.JsonObject;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
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
    void afterEachTest(){
        ApiRequest.delete(apiContext, "projects/" + projectId);
    }

     @Test
     void should_be_able_to_complete_task_test(){
         //GIVEN
         final String projectName = StringUtils.getRandomName();
         final String taskName = "Napisać testy do UI i API";

         final var  response = ApiSteps.createProject(apiContext, projectName);
         projectId = ResponseUtils.apiResponseToJsonObject(response).get("id").getAsString();

         JsonObject taskPayload = new JsonObject();
         taskPayload.addProperty("content", taskName);
         taskPayload.addProperty("project_id", projectId);
         final var taskResponse = apiContext.post("tasks", RequestOptions.create().setData(taskPayload));

         PlaywrightAssertions.assertThat(taskResponse).isOK();
         Assertions.assertThat(ResponseUtils.apiResponseToJsonObject(taskResponse).get("id").getAsString()).isNotNull();
         Assertions.assertThat(ResponseUtils.apiResponseToJsonObject(taskResponse).get("content").getAsString()).isEqualTo(taskName);
         page.waitForTimeout(15000);
     }
}
