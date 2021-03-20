/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import trongns.user.CreateAnAccountError;
import trongns.user.UserDAO;
import trongns.util.MyUtility;

/**
 *
 * @author TrongNS
 */
public class CreateNewAccountServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String url = "register-page";

        try {
            if (request.getParameterMap().isEmpty() || request.getParameterMap() == null) {
                return;
            }

            url = "register--page";

            String email = request.getParameter("txtEmail");
            String password = request.getParameter("txtPassword");
            String confirm = request.getParameter("txtConfirm");
            String firstname = request.getParameter("txtFirstName");
            String lastname = request.getParameter("txtLastname");
            String phone = request.getParameter("txtPhone");
            String address = request.getParameter("txtAddress");
            boolean valid = true;

            CreateAnAccountError error = new CreateAnAccountError();

            if (!email.matches("^[\\w!#$%&'*+=?`{|}~^-]+(?:\\.[\\w!#$%&'*+=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
                error.setInvalidEmail("Invalid email.");
                valid = false;
            }

            if (password.length() <= 0) {
                error.setEmptyPassword("Required password.");
                valid = false;
            } else {
                if (!confirm.equals(password)) {
                    error.setConfirmNotMatchPassword("Confirm not match password.");
                    valid = false;
                }
            }

            if (firstname.length() <= 0) {
                error.setEmptyFirstName("Required first name.");
                valid = false;
            }

            if (lastname.length() <= 0) {
                error.setEmptyLastName("Required last name.");
                valid = false;
            }

            if (!phone.matches("(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})")) {
                error.setInvalidPhone("Invalid phone number.");
                valid = false;
            }

            if (address.length() <= 0) {
                error.setEmptyAddress("Required address.");
                valid = false;
            }

            boolean result;
            UserDAO dao = new UserDAO();
            if (valid) {
                result = dao.checkExistAccount(email);
                if (result) {
                    error.setDuplicateEmail("Email is already existed.");
                    valid = false;
                }
            }

            if (valid) {
                String hashedPassword = MyUtility.getEncryptedPassword(password);

                String code = dao.generateRandomString();

                result = dao.createNewAccount(email, hashedPassword, firstname, lastname, phone, address, code);

                if (result) {
                    url = "login--page";
                    request.setAttribute("CREATE_ACCOUNT_SUCCESS", "Register successfully. Please login!");
                    dao.sendMail("trongiwa@gmail.com", email, "Car Rental - Welcome to our page", "Welcome " + email + ", ", code);
                } else {
                    request.setAttribute("CREATE_ACCOUNT_FAIL", "Sorry. Account registered fail.");
                }
            } else {
                request.setAttribute("CREATE_ACCOUNT_ERROR", error);
            }
        } catch (NoSuchAlgorithmException ex) {
            String msg = ex.getMessage();
            log("Error at CreateNewAccountServlet _ NoSuchAlgorithmException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at CreateNewAccountServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at CreateNewAccountServlet _ SQLException : " + msg);
        } finally {
            HashMap<String, String> mappingList = (HashMap<String, String>) request.getServletContext().getAttribute("MAPPING_LIST");
            if (mappingList != null) {
                url = mappingList.get(url);
            }
            request.getRequestDispatcher(url).forward(request, response);
            out.close();
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
