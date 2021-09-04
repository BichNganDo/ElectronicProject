package entity;

import java.util.List;

public class ListCategoryProduct {

    private int total;
    private List<CategoryProduct> listCategory;
    private int itemPerPage;

    public ListCategoryProduct() {
    }

    public ListCategoryProduct(int total, List<CategoryProduct> listCategory, int itemPerPage) {
        this.total = total;
        this.listCategory = listCategory;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CategoryProduct> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<CategoryProduct> listCategory) {
        this.listCategory = listCategory;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
