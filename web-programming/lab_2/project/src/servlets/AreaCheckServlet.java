package servlets;

import data.Point;
import data.Results;

import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import java.io.IOException;

public class AreaCheckServlet extends HttpServlet {
    private final Results results = new Results();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            results.addPoint(new Point(
                    Double.parseDouble(request.getParameter("x")),
                    Double.parseDouble(request.getParameter("y")),
                    Double.parseDouble(request.getParameter("r"))
            ));
        } catch (NumberFormatException|NullPointerException ignore) { }

        if (request.getHeader("Accept-Charset") == null)
            response.setCharacterEncoding("UTF-8");
        else
            response.setCharacterEncoding(request.getHeader("Accept-Charset"));

        if (results.isEmpty())
            response.getWriter().write(
                    "<h4><span class=\"notification\">Результаты отсутствуют</span></h4>");
        else
            response.getWriter().write(results.toHTML());
    }

    @Override
    public String getServletInfo() {
        return "AreaCheckServlet - осуществляет проверку попадания точки в область на координатной плоскости, " +
                "хранит результаты предыдущих проверок и формирует таблицу результатов.";
    }
}