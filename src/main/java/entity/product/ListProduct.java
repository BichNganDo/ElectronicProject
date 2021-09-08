package entity.product;

import java.util.List;

public class ListProduct {

    private int total;
    private List<Product> listProduct;
    private int itemPerPage;

    public ListProduct() {
    }

    public ListProduct(int total, List<Product> listProduct, int itemPerPage) {
        this.total = total;
        this.listProduct = listProduct;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
