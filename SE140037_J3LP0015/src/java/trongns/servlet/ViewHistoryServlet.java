/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.feedback.FeedbackDAO;
import trongns.feedback.FeedbackDTO;
import trongns.order.OrderDAO;
import trongns.order.OrderDTO;
import trongns.orderdetail.OrderDetailDAO;
import trongns.orderdetail.OrderDetailDTO;

/**
 *
 * @author TrongNS
 */
public class ViewHistoryServlet extends HttpServlet {

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
        boolean valid = true;
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String user = (String) session.getAttribute("USER");

                if (user != null) {
                    url = "history-page";

                    int initPageSize = 5;
                    int initPageNo = 1;

                    OrderDAO orderDAO = new OrderDAO();
                    orderDAO.searchOrdersWithConditions(user, null, null, initPageSize, initPageNo);
                    ArrayList<OrderDTO> listHistoryOrders = orderDAO.getListSearchedOrders();

                    if (listHistoryOrders != null) {
                        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                        FeedbackDAO feedbackDAO = new FeedbackDAO();

                        for (OrderDTO order : listHistoryOrders) {
                            orderDetailDAO.loadListOrderDetailByOrderId(order.getOrderId());
                            ArrayList<OrderDetailDTO> listHistoryOrderDetails = orderDetailDAO.getListHistoryOrderDetals();
                            if (listHistoryOrderDetails != null) {
                                for (OrderDetailDTO detail : listHistoryOrderDetails) {
                                    feedbackDAO.loadFeedbackByOrderDetail(detail.getOrderId(), detail.getCarModelId());
                                    FeedbackDTO feedback = feedbackDAO.getOrderDetailFeedback();

                                    if (feedback != null) {
                                        detail.setRating(feedback.getRating());
                                        detail.setFeedback(feedback.getFeedback());
                                    }
                                }
                            } else {
                                url = "home";
                                valid = false;
                                break;
                            }
                            order.setRentingItems(listHistoryOrderDetails);

                            boolean result = orderDetailDAO.checkBeforePickUpDate(order.getOrderId());
                            if (result) {
                                order.setActivate(true);
                                orderDAO.updateActivateStatusOfOrder(order.getOrderId(), true);
                            } else {
                                order.setActivate(false);
                                orderDAO.updateActivateStatusOfOrder(order.getOrderId(), false);
                            }
                        }

                        orderDAO.countAllOrdersByUserId(user);
                        int totalOrders = orderDAO.getTotalOrders();

                        request.setAttribute("ORDER_LIST", listHistoryOrders);

                        if (((initPageSize * initPageNo) >= totalOrders) || listHistoryOrders.size() < initPageSize) {
                            request.setAttribute("LAST_LIST", "TRUE");
                        }
                    }

                    request.setAttribute("PAGE_NO", initPageNo);
                    request.setAttribute("PAGE_SIZE", initPageSize);
                }
            }
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at ViewHistoryServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at ViewHistoryServlet _ SQLException : " + msg);
        } finally {
            if (valid) {
                HashMap<String, String> mappingList = (HashMap<String, String>) request.getServletContext().getAttribute("MAPPING_LIST");
                if (mappingList != null) {
                    url = mappingList.get(url);
                }
                request.getRequestDispatcher(url).forward(request, response);
            } else {
                response.sendRedirect(url);
            }
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
