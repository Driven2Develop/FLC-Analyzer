package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wsclient.MyWSClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private MyWSClient myWSClient;

    @Mock
    private ProjectService projectService;

    private static long DEFAULT_USER_ID = 1L;
    private static String DEFAULT_USERNAME = "john_doe";
    private static String DEFAULT_EMAIL = "johndoe@soen.edu";
    private static String DEFAULT_USER_DISPLAY_NAME = "John Doe";
    private static String DEFAULT_USER_PUBLIC_NAME = "John Doe";
    private static String DEFAULT_CITY = "Montreal";
    private static String DEFAULT_COUNTRY_NAME = "Canada";
    private static String DEFAULT_LANGUAGE = "en";
    private static boolean DEFAULT_FREELANCER_VERIFIED_USER = true;
    private static boolean DEFAULT_EMAIL_VERIFIED = true;
    private static boolean DEFAULT_PAYMENT_VERIFIED = false;

    private static String DEFAULT_PROJECT_TYPE = "Default Project Type";

    @Test
    public void findUserById() throws Exception {
        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
        when(myWSClient.get()).thenReturn(buildUserFromJsonNode());
        User user = userService.findUserById(DEFAULT_USER_ID);

        assertNotNull(user);
        assertEquals(user.getId(), DEFAULT_USER_ID);
        assertEquals(user.getDisplayName(), DEFAULT_USER_DISPLAY_NAME);
    }

    @Test
    public void findUserAndProjectsById() throws Exception {
        String test1Title = "Test 1";
        String test2Title = "Test 2";

        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
        when(myWSClient.get()).thenReturn(buildUserFromJsonNode());
        when(projectService.findProjectsByOwnerId(DEFAULT_USER_ID)).thenReturn(Arrays.asList(
                buildProject(test1Title, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, Collections.emptyList(), 1647003600L),
                buildProject(test2Title, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, Collections.emptyList(), 1646139600L)
        ));

        User user = userService.findUserAndProjectsById(DEFAULT_USER_ID);

        assertNotNull(user);
        assertEquals(user.getId(), DEFAULT_USER_ID);
        assertEquals(user.getUsername(), DEFAULT_USERNAME);
        assertEquals(user.getDisplayName(), DEFAULT_USER_DISPLAY_NAME);
        assertEquals(user.getPublicName(), DEFAULT_USER_PUBLIC_NAME);
        assertEquals(user.getEmail(), DEFAULT_EMAIL);
        assertEquals(user.getPrimaryLanguage(), DEFAULT_LANGUAGE);

        assertEquals(user.getLocation().getCity(), DEFAULT_CITY);
        assertEquals(user.getLocation().getCountry().getName(), DEFAULT_COUNTRY_NAME);

        assertEquals(user.getStatus().getEmailVerified(), DEFAULT_EMAIL_VERIFIED);
        assertEquals(user.getStatus().getFreelancerVerifiedUser(), DEFAULT_FREELANCER_VERIFIED_USER);
        assertEquals(user.getStatus().getPaymentVerified(), DEFAULT_PAYMENT_VERIFIED);

        assertThat(user.getProjects())
                .isNotNull()
                .hasSize(2)
                .extracting(Project::getTitle, Project::getOwnerId, Project::getType, Project::getJobs, Project::getTimeSubmitted)
                .containsExactlyInAnyOrder(
                        tuple(test1Title, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, Collections.emptyList(), LocalDate.of(2022, 03, 11)),
                        tuple(test2Title, DEFAULT_USER_ID, DEFAULT_PROJECT_TYPE, Collections.emptyList(), LocalDate.of(2022, 03, 1))
                );
    }

    private JsonNode buildUserFromJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.valueToTree(buildDefaultUser());
    }

    private static Project buildProject(String title, long ownerId, String jobType, List<Job> jobs, long time) {
        Project project = new Project();
        project.setTitle(title);
        project.setOwnerId(ownerId);
        project.setType(jobType);
        project.setJobs(jobs);
        project.setTimeSubmitted(time);
        return project;
    }

    private static User buildDefaultUser() {
        User user = new User();
        user.setId(DEFAULT_USER_ID);
        user.setUsername(DEFAULT_USERNAME);
        user.setEmail(DEFAULT_EMAIL);
        user.setDisplayName(DEFAULT_USER_DISPLAY_NAME);
        user.setPrimaryLanguage(DEFAULT_LANGUAGE);
        user.setPublicName(DEFAULT_USER_PUBLIC_NAME);

        Location defaultLocation = new Location();
        defaultLocation.setCity(DEFAULT_CITY);

        Country defaultCountry = new Country();
        defaultCountry.setName(DEFAULT_COUNTRY_NAME);
        defaultLocation.setCountry(defaultCountry);
        user.setLocation(defaultLocation);

        Status defaultStatus = new Status();
        defaultStatus.setFreelancerVerifiedUser(DEFAULT_FREELANCER_VERIFIED_USER);
        defaultStatus.setEmailVerified(DEFAULT_EMAIL_VERIFIED);
        defaultStatus.setPaymentVerified(DEFAULT_PAYMENT_VERIFIED);
        user.setStatus(defaultStatus);

        return user;
    }
}
