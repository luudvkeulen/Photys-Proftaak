import java.util.ArrayList;

import controllers.PrijsController;
import models.Product;
import models.User;
import org.junit.*;

import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void testCalcPrice()
    {
        PrijsController prijsController = new PrijsController();

        ArrayList<Product> products = new ArrayList<>();
        Product product1 = new Product(1, "Product1", "-", 2.50);
        products.add(product1);
        Product product2 = new Product(2, "Product2", "-", 2.70);
        products.add(product2);
        Product product3 = new Product(3, "Product3", "-", 0.50);
        products.add(product3);
        Product product4 = new Product(4, "Product4", "-", 12.25);
        products.add(product4);

        double actual = prijsController.calcTotalPrice(products);

        assertEquals(17.95, actual, 0);
    }

    @Test
    public void testCalcPriceWithNullPrice()
    {
        PrijsController prijsController = new PrijsController();

        ArrayList<Product> products = new ArrayList<>();
        Product product1 = new Product(1, "Product1", "-", 0);
        products.add(product1);
        Product product2 = new Product(2, "Product2", "-", 2.70);
        products.add(product2);
        Product product3 = new Product(3, "Product3", "-", 0.50);
        products.add(product3);
        Product product4 = new Product(4, "Product4", "-", 12.25);
        products.add(product4);

        double actual = prijsController.calcTotalPrice(products);

        assertEquals(15.45, actual, 0);
    }

    @Test
    public void changeUserTest()
    {
        User user1;
        User user2;

        //user1 = new User("1234", "Heank", "de Klaas", "Klaasje@hotmai.com", "1337EL", "Straatnaam", 13, 12345695, 2);
        //user2 = new User("1235", "Pietje", "de Klaas", "Pietje@hotmail.com", "1234AP", "Straatje", 2, 13245667, 0);


        //assertTrue(adminController.ChangeUserToPhotographer(user1));
        //assertFalse(adminController.ChangeUserToPhotographer(user2));
    }

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }

    @Test
    public void renderTemplate() {
        //Content html = views.html.index.render();
        //assertEquals("text/html", html.contentType());
        //assertTrue(html.body().contains("Your new application is ready."));
    }
}
