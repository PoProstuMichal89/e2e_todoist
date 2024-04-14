package pl.mmazur.tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import pl.mmazur.factory.BrowserFactory;
import pl.mmazur.utils.Properties;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static pl.mmazur.utils.Properties.getProperty;
import static pl.mmazur.utils.StringUtils.removeRoundBrackets;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseTest.class);

    private BrowserFactory browserFactory;

    //UI
    protected Browser browser;
    protected BrowserContext uiContext;
    protected Page page;

    //API
    protected APIRequestContext apiContext;

    @BeforeAll
    void beforeAll(){
        browserFactory = new BrowserFactory();
        browser = browserFactory.getBrowser();
    }

    @BeforeEach
    void beforeEach(){
        //UI CONTEXT
        uiContext = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get(Properties.getProperty("app.login.storage.file.path"))));

        //TRACING START
        if (isTracingEnabled()) {
            uiContext.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true)
                    .setSources(true));
        }
        page = uiContext.newPage();
        page.setViewportSize(1920, 1080);
        page.navigate(getProperty("app.ui.url"));

        //API CONTEXT
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + getProperty("app.api.token"));

        apiContext = browserFactory.getPlaywright().request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(getProperty("app.api.url"))
                .setExtraHTTPHeaders(headers));
    }

    @AfterEach
    void afterEach(TestInfo testInfo){
        // TRACING STOP
        if (isTracingEnabled()) {
            String traceName = "traces/trace_"
                    + removeRoundBrackets(testInfo.getDisplayName())
                    + "_" + LocalDateTime.now().format(DateTimeFormatter
                    .ofPattern(getProperty("app.tracing.date.format"))) + ".zip";
            uiContext.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(traceName)));
        }

        apiContext.dispose();
        uiContext.close();
    }

    @AfterAll
    void afterAll() {
        browserFactory.getPlaywright().close();
    }

    private boolean isTracingEnabled() {
        return Boolean.parseBoolean(getProperty("app.tracing.enabled"));
    }







}
