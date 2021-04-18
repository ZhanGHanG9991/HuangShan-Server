package cn.hfut.huangshan.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
public class PmmlUtil {
    /**
     * 载入PMML模型的方法
     *
     * @param pmmlFileName
     * @return
     * @throws JAXBException
     * @throws SAXException
     * @throws IOException
     */
    public static Evaluator loadEvaluator(String pmmlFileName) throws JAXBException, SAXException, IOException {
        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                .load(PmmlUtil.class.getResourceAsStream(pmmlFileName))
                .build();
        evaluator.verify(); //自校验——预热模型
        log.info("StayAlert分类评估器自校验&预热完成");
        return evaluator;
    }
}
