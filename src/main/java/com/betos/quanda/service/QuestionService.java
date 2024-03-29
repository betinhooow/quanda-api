package com.betos.quanda.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.betos.quanda.exception.ResourceNotFoundException;
import com.betos.quanda.model.Answer;
import com.betos.quanda.model.Question;
import com.betos.quanda.repository.AnswerRepository;
import com.betos.quanda.repository.QuestionRepository;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;

	public Page<Question> findAll(Pageable pageable){
		return questionRepository.findAll(pageable);
	}
	
	public Question findById(Long questionId){
		Question question = questionRepository.findById(questionId).orElseThrow(
                () -> new ResourceNotFoundException("Pergunta nao encontrada, id: " + questionId));
		
		List<Answer> ans = answerRepository.findByQuestionId(questionId);
		question.setAnswers(ans);
		return question;
	}
	
	public Question insert(Question question) {
		question.setId(null);
		return questionRepository.save(question);
	}
	
	public Question update(Long questionId, Question questionRequest) {
		return questionRepository.findById(questionId)
		.map(question -> {
			
			question.setTitle(questionRequest.getTitle());
			question.setDescription(questionRequest.getDescription());
			
			return questionRepository.save(question);
		}).orElseThrow(() -> new ResourceNotFoundException("Question not found with id" + questionId));	
	}
	
	public ResponseEntity<?> delete(Long questionId) {
		return questionRepository.findById(questionId)
				.map(question -> {
					questionRepository.delete(question);
					return ResponseEntity.ok().build();
				}).orElseThrow(() -> new ResourceNotFoundException("Question not found with id" + questionId));
	}
}



