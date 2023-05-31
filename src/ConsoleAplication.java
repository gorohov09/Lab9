import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ConsoleAplication {

    private final Scanner consoleReader;
    private WorkWithSQL sql;
    private final String inputInteger = " Введите число: ";
    private final String inputCommand = " Введите команду: ";

    private final Place[] GetDefaultPlaces() {
        Path path = Paths.get("C:\\Users\\andre\\IdeaProjects\\Lab9\\src\\data-place");
        var result = new Place[4];

        try {
            List<String> lines = Files.readAllLines(path);
            var i = 0;
            for (String line : lines)
            {
                var place = line.split(",");
                result[i] = new Place(Integer.parseInt(place[0]),
                        Integer.parseInt(place[1]),
                        Integer.parseInt(place[2]),
                        Integer.parseInt(place[3]));
                i++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private final Book[] GetDefaultBook() {
        Path path = Paths.get("C:\\Users\\andre\\IdeaProjects\\Lab9\\src\\data-book");
        var result = new Book[4];

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            var i = 0;
            for (String line : lines)
            {
                var book = line.split(",");
                result[i] = new Book(book[0], book[1], book[2],
                        Integer.parseInt(book[3]),
                        Integer.parseInt(book[4]),
                        Integer.parseInt(book[5]),
                        Integer.parseInt(book[6]),
                        Integer.parseInt(book[7]));
                i++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private final Place[] Places = new Place[]
    {
            new Place(1,1,1,2),
            new Place(2,2,1,1),
            new Place(3,1,2,2),
            new Place(4,1,1,3)
    };

    private final Book[] Books = new Book[]
            {
                    new Book("Пушкин","Экмо","Лань", 2010,56,1858,129, 1),
                    new Book("Толстой","Золотая Классика","Литератор", 2015,345,1825,45, 2),
                    new Book("Check","Checking","Проверочка", 2015,777,2013,22, 3),
                    new Book("Проверка","Андре","Литл", 2022,56,2015,59, 4)
            };


    public ConsoleAplication()
    {
        consoleReader = new Scanner(System.in);
    }


    public void StartWork()
    {
        try
        {
            var connection = DbConnector.connectToDb();
            System.out.println("Успешное подключение к бд");

            boolean continueWork = true;

            sql = new WorkWithSQL(connection);

            PrintMenu();

            while (continueWork)
            {
                int action = IntegerInput(inputCommand);

                switch (action) {

                    case 0 -> continueWork = false;
                    case 1 -> CreateNewPlace();
                    case 2 -> CreateNewBook();
                    case 3 -> System.out.print(sql.ShowAllPlaces());
                    case 4 -> System.out.print(sql.ShowAllBooks());
                    case 5 -> UpdatePlace();
                    case 6 -> UpdateBook();
                    case 7 -> DeletePlace();
                    case 8 -> DeleteBook();
                    case 9 -> System.out.print(sql.ShowFirstFieldInAllBooks());
                    case 10 -> System.out.print(sql.ShowSecondFieldInAllPlacesByLexicOrder());
                    case 11 -> ShowAuthorsByWardrobe();
                    case 12 -> ShowWeightsByWardrobe();



                    case 22 -> PrintMenu();
                    case 77 -> SetDefaultData();

                    default -> System.out.println("Введен неправильный аргумент!");
                }
            }

            connection.close();

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Ошибка при подключении к бд");
        }
    }

    private void PrintMenu()
    {
        System.out.print(
                """
                        Выберите действие для Домашней библиотеки
                        1. Добавить новое место хранения.
                        2. Добавить новую книгу.
                        3. Посмотреть все места для книг.
                        4. Посмотреть все книги.
                        5. Изменить место по id.
                        6. Изменить книгу по id
                        7. Удалить место по id
                        8. Удалить книгу по id
                        9 Показать 1 поле в таблице - книги(в лекс порядке)
                        10 Показать 2 поле в таблице - места                      

                        11 Вывести авторов в указанном шкафу в лекс. порядке
                        12 Вывести суммарный вес изданий в указанном шкафу

                        77 Вернуть данные по умолчанию из файла
                        22 вывести меню
                        0. Выйти.\n\s""");
    }

    private void CreateNewPlace()
    {
        System.out.print("Введите номер этажа");
        int floor = IntegerInput(inputInteger);
        System.out.print("Введите номер шкафа ");
        int wardrobe = IntegerInput(inputInteger);
        System.out.print("Введите номер полки ");
        int shelf = IntegerInput(inputInteger);
        System.out.println(sql.CreateNewPlace(floor, wardrobe, shelf));
    }

    private void CreateNewBook()
    {
        System.out.print("Введите автора книги: ");
        String author = consoleReader.nextLine();
        System.out.print("Введите издание: ");
        String publication = consoleReader.nextLine();
        System.out.print("Введите издательство: ");
        String publishingHouse = consoleReader.nextLine();

        System.out.print("Год публикации,");
        int yearPublic = IntegerInput(inputInteger);

        System.out.print("Кол-во страниц, ");
        int pages = IntegerInput(inputInteger);

        System.out.print("Год написания, ");
        int yearWrite = IntegerInput(inputInteger);

        System.out.print("Вес в граммах, ");
        int weight = IntegerInput(inputInteger);

        System.out.print("Id места, где будет находиться книга, ");
        int placeId = IntegerInput(inputInteger);
        System.out.println(sql.CreateNewBook(
                author, publication, publishingHouse, yearPublic,
                pages, yearWrite, weight, placeId));
    }

    private void UpdatePlace() {
        System.out.print("Введите id места ");
        int id = IntegerInput(inputInteger);

        if (!sql.CheckExistsPlaceById(id)) {
            System.out.println("Места с таким id не существует!");
            return;
        }
        System.out.print("Введите новый номер этажа, ");
        int floor = IntegerInput(inputInteger);

        System.out.print("Введите новый номер шкафа,");
        int wardrobe = IntegerInput(inputInteger);

        System.out.print("Введите новый номер полки,");
        int shelf = IntegerInput(inputInteger);

        System.out.println(sql.UpdatePlace(id, floor, wardrobe, shelf));
    }


    private void UpdateBook() {

        System.out.print("Введите id книги, ");
        int id = IntegerInput(inputInteger);
        if (!sql.CheckExistsBookById(id)) {
            System.out.println("Книга с таким id не существует!");
            return;
        }
        System.out.print("Введите id места, где будет находиться книга, ");
        int placeId = IntegerInput(inputInteger);
        if (!sql.CheckExistsPlaceById(placeId)) {
            System.out.println("Места с таким id не существует!");
            return;
        }
        System.out.print("Введите нового автора книги: ");
        String author = consoleReader.nextLine();
        System.out.print("Введите новое издание: ");
        String publication = consoleReader.nextLine();
        System.out.print("Введите новое издательство: ");
        String publishingHouse = consoleReader.nextLine();

        System.out.print("Введите новый год публикации, ");
        int yearPublic = IntegerInput(inputInteger);

        System.out.print("Введите новое кол-во страниц,");
        int pages = IntegerInput(inputInteger);

        System.out.print("Введите новый год написания, ");
        int yearWrite = IntegerInput(inputInteger);
        System.out.print("Введите новый вес в граммах, ");
        int weight = IntegerInput(inputInteger);

        System.out.println(sql.UpdateBook(id,
                author, publication, publishingHouse, yearPublic,
                pages, yearWrite, weight, placeId));

    }

    private void DeletePlace()  {

        System.out.print("Введите id места, ");
        int id = IntegerInput(inputInteger);
        System.out.println(sql.DeletePlace(id));
    }

    private  void DeleteBook() {

        System.out.print("Введите id книги,");
        int id = IntegerInput(inputInteger);
        System.out.println(sql.DeleteBook(id));
    }

    private void ShowAuthorsByWardrobe() {
        System.out.print("Введите номер этажа, ");
        int floor = IntegerInput(inputInteger);

        System.out.print("Введите номер шкафа, ");
        int wardrobe = IntegerInput(inputInteger);

        System.out.println(sql.ShowAllAuthorInCurrentWardrobeByLexicOrder(floor, wardrobe));
    }

    private void ShowWeightsByWardrobe() {
        System.out.print("Введите номер этажа, ");
        int floor = IntegerInput(inputInteger);

        System.out.print("Введите номер шкафа, ");
        int wardrobe = IntegerInput(inputInteger);

        System.out.println("Общий вес:" + sql.ShowAllWeightInCurrentWardrobe(floor, wardrobe));
    }


    private void SetDefaultData()
    {
        sql.DeleteAll();
        var places = GetDefaultPlaces();
        var books = GetDefaultBook();

        for (Place place : places)
        {
            sql.InsertDefaultData(place.getId(),place.getFloor(),place.getWardrobe(), place.getShelf());
        }
        for (Book book :books)
        {
            sql.CreateNewBook(book.getAuthor(), book.getPublication(), book.getPublishing_house(), book.getYear_public(), book.getPages(), book.getYear_write(), book.getWeight(), book.getFk());
        }

    }

    private int IntegerInput(String message)
    {
        while (true)
        {
            try
            {
                System.out.print(message);
                return Integer.parseInt(consoleReader.nextLine());
            } catch (Exception e) {
                System.out.println("Ошибка ввода, попробуйте еще раз!");
            }
        }
    }






}
