package it.uniroma3.siw.taskmanager.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.taskmanager.model.Comments;

public interface CommentsRepository extends CrudRepository<Comments, Long>{

}
