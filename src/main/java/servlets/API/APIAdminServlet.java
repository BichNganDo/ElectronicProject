package servlets.API;

import com.google.gson.Gson;
import common.APIResult;
import entity.admin.Admin;
import entity.admin.ListAdmin;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AdminModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIAdminServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getadmin": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));

                int offset = (pageIndex - 1) * limit;
                List<Admin> sliceAdmin = AdminModel.INSTANCE.getSliceAdmin(offset, limit, searchQuery, searchStatus);
                int totalAdmin = AdminModel.INSTANCE.getTotalAdmin(searchQuery, searchStatus);

                ListAdmin listAdmin = new ListAdmin();
                listAdmin.setTotal(totalAdmin);
                listAdmin.setListAdmin(sliceAdmin);
                listAdmin.setItemPerPage(10);

                if (sliceAdmin.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listAdmin);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getAdminById": {
                int idAdmin = NumberUtils.toInt(request.getParameter("id_admin"));

                Admin adminByID = AdminModel.INSTANCE.getAdminByID(idAdmin);

                if (adminByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(adminByID);
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
            case "add": {
                String name = request.getParameter("name");
                int role = NumberUtils.toInt(request.getParameter("role"));
                String username = request.getParameter("username");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                int status = NumberUtils.toInt(request.getParameter("status"));

                int addAdmin = AdminModel.INSTANCE.addAdmin(name, role, username, phone, password, status);

                if (addAdmin >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm admin thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm admin thất bại!");
                }
                break;
            }
            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                int role = NumberUtils.toInt(request.getParameter("role"));
                String username = request.getParameter("username");
                String phone = request.getParameter("phone");
                String password = request.getParameter("password");
                int status = NumberUtils.toInt(request.getParameter("status"));

                Admin adminByID = AdminModel.INSTANCE.getAdminByID(id);
                if (adminByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editAdmin = AdminModel.INSTANCE.editAdmin(id, name, role, username, phone, password, status);

                if (editAdmin >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa admin thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa admin thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteAdmin = AdminModel.INSTANCE.deleteAdmin(id);
                if (deleteAdmin >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa admin thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa admin thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }

}
