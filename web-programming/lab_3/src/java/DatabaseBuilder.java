public class DatabaseBuilder {
    public static Database newDatabase(String prefix) throws Exception {
        String error = "Ошибка при подключении к БД! ";

        String url = System.getenv(prefix + "URL");
        String login = System.getenv(prefix + "login");
        String password = System.getenv(prefix + "password");

        if (url == null) throw new Exception(error + "В переменных среды не задан URL.");
        if (login == null) throw new Exception(error + "В переменных среды не задан логин.");
        if (password == null) throw new Exception(error + "В переменных среды не задан пароль.");

        return new Database(url, login, password);
    }
}