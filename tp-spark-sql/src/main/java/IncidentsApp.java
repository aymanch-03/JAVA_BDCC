import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class IncidentsApp {
    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
                .appName("IncidentStatistics")
                .master("local[*]")
                .getOrCreate();

        Dataset<Row> incidents = spark.read()
                .option("header", true)
                .option("inferSchema", true)
                .csv("incidents.csv");

        incidents.createOrReplaceTempView("tbl_incidents");

        Dataset<Row> serviceCounts = spark.sql(
                "SELECT service AS service_name, " +
                        "COUNT(*) AS total_incidents " +
                        "FROM tbl_incidents " +
                        "GROUP BY service " +
                        "ORDER BY total_incidents DESC"
        );
        serviceCounts.show();

        Dataset<Row> topYears = spark.sql(
                "SELECT YEAR(TO_DATE(date)) AS incident_year, " +
                        "COUNT(*) AS total_incidents " +
                        "FROM tbl_incidents " +
                        "GROUP BY incident_year " +
                        "ORDER BY total_incidents DESC " +
                        "LIMIT 2"
        );
        topYears.show();

        spark.stop();
    }
}
