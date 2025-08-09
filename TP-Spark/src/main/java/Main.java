import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class Main {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf()
                .setAppName("SalesAnalysis")
                .setMaster("local[*]");

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<String> lines = sparkContext.textFile("ventes.txt");

        long totalRecords = lines.count();
        System.out.println("Nombre total de ventes : " + totalRecords);

        JavaRDD<String[]> splittedLines = lines.map(line -> line.split(" "));

        JavaPairRDD<String, Integer> citySales = splittedLines.mapToPair(parts ->
                new Tuple2<>(parts[1], Integer.parseInt(parts[3]))
        );

        JavaPairRDD<String, Integer> totalSalesByCity = citySales.reduceByKey(Integer::sum);

        totalSalesByCity.foreach(record ->
                System.out.println("Ville: " + record._1 + " | Total des ventes: " + record._2)
        );

        JavaPairRDD<String, Integer> salesByCityYear = splittedLines.mapToPair(parts -> {
            String city = parts[1];
            String year = parts[0].split("/")[2];
            int price = Integer.parseInt(parts[3]);
            return new Tuple2<>(city + "_" + year, price);
        });

        JavaPairRDD<String, Integer> totalSalesByCityYear = salesByCityYear.reduceByKey(Integer::sum);

        JavaPairRDD<String, Integer> sortedSales = totalSalesByCityYear.sortByKey();

        sortedSales.foreach(record -> {
            String[] keys = record._1.split("_");
            String city = keys[0];
            String year = keys[1];
            System.out.println("Ville: " + city + " | Année: " + year + " | Total: " + record._2);
        });

        sparkContext.close();
    }
}
