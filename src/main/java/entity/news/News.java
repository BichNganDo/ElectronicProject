package entity.news;

import common.Config;

public class News {

    private int id;
    private int idCategoryNews;
    private String title;
    private String info;
    private String content;
    private String image;
    private String created_date;
    private String updated_date;
    private String categoryNews;

    public News() {
    }

    public News(int id, int idCategoryNews, String title, String info, String content, String image, String created_date, String updated_date, String categoryNews) {
        this.id = id;
        this.idCategoryNews = idCategoryNews;
        this.title = title;
        this.info = info;
        this.content = content;
        this.image = image;
        this.created_date = created_date;
        this.updated_date = updated_date;
        this.categoryNews = categoryNews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoryNews() {
        return idCategoryNews;
    }

    public void setIdCategoryNews(int idCategoryNews) {
        this.idCategoryNews = idCategoryNews;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageUrlWithBaseDomain(String image) { // set de hien thi
        if (image.startsWith("http")) {
            this.image = image;
        } else {
            this.image = Config.APP_DOMAIN + "/" + image;
        }
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

    public String getCategoryNews() {
        return categoryNews;
    }

    public void setCategoryNews(String categoryNews) {
        this.categoryNews = categoryNews;
    }

}
