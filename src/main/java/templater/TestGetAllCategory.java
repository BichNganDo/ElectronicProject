package templater;

import com.google.gson.Gson;
import entity.category_product.CategoryProduct;
import java.util.List;
import model.CategoryModel;

public class TestGetAllCategory {

    public static void main(String[] args) {
        List<CategoryProduct> listCate = CategoryModel.INSTANCE.getAllCategory();
        Gson gson = new Gson();
        String a = gson.toJson(listCate);
        System.out.println(a);
    }
}
