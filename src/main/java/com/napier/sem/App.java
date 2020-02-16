package com.napier.sem;

import java.sql.*;

public class App {
    private enum AreaType {
        World,
        Continent,
        Region,
        Country,
        District
    }

    private Connection con = null;

    public static void main(String[] args) {
        //Enum for functions requiring an area type
        AreaType areaType;

        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Display Example Country
        a.displayCountry(a.getCountry("ABW"));

        //display example capital city report
        a.displayCapitalCityReport(a.getCity("GBR"));

        //Display country population report
        areaType = AreaType.Country;
        a.displayPopulation(a.getPopulation(areaType));

        // Disconnect from database
        a.disconnect();
    }

    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    //Get the details of a country from the database
    public Country getCountry(String ID) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, country.Name, Continent, Region, city.Name, country.Population "
                            + "FROM country JOIN city "
                            + "ON country.Capital=city.ID "
                            + "WHERE Code = '" + ID + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next()) {
                Country cnt = new Country();
                cnt.code = rset.getString("Code");
                cnt.name = rset.getString("country.Name");
                cnt.continent = rset.getString("Continent");
                cnt.region = rset.getString("Region");
                cnt.capital = rset.getString("city.Name");
                cnt.population = rset.getInt("country.Population");
                return cnt;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    //Get Capital City from Database
    public CapitalCity getCity(String ID) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT city.ID, city.Name, CountryCode, District, city.Population, country.Capital "
                            + "FROM city JOIN country "
                            + "ON country.Capital=city.ID "
                            + "WHERE Code = '" + ID + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next()) {
                CapitalCity cptc = new CapitalCity();
                //cptc.id = rset.getString("ID");
                cptc.name = rset.getString("city.Name");
                cptc.country = rset.getString("CountryCode");
                cptc.district = rset.getString("District");
                cptc.population = rset.getInt("city.Population");
                return cptc;
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get city details");
            return null;
        }
    }

    //Get the details of a country from the database
    public Population getPopulation(AreaType areaType)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "";
            if (areaType == AreaType.Country)
            {
                strSelect =
                        "SELECT co.Name AS Name, SUM(DISTINCT co.Population) AS TotalPop, "
                                + "SUM(ci.Population) AS CityPop, (SUM(ci.Population)/SUM(DISTINCT co.Population))*100 AS CityPercent, "
                                + "SUM(DISTINCT co.Population) - SUM(ci.Population) AS NonCityPop, ((SUM(DISTINCT co.Population) - SUM(ci.Population))/SUM(DISTINCT co.Population))*100 AS NonCityPercent "
                                + "FROM country co JOIN city ci ON co.Code = ci.CountryCode "
                                + "GROUP BY `Name` "
                                + "ORDER BY `Name` ";
            }
            else
            {
                strSelect =
                        "SELECT co." + areaType.toString() + " AS Name, SUM(DISTINCT co.Population) AS TotalPop, "
                                + "SUM(ci.Population) AS CityPop, (SUM(ci.Population)/SUM(DISTINCT co.Population))*100 AS CityPercent, "
                                + "SUM(DISTINCT co.Population) - SUM(ci.Population) AS NonCityPop, ((SUM(DISTINCT co.Population) - SUM(ci.Population))/SUM(DISTINCT co.Population))*100 AS NonCityPercent "
                                + "FROM country co JOIN city ci ON co.Code = ci.CountryCode "
                                + "GROUP BY `Name` "
                                + "ORDER BY `Name` ";
            }
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Population pop = new Population();
                pop.name = rset.getString("Name");
                pop.totalPop = rset.getString("TotalPop");
                pop.cityPop = rset.getString("CityPop");
                pop.nonCityPop = rset.getString("NonCityPop");
                pop.cityPercent = rset.getFloat("CityPercent");
                pop.nonCityPercent = rset.getFloat("NonCityPercent");
                return pop;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }

    //Displays details of a given Country object to terminal
    public void displayCountry(Country cnt)
    {
        if (cnt != null)
        {
            System.out.println(
                    cnt.code + " "
                            + cnt.name + "\n"
                            + "Continent: " + cnt.continent + "\n"
                            + "Region: " + cnt.region + "\n"
                            + "Population: " + cnt.population + "\n"
                            + "Capital: " + cnt.capital + "\n");
        }
    }

    //Displays details of a given CapitalCity object to terminal
    public void displayCapitalCityReport(CapitalCity cptc)
    {
        if(cptc != null)
        {
            System.out.println(
                    //cptc.code + " "
                            "Capital City " + cptc.name + "\n"
                            + "Country Code " + cptc.country + "\n"
                            +  "Capital Population " + cptc.population + "\n"
            );
        }
    }

    public void displayPopulation(Population pop)
    {
        if (pop != null)
        {
            System.out.println(
                    pop.name + "\n"
                            + "Total Population: " + pop.totalPop + "\n"
                            + "City Population: " + pop.cityPop + " (" + pop.cityPercent + "%)" + "\n"
                            + "Non-City Population: " + pop.nonCityPop + " (" + pop.nonCityPercent + "%)" + "\n"
            );
        }
    }
}