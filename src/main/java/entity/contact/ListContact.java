package entity.contact;

import java.util.List;

public class ListContact {

    private int total;
    private List<ContactClient> listContact;
    private int itemPerPage;

    public ListContact() {
    }

    public ListContact(int total, List<ContactClient> listContact, int itemPerPage) {
        this.total = total;
        this.listContact = listContact;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ContactClient> getListContact() {
        return listContact;
    }

    public void setListContact(List<ContactClient> listContact) {
        this.listContact = listContact;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
