package com.javaassign5.ex4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

/**
 * GET  /students -> table of students
 * POST /students -> add a student from the HTML form (then redirect back - PRG pattern)
 */
@WebServlet(name = "StudentServlet", urlPatterns = "/students")
public class StudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final transient StudentStore store = StudentStore.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder rows = new StringBuilder();
        for (StudentStore.Student s : store.all()) {
            rows.append("<tr><td>").append(StringEscapeUtils.escapeHtml4(s.getName()))
                .append("</td><td>").append(StringEscapeUtils.escapeHtml4(s.getCourse()))
                .append("</td></tr>");
        }
        String error = req.getParameter("error");
        String errorHtml = (error == null) ? ""
                : "<p class='error'>" + StringEscapeUtils.escapeHtml4(error) + "</p>";

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(Page.wrap("Students",
                "<h1>Students (" + store.all().size() + ")</h1>" + errorHtml
                + "<table><tr><th>Name</th><th>Course</th></tr>" + rows + "</table>"
                + "<h2>Add a student</h2>"
                + "<form method='post' action='" + req.getContextPath() + "/students'>"
                + "<input name='name' placeholder='Name' required> "
                + "<input name='course' placeholder='Course' required> "
                + "<button type='submit'>Add</button></form>"
                + "<p><a href='" + req.getContextPath() + "/'>Back</a></p>"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String error = store.add(req.getParameter("name"), req.getParameter("course"));
        String target = req.getContextPath() + "/students";
        if (error != null) target += "?error=" + java.net.URLEncoder.encode(error, "UTF-8");
        resp.sendRedirect(target);
    }
}
