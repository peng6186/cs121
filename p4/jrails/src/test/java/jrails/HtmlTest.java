package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class HtmlTest {

    private Html html;

    @Before
    public void setUp() throws Exception {
        html = new Html();
    }

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }

    @Test
    public void test_toString() {
        assertThat(html.toString(), isEmptyString());
    }

    @Test
    public void test_seq() {
        Html h1 = new Html("<p>p1</p>");
        Html h2 = new Html("<p>p2</p>");
        Html h3 = h1.seq(h2);
        assertEquals(h3.toString(), "<p>p1</p><p>p2</p>");
    }

    @Test
    public void test_br() {
        Html h1 = html.br();
        assertEquals(h1.toString(), "<br/>");
        Html h2 = new Html("<p>p2</p>");
        Html h3 = h1.seq(h2);
        assertEquals("<br/><p>p2</p>", h3.toString());

    }

    @Test
    public void test_t() { // g
        Html h1 = html.t("Hello");
        assertEquals(h1.toString(), "Hello");

    }

    @Test
    public void test_p() { // g
        Html h1 = html.t("Hello");
        Html h2 = h1.p(h1);
        assertEquals("Hello<p>Hello</p>", h2.toString());
    }

    @Test
    public void test_div() {
        Html h1 = html.t("Hello");
        Html h2 = h1.div(h1);
        assertEquals(h2.toString(), "Hello<div>Hello</div>");
    }

    @Test
    public void test_strong() {
        Html h1 = html.t("Hello");
        Html h2 = h1.strong(h1);
        assertEquals(h2.toString(), "Hello<strong>Hello</strong>");
    }

    @Test
    public void test_h1() { // g
        Html h1 = html.t("Hello");
        Html h2 = h1.h1(h1);
        assertEquals(h2.toString(), "Hello<h1>Hello</h1>");
    }

    @Test
    public void test_tr() {
        Html h1 = html.t("Hello");
        Html h2 = h1.tr(h1);
        assertEquals(h2.toString(), "Hello<tr>Hello</tr>");
    }

    @Test
    public void test_th() {
        Html h1 = html.t("Hello");
        Html h2 = h1.th(h1);
        assertEquals(h2.toString(), "Hello<th>Hello</th>");
    }

    @Test
    public void test_td() {
        Html h1 = html.t("Hello");
        Html h2 = h1.td(h1);
        assertEquals(h2.toString(), "Hello<td>Hello</td>");
    }

    @Test
    public void test_table() {
        Html h1 = html.t("Hello");
        Html h2 = h1.table(h1);
        assertEquals(h2.toString(), "Hello<table>Hello</table>");
    }

    @Test
    public void test_thead() {
        Html h1 = html.t("Hello");
        Html h2 = h1.thead(h1);
        assertEquals(h2.toString(), "Hello<thead>Hello</thead>");
    }

    @Test
    public void test_tbody() { // g
        Html h1 = html.t("Hello");
        Html h2 = h1.tbody(h1);
        assertEquals(h2.toString(), "Hello<tbody>Hello</tbody>");
    }

    @Test
    public void test_textarea() { // g
        Html h1 = html.t("Hello");
        Html h2 = h1.textarea("thistextarea",h1);
        assertEquals(h2.toString(), "Hello<textarea name=\"thistextarea\">Hello</textarea>");
    }

    @Test
    public void test_linkto() { // g
        Html h1 = html.t("Hello");
        Html h2 = h1.link_to("gohere", "/weirdplace");
        assertEquals("Hello<a href=\"/weirdplace\">gohere</a>", h2.toString());
    }

    @Test
    public void test_form() { // g
        Html h1 = html.t("Hello");
        Html h2 = h1.form("goThere.php", h1);
        assertEquals(h2.toString(), "Hello<form action=\"goThere.php\" accept-charset=\"UTF-8\" method=\"post\">Hello</form>");
    }

    @Test
    public void test_submit() { // g
        Html h1 = html.submit("Save");

        assertEquals(h1.toString(), "<input type=\"submit\" value=\"Save\"/>");
    }
}
