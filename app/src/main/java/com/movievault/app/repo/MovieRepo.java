package com.movievault.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movievault.app.entities.Movie;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Integer>{

}
