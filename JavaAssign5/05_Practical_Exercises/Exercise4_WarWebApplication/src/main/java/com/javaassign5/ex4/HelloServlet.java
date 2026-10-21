package com.javaassign5.ex4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.time.LocalTime;

/**
 * GET /hello?name=Shrey  -> greeting page.
 * Registered with the @WebServlet ANNOTATION (no web.xml entry needed).
 */
@WebServlet(name = "HelloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        if (name == null || name.isBlank()) name = "World";

        // commons-text (packaged in WEB-INF/lib) escapes HTML to block XSS
        String safeName = StringEscapeUtils.escapeHtml4(name);

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(Page.wrap("Hello",
                "<h1>Hello, " + safeName + "!</h1>"
                + "<p>Server time: " + LocalTime.now().withNano(0) + "</p>"
                + "<p><a href='" + req.getContextPath() + "/'>Back</a></p>"));
    }
}
