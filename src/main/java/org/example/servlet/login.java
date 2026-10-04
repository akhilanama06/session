package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class login extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sessiondb";

    private static final String USER =
            "root";

    private static final String PASSWORD =
            "akhila";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        String password = request.getParameter("password");

        String sql =
                "SELECT * FROM users WHERE username = ? AND password = ?";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);

            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession();

                session.setAttribute("username", username);

                response.sendRedirect("home");

            } else {

                response.setContentType("text/html");

                response.getWriter().println(
                        "<h2>Invalid Username or Password</h2>"
                );

                response.getWriter().println(
                        "<a href='login.html'>Try Again</a>"
                );
            }

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");

            response.getWriter().println(
                    "<h2>Database Error: "
                            + e.getMessage()
                            + "</h2>"
            );
        }
    }
}