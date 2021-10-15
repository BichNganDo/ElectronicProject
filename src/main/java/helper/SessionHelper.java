package helper;

import entity.item.CartItem;
import entity.user_register.UserRegister;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHelper {

    public static SessionHelper INSTANCE = new SessionHelper();

    public void addRecentIdProduct(HttpServletRequest request, int id) {
        HttpSession session = request.getSession();

        if (session.getAttribute("recent_id_product") == null) {
            session.setAttribute("recent_id_product", id);
        } else {

            String s = session.getAttribute("recent_id_product").toString();
            List<Integer> listIdProduct = ServletUtil.convertStringToArray(s);
            if (listIdProduct.contains(id)) {
                return;
            }
            if (listIdProduct.size() < 8) {
                listIdProduct.add(0, id);
            } else {
                listIdProduct.add(0, id);
                listIdProduct.remove(8);
            }

            s = ServletUtil.convertArrayToString(listIdProduct);
            session.setAttribute("recent_id_product", s);
        }
    }

    public List<Integer> getRecentIdProduct(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Integer> listIdProduct = new ArrayList<>();
        if (session.getAttribute("recent_id_product") != null) {
            String result = session.getAttribute("recent_id_product").toString();
            String[] parts = result.split(",");
            for (String part : parts) {
                int recentIdProduct = Integer.parseInt(part);
                listIdProduct.add(recentIdProduct);
            }
        }
        return listIdProduct;
    }

    public List<CartItem> getCartItem(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<CartItem> listCart = new ArrayList<>();
        if (session.getAttribute("list_cart_item") != null) {
            return (List<CartItem>) session.getAttribute("list_cart_item");
        } else {
            return listCart;
        }

    }

    public void addToCart(HttpServletRequest request, int quantity, int id_product) {
        HttpSession session = request.getSession();

        if (quantity > 0 && id_product > 0) {
            CartItem cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setId_product(id_product);

            if (session.getAttribute("list_cart_item") == null) {
                List<CartItem> listCart = new ArrayList<>();
                listCart.add(cartItem);
                session.setAttribute("list_cart_item", listCart);
            } else {
                List<CartItem> listCart = (List<CartItem>) session.getAttribute("list_cart_item");

                CartItem oldItem = null;
                for (CartItem cartItem1 : listCart) {
                    if (cartItem1.getId_product() == id_product) {
                        oldItem = cartItem1;
                    }
                }

                if (oldItem != null) {
                    oldItem.setQuantity(oldItem.getQuantity() + quantity);
                } else {
                    listCart.add(cartItem);
                }

                session.setAttribute("list_cart_item", listCart);
//                for (CartItem cartItem1 : listCart) {
//                    System.out.println("Quantity: " + cartItem1.getQuantity());
//                    System.out.println("Id_product: " + cartItem1.getId_product());
//                }
            }
        }

    }

    public void removeSession(HttpServletRequest request, int id_product) {
        HttpSession session = request.getSession();
        if (id_product > 0) {
            if (session.getAttribute("list_cart_item") != null) {
                List<CartItem> listCart = (List<CartItem>) session.getAttribute("list_cart_item");

                int deleteIndex = -1;
                for (int i = 0; i < listCart.size(); i++) {
                    CartItem cartItem = listCart.get(i);
                    if (cartItem.getId_product() == id_product) {
                        deleteIndex = i;
                        break;
                    }
                }
                if (deleteIndex >= 0) {
                    listCart.remove(deleteIndex);
                }

                session.setAttribute("list_cart_item", listCart);
            }
        }
    }

    public UserRegister getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserRegister userRegister = new UserRegister();
        if (session.getAttribute("name") != null && session.getAttribute("email") != null) {
            userRegister.setName(session.getAttribute("name").toString());
            userRegister.setEmail(session.getAttribute("email").toString());
        }
        return userRegister;
    }

}
