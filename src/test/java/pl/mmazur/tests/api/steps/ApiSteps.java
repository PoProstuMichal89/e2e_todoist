package pl.mmazur.tests.api.steps;

import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.assertj.core.api.Assertions;
import pl.mmazur.tests.api.request.ApiRequest;
import pl.mmazur.utils.ResponseUtils;

public class ApiSteps {
    protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApiRequest.class);

    public static APIResponse createProject(APIRequestContext apiContext, String projectName){
        JsonObject payload = new JsonObject();
        payload.addProperty("name", projectName);

        //WHEN
        final var apiResponse = ApiRequest.post(apiContext, "projects", payload);
        log.info("Created new project with id {}", ResponseUtils.apiResponseToJsonObject(apiResponse).get("id").getAsString());
        PlaywrightAssertions.assertThat(apiResponse).isOK();

        Assertions.assertThat(ResponseUtils.apiResponseToJsonObject(apiResponse).get("id").getAsString()).isNotNull();
        Assertions.assertThat(ResponseUtils.apiResponseToJsonObject(apiResponse).get("name").getAsString()).isEqualTo(projectName);

        return apiResponse;
    }
}
