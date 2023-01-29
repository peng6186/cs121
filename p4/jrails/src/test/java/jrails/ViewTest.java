package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static jrails.View.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class ViewTest {

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }

    @Test
    public void test_br() {

        assertEquals(br().toString(), "<br/>");
    }

    @Test
    public void test_t() { // g
        assertEquals(t("Hello").toString(), "Hello");
    }

    @Test
    public void test_p() { // g

        assertEquals(p(t("Hello")).toString(), "<p>Hello</p>");
    }

    @Test
    public void test_div() {

        assertEquals(div(t("Hello")).toString(), "<div>Hello</div>");
    }

    @Test
    public void test_strong() {

        assertEquals(strong(t("Hello")).toString(), "<strong>Hello</strong>");
    }

    @Test
    public void test_h1() { // g

        assertEquals(h1(t("Hello")).toString(), "<h1>Hello</h1>");
    }

    @Test
    public void test_tr() {

        assertEquals(tr(t("Hello")).toString(), "<tr>Hello</tr>");
    }

    @Test
    public void test_th() {

        assertEquals(th(t("Hello")).toString(), "<th>Hello</th>");
    }

    @Test
    public void test_td() {


        assertEquals(td(t("Hello")).toString(), "<td>Hello</td>");
    }

    @Test
    public void test_table() {

        assertEquals(table(t("Hello")).toString(), "<table>Hello</table>");
    }

    @Test
    public void test_thead() {


        assertEquals(thead(t("Hello")).toString(), "<thead>Hello</thead>");
    }

    @Test
    public void test_tbody() { // g


        assertEquals(tbody(t("Hello")).toString(), "<tbody>Hello</tbody>");
    }

    @Test
    public void test_textarea() { // g

        assertEquals(textarea("thistextarea", t("Hello")).toString(), "<textarea name=\"thistextarea\">Hello</textarea>");
    }

    @Test
    public void test_linkto() { // g

        assertEquals(link_to("gohere", "/weirdplace").toString(), "<a href=\"/weirdplace\">gohere</a>");
    }

    @Test
    public void test_form() { // g


        assertEquals(form("goThere.php", t("Hello")).toString(), "<form action=\"goThere.php\" accept-charset=\"UTF-8\" method=\"post\">Hello</form>");
    }

    @Test
    public void test_submit() { // g

        assertEquals(submit("Save").toString(), "<input type=\"submit\" value=\"Save\"/>");
    }



}