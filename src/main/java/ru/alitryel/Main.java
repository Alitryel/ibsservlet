package org.example;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/calculate")
public class Main extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        StringBuilder jsonInput = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            jsonInput.append(line);
        }

        Gson gson = new Gson();
        CalculationRequest calcRequest = gson.fromJson(jsonInput.toString(), CalculationRequest.class);

        double result = performOperation(calcRequest.a, calcRequest.b, calcRequest.math);

        out.println(gson.toJson(new CalculationResult(result)));
    }

    private double performOperation(double a, double b, String math) {
        switch (math) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) throw new IllegalArgumentException("Делить на ноль нельзя!");
                return a / b;
            default:
                throw new IllegalArgumentException("Неподдерживаемая операция!");
        }
    }

    static class CalculationRequest {
        double a;
        double b;
        String math;
    }

    static class CalculationResult {
        double result;

        CalculationResult(double result) {
            this.result = result;
        }
    }
}
