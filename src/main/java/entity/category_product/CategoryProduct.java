package entity.category_product;

import entity.product.Product;
import java.util.ArrayList;
import java.util.List;

public class CategoryProduct {
    
    private int id;
    private String name;
    private int id_parent;
    private int orders;
    private int status;
    private String created_date;
    private String updated_date;
    private String cate_parent;
    private int hot;
    private List<CategoryProduct> listSubCategory = new ArrayList<>();
    
    public CategoryProduct() {
    }
    
    public CategoryProduct(int id, String name, int id_parent, int orders, int status, String created_date, String updated_date, String cate_parent, int hot) {
        this.id = id;
        this.name = name;
        this.id_parent = id_parent;
        this.orders = orders;
        this.status = status;
        this.created_date = created_date;
        this.updated_date = updated_date;
        this.cate_parent = cate_parent;
        this.hot = hot;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getId_parent() {
        return id_parent;
    }
    
    public void setId_parent(int id_parent) {
        this.id_parent = id_parent;
    }
    
    public int getOrders() {
        return orders;
    }
    
    public void setOrders(int orders) {
        this.orders = orders;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getCreated_date() {
        return created_date;
    }
    
    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
    
    public String getUpdated_date() {
        return updated_date;
    }
    
    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
    
    public String getCate_parent() {
        return cate_parent;
    }
    
    public void setCate_parent(String cate_parent) {
        this.cate_parent = cate_parent;
    }
    
    public int getHot() {
        return hot;
    }
    
    public void setHot(int hot) {
        this.hot = hot;
    }
    
    public List<CategoryProduct> getListSubCategory() {
        return listSubCategory;
    }
    
    public void addSubCategory(CategoryProduct subCategory) {
        this.listSubCategory.add(subCategory);
    }
    
}
