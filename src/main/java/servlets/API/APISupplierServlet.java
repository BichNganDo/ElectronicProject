package servlets.API;

import com.google.gson.Gson;
import common.APIResult;
import entity.category_product.CategoryProduct;
import entity.category_product.ListCategoryProduct;
import entity.supplier.ListSupplier;
import entity.supplier.Supplier;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryProductModel;
import model.SupplierModel;

public class APISupplierServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getsupplier": {
                int pageIndex = Integer.parseInt(request.getParameter("page_index"));
                int limit = Integer.parseInt(request.getParameter("limit"));

                int offset = (pageIndex - 1) * limit;
                List<Supplier> sliceSupplier = SupplierModel.INSTANCE.getSliceSupplier(offset, limit);
                int totalSupplier = SupplierModel.INSTANCE.getTotalSupplier();

                ListSupplier listSupplier = new ListSupplier();
                listSupplier.setTotal(totalSupplier);
                listSupplier.setListSupplier(sliceSupplier);
                listSupplier.setItemPerPage(10);

                if (sliceSupplier.size() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listSupplier);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getSupplierById": {
                int idSupplier = Integer.parseInt(request.getParameter("id_supplier"));
                Supplier supplierByID = SupplierModel.INSTANCE.getSupplierByID(idSupplier);

                if (supplierByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(supplierByID);
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

}
