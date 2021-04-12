package cn.hfut.huangshan.service.predict;

import cn.hfut.huangshan.mapper.DailyStatisticsMapper;
import cn.hfut.huangshan.pojo.DailyStatistics;
import cn.hfut.huangshan.predict.inpput.Factors;
import cn.hfut.huangshan.utils.DailyStatisticsPredictUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.Tuple8;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Administrator
 */
@Service
public class RandomForestPredictService {

    @Autowired
    DailyStatisticsMapper dailyStatisticsMapper;


    // 设置参数
    /**
     * 分类特征信息，指定哪些特征是分类的，以及每个特征可以采用的分类值
     */
    private static Map<Integer, Integer> categoricalFeaturesInfo = new HashMap() {{
//        put(0, 7);
//        put(5, 16);
    }};
    /**
     * 树的数量，越多结果越准确，但是消耗性能
     */
    private static int numTrees = 500;
    /**
     * 每个节点上要考虑拆分的要素数量
     */
    private static String featureSubsetStrategy = "auto";
    /**
     * 用于信息增益计算的标准，其实就是决策树的生成算法,回归只能选variance
     */
    private static String impurity = "variance";
    /**
     * 树的最大深度，太深容易造成过拟合
     */
    private static int maxDepth = 10;
    /**
     * 用于拆分要素的最大数
     */
    private static int maxBins = 100;
    /**
     * 随机种子
     */
    private static int seed = 12345;

    /**
     * 用于训练模型
     * 可在以后设置定时任务，不断更新模型
     */

    public void modelTrainer() {
        // 设置环境
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("RandomForestRegression");
        sparkConf.set("spark.testing.memory", "471859200");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        Logger.getRootLogger().setLevel(Level.ERROR);


        List<DailyStatistics> sourceData = dailyStatisticsMapper.getPeriodDailyStatistics("2019-11-01", "2019-11-10");

        JavaRDD<DailyStatistics> sourceRDD = jsc.parallelize(sourceData);
        JavaRDD<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> sourceTupleRDD = sourceRDD.map(
                obj -> new Tuple8<>(obj.getTodayTotalNum(),
                        DailyStatisticsPredictUtil.dateWeekToNum(obj.getDateWeek()),
                        obj.getPre1TotalNum(),
                        obj.getPre2TotalNum(),
                        obj.getPre3TotalNum(),
                        obj.getPre7TotalNum(),
                        DailyStatisticsPredictUtil.weatherTypeToNum(obj.getWeatherName()),
                        obj.getOrderNum())
        );

        JavaRDD<LabeledPoint> data = sourceTupleRDD.map(x -> {
            double[] points = new double[7];
            points[0] = x._2();
            points[1] = x._3();
            points[2] = x._4();
            points[3] = x._5();
            points[4] = x._6();
            points[5] = x._7();
            points[6] = x._8();

            return new LabeledPoint(x._1(), Vectors.dense(points));
        });

        // 切分数据集
        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // 训练模型
        RandomForestModel model = RandomForest.trainRegressor(trainingData,
                categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins, seed);
        // 进行预测
        JavaPairRDD<Double, Double> predictionAndLabel = testData.mapToPair(
                p -> new Tuple2<>(model.predict(p.features()), p.label())
        );

        // 计算误差
        double predictMSE = predictionAndLabel.mapToDouble(pl -> {
            double diff = pl._1() - pl._2();
            return diff * diff;
        }).mean();
        double predictRMSE = Math.sqrt(predictMSE);
        System.out.println("Test Mean Squared Error: " + predictMSE);
        System.out.println("Test Root Mean Squared Error: " + predictRMSE);
        // 输出预测结果
        List<Tuple2<Double, Double>> predictions = predictionAndLabel.take(50);
        for (Tuple2<Double, Double> tuple2 : predictions) {
            String s = tuple2.toString();
            String[] result = s.substring(1, s.length() - 1).split(",");
            System.out.println("lable: " + result[0] + "\t\t" + "prediction: " + result[1]);
        }
        model.save(jsc.sc(), "target/tmp/RandomForestRegressionModel");
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("src/main/resources/data/model.txt"));
            out.write(model.toDebugString());
            out.close();
            System.out.println("文件创建成功！");
        } catch (IOException e) {
        }

        jsc.stop();
    }

    public List<DailyStatistics> predictor(List<DailyStatistics> dailyStatisticsList) {
        // 设置环境
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("RandomForestRegression");
        sparkConf.set("spark.testing.memory", "471859200");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        Logger.getRootLogger().setLevel(Level.ERROR);

        // 加载已有的模型
        RandomForestModel model = RandomForestModel.load(jsc.sc(), "target/tmp/RandomForestRegressionModel");

        JavaRDD<DailyStatistics> sourceRDD = jsc.parallelize(dailyStatisticsList);
        JavaRDD<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> sourceTupleRDD = sourceRDD.map(
                obj -> new Tuple8<>(obj.getTodayTotalNum(),
                        DailyStatisticsPredictUtil.dateWeekToNum(obj.getDateWeek()),
                        obj.getPre1TotalNum(),
                        obj.getPre2TotalNum(),
                        obj.getPre3TotalNum(),
                        obj.getPre7TotalNum(),
                        DailyStatisticsPredictUtil.weatherTypeToNum(obj.getWeatherName()),
                        obj.getOrderNum())
        );

        JavaRDD<LabeledPoint> predictData = sourceTupleRDD.map(x -> {
            double[] points = new double[7];
            points[0] = x._2();
            points[1] = x._3();
            points[2] = x._4();
            points[3] = x._5();
            points[4] = x._6();
            points[5] = x._7();
            points[6] = x._8();

            return new LabeledPoint(x._1(), Vectors.dense(points));
        });

        // 进行预测
        JavaRDD<Tuple2<Double, Double>> predictionAndLabel = predictData.map(
                p -> new Tuple2<>(model.predict(p.features()), p.label())
        );

        // 回填预测结果
        List<DailyStatistics> result = new ArrayList<>();
        List<Tuple2<Double, Double>> predictions = predictionAndLabel.collect();
        for (int i = 0; i < predictions.size(); i++) {
            DailyStatistics item = dailyStatisticsList.get(i);
            item.setPredictNum((int) predictions.get(i)._1.doubleValue());
            result.add(item);
        }

        jsc.stop();

        return result;

    }

}
