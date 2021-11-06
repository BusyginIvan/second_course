package my_java.controller;

import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.*;

public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ajaxHeader = request.getHeader("X-Requested-With");
        if (ajaxHeader != null && ajaxHeader.equals("XMLHttpRequest")) {
            if (request.getParameter("x") != null &&
                    request.getParameter("y") != null &&
                    request.getParameter("r") != null)
                getServletContext().getNamedDispatcher("AreaChecker").forward(request, response);
            else
                getServletContext().getRequestDispatcher("/results.jsp").forward(request, response);
        } else
            getServletContext().getRequestDispatcher("/main.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "ControllerServlet - определяет тип запроса и делегирует его обработку jsp-странице или AreaCheckServlet-у.";
    }
}