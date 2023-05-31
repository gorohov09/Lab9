import java.sql.*;

public class WorkWithSQL {
    private final Connection connection;
    private PreparedStatement preparedStatement;

    private void setBookParametersInStatement(
            String author, String publication, String publishingHouse, int yearPublic,
            int pages, int yearWrite, int weight) throws SQLException {
        preparedStatement.setString(1, author);
        preparedStatement.setString(2, publication);
        preparedStatement.setString(3, publishingHouse);
        preparedStatement.setInt(4, yearPublic);
        preparedStatement.setInt(5, pages);
        preparedStatement.setInt(6, yearWrite);
        preparedStatement.setInt(7, weight);
    }

    public WorkWithSQL(Connection connection) {
        this.connection = connection;
    }

    public String CreateNewPlace(int floor, int wardrobe, int shelf) {
        try {
            preparedStatement = connection.prepareStatement(
                    "insert into place(floor, wardrobe, shelf) values (?, ?, ?)");
            preparedStatement.setInt(1, floor);
            preparedStatement.setInt(2, wardrobe);
            preparedStatement.setInt(3, shelf);
            preparedStatement.executeUpdate();
            return "Новое место хранения создано!";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }


    public String CreateNewBook(
            String author, String publication, String publishingHouse,
            int yearPublic, int pages, int yearWrite, int weight, int placeId) {
        try {
            preparedStatement = connection.prepareStatement(
                    "insert into book(author, publication, publishingHouse," +
                            " yearPublic, pages, yearWrite, weight, FK_placeId)" +
                            " values (?, ?, ?, ?, ?, ?, ?, ?)");
            setBookParametersInStatement(author, publication, publishingHouse, yearPublic, pages, yearWrite, weight);
            preparedStatement.setInt(8, placeId);
            preparedStatement.executeUpdate();
            return "Новая книга создана!";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String ShowAllPlaces() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from place order by id");
            StringBuilder table = new StringBuilder(" id|этаж| шкаф   |полка\n");
            while (resultSet.next()) {
                table.append(String.format("%3d|%4d|%8d| %4d\n",
                        resultSet.getInt("id"),
                        resultSet.getInt("floor"),
                        resultSet.getInt("wardrobe"),
                        resultSet.getInt("shelf")));
            }
            return table.toString();
        } catch (SQLException e) {
            return "Произошла ошибка при просмотре!";
        }
    }

    public String ShowAllBooks() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select * from book order by id");
            StringBuilder table = new StringBuilder(
                    " id|       автор       |       издание       | издательство |" +
                            "год публикации|страниц|год написания|  вес  | id места \n");
            while (resultSet.next()) {
                table.append(String.format(
                        "%3d|%16s   | %20s| %12s |%11d   | %5d | %10d  | %6d| %8d\n",
                        resultSet.getInt("id"),
                        resultSet.getString("author"),
                        resultSet.getString("publication"),
                        resultSet.getString("publishingHouse"),
                        resultSet.getInt("yearPublic"),
                        resultSet.getInt("pages"),
                        resultSet.getInt("yearWrite"),
                        resultSet.getInt("weight"),
                        resultSet.getInt("FK_placeId")));
            }

            return table.toString();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public boolean CheckExistsPlaceById(int id) {
        System.out.println(id);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) as countOfPlaces from place where id = " + id );
            resultSet.next();
            return resultSet.getInt("countOfPlaces") == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean CheckExistsBookById(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) as countOfBooks from book where id = " + id);
            resultSet.next();
            return resultSet.getInt("countOfBooks") == 1;
        } catch (SQLException e) {
            return false;
        }
    }

    public String UpdatePlace(int id, int floor, int wardrobe, int shelf) {
        try {
            preparedStatement = connection.prepareStatement(
                    "update place set floor = ?, wardrobe = ?, shelf = ? where id = ?");
            preparedStatement.setInt(1, floor);
            preparedStatement.setInt(2, wardrobe);
            preparedStatement.setInt(3, shelf);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            return "Место обновлено!";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String UpdateBook(int id, String author, String publication, String publishingHouse,
                             int yearPublic, int pages, int yearWrite, int weight, int placeId) {
        try {
            preparedStatement = connection.prepareStatement(
                    "update book set author = ?, publication = ?, publishingHouse = ?," +
                            " yearPublic = ?, pages = ?, yearWrite = ?, weight = ?, FK_placeId = ? where id = ?");
            setBookParametersInStatement(author, publication, publishingHouse,
                    yearPublic, pages, yearWrite, weight);
            preparedStatement.setInt(8, placeId);
            preparedStatement.setInt(9, id);
            preparedStatement.executeUpdate();
            return "Книга обновлена!";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String DeletePlace(int id) {
        try {
            preparedStatement = connection.prepareStatement(
                    "delete from place where id = ?");
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() == 0)
                return "Ошибка при удалении места!";
            return "Место удалено!";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String DeleteBook(int id) {
        try {
            preparedStatement = connection.prepareStatement(
                    "delete from book where id = ?");
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() == 0)
                return "Ошибка при удалении книги!";
            return "Книга удалена!";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String ShowFirstFieldInAllBooks()
    {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select author from book order by author");
            StringBuilder table = new StringBuilder();
            table.append("Авторы:\n");
            while (resultSet.next()) {
                table.append(resultSet.getString("author"))
                        .append("\n");
            }
            return table.toString();

        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String ShowSecondFieldInAllPlacesByLexicOrder() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select wardrobe from place order by place.wardrobe");
            StringBuilder table = new StringBuilder();
            table.append("Шкафы:\n");
            while (resultSet.next()) {
                table.append(resultSet.getString("wardrobe"))
                        .append("\n");
            }
            return table.toString();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String ShowAllAuthorInCurrentWardrobeByLexicOrder(int floor, int wardrobe) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select author from book where FK_placeId in(select id from place where" +
                            " floor = " + floor + "AND wardrobe = " + wardrobe + ") order by author");

            StringBuilder table = new StringBuilder();
            while (resultSet.next()) {
                table.append(resultSet.getString("author"))
                        .append("\n");
            }
            if (table.length() == 0) {
                return "Произошла ошибка при просмотре!";
            }
            return table.toString();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String ShowAllPublicationInCurrentFloorByLexicOrder(int floor) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select publication from book where FK_placeId in(select id from place where" +
                            " floor = " + floor + ") order by publication");
            StringBuilder table = new StringBuilder();
            while (resultSet.next()) {
                table.append(resultSet.getString("publication"))
                        .append("\n");
            }
            if (table.length() == 0) {
                return "Произошла ошибка при просмотре!";
            }
            return table.toString();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public String ShowAllWeightInCurrentWardrobe(int floor, int wardrobe) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select SUM(weight) AS res from book where FK_placeId in(select id from place where" +
                            " floor = " + floor + "AND wardrobe = " + wardrobe + ")");

            StringBuilder table = new StringBuilder();
            while (resultSet.next()) {
                table.append(resultSet.getString("res"))
                        .append("\n");
            }
            if (table.length() == 0) {
                return "Произошла ошибка при просмотре!";
            }
            return table.toString();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }


    public String ShowAllSpecialPublication() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "select publication from book where yearPublic - yearWrite > 10");
            StringBuilder table = new StringBuilder();
            while (resultSet.next()) {
                table.append(resultSet.getString("publication"))
                        .append("\n");
            }
            if (table.length() == 0) {
                return "Произошла ошибка при просмотре!";
            }
            return table.toString();
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public void DeleteAll() {
        deleteBooks();
        deletePlaces();
    }

    private void deletePlaces()
    {
        try
        {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(
                    "delete from place");
        }catch (SQLException e)
        {
        }
    }


    private void deleteBooks()
    {
        try
        {
            var statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(
                    "delete from book");
        }catch (SQLException e)
        {

        }
    }

    public String InsertDefaultData(int id, int floor, int wardrobe, int shelf) {
        try {
            preparedStatement = connection.prepareStatement(
                    "SET IDENTITY_INSERT Place ON; insert into place(id, floor, wardrobe, shelf) values (?, ?, ?, ?); SET IDENTITY_INSERT Place OFF;");
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, floor);
            preparedStatement.setInt(3, wardrobe);
            preparedStatement.setInt(4, shelf);
            preparedStatement.executeUpdate();
            return "Новое место хранения создано!";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

}
