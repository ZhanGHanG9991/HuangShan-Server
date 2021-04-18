package cn.hfut.huangshan.utils;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PmmlUtilTest {

    private Evaluator evaluator;
    private List<InputField> inputFields;

    @Test
    void loadEvaluatorTest() throws IOException, JAXBException, SAXException {
        evaluator = PmmlUtil.loadEvaluator("/model/rfr_pipeline.pmml");
        inputFields = evaluator.getInputFields();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();
            System.out.println(inputName.getValue().equals("date_week"));
        }
    }


}