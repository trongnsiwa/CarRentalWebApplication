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
import trongns.order.OrderDAO;
import trongns.orderdetail.OrderDetailDAO;

/**
 *
 * @author TrongNS
 */
public class CancelOrderServlet extends HttpServlet {

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

        String url = "home";

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("USER") == null) {
                    return;
                }
            }

            url = "history";

            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                return;
            }

            String orderId = request.getParameter("id");
            String searchHistory = request.getParameter("txtSearchHistory");
            String srentalDate = request.getParameter("txtRentalDate");
            String spageNo = request.getParameter("pageNo");
            String spageSize = request.getParameter("pageSize");

            if (orderId != null && !orderId.trim().isEmpty()) {
                OrderDAO orderDAO = new OrderDAO();
                OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

                boolean result = orderDAO.updateActivateStatusOfOrder(orderId, false);
                if (result) {
                    result = orderDetailDAO.updateAllRentingStatusOfOrder(orderId, false);
                }

                url = "search-history?"
                        + "txtSearchHistory=" + searchHistory
                        + "&txtRentalDate=" + srentalDate;
                if (spageNo != null && !spageNo.trim().isEmpty()) {
                    url += "&pageNo=" + spageNo;
                }
                if (spageSize != null && !spageSize.trim().isEmpty()) {
                    url += "&pageSize=" + spageSize;
                }
                
                if (result) {
                    url += "&cancel-status=success";
                } else {
                    url += "&cancel-status=fail";
                }
            }

        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at CancelOrderServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at CancelOrderServlet _ SQLException : " + msg);
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
