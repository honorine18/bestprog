package com.mulk.usermanagmentsystem.Web;

import com.mulk.usermanagmentsystem.Dao.UserDao;
import com.mulk.usermanagmentsystem.Model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "UserServlet", value = "/")
public class UserServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(UserServlet.class);
    private UserDao dao;

    public UserServlet() {
        this.dao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        logger.debug("Received request for action: {}", action);

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error handling action {}: {}", action, e.getMessage());
            e.printStackTrace();
        }
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User theUser = new User(fullName, email, country);
        dao.registerUSer(theUser);
        logger.info("Inserted new user: {}", theUser);
        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));

        User theUser = new User(userId);
        dao.deleteUser(theUser);
        logger.info("Deleted user with ID: {}", userId);
        response.sendRedirect("list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User theUser = new User(userId);
        User existingUser = dao.findUserById(theUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
        request.setAttribute("user", existingUser);
        logger.debug("Showing edit form for user: {}", existingUser);
        dispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User theUser = new User(userId, fullName, email, country);
        dao.updateUser(theUser);
        logger.info("Updated user: {}", theUser);
        response.sendRedirect("list");
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = dao.retrieveAllUser();
        request.setAttribute("listuser", userList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("user_list.jsp");
        logger.debug("Listing users: {}", userList.size());
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user_form.jsp");
        logger.debug("Showing new user form");
        dispatcher.forward(request, response);
    }
}
