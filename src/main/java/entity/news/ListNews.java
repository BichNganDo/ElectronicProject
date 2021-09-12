package entity.news;

import java.util.List;

public class ListNews {

    private int total;
    private List<News> listNews;
    private int itemPerPage;

    public ListNews() {
    }

    public ListNews(int total, List<News> listNews, int itemPerPage) {
        this.total = total;
        this.listNews = listNews;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<News> getListNews() {
        return listNews;
    }

    public void setListNews(List<News> listNews) {
        this.listNews = listNews;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
