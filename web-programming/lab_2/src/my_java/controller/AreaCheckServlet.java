package my_java.controller;

import my_java.model.Results;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import my_java.model.Point;

import java.io.IOException;
import java.util.Arrays;

public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Boolean isForward = (Boolean) request.getAttribute("forward");
        if (isForward != null && isForward)
            synchronized (getServletContext()) {
                try {
                    double r = Double.parseDouble(request.getParameter("r"));
                    if (r > 0) {
                        Results results = (Results) request.getSession().getAttribute("results");
                        if (results == null) results = new Results();
                        results.addPoint(new Point(
                                Double.parseDouble(request.getParameter("x")),
                                Double.parseDouble(request.getParameter("y")),
                                r));
                        request.getSession().setAttribute("results", results);
                    }
                } catch (NumberFormatException ignore) { }
            }
        getServletContext().getRequestDispatcher("/results.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "AreaCheckServlet - осуществляет проверку попадания точки в область на координатной плоскости, " +
                "хранит результаты предыдущих проверок и формирует таблицу результатов.";
    }
}