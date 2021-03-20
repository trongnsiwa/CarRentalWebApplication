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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import trongns.carmodel.CarModelDAO;
import trongns.carmodel.CarModelDTO;
import trongns.carfeature.CarFeatureDAO;
import trongns.feature.FeatureDAO;
import trongns.feature.FeatureDTO;
import trongns.feedback.FeedbackDAO;
import trongns.feedback.FeedbackDTO;
import trongns.orderdetail.OrderDetailDAO;

/**
 *
 * @author TrongNS
 */
public class ViewDetailServlet extends HttpServlet {

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
            if (request.getParameterMap() == null || request.getParameterMap().isEmpty()) {
                return;
            }

            String srentalDate = null, sreturnDate = null;
            String srentalTime = null, sreturnTime = null;

            String goBackPage = request.getParameter("goBackPage");
            if ("search".equals(goBackPage)) {
                url = "search";
                srentalDate = request.getParameter("txtRentalDate");
                sreturnDate = request.getParameter("txtReturnDate");
                srentalTime = request.getParameter("txtRentalTime");
                sreturnTime = request.getParameter("txtReturnTime");

                if ("fail".equals(request.getParameter("add-status"))) {
                    request.setAttribute("ADD_TO_CART_FAIL", "Please choose return date higher than rental date!");
                }
            }

            if ("cart".equals(goBackPage)) {
                srentalDate = request.getParameter("rentalDate");
                sreturnDate = request.getParameter("returnDate");
                srentalTime = request.getParameter("rentalTime");
                sreturnTime = request.getParameter("returnTime");
                String status = request.getParameter("status");
                String changeStatus = request.getParameter("change-status");

                url = "cart";
                request.setAttribute("USER_RENTAL_DATE", srentalDate);
                request.setAttribute("USER_RETURN_DATE", sreturnDate);
                request.setAttribute("USER_RENTAL_TIME", srentalTime);
                request.setAttribute("USER_RETURN_TIME", sreturnTime);

                if ("success".equals(status)) {
                    request.setAttribute("CHANGE_DATE_SUCCESS", "Change date successfully");
                }

                if ("fail".equals(status)) {
                    request.setAttribute("CHANGE_DATE_FAIL", request.getParameter("carName") + " aren't available between " + srentalDate + " and " + sreturnDate);
                }

                if ("fail".equals(changeStatus)) {
                    request.setAttribute("CHANGE_DATE_FAIL", "Please choose return date higher than rental date!");
                }
            }

            String id = request.getParameter("id");

            if (id != null && !id.trim().isEmpty()) {
                if (id.matches("\\d+")) {
                    HttpSession session = request.getSession(false);
                    String role = null;
                    if (session != null) {
                        role = (String) session.getAttribute("ROLE");
                    }

                    CarModelDAO carDAO = new CarModelDAO();
                    FeatureDAO featureDAO = new FeatureDAO();
                    CarFeatureDAO carFeatureDAO = new CarFeatureDAO();
                    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
                    FeedbackDAO feedbackDAO = new FeedbackDAO();

                    carDAO.loadCarModelDetail(Integer.parseInt(id), role);
                    CarModelDTO carDetail = carDAO.getCarModelDetail();

                    if (carDetail == null) {
                        return;
                    } else {
                        carFeatureDAO.getCarFeatureListByCarModelId(carDetail.getCarModelId());
                        ArrayList<Integer> carFeatureList = carFeatureDAO.getCarFeatureList();

                        ArrayList<FeatureDTO> featureList = new ArrayList<>();

                        for (Integer featureId : carFeatureList) {
                            featureDAO.getFeatureById(featureId);
                            featureList.add(featureDAO.getFeature());
                        }

                        Date rentalDate = new SimpleDateFormat("dd/MM/yyyy").parse(srentalDate);

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(rentalDate);
                        if (srentalTime != null && !srentalTime.trim().isEmpty()) {
                            if (srentalTime.matches("\\d+")) {
                                calendar1.add(Calendar.MINUTE, Integer.parseInt(srentalTime));
                            }
                        }

                        rentalDate = calendar1.getTime();

                        Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(sreturnDate);

                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(returnDate);
                        if (sreturnTime != null && !sreturnTime.trim().isEmpty()) {
                            if (sreturnTime.matches("\\d+")) {
                                calendar2.add(Calendar.MINUTE, Integer.parseInt(sreturnTime));
                            }
                        }
                        returnDate = calendar2.getTime();

                        carDetail.setFeatureList(featureList);

                        orderDetailDAO.loadCarModelBetweenDates(rentalDate, returnDate);
                        HashMap<Integer, Integer> carModelBetweenDatesList = orderDetailDAO.getListCarModelBetweenDates();

                        if (carModelBetweenDatesList != null) {
                            if (carModelBetweenDatesList.containsKey(carDetail.getCarModelId())) {
                                int rentingAmount = carModelBetweenDatesList.get(carDetail.getCarModelId());
                                carDetail.setQuantity(carDetail.getQuantity() - rentingAmount);
                            }
                        }

                        feedbackDAO.loadCarModelFeedback(carDetail.getCarModelId());
                        ArrayList<FeedbackDTO> carModelFeedbackList = feedbackDAO.getListCarModelFeedback();

                        int numOfFeedback = 0;
                        int averageRating = 0;
                        int sumRating = 0;

                        if (carModelFeedbackList != null) {
                            for (FeedbackDTO feedbackDTO : carModelFeedbackList) {
                                numOfFeedback += 1;
                                sumRating += feedbackDTO.getRating();

                                Date now = new Date();

                                long diffInMillies = Math.abs(now.getTime() - feedbackDTO.getCreatedDate().getTime());
                                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                                int days = (int) diff;
                                feedbackDTO.setDaysSinceCreatedDate(days);
                            }

                            if (numOfFeedback > 0) {
                                averageRating = Math.round(sumRating / numOfFeedback);
                            }
                        }

                        carDetail.setFeedbackList(carModelFeedbackList);

                        request.setAttribute("ITEM_DETAIL", carDetail);
                        request.setAttribute("AVERAGE_RATING", averageRating);
                        request.setAttribute("COUNT_FEEDBACK", numOfFeedback);
                    }
                }
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("ORDER_INFO") != null) {
                    session.removeAttribute("ORDER_INFO");
                }
            }

        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at ViewDetailServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at ViewDetailServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at ViewDetailServlet _ SQLException : " + msg);
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
