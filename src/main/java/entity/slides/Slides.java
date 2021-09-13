package entity.slides;

public class Slides {

    private int id;
    private String name;
    private String image;
    private String link;
    private int orders;
    private int status;
    private String created_date;

    public Slides() {
    }

    public Slides(int id, String name, String image, String link, int orders, int status, String created_date) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.link = link;
        this.orders = orders;
        this.status = status;
        this.created_date = created_date;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

}
