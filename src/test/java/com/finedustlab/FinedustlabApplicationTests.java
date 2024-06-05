package com.finedustlab;

import com.finedustlab.controller.SurveyController;
import com.finedustlab.model.survey.SurveyAnswer;
import com.finedustlab.model.survey.SurveyInputWrapper;
import com.finedustlab.model.survey.SurveySubQuestion;
import com.finedustlab.model.user.StudentProfile;
import com.finedustlab.model.user.TeacherProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class FinedustlabApplicationTests {
	@Autowired SurveyController surveyController;

	void makeTestSurvey() {
		try{
			StudentProfile teacherProfile = new StudentProfile();
			teacherProfile.setName("인현민");
			teacherProfile.setGrade(2);
			teacherProfile.setSchool_code(7091407);
			teacherProfile.setUser_type("elementary");
			teacherProfile.setClass_num(12);
			teacherProfile.setStudent_num(0);
			for(int i=0;i<15;i++){
				SurveyAnswer surveyAnswer = new SurveyAnswer();
				List<SurveySubQuestion> answers = new ArrayList<SurveySubQuestion>();
				for(int j=0;j<3;j++){
					SurveySubQuestion question = new SurveySubQuestion();
					question.setSub_question_id(j+1);
					question.setSub_question_answer("답변");
					question.setSub_question_input("");
					question.setType("choice");
					answers.add(question);
				}
				surveyAnswer.setAnswers(answers);
				surveyAnswer.setDate("2024-06-05T10:00:00Z");
				surveyAnswer.setQuestion_id(i+1);
				SurveyInputWrapper wrapper = new SurveyInputWrapper();
				wrapper.setSurvey_data(surveyAnswer);
				wrapper.setUser(teacherProfile);
				surveyController.setSurveyAnswer(wrapper);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
