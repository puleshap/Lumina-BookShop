package lk.GamerShop.DTO;

import lk.GamerShop.Entities.Book;

import java.util.List;

public class CheckoutDTO {

    private int userId;
    private double total;
    private List<BookDTO> books;
    private int ItemCount;




    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }
}
