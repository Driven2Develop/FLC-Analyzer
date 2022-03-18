package services;

import models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wsclient.MyWSClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static helpers.DateUtil.parseDate;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static services.TestData.TEST_SUBMITTIME_1;

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
    private static String DEFAULT_USER_DISPLAY_NAME = "John Doe";
    private static long DEFAULT_REGISTRATION_DATE = 1647003600L;
    private static String DEFAULT_ROLE = "Owner";
    private static String DEFAULT_CHOSEN_ROLE = "Owner";

    private static String DEFAULT_COUNTRY_NAME = "Canada";
    private static String DEFAULT_CURRENCY_NAME = "US Dollar";

    private static boolean DEFAULT_EMAIL_VERIFIED = true;
    private static boolean DEFAULT_LIMITED_ACCOUNT = false;

    private static final CompletionStage<User> DEFAULT_USER = CompletableFuture.completedStage(buildDefaultUser());

    /**
     * Setup data for tests
     *
     * @author Yvonne Lee
     */
    @Before
    public void setup() {
        when(myWSClient.initRequest(any())).thenReturn(myWSClient);
        when(myWSClient.getResults(new HashMap<>(), DEFAULT_USER_ID, User.class)).thenReturn(DEFAULT_USER);
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
        assertEquals(DEFAULT_USERNAME, user.getUsername());
        assertEquals(parseDate(DEFAULT_REGISTRATION_DATE), user.getRegistrationDate());
        assertEquals(DEFAULT_LIMITED_ACCOUNT, user.isLimitedAccount());
        assertEquals(DEFAULT_ROLE, user.getRole());
        assertEquals(DEFAULT_CHOSEN_ROLE, user.getChosenRole());

        assertNotNull(user.getLocation());
        assertNotNull(user.getLocation().getCountry());
        assertEquals(DEFAULT_COUNTRY_NAME, user.getLocation().getCountry().getName());

        assertNotNull(user.getStatus());
        assertEquals(DEFAULT_EMAIL_VERIFIED, user.getStatus().getEmailVerified());

        assertNotNull(user.getPrimaryCurrency());
        assertEquals(DEFAULT_CURRENCY_NAME, user.getPrimaryCurrency().getName());
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
        user.setRegistrationDate(DEFAULT_REGISTRATION_DATE);
        user.setRole(DEFAULT_ROLE);
        user.setChosenRole(DEFAULT_CHOSEN_ROLE);
        user.setLimitedAccount(DEFAULT_LIMITED_ACCOUNT);
        user.setLocation(buildDefaultLocation());
        user.setPrimaryCurrency(buildDefaultCurrency());
        user.setStatus(buildDefaultStatus());

        return user;
    }

    /**
     * static method to build object default <code>Location</code>
     *
     * @author Yvonne Lee
     */
    private static Location buildDefaultLocation() {
        Location defaultLocation = new Location();
        Country defaultCountry = new Country();

        defaultCountry.setName(DEFAULT_COUNTRY_NAME);
        defaultLocation.setCountry(defaultCountry);

        return defaultLocation;
    }

    /**
     * static method to build object default <code>Currency</code>
     *
     * @author Yvonne Lee
     */
    private static Currency buildDefaultCurrency() {
        Currency defaultCurrency = new Currency();
        defaultCurrency.setName(DEFAULT_CURRENCY_NAME);
        return defaultCurrency;
    }


    /**
     * static method to build object default <code>Status</code>
     *
     * @author Yvonne Lee
     */
    private static Status buildDefaultStatus() {
        Status defaultStatus = new Status();
        defaultStatus.setEmailVerified(DEFAULT_EMAIL_VERIFIED);
        return defaultStatus;
    }
}
