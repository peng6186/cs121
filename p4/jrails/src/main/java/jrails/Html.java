package jrails;

public class Html {

    private String html;

    Html() { html = ""; }
    Html(String str) { html = str; }

    public String toString() { // okay good
        return html;
    }

    public Html seq(Html h) { // okay good
        return  new Html( this.html + h.toString() );
    }

    public Html br() { // okay g
        return new Html(this.html + "<br/>");
    }

    public Html t(Object o) { // okay g
        // Use o.toString() to get the text for this
        return new Html(this.html +  o.toString() );
    }

    public Html p(Html child) { // okay g

        return new Html(this.html + "<p>" + child.toString() + "</p>");
    }

    public Html div(Html child) { // okay g

        return new Html(this.html + "<div>" + child.toString() + "</div>");
    }

    public Html strong(Html child) { // okay g
        return new Html(this.html + "<strong>" + child.toString() + "</strong>");

    }

    public Html h1(Html child) { // g
        return new Html(this.html + "<h1>" + child.toString() + "</h1>");
    }

    public Html tr(Html child) {
        return new Html(this.html + "<tr>" + child.toString() + "</tr>");
    }

    public Html th(Html child) {

        return new Html(this.html + "<th>" + child.toString() + "</th>");
    }

    public Html td(Html child) { //g

        return new Html(this.html + "<td>" + child.toString() + "</td>");
    }

    public Html table(Html child) {
        return new Html(this.html + "<table>" + child.toString() + "</table>");

    }

    public Html thead(Html child) {
        return new Html(this.html + "<thead>" + child.toString() + "</thead>");

    }

    public Html tbody(Html child) { // g
        return new Html(this.html + "<tbody>" + child.toString() + "</tbody>");


    }

    public Html textarea(String name, Html child) { // okay

        return new Html(this.html + "<textarea name=" + "\"" +name+"\""+">" + child.toString() + "</textarea>");
    }

    public Html link_to(String text, String url) { // okay
        return new Html(this.html + "<a href=\"" + url + "\">" + text + "</a>");
    }

    public Html form(String action, Html child) { // okay
        return new Html(this.html + "<form action=\"" + action + "\" accept-charset=\"UTF-8\" method=\"post\">" + child.toString() + "</form>");
    }

    public Html submit(String value) { // okay
        return new Html(this.html + "<input type=\"submit\" value=\"" + value + "\"/>" );
    }
}