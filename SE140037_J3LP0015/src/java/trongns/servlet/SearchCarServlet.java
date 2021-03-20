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
import trongns.type.TypeDAO;
import trongns.type.TypeDTO;

/**
 *
 * @author TrongNS
 */
public class SearchCarServlet extends HttpServlet {

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

            url = "search-page";

            int pageNo = 1;
            int pageSize = 20;
            String keyword = request.getParameter("txtCarName");
            String carType = request.getParameter("txtCarType");
            String srentalDate = request.getParameter("txtRentalDate");
            String srentalTime = request.getParameter("txtRentalTime");
            String sreturnDate = request.getParameter("txtReturnDate");
            String sreturnTime = request.getParameter("txtReturnTime");
            String samount = request.getParameter("txtAmountOfCar");
            String spageSize = request.getParameter("pageSize");
            String spageNo = request.getParameter("pageNo");

            if (spageNo == null) {
                pageNo = 1;
            } else {
                if (spageNo.matches("\\d+")) {
                    pageNo = Integer.parseInt(spageNo);
                } else {
                    pageNo = 1;
                }
            }

            HttpSession session = request.getSession(false);
            String role = null;
            if (session != null) {
                role = (String) session.getAttribute("ROLE");
            }

            String action = request.getParameter("action");

            if ("Prev".equals(action)) {
                if (pageNo > 1) {
                    pageNo--;
                }
            } else if ("Next".equals(action)) {
                pageNo++;
            }

            if (!"all".equals(spageSize) && spageSize != null && !spageSize.trim().isEmpty()) {
                if (spageSize.matches("\\d+")) {
                    pageSize = Integer.parseInt(spageSize);
                } else {
                    pageSize = 20;
                }
            } else {
                pageSize = 20;
            }

            CarModelDAO carDAO = new CarModelDAO();
            TypeDAO typeDAO = new TypeDAO();
            FeatureDAO featureDAO = new FeatureDAO();
            CarFeatureDAO carFeatureDAO = new CarFeatureDAO();

            ArrayList<CarModelDTO> listSearchedCars = null;
            ArrayList<TypeDTO> carTypeList = null;

            Date rentalDate = new SimpleDateFormat("dd/MM/yyyy").parse(srentalDate);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(rentalDate);
            if (srentalTime.matches("\\d+")) {
                calendar1.add(Calendar.MINUTE, Integer.parseInt(srentalTime));
            }

            rentalDate = calendar1.getTime();

            Date returnDate = new SimpleDateFormat("dd/MM/yyyy").parse(sreturnDate);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(returnDate);
            if (sreturnTime.matches("\\d+")) {
                calendar2.add(Calendar.MINUTE, Integer.parseInt(sreturnTime));
            }

            returnDate = calendar2.getTime();

            long diffInMillies = returnDate.getTime() - rentalDate.getTime();

            if (diffInMillies > 0) {
                carDAO.searchCarModelWithConditions(keyword, carType, Integer.parseInt(samount), rentalDate, returnDate, role, pageSize, pageNo);
                listSearchedCars = carDAO.getListSearchedCarModel();

                if (listSearchedCars != null) {

                    for (CarModelDTO car : listSearchedCars) {
                        carFeatureDAO.getCarFeatureListByCarModelId(car.getCarModelId());
                        ArrayList<Integer> carFeatureList = carFeatureDAO.getCarFeatureList();

                        ArrayList<FeatureDTO> featureList = new ArrayList<>();

                        for (Integer featureId : carFeatureList) {
                            featureDAO.getFeatureById(featureId);
                            featureList.add(featureDAO.getFeature());
                        }

                        car.setFeatureList(featureList);
                    }

                    request.setAttribute("CAR_ITEMS", listSearchedCars);

                    carDAO.countAllCarModel(role);
                    int totalCars = carDAO.getTotalCarModel();

                    carDAO.countCarModelWithConditions(keyword, carType, Integer.parseInt(samount), rentalDate, returnDate, role);
                    int totalSearchedCars = carDAO.getTotalSearchedCarModel();

                    if (((pageSize * pageNo) >= totalCars) || listSearchedCars.size() < pageSize
                            || totalSearchedCars == pageSize || (pageSize * pageNo) >= totalSearchedCars) {
                        request.setAttribute("LAST_LIST", "TRUE");
                    }
                }
            }

            typeDAO.loadTypeList();
            carTypeList = typeDAO.getTypeList();
            request.setAttribute("CAR_TYPE_LIST", carTypeList);
            request.setAttribute("PAGE_NO", pageNo);
            request.setAttribute("PAGE_SIZE", pageSize);
        } catch (ParseException ex) {
            String msg = ex.getMessage();
            log("Error at SearchCarServlet _ ParseException : " + msg);
        } catch (NamingException ex) {
            String msg = ex.getMessage();
            log("Error at SearchCarServlet _ NamingException : " + msg);
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            log("Error at SearchCarServlet _ SQLException : " + msg);
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
