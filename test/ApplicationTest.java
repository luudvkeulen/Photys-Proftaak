import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.AdminController;
import models.User;
import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void changeUserTest()
    {
        User user1;
        User user2;

        AdminController adminController = new AdminController();

        user1 = new User("1234", "Heank", "de Klaas", "Klaasje@hotmai.com", "1337EL", "Straatnaam", 13, 12345695, 2);
        user2 = new User("1235", "Pietje", "de Klaas", "Pietje@hotmail.com", "1234AP", "Straatje", 2, 13245667, 0);


        assertTrue(adminController.ChangeUserToPhotographer(user1));
        assertFalse(adminController.ChangeUserToPhotographer(user2));
    }

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render();
        assertEquals("text/html", html.contentType());
        assertTrue(html.body().contains("Your new application is ready."));
    }


}
