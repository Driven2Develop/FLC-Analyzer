package controllers;

import org.junit.Test;
import org.mockito.InjectMocks;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

/**
 * Integration test for Controller class
 *
 * @author Mengqi Liu
 * @author Bowen Cheng
 */
public class HomeControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    /**
     * Test for the <code>/</code> route
     *
     * @author Mengqi Liu
     */
    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the <code>/project/:searchTerms</code> route
     *
     * @author Mengqi Liu
     */
    @Test
    public void testSearchLatestTenProjects() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/project/Java");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the <code>/project/job/:jobId</code> route
     *
     * @author Mengqi Liu
     */
    @Test
    public void testFindProjectsByJobId() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/project/job/7");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the </code>/stats/global/:searchTerms</code> route
     *
     * @author Bowen Cheng
     */
    @Test
    public void testGetGlobalStats() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/stats/global/write");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the </code>/stats/single/:projectDesc</code> route
     *
     * @author Mengqi Liu
     */
    @Test
    public void testGetProjectStats() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/stats/single/software");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the </code>/user/:userId</code> route
     *
     * @author Mengqi Liu
     */
    @Test
    public void testFindUserById() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/user/25916363");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the </code>/user/:userId/projects</code> route
     *
     * @author Mengqi Liu
     */
    @Test
    public void testFindUserProjectsById() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/user/25916363/projects");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the </code>/project/readability/:projectDesc</code> route
     *
     * @author Iymen Abdella
     */
    @Test
    public void testGetReadability() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/project/readability/description");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * Test for the </code>/project/readability/average/:searchTerms</code> route
     *
     * @author Iymen Abdella
     */
    @Test
    public void testGetAverageReadability() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/project/readability/average/write");
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
}
