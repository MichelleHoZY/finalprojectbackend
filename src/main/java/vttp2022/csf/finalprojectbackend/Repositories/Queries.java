package vttp2022.csf.finalprojectbackend.Repositories;

public class Queries {

    // food form queries

    public static final String SQL_SAVE_USER_MEAL_ATTENDANCE = "insert into attendance(user_id, meal_id) values (?, ?)";

    // food menu queries

    public static final String SQL_SAVE_MENU = "insert into meals(meal_id, meal_date, meal_time, vegetarian, main, drinks, dessert, remarks) values(?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_GET_ALL_MENUS = "select * from meals order by meal_date desc, meal_time asc";

    public static final String SQL_UPDATE_MENU = "update meals set main = ?, drinks = ?, dessert = ?, remarks = ? where meal_id = ?";
    
    public static final String SQL_DELETE_MENU = "delete from meals where meal_id = ?";

    public static final String SQL_GET_MENU = "select * from meals where meal_id = ?";

    public static final String SQL_GET_MENU_BY_DATE = "select * from meals where meal_date = ? order by meal_time";

    public static final String SQL_GET_MENU_BY_DATE_STUDENT = "select * from meals where meal_date = ? and vegetarian = ? order by meal_time";

    public static final String SQL_GET_PRESELECTED_MENUS = "select a.meal_id from attendance a join meals m on a.meal_id = m.meal_id where m.meal_date = ? and user_id = ?";

    public static final String SQL_DELETE_MENU_ATTENDANCE = "delete from attendance where user_id = ? and meal_id = ?";

    // food review queries

    public static final String SQL_GET_REVIEWS = "select * from reviews where meal_id = ?";

    public static final String SQL_GET_USER_MEAL_REVIEW = "select r.post_id, r.caption, r.rating, r.pic from reviews r join meals m on r.meal_id = m.meal_id join attendance a on m.meal_id = a.meal_id where r.user_id = ? and m.meal_id = ?";

    public static final String SQL_GET_USER_REVIEWS = "select * from reviews where user_id = ?";

    public static final String SQL_DELETE_PIC_FOR_UPDATE = "select pic from reviews where post_id = ?";

    public static final String SQL_GET_USER_MEALS = "select m.meal_id, m.meal_date, m.meal_time, m.main, m.drinks, m.dessert, m.remarks from meals m join attendance a on m.meal_id = a.meal_id where a.user_id = ? order by meal_date desc";

    public static final String SQL_SAVE_USER_REVIEWS = "insert into reviews(user_id, post_id, meal_id, post_date, rating, caption, pic) values (?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_UPDATE_USER_MEAL_REVIEW = "update reviews set rating = ?, caption = ?, pic = ?, post_date = ? where post_id = ?";

    // login page

    public static final String SQL_LOGIN = "select * from user_info where login_id = ? and password = sha1(?)";

    public static final String SQL_GET_USER_INFO = "select * from user_info where user_id = ?";

    // create new user

    public static final String SQL_CHECK_LOGIN_ID_EXISTS = "select * from user_info where username = ?";

    public static final String SQL_CREATE_NEW_USER = "insert into user_info(username, password, user_name, vegetarian, start_date, end_date, user_type) values(?, ?, ?, ?, ?, ?, ?)";

    // statistics

    public static final String SQL_COUNT_MEAL_ATTENDANCE = "select count(meal_id) from attendance where meal_id = ?";

    public static final String SQL_GET_MEAL_RATINGS = "select r.rating from meals m join reviews r on m.meal_id = r.meal_id where m.meal_id = ? and m.meal_time= ?";

    public static final String SQL_GET_MEAL_NO_OF_RATINGS = "select count(r.rating) from meals m join reviews r on m.meal_id = r.meal_id where m.meal_id = ? and m.meal_time= ?";
}
