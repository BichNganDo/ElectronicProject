package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.category_product.CategoryProduct;
import entity.category_product.ListCategoryProduct;
import entity.contact.ContactClient;
import entity.contact.ListContact;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryModel;
import model.ContactModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIContact extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getcontact": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));
                int offset = (pageIndex - 1) * limit;
                List<ContactClient> listSliceContact = ContactModel.INSTANCE.getSliceContact(offset, limit, searchQuery, searchStatus);
                int totalContact = ContactModel.INSTANCE.getTotalContact(searchQuery, searchStatus);

                ListContact listContact = new ListContact();
                listContact.setTotal(totalContact);
                listContact.setListContact(listSliceContact);
                listContact.setItemPerPage(10);

                if (listSliceContact.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listContact);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getcontactbyid": {
                int idContact = NumberUtils.toInt(request.getParameter("id_contact"));

                ContactClient contactById = ContactModel.INSTANCE.getContactByID(idContact);

                if (contactById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(contactById);
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
                String message = request.getParameter("message");
                int status = NumberUtils.toInt(request.getParameter("status"));

                ContactClient contactById = ContactModel.INSTANCE.getContactByID(id);
                if (contactById.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editContact = ContactModel.INSTANCE.editContact(id, name, email, phone, message, status);
                if (editContact >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa Contact thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa Contact thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteContact = ContactModel.INSTANCE.deleteContact(id);
                if (deleteContact >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa Contact thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa Contact thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}
