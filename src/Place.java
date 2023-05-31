public class Place {
    private int id;
    private final int floor;
    private final int wardrobe;
    private final int shelf;

    public Place(int floor, int wardrobe, int shelf) {
        this.floor = floor;
        this.wardrobe = wardrobe;
        this.shelf = shelf;
    }

    public Place(int id, int floor, int wardrobe, int shelf) {
        this.id = id;
        this.floor = floor;
        this.wardrobe = wardrobe;
        this.shelf = shelf;
    }

    public int getId() {
        return id;
    }
    public int getFloor() {
        return floor;
    }

    public int getWardrobe() {
        return wardrobe;
    }

    public int getShelf() {
        return shelf;
    }
}
