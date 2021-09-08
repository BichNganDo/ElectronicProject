package servlets.API;

import com.google.gson.Gson;
import common.APIResult;
import entity.product.ListProduct;
import entity.product.Product;
import entity.supplier.ListSupplier;
import entity.supplier.Supplier;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductModel;
import model.SupplierModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIProductServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getproduct": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchCate = NumberUtils.toInt(request.getParameter("search_cate"));
                int searchSupplier = NumberUtils.toInt(request.getParameter("search_supplier"));

                int offset = (pageIndex - 1) * limit;
                List<Product> sliceProduct = ProductModel.INSTANCE.getSliceProduct(offset, limit, searchQuery, searchCate, searchSupplier);
                int totalProduct = ProductModel.INSTANCE.getTotalProduct(searchQuery, searchCate, searchSupplier);

                ListProduct listProduct = new ListProduct();
                listProduct.setTotal(totalProduct);
                listProduct.setListProduct(sliceProduct);
                listProduct.setItemPerPage(10);

                if (sliceProduct.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listProduct);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getProductById": {
                int idProduct = Integer.parseInt(request.getParameter("id_product"));
                Product productByID = ProductModel.INSTANCE.getProductByID(idProduct);

                if (productByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(productByID);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Error");
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
                int id_cate = NumberUtils.toInt(request.getParameter("category"));
                int id_supplier = NumberUtils.toInt(request.getParameter("supplier"));
                String name = request.getParameter("name");
                int price = NumberUtils.toInt(request.getParameter("price"));
                int price_sale = NumberUtils.toInt(request.getParameter("price_sale"));
                int quantity = NumberUtils.toInt(request.getParameter("quantity"));
                String image_url = request.getParameter("image_url");
                String content = request.getParameter("content");
                String warranty = request.getParameter("warranty");
                String hot = request.getParameter("hot");

                int addProduct = ProductModel.INSTANCE.addProduct(id_cate, id_supplier, name, price, price_sale, quantity, image_url, content, warranty, hot);
                if (addProduct >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm product thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm product thất bại!");
                }
                break;
            }
//            case "edit": {
//                int id = NumberUtils.toInt(request.getParameter("id"));
//                String name = request.getParameter("name");
//                String address = request.getParameter("address");
//                String phone = request.getParameter("phone");
//                String email = request.getParameter("email");
//                String fax = request.getParameter("fax");
//
//                Supplier supplierByID = SupplierModel.INSTANCE.getSupplierByID(id);
//                if (supplierByID.getId() == 0) {
//                    result.setErrorCode(-1);
//                    result.setMessage("Thất bại!");
//                    return;
//                }
//
//                int editSupplier = SupplierModel.INSTANCE.editSupplier(id, name, address, phone, email, fax);
//                if (editSupplier >= 0) {
//                    result.setErrorCode(0);
//                    result.setMessage("Sửa supplier thành công!");
//                } else {
//                    result.setErrorCode(-1);
//                    result.setMessage("Sửa supplier thất bại!");
//                }
//                break;
//            }

//            case "delete": {
//                int id = NumberUtils.toInt(request.getParameter("id"));
//                int deleteSUpplier = SupplierModel.INSTANCE.deleteSUpplier(id);
//                if (deleteSUpplier >= 0) {
//                    result.setErrorCode(0);
//                    result.setMessage("Xóa supplier thành công!");
//                } else {
//                    result.setErrorCode(-2);
//                    result.setMessage("Xóa supplier thất bại!");
//                }
//                break;
//            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}