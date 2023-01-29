package jrails;

import books.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class ModelTest {

    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new Model(){};
        Model.reset();
    }

    @Test
    public void id() {
        assertThat(model.id(), notNullValue());
        Assert.assertEquals(model.id(), 0);
        Book b1 = new Book();
        b1.title = "Book1";
        b1.author = "author1";
        b1.num_copies = 1;
        Assert.assertEquals(b1.id(), 0);
        b1.save();
        Assert.assertNotEquals(b1.id(), 0);
        Book b2 = new Book();
        b2.title = "Book2";
        b2.author = "author2";
        b2.num_copies = 2;
        Assert.assertEquals(b2.id(), 0);
        b2.save();
        Assert.assertNotEquals(b1.id(), b2.id());

    }

    @Test(expected = Exception.class)
    public void test_save() {
        Book b1 = new Book();
        b1.title = "Book1";
        b1.author = "author1";
        b1.num_copies = 1;
        Assert.assertEquals(b1.id(), 0);
        Assert.assertEquals(b1.id(), 0);
        b1.save();
        Assert.assertNotEquals(b1.id(), 0);
        b1.destroy();
        b1.save();
    }

    @Test
    public void test_find() {
        Book b1 = new Book();
        b1.title = "Book1";
        b1.author = "author1";
        b1.num_copies = 1;
        b1.save();
        Book b = Model.find(b1.getClass(), b1.id());
        assertEquals(b.author, "author1");
        assertEquals(b.title, "Book1");
        assertEquals(b.num_copies, 1);
    }

    @Test
    public void test_all() {
        Book b1 = new Book();
        b1.title = "Book1";
        b1.author = "author1";
        b1.num_copies = 1;
        b1.save();
        Book b2 = new Book();
        b2.title = "Book2";
        b2.author = "author2";
        b2.num_copies = 2;
        b2.save();
        List<Book> books = Model.all(Book.class);
        Assert.assertEquals(books.size(), 2);

        Assert.assertEquals(books.get(0).author, "author1");
        Assert.assertEquals(books.get(0).title, "Book1");
        Assert.assertEquals(books.get(0).num_copies, 1);

        Assert.assertEquals(books.get(1).author, "author2");
        Assert.assertEquals(books.get(1).title, "Book2");
        Assert.assertEquals(books.get(1).num_copies, 2);
    }
    @Test
    public void test_reset() {
        Model.reset();
        List<Book> books = Model.all(Book.class);
        Assert.assertEquals(books.size(), 0);

    }

    @After
    public void tearDown() throws Exception {
    }
}