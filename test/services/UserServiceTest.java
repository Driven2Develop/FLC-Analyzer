package services;

import models.Country;
import models.Location;
import models.Status;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wsclient.MyWSClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for class <code>UserService</code>
 *
 * @author Yvonne Lee
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private MyWSClient myWSClient;

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

    private static final CompletionStage<User> DEFAULT_USER = CompletableFuture.completedStage(buildDefaultUser());

    /**
     * Setup data for tests
     *
     * @author Yvonne Lee
     */
    @Before
    public void setup() {
        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
        when(myWSClient.getResults(User.class)).thenReturn(DEFAULT_USER);
    }

    /**
     * test method <code>findUserById</code> in class <code>UserService</code>
     *
     * @author Yvonne Lee
     */
    @Test
    public void findUserById() throws Exception {
        //when
        User user = userService.findUserById(DEFAULT_USER_ID).toCompletableFuture().get();

        //then
        validateDefaultUser(user);
    }

    /**
     * validates default user
     *
     * @author Yvonne
     */
    private void validateDefaultUser(User user) {
        assertNotNull(user);
        assertEquals(DEFAULT_USER_ID, user.getId());
        assertEquals(DEFAULT_USER_DISPLAY_NAME, user.getDisplayName());
        assertEquals(DEFAULT_EMAIL, user.getEmail());
        assertEquals(DEFAULT_LANGUAGE, user.getPrimaryLanguage());
        assertEquals(DEFAULT_USER_PUBLIC_NAME, user.getPublicName());

        assertNotNull(user.getLocation());
        assertEquals(DEFAULT_CITY, user.getLocation().getCity());

        assertNotNull(user.getLocation().getCountry());
        assertEquals(DEFAULT_COUNTRY_NAME, user.getLocation().getCountry().getName());

        assertNotNull(user.getStatus());
        assertEquals(DEFAULT_FREELANCER_VERIFIED_USER, user.getStatus().getFreelancerVerifiedUser());
        assertEquals(DEFAULT_EMAIL_VERIFIED, user.getStatus().getEmailVerified());
        assertEquals(DEFAULT_PAYMENT_VERIFIED, user.getStatus().getPaymentVerified());
    }

    /**
     * static method to build object default <code>User</code>
     *
     * @author Yvonne Lee
     */
    private static User buildDefaultUser() {
        User user = new User();
        user.setId(DEFAULT_USER_ID);
        user.setUsername(DEFAULT_USERNAME);
        user.setDisplayName(DEFAULT_USER_DISPLAY_NAME);
        user.setPublicName(DEFAULT_USER_PUBLIC_NAME);
        user.setEmail(DEFAULT_EMAIL);
        user.setPrimaryLanguage(DEFAULT_LANGUAGE);

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
