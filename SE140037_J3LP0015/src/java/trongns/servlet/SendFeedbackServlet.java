/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.feedback.FeedbackDAO;
import trongns.user.UserDAO;

/**
 *
 * @author TrongNS
 */
public class SendFeedbackServlet extends HttpServlet {

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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String url = "home";

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String user = (String) session.getAttribute("USER");

                if (user != null) {
                    url = "history";

                    if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                        return;
                    }
                    
                    String orderId = request.getParameter("orderId");
                    String scarModelId = request.getParameter("carModelId");
                    String feedback = request.getParameter("feedback_" + orderId + "_" + scarModelId);
                    String srating = request.getParameter("rating_" + orderId + "_" + scarModelId);
                    String searchHistory = request.getParameter("txtSearchHistory");
                    String srentalDate = request.getParameter("txtRentalDate");
                    String spageNo = request.getParameter("pageNo");
                    String spageSize = request.getParameter("pageSize");

                    if (orderId != null && !orderId.trim().isEmpty()
                            && scarModelId != null && !scarModelId.trim().isEmpty()
                            && srating != null && !srating.trim().isEmpty()) {
                        if (scarModelId.matches("\\d+") && srating.matches("\\d+")) {
                            url = "search-history?"
                                    + "txtSearchHistory=" + searchHistory
                                    + "&txtRentalDate=" + srentalDate;
                            if (spageNo != null && !spageNo.trim().isEmpty()) {
                                url += "&pageNo=" + spageNo;
                            }
                            if (spageSize != null && !spageSize.trim().isEmpty()) {
                                url += "&pageSize=" + spageSize;
                            }
                            
                            UserDAO userDAO = new UserDAO();
                            userDAO.getUserFullname(user);
                            String nameOfUser = userDAO.getNameOfUser();

                            FeedbackDAO dao = new FeedbackDAO();
                            boolean existed = dao.checkExistedFeedback(orderId, Integer.parseInt(scarModelId));
                            boolean result;
                            if (existed) {
                                result = dao.updateFeedback(orderId, Integer.parseInt(scarModelId), feedback, Integer.parseInt(srating));
                            } else {
                                result = dao.insertFeedback(orderId, Integer.parseInt(scarModelId), nameOfUser, feedback, Integer.parseInt(srating));
                            }
                            

                            if (result) {
                                url += "&feedback-status=success";
                            } else {
                                url += "&feedback-status=fail";
                            }

                        }
                    }
                }
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at SendFeedbackServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at SendFeedbackServlet _ SQLException : " + msg);
        } finally {
            response.sendRedirect(url);
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
