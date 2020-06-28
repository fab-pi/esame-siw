package it.uniroma3.siw.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Comments;
import it.uniroma3.siw.taskmanager.repository.CommentsRepository;

@Service
public class CommentsService {

	@Autowired
	CommentsRepository commentsRepository;
	
	@Transactional
	public Comments save(Comments comments) {
		return this.commentsRepository.save(comments);
	}
}
