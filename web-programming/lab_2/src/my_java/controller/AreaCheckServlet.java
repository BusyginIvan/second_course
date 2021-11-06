package my_java.controller;

import my_java.model.Results;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import my_java.model.Point;

import java.io.IOException;

public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Results results = (Results) request.getSession().getAttribute("results");
        if (results == null) results = new Results();

        try {
            log("addPoint");
            results.addPoint(new Point(
                    Double.parseDouble(request.getParameter("x")),
                    Double.parseDouble(request.getParameter("y")),
                    Double.parseDouble(request.getParameter("r"))
            ));
            request.getSession().setAttribute("results", results);
        } catch (NumberFormatException ignore) { }

        getServletContext().getRequestDispatcher("/results.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "AreaCheckServlet - осуществляет проверку попадания точки в область на координатной плоскости, " +
                "хранит результаты предыдущих проверок и формирует таблицу результатов.";
    }
}