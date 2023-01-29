package jrails;

import books.BookController;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class JRouterTest {

    private JRouter jRouter;

    @Before
    public void setUp() throws Exception {
        jRouter = new JRouter();
    }

    @Test
    public void addRoute() {
        jRouter.addRoute("GET", "/", String.class, "index");
        assertThat(jRouter.getRoute("GET", "/"), is("java.lang.String#index"));
    }

    @Test
    public void getRoute() {
        jRouter.addRoute("GET", "/", String.class, "good");
        assertThat(jRouter.getRoute("GET", "/"), is("java.lang.String#good"));
        assertNull(jRouter.getRoute("GET", "/newStuff"));
    }

    @Test
    public void testRoute() {

        Model model = new Model();
        jRouter.addRoute("GET", "/", BookController.class, "index");
        jRouter.addRoute("GET", "/show", BookController.class, "show");
        jRouter.addRoute("GET", "/new", BookController.class, "new_book");
        jRouter.addRoute("GET", "/edit", BookController.class, "edit");
        jRouter.addRoute("POST", "/create", BookController.class, "create");
        jRouter.addRoute("POST", "/update", BookController.class, "update");
        jRouter.addRoute("GET", "/destroy", BookController.class, "destroy");

        Map<String, String> param = new HashMap<>();
        param.put("id", "1");

        jRouter.route("GET", "/", param);
    }
}