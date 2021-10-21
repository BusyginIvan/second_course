package servlets;

import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.*;

public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String header = request.getHeader("X-Requested-With");
        if (header != null && header.equals("XMLHttpRequest"))
            getServletContext().getNamedDispatcher("AreaChecker").forward(request, response);
        else
            getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "ControllerServlet - определяет тип запроса и делегирует его обработку jsp-странице или AreaCheckServlet-у.";
    }
}