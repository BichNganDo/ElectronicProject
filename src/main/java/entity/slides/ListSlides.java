package entity.slides;

import java.util.List;

public class ListSlides {

    private int total;
    private List<Slides> listSlides;
    private int itemPerPage;

    public ListSlides() {
    }

    public ListSlides(int total, List<Slides> listSlides, int itemPerPage) {
        this.total = total;
        this.listSlides = listSlides;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Slides> getListSlides() {
        return listSlides;
    }

    public void setListSlides(List<Slides> listSlides) {
        this.listSlides = listSlides;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
