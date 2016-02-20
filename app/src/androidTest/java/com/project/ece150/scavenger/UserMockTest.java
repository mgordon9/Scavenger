package com.project.ece150.scavenger;

import android.app.Application;
import android.test.ApplicationTestCase;
import com.project.ece150.scavenger.mocks.UserMock;

public class UserMockTest extends ApplicationTestCase<Application> {
    public UserMockTest() {
        super(Application.class);
    }

    public void UserMockTest() throws Exception {
        final String expected = "MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0; MyTitle: User9, User8, User7, User6, User5, User4, User3, User2, User1, User0";

        UserMock mock = new UserMock();
        final String reality = mock.toString();

        assertEquals(expected, reality);
    }
}