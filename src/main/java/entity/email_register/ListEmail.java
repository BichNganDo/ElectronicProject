package entity.email_register;

import java.util.List;

public class ListEmail {

    private int total;
    private List<Email> listEmail;
    private int itemPerPage;

    public ListEmail() {
    }

    public ListEmail(int total, List<Email> listEmail, int itemPerPage) {
        this.total = total;
        this.listEmail = listEmail;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Email> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<Email> listEmail) {
        this.listEmail = listEmail;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
