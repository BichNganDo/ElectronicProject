package entity.user_register;

import java.util.List;

public class ListUserRegister {

    private int total;
    private List<UserRegister> listUserRegister;
    private int itemPerPage;

    public ListUserRegister() {
    }

    public ListUserRegister(int total, List<UserRegister> listUserRegister, int itemPerPage) {
        this.total = total;
        this.listUserRegister = listUserRegister;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UserRegister> getListUserRegister() {
        return listUserRegister;
    }

    public void setListUserRegister(List<UserRegister> listUserRegister) {
        this.listUserRegister = listUserRegister;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
