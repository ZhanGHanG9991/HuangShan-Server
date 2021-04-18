package cn.hfut.huangshan.service.predict;

import cn.hfut.huangshan.pojo.DailyStatistics;
import cn.hfut.huangshan.utils.Json2MapUtil;
import cn.hfut.huangshan.utils.PmmlUtil;
import cn.hfut.huangshan.utils.StringUtil;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */

@Service
public class RandomForestPmmlPredictService {

    private Evaluator evaluator;
    private List<InputField> inputFields;

    public DailyStatistics predict(DailyStatistics dailyStatistics){
        try{
            evaluator = PmmlUtil.loadEvaluator("/model/rfr_pipeline.pmml");
            inputFields = evaluator.getInputFields();
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, ?> inputRecord = Json2MapUtil.objectToMap(dailyStatistics);
        System.out.println(inputRecord.toString());

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        // 从数据源模式到PMML模式逐字段映射记录
        for (InputField inputField : inputFields) {
            FieldName inputFieldName = inputField.getName();
            Object rawValue = inputRecord.get(StringUtil.fieldToAttribute(inputFieldName.getValue()));
            FieldValue inputFieldValue = inputField.prepare(rawValue);
            arguments.put(inputFieldName, inputFieldValue);
        }
        // 用已知的特征来评估模型
        Map<FieldName, ?> results = evaluator.evaluate(arguments);
        System.out.println(results);

        // 解耦结果来自jpmml-evaluator运行时环境
        Map<String, ?> resultRecord = EvaluatorUtil.decodeAll(results);

        dailyStatistics.setPredictNum((int) Double.parseDouble(resultRecord.get("today_total_num").toString()));

        return dailyStatistics;

    }


}
