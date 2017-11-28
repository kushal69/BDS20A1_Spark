import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{ Seconds, StreamingContext }

object EvenLinesApp {
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("EvenLines App")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    val lines = ssc.socketTextStream("localhost",9999);

    val linesFiltered = lines.filter { x => lineSum(x) % 2 == 0 };

    val linesSum = linesFiltered.map { x => lineSum(x) };

    println("Lines having even sum");
    linesFiltered.print();
    println("");
    print("Sum of remaining numbers : ");
    linesSum.reduce((sum1, sum2) => sum1 + sum2).print();

    ssc.start()
    ssc.awaitTermination()
  }

  def lineSum(ln: String): Double = {
    val words = ln.split(" ");
    var num: Double = 0;
    for (x <- words) {
      try {
        val len = x.length();
        num = num + len;
      } catch {
        case ex: Exception => {}
      }
    }
    return num;
  }

}
