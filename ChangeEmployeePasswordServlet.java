/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.CustomerDAO;
import DAOs.EmployeeDAO;
import Models.Customer;
import Models.Employee;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author TuongMPCE180644
 */
@WebServlet(name = "UpdateEmployeePasswordServlet", urlPatterns = {"/ChangeEmployeePassword"})
public class ChangeEmployeePasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        EmployeeDAO emDAO = new EmployeeDAO();

        Employee emLogin = (Employee) session.getAttribute("employee");
        if (emLogin != null) {
            Employee emView = emDAO.getEmployeeById(emLogin.getEmployeeId() + "");
            session.setAttribute("employee", emView);
            request.getRequestDispatcher("ChangeEmployeePasswordView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/EmployeeLogin");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Current Password param: " + request.getParameter("current"));
        System.out.println("New Password param: " + request.getParameter("new"));

        String currentPassword = request.getParameter("current");
        String newPassword = request.getParameter("new");

        System.out.println(currentPassword);
        System.out.println(newPassword);
        HttpSession session = request.getSession();
        Employee em = (Employee) session.getAttribute("employee");
        if (em == null) {
            response.sendRedirect(request.getContextPath() + "/EmployeeLogin");
        } else {
            EmployeeDAO emDAO = new EmployeeDAO();
            if (emDAO.checkPassword(em.getEmployeeId(), currentPassword) == 1) {
                if (currentPassword.equals(newPassword)) {
                    session.setAttribute("empromess", "Current Password is not same with New Password!");
                    request.getRequestDispatcher("ChangeEmployeePasswordView.jsp").forward(request, response);
                    return;
                }
                if (emDAO.changePassword(em.getEmployeeId(), newPassword) == 1) {
                    session.setAttribute("empromess", "Change Password Success");
                    response.sendRedirect(request.getContextPath() + "/ViewEmployeeProfile");
                } else {
                    session.setAttribute("empromess", "Change Password Fail!");
                    request.getRequestDispatcher("ChangeEmployeePasswordView.jsp").forward(request, response);
                }
            } else {
                session.setAttribute("empromess", "Current Password is not correct!");
                request.getRequestDispatcher("ChangeEmployeePasswordView.jsp").forward(request, response);
            }
        }

    }

    /**
     * Returns the servlet description.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "SMARTTICK servlet";
    }// </editor-fold>

}
