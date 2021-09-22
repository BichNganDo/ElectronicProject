package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.product.FilterProduct;
import entity.product.ListProduct;
import entity.product.Product;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductModel;
import org.apache.commons.lang3.math.NumberUtils;

public class APIProductServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getproduct": {
                FilterProduct filterProduct = new FilterProduct();
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchCate = NumberUtils.toInt(request.getParameter("search_cate"));
                int searchSupplier = NumberUtils.toInt(request.getParameter("search_supplier"));
                int searchProperty = NumberUtils.toInt(request.getParameter("search_property"));
                int orderView = NumberUtils.toInt(request.getParameter("order_view"));
                int offset = (pageIndex - 1) * limit;

                filterProduct.setLimit(limit);
                filterProduct.setOffset(offset);
                filterProduct.setSearchQuery(searchQuery);
                filterProduct.setSearchCate(searchCate);
                filterProduct.setSearchSupplier(searchSupplier);
                filterProduct.setSearchProperty(searchProperty);
                filterProduct.setOrderView(orderView);

                List<Product> sliceProduct = ProductModel.INSTANCE.getSliceProduct(filterProduct);
                int totalProduct = ProductModel.INSTANCE.getTotalProduct(filterProduct);

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
                int property = NumberUtils.toInt(request.getParameter("property"));

                int addProduct = ProductModel.INSTANCE.addProduct(id_cate, id_supplier, name, price, price_sale, quantity, image_url, content, warranty, property);
                if (addProduct >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm product thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm product thất bại!");
                }
                break;
            }

            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int id_cate = NumberUtils.toInt(request.getParameter("category"));
                int id_supplier = NumberUtils.toInt(request.getParameter("supplier"));
                String name = request.getParameter("name");
                int price = NumberUtils.toInt(request.getParameter("price"));
                int price_sale = NumberUtils.toInt(request.getParameter("price_sale"));
                int quantity = NumberUtils.toInt(request.getParameter("quantity"));
                String image_url = request.getParameter("image_url");
                String content = request.getParameter("content");
                String warranty = request.getParameter("warranty");
                int property = NumberUtils.toInt(request.getParameter("property"));

                Product productByID = ProductModel.INSTANCE.getProductByID(id);
                if (productByID.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editProduct = ProductModel.INSTANCE.editProduct(id, id_cate, id_supplier, name, price, price_sale, quantity, image_url, content, warranty, property);
                if (editProduct >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa product thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa product thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteProduct = ProductModel.INSTANCE.deleteProduct(id);
                if (deleteProduct >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa product thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa product thất bại!");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}
