//package controllers;
//
//import org.junit.Test;
//import play.Application;
//import play.inject.guice.GuiceApplicationBuilder;
//import play.mvc.Http;
//import play.mvc.Result;
//import play.test.WithApplication;
//
//import static org.junit.Assert.assertEquals;
//import static play.mvc.Http.Status.OK;
//import static play.test.Helpers.GET;
//import static play.test.Helpers.route;
//
///**
// * Integration test for Controller class
// *
// * @author Mengqi Liu
// * @author Bowen Cheng
// */
//public class HomeControllerTest extends WithApplication {
//
//    @Override
//    protected Application provideApplication() {
//        return new GuiceApplicationBuilder().build();
//    }
//
//    /**
//     * Test for index route
//     *
//     * @author Mengqi Liu
//     */
//    @Test
//    public void testIndex() {
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/");
//
//        Result result = route(app, request);
//        assertEquals(OK, result.status());
//    }
//
//    /**
//     * Test for global stats
//     *
//     * @author Bowen Cheng
//     */
//    @Test
//    public void testGetGlobalStats() {
//        Http.RequestBuilder request = new Http.RequestBuilder()
//                .method(GET)
//                .uri("/stats/global/write");
//
//        Result result = route(app, request);
//        assertEquals(OK, result.status());
//    }
//
//}
