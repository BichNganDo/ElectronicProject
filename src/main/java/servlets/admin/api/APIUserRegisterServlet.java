package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.email_register.Email;
import entity.email_register.ListEmail;
import entity.user_register.ListUserRegister;
import entity.user_register.UserRegister;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EmailRegisterModel;
import model.RegisterModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIUserRegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getuserreister": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int offset = (pageIndex - 1) * limit;
                List<UserRegister> listSliceUserRegister = RegisterModel.INSTANCE.getSliceRegister(offset, limit, searchQuery);
                int totalUserRegister = RegisterModel.INSTANCE.getTotalUserRegister(searchQuery);

                ListUserRegister listUserRegister = new ListUserRegister();
                listUserRegister.setTotal(totalUserRegister);
                listUserRegister.setListUserRegister(listSliceUserRegister);
                listUserRegister.setItemPerPage(10);

                if (listSliceUserRegister.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listUserRegister);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getuserregisterbyid": {
                int idUserRegister = NumberUtils.toInt(request.getParameter("id_user_register"));
                UserRegister userRegisterById = RegisterModel.INSTANCE.getUserRegisterByID(idUserRegister);

                if (userRegisterById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(userRegisterById);
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
                UserRegister userRegisterById = RegisterModel.INSTANCE.getUserRegisterByID(id);
                if (userRegisterById.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editUserRegister = RegisterModel.INSTANCE.editUserRegister(id, name, email);
                if (editUserRegister >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
