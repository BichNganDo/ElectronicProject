package servlets.API;

import com.google.gson.Gson;
import common.APIResult;
import entity.CategoryProduct;
import entity.ListCategoryProduct;
import helper.HttpHelper;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryProductModel;

public class APICategoryServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getcate": {
                int pageIndex = Integer.parseInt(request.getParameter("page_index"));
                int limit = Integer.parseInt(request.getParameter("limit"));

                int offset = (pageIndex - 1) * limit;
                List<CategoryProduct> listCategory = CategoryProductModel.INSTANCE.getSliceCategory(offset, limit);
                int totalCategory = CategoryProductModel.INSTANCE.getTotalCategory();

                ListCategoryProduct listCategoryProduct = new ListCategoryProduct();
                listCategoryProduct.setTotal(totalCategory);
                listCategoryProduct.setListCategory(listCategory);
                listCategoryProduct.setItemPerPage(10);

                if (listCategory.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listCategoryProduct);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getCateById": {
                int idCate = Integer.parseInt(request.getParameter("id_cate"));

                CategoryProduct cateById = CategoryProductModel.INSTANCE.getCategoryByID(idCate);

                if (cateById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(cateById);
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
                int id_parent = Integer.parseInt(request.getParameter("id_parent"));
                int orders = Integer.parseInt(request.getParameter("orders"));
                int status = Integer.parseInt(request.getParameter("status"));

                int addCategory = CategoryProductModel.INSTANCE.addCategory(name, id_parent, orders, status);

                if (addCategory >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm category thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm category thất bại!");
                }
                break;
            }
            case "edit": {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name");
                int id_parent = Integer.parseInt(request.getParameter("id_parent"));
                int orders = Integer.parseInt(request.getParameter("orders"));
                int status = Integer.parseInt(request.getParameter("status"));

                CategoryProduct categoryByID = CategoryProductModel.INSTANCE.getCategoryByID(id);
                if (categoryByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editCategory = CategoryProductModel.INSTANCE.editCategory(id, name, id_parent, orders, status);

                if (editCategory >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa Category thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa Category thất bại!");
                }
                break;
            }

            case "delete": {
                int id = Integer.parseInt(request.getParameter("id"));
                int deleteCategory = CategoryProductModel.INSTANCE.deleteCategory(id);
                if (deleteCategory >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa Category thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa Category thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}
