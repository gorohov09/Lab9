public class Book {
    private String author;
    private String publication;
    private String publishing_house;
    private int year_public;
    private int pages;
    private int year_write;
    private int weight;
    private int fk;

    public Book(String author, String publication, String publishing_house,
                int year_public, int pages, int year_write, int weight) {
        this.author = author;
        this.publication = publication;
        this.publishing_house = publishing_house;
        this.year_public = year_public;
        this.pages = pages;
        this.year_write = year_write;
        this.weight = weight;
    }

    public Book(String author, String publication, String publishing_house,
                int year_public, int pages, int year_write, int weight, int FK) {
        this.author = author;
        this.publication = publication;
        this.publishing_house = publishing_house;
        this.year_public = year_public;
        this.pages = pages;
        this.year_write = year_write;
        this.weight = weight;
        this.fk = FK;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublication() {
        return publication;
    }

    public String getPublishing_house() {
        return publishing_house;
    }

    public int getYear_public() {
        return year_public;
    }

    public int getPages() {
        return pages;
    }

    public int getYear_write() {
        return year_write;
    }

    public int getWeight() {
        return weight;
    }
    public int getFk() {
        return fk;
    }
}
