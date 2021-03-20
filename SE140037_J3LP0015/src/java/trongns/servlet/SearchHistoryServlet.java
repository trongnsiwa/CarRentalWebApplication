/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class SearchHistoryServlet extends HttpServlet {

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
            String searchedHistory = request.getParameter("txtSearchHistory");
            String srentalDate = request.getParameter("txtRentalDate");
            String spageNo = request.getParameter("pageNo");
            String spageSize = request.getParameter("pageSize");
            String cancelStatus = request.getParameter("cancel-status");
            String feedbackStatus = request.getParameter("feedback-status");

            HttpSession session = request.getSession(false);
            if (session != null) {
                url = "history";
                String user = (String) session.getAttribute("USER");

                if ("success".equals(cancelStatus)) {
                    request.setAttribute("CANCEL_ORDER_MESSAGE", "Cancel order successfully");
                } else if ("fail".equals(cancelStatus)) {
                    request.setAttribute("CANCEL_ORDER_MESSAGE", "Cancel order fail");
                }

                if ("success".equals(feedbackStatus)) {
                    request.setAttribute("FEEDBACK_MESSAGE", "Thank you for giving your feedback!");
                } else if ("fail".equals(feedbackStatus)) {
                    request.setAttribute("FEEDBACK_MESSAGE", "Send feedback fail");
                }

                int pageNo = 0;
                int pageSize = 0;

                if (spageNo == null || spageNo.trim().isEmpty()) {
                    pageNo = 1;
                } else {
                    if (spageNo.matches("\\d+")) {
                        pageNo = Integer.parseInt(spageNo);
                    } else {
                        pageNo = 1;
                    }
                }

                if (!"all".equals(spageSize) && spageSize != null && !spageSize.trim().isEmpty() && spageSize.matches("\\d+")) {
                    pageSize = Integer.parseInt(spageSize);
                } else {
                    pageSize = 10;
                }

                String action = request.getParameter("action");

                if ("Prev".equals(action)) {
                    if (pageNo > 1) {
                        pageNo--;
                    }
                } else if ("Next".equals(action)) {
                    pageNo++;
                }

                if ((searchedHistory != null && !searchedHistory.trim().isEmpty())
                        || (srentalDate != null && !srentalDate.trim().isEmpty())
                        || (action != null && !action.trim().isEmpty())
                        || (cancelStatus != null && !cancelStatus.trim().isEmpty()
                        || (feedbackStatus != null && !feedbackStatus.trim().isEmpty()))) {
                    url = "history-page";
                    OrderDAO dao = new OrderDAO();

                    dao.countAllOrdersByUserId(user);
                    int totalOrders = dao.getTotalOrders();

                    if (!"all".equals(spageSize) && spageSize != null && !spageSize.trim().isEmpty()) {
                        pageSize = Integer.parseInt(spageSize);
                    } else {
                        pageSize = totalOrders;
                    }

                    Date rentalDate = null;
                    if (srentalDate != null && !srentalDate.trim().isEmpty()) {
                        rentalDate = new SimpleDateFormat("dd/MM/yyyy").parse(srentalDate);
                    }

                    dao.searchOrdersWithConditions(user, searchedHistory, rentalDate, pageSize, pageNo);
                    ArrayList<OrderDTO> orderList = dao.getListSearchedOrders();

                    if (orderList != null) {
                        OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                        FeedbackDAO feedbackDAO = new FeedbackDAO();

                        for (OrderDTO dto : orderList) {
                            orderDetailDAO.loadListOrderDetailByOrderId(dto.getOrderId());
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
                            dto.setRentingItems(listHistoryOrderDetails);

                            boolean result = orderDetailDAO.checkBeforePickUpDate(dto.getOrderId());
                            if (result) {
                                dto.setActivate(true);
                                dao.updateActivateStatusOfOrder(dto.getOrderId(), true);
                            } else {
                                dto.setActivate(false);
                                dao.updateActivateStatusOfOrder(dto.getOrderId(), false);
                            }
                        }

                        dao.countOrdersWithConditions(user, searchedHistory, rentalDate);
                        int totalSearchedOrders = dao.getTotalSearchedOrders();

                        request.setAttribute("ORDER_LIST", orderList);
                        if (((pageSize * pageNo) >= totalOrders) || orderList.size() < pageSize || totalSearchedOrders == pageSize || ((pageSize * pageNo) >= totalSearchedOrders)) {
                            request.setAttribute("LAST_LIST", "last");
                        }
                    }
                }

                request.setAttribute("PAGE_NO", pageNo);
                request.setAttribute("PAGE_SIZE", pageSize);
            }
        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at SearchHistoryServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at SearchHistoryServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at SearchHistoryServlet _ SQLException : " + msg);
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
