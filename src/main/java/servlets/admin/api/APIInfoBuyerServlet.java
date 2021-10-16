package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.email_register.Email;
import entity.email_register.ListEmail;
import entity.info_buyer.InfoBuyer;
import entity.info_buyer.ListInfoBuyer;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EmailRegisterModel;
import model.InfoBuyerModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIInfoBuyerServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getinfobuyer": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int offset = (pageIndex - 1) * limit;
                List<InfoBuyer> listSliceInfoBuyer = InfoBuyerModel.INSTANCE.getSliceInfoBuyer(offset, limit, searchQuery);
                int totalInfoBuyer = InfoBuyerModel.INSTANCE.getTotalInfoBuyer(searchQuery);

                ListInfoBuyer listInfoBuyer = new ListInfoBuyer();
                listInfoBuyer.setTotal(totalInfoBuyer);
                listInfoBuyer.setListInfoBuyer(listSliceInfoBuyer);
                listInfoBuyer.setItemPerPage(10);

                if (listSliceInfoBuyer.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listInfoBuyer);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getinfobuyerbyid": {
                int idInfoBuyer = NumberUtils.toInt(request.getParameter("id_info_buyer"));
                InfoBuyer infoBuyerById = InfoBuyerModel.INSTANCE.getInfoBuyerByID(idInfoBuyer);

                if (infoBuyerById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(infoBuyerById);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String note = request.getParameter("note");
                String listCart = request.getParameter("listCart");
                InfoBuyer infoBuyerById = InfoBuyerModel.INSTANCE.getInfoBuyerByID(id);
                if (infoBuyerById.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editInfoBuyer = InfoBuyerModel.INSTANCE.editInfoBuyer(id, name, email, phone, address, note, listCart);
                if (editInfoBuyer >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa thông tin thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa thông tin thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteInfoBuyer = InfoBuyerModel.INSTANCE.deleteInfoBuyer(id);
                if (deleteInfoBuyer >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa thông tin thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa thông tin thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
