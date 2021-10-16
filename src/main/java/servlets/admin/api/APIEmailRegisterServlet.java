package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.email_register.Email;
import entity.email_register.ListEmail;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EmailRegisterModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIEmailRegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getemail": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int offset = (pageIndex - 1) * limit;
                List<Email> listSliceEmail = EmailRegisterModel.INSTANCE.getSliceEmail(offset, limit, searchQuery);
                int totalEmail = EmailRegisterModel.INSTANCE.getTotalEmail(searchQuery);

                ListEmail listEmail = new ListEmail();
                listEmail.setTotal(totalEmail);
                listEmail.setListEmail(listSliceEmail);
                listEmail.setItemPerPage(10);

                if (listSliceEmail.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listEmail);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getemailbyid": {
                int idEmail = NumberUtils.toInt(request.getParameter("id_email"));
                Email emailById = EmailRegisterModel.INSTANCE.getEmailByID(idEmail);

                if (emailById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(emailById);
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
                String email = request.getParameter("email");
                Email emailById = EmailRegisterModel.INSTANCE.getEmailByID(id);
                if (emailById.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editEmail = EmailRegisterModel.INSTANCE.editEmail(id, email);
                if (editEmail >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa Email thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa Email thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteEmail = EmailRegisterModel.INSTANCE.deleteEmail(id);
                if (deleteEmail >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa Email thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa Email thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
