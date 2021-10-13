package servlets.client;

import com.google.gson.Gson;
import common.APIResult;
import common.Config;
import entity.item.CartItem;
import entity.product.Product;
import helper.HttpHelper;
import helper.ServletUtil;
import helper.SessionHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.InfoBuyerModel;
import model.ProductModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

public class InfoBuyer extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);

        String name = jbody.optString("name");
        String email = jbody.optString("email");
        String phone = jbody.optString("phone");
        String address = jbody.optString("address");
        String note = jbody.optString("note");

        List<CartItem> listCartPayment = SessionHelper.INSTANCE.getCartItem(request);
        if (listCartPayment.size() <= 0) {
            result.setErrorCode(-3);
            result.setMessage("Không có sản phẩm trong giỏ hàng.");
            return;
        }

        List<Product> listProductItem = new ArrayList<>();
        int payTotal = 0;
        for (CartItem cartItem : listCartPayment) {
            int product_id = cartItem.getId_product();
            Product productItem = ProductModel.INSTANCE.getSortProduct(product_id);
            productItem.setQuantity_buy(cartItem.getQuantity());
            payTotal = payTotal + productItem.getQuantity_buy() * productItem.getPrice_sale();
            listProductItem.add(productItem);
        }

        String listCart = gson.toJson(listProductItem);
        int totalPayment = payTotal + Config.SHIPPING_FEE;
        int addInfoBuyer = InfoBuyerModel.INSTANCE.addInfoBuyer(name, email, phone, address, note, listCart, totalPayment);
        if (addInfoBuyer >= 0) {
            result.setErrorCode(0);
            result.setMessage("Thanh toán thành công!");
        } else {
            result.setErrorCode(-1);
            result.setMessage("Thanh toán thất bại!");
        }
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
