package lk.GamerShop.DTO;

public class AuthorDTO {
    private int id;
    private String name;
    private String AddedDate;
    private Integer books;
    private Integer BookCount;


    public int getBookCount() {
        return BookCount;
    }

    public void setBookCount(int bookCount) {
        BookCount = bookCount;
    }

    public int getBooks() {
        return books;
    }

    public void setBooks(int books) {
        this.books = books;
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

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }
}
