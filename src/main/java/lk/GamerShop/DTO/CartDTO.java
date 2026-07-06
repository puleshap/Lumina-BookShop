package lk.GamerShop.DTO;

import java.util.List;
import java.util.Set;

public class CartDTO {

    private int cartId;
    private Set<CartItemDTO> items;
    private double total;


    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public Set<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<CartItemDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
