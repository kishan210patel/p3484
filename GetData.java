import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeSet;
import java.util.Vector;

import org.json.JSONObject;
import org.json.JSONArray;

public class GetData {

    static String prefix = "project3.";

    // You must use the following variable as the JDBC connection
    Connection oracleConnection = null;

    // You must refer to the following variables for the corresponding 
    // tables in your database
    String userTableName = null;
    String friendsTableName = null;
    String cityTableName = null;
    String currentCityTableName = null;
    String hometownCityTableName = null;

    // DO NOT modify this constructor
    public GetData(String u, Connection c) {
        super();
        String dataType = u;
        oracleConnection = c;
        userTableName = prefix + dataType + "_USERS";
        friendsTableName = prefix + dataType + "_FRIENDS";
        cityTableName = prefix + dataType + "_CITIES";
        currentCityTableName = prefix + dataType + "_USER_CURRENT_CITIES";
        hometownCityTableName = prefix + dataType + "_USER_HOMETOWN_CITIES";
    }

    // TODO: Implement this function
    @SuppressWarnings("unchecked")
    public JSONArray toJSON() throws SQLException {

        // This is the data structure to store all users' information
        JSONArray users_info = new JSONArray();
        
        try (Statement stmt = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rst = stmt.executeQuery(
                    "SELECT user_id, first_name, last_name, gender, year_of_birth, month_of_birth, day_of_birth " +
                            "FROM " + userTableName); 
            while(rst.next()){
                int userId = rst.getInt(1);
                String firstName = rst.getString(2);
                String lastName = rst.getString(3);
                String gender = rst.getString(4);
                int yob = rst.getInt(5);
                int mob = rst.getInt(6);
                int dob = rst.getInt(7);

                JSONObject dude = new JSONObject();
                dude.put("user_id", userId);
                dude.put("first_name", firstName);
                dude.put("last_name", lastName);
                dude.put("gender", gender);
                dude.put("YOB", yob);
                dude.put("MOB", mob);
                dude.put("DOB", dob);

                Statement stmt2 = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rst2 = stmt2.executeQuery(
                    "SELECT c.city_name, c.state_name, c.country_name " +
                    "FROM " + cityTableName +  " c " + 
                    "JOIN " + currentCityTableName +  " c1 ON c1.current_city_id = c.city_id " +
                    "JOIN " + userTableName + " u ON u.user_id = " + userId
                );
                
                JSONObject currCity = new JSONObject();
                
                if(rst2.next()){
                    currCity.put("country", rst2.getString(3));
                    currCity.put("city", rst2.getString(1));
                    currCity.put("state", rst2.getString(2));
                }
                dude.put("current", currCity);

                Statement stmt3 = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rst3 = stmt3.executeQuery(
                    "SELECT c.city_name, c.state_name, c.country_name " +
                    "FROM " + cityTableName +  " c " + 
                    "JOIN " + hometownCityTableName +  " c1 ON c1.hometown_city_id = c.city_id " +
                    "JOIN " + userTableName + " u ON u.user_id = " + userId
                );
                JSONObject hometown = new JSONObject();
                if(rst3.next()){
                    hometown.put("country", rst3.getString(3));
                    hometown.put("city", rst3.getString(1));
                    hometown.put("state", rst3.getString(2));

                }
                dude.put("hometown", hometown);

                Statement stmt4 = oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rst4 = stmt4.executeQuery(
                    "SELECT f1.user2_id " +
                    "FROM " + friendsTableName +  " f1 " + 
                    "WHERE  f1.user1_id = " + userId + " " +
                    "AND f1.user2_id > " + userId
                );
                
                JSONArray friends = new JSONArray();
                while(rst4.next()){
                    friends.put(rst4.getInt(1));
                }
                dude.put("friends", friends);
                users_info.put(dude);



                rst2.close();
                rst3.close();
                rst4.close();

                stmt2.close();
                stmt3.close();
                stmt4.close();








            }


            
            rst.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return users_info;
    }

    // This outputs to a file "output.json"
    // DO NOT MODIFY this function
    public void writeJSON(JSONArray users_info) {
        try {
            FileWriter file = new FileWriter(System.getProperty("user.dir") + "/output.json");
            file.write(users_info.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
