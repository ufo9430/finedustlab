package com.finedustlab;

import com.finedustlab.service.SurveyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
public class finedustSurveyServiceTest {
    @Autowired
    SurveyService surveyService;

    @Test
    public void 응답_데이터_엑셀변환(){
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();
//        surveyService.exportDataToXls(mockResponse,"elementary");
    }
}
