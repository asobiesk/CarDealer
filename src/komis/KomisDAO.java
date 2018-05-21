package komis;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.*;

public class KomisDAO {
    private static final String user = "****";
    private static final String password = "****";
    private static final String database = "jdbc:oracle:thin:asobiesk/asobiesk@//localhost:1521/xe";

    private Connection conn = null;

    public void connect() throws SQLException {
        try {
            System.out.println("Wszedłem do connecta!");
            OracleDataSource ods = new OracleDataSource();
            System.out.println("Jestem tutaj!");
            ods.setURL(database);
            ods.setUser(user);
            ods.setPassword(password);
            conn = ods.getConnection();
            System.out.println("Połączono z bazą danych!");

        } catch (SQLException s) {
            System.out.println("Failed to connect database" + s.getMessage());
        }
    }

    public StringBuffer select(int option) {
        try {
            Statement statement = conn.createStatement();
            //ResultSet resultSet = statement.executeQuery("Select * from Samochód");
            ResultSet resultSet = null;
            switch (option) {
                case 0:
                    resultSet = statement.executeQuery("Select * from Kupujący");
                    break;
                case 1:
                    resultSet = statement.executeQuery("Select * from Sprzedający");
                    break;
                case 2:
                    resultSet = statement.executeQuery("Select * from DOSTAWCA_CZĘŚCI");
                    break;
                case 3:
                    resultSet = statement.executeQuery("Select * from Miejsce_parkingowe");
                    break;
                case 4:
                    resultSet = statement.executeQuery("Select * from Samochód");
                    break;


            }
            StringBuffer results = new StringBuffer();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            int lenght = 0;


            //dostosowywanie wyrównania
            for (int i = 1; i <= numberOfColumns; ++i) {
                if (lenght < metaData.getColumnName(i).length())
                    lenght = metaData.getColumnName(i).length();
            }
            lenght += 15;


            for (int i = 1; i <= numberOfColumns; i++) {
                results.append(metaData.getColumnName(i));
                for (int j = lenght; j > metaData.getColumnName(i).length(); --j) {
                    results.append("  ");
                }
            }
            results.append("\n");
            while (resultSet.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    results.append(resultSet.getObject(i));
                    for (int j = lenght; j >= resultSet.getObject(i).toString().length(); --j) {
                        results.append("  ");
                    }

                }
                results.append("\n");

            }
            statement.close();
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insertHandler(int table, String[] parameters) {
        try {
            PreparedStatement ps = null;
            switch (table) {
                case 0:
                    ps = conn.prepareStatement("INSERT into Kupujący VALUES (?,?,?)");
                    ps.setString(1, parameters[1]);
                    ps.setString(2, parameters[2]);
                    ps.setString(3, parameters[0]);
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 1:
                    ps = conn.prepareStatement("INSERT into Sprzedający VALUES (?,?,?)");
                    ps.setString(1, parameters[1]);
                    ps.setString(2, parameters[2]);
                    ps.setString(3, parameters[0]);
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 2:
                    ps = conn.prepareStatement("INSERT into DOSTAWCA_CZĘŚCI VALUES (?,?,?)");
                    ps.setInt(1, Integer.parseInt(parameters[0]));
                    ps.setString(2, parameters[1]);
                    ps.setString(3, parameters[2]);
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 3:
                    ps = conn.prepareStatement("INSERT into Miejsce_parkingowe VALUES (?,?,?,?)");
                    ps.setInt(1, Integer.parseInt(parameters[0]));
                    ps.setInt(2, Integer.parseInt(parameters[1]));
                    ps.setInt(3, Integer.parseInt(parameters[2]));
                    ps.setString(4, parameters[3]);
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 4:
                    ps = conn.prepareStatement("INSERT into Samochód VALUES (?,?,?,?,?,?)");
                    ps.setString(1, parameters[0]);
                    ps.setString(2, parameters[1]);
                    ps.setString(3, parameters[2]);
                    ps.setInt(4, Integer.parseInt(parameters[3]));
                    ps.setString(5, parameters[4]);
                    ps.setString(6, parameters[5]);
                    ps.executeUpdate();
                    ps.close();
                    break;
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean updateHandler(int table, String[] parameters) {
        try {
            if (parameters[0] == null)
                return false;
            PreparedStatement ps = null;
            String SQL = null;
            int givenParams = 1;
            boolean[] isNull = new boolean[6];
            boolean thereHasBeenSetStatement = false;
            for (int i = 0; i < 6; ++i)
                isNull[i] = (parameters[i] == null || parameters[i].length() == 0);


            switch (table) {
                case 0:
                    SQL = "UPDATE Kupujący ";
                    if (!isNull[1]) {
                        SQL += "SET imię = (?),";
                        thereHasBeenSetStatement = true;
                    }
                    if (!isNull[2]) {
                        if (!thereHasBeenSetStatement) {
                            thereHasBeenSetStatement = true;
                            SQL += "SET ";
                        }
                        SQL += "nazwisko = (?),";
                    }
                    if (SQL.substring(SQL.length() - 1, SQL.length()) == ",") ; //usuń końcowy przecinek
                    SQL = SQL.substring(0, SQL.length() - 1);
                    SQL += " WHERE pesel = ";
                    SQL += parameters[0];

                    System.out.println(SQL);
                    ps = conn.prepareStatement(SQL);

                    for (int i = 1; i < 3; ++i) {
                        if (!isNull[i]) {
                            System.out.println("ps.setString(" + givenParams + ", " + parameters[i] + ")");
                            System.out.println("\n" + parameters[i].length());
                            ps.setString(givenParams, parameters[i]);
                            ++givenParams;
                        }
                    }
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 1:
                    SQL = "UPDATE Sprzedający ";
                    if (!isNull[1]) {
                        SQL += "SET imię = (?),";
                        thereHasBeenSetStatement = true;
                    }
                    if (!isNull[2]) {
                        if (!thereHasBeenSetStatement) {
                            thereHasBeenSetStatement = true;
                            SQL += "SET ";
                        }
                        SQL += "nazwisko = (?),";
                    }
                    if (SQL.substring(SQL.length() - 1, SQL.length()) == ",") ; //usuń końcowy przecinek
                    SQL = SQL.substring(0, SQL.length() - 1);
                    SQL += " WHERE pesel = ";
                    SQL += parameters[0];

                    System.out.println(SQL);
                    ps = conn.prepareStatement(SQL);
                    for (int i = 1; i < 3; ++i) {
                        if (!isNull[i]) {
                            System.out.println("ps.setString(" + givenParams + ", " + parameters[i] + ")");
                            System.out.println("\n" + parameters[i].length());
                            ps.setString(givenParams, parameters[i]);
                            ++givenParams;
                        }
                    }
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 2:
                    SQL = "UPDATE DOSTAWCA_CZĘŚCI ";
                    if (!isNull[1]) {
                        SQL += "SET nazwa_firmy = (?),";
                        thereHasBeenSetStatement = true;
                    }
                    if (!isNull[2]) {
                        if (!thereHasBeenSetStatement) {
                            thereHasBeenSetStatement = true;
                            SQL += "SET ";
                        }
                        SQL += "lokalizacja = (?),";
                    }
                    if (SQL.substring(SQL.length() - 1, SQL.length()) == ",") ; //usuń końcowy przecinek
                    SQL = SQL.substring(0, SQL.length() - 1);
                    SQL += " WHERE id_dostawcy = ";
                    SQL += parameters[0];

                    System.out.println(SQL);
                    ps = conn.prepareStatement(SQL);
                    for (int i = 1; i < 3; ++i) {
                        if (!isNull[i]) {
                            System.out.println("ps.setString(" + givenParams + ", " + parameters[i] + ")");
                            System.out.println("\n" + parameters[i].length());
                            ps.setString(givenParams, parameters[i]);
                            ++givenParams;
                        }
                    }
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 3:
                case 4:
                    System.out.println("Temporary unavailable!");
                    return false;


            }

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteHandler(int table, String[] parameters) {
        try {
            PreparedStatement ps = null;
            switch (table) {
                case 0:
                    ps = conn.prepareStatement("DELETE from Kupujący WHERE pesel=(?)");
                    ps.setString(1, parameters[0]);
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 1:
                    ps = conn.prepareStatement("DELETE from Sprzedający WHERE pesel=(?)");
                    ps.setString(1, parameters[0]);
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 2:
                    ps = conn.prepareStatement("DELETE from DOSTAWCA_CZĘŚCI WHERE id_dostawcy=(?)");
                    ps.setInt(1, Integer.parseInt(parameters[0]));
                    ps.executeUpdate();
                    ps.close();
                    break;

                case 3:
                    ps = conn.prepareStatement("DELETE from Miejsce_parkingowe WHERE numer_miejsca=(?)");
                    ps.setInt(1, Integer.parseInt(parameters[0]));
                    ps.executeUpdate();
                    ps.close();
                    break;
                case 4:
                    ps = conn.prepareStatement("DELETE from Samochód WHERE numer_rejestracyjny=(?)");
                    ps.setString(1, parameters[0]);
                    ps.executeUpdate();
                    ps.close();
                    break;

            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int average() {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Avg(Cena) from Samochód");
            resultSet.next();
            int average = resultSet.getInt(1);
            System.out.println("Srednia cena: " + average);
            return average;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public int count()
    {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Count(*) from Samochód");
            resultSet.next();
            int sum = resultSet.getInt(1);
            System.out.println("Suma: " + sum);
            return sum;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

    }


}
