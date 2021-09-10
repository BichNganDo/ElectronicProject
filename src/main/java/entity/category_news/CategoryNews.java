package entity.category_news;

public class CategoryNews {

    private int id;
    private String name;
    private int orders;
    private int status;
    private String created_date;
    private String updated_date;

    public CategoryNews() {
    }

    public CategoryNews(int id, String name, int orders, int status, String created_date, String updated_date) {
        this.id = id;
        this.name = name;
        this.orders = orders;
        this.status = status;
        this.created_date = created_date;
        this.updated_date = updated_date;
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

}
