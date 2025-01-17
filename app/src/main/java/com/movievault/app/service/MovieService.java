package com.movievault.app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.movievault.app.dto.MovieDto;

public interface MovieService {
	
	public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;
	
	public MovieDto getMovie(Integer movieId); 
	
	public MovieDto updateMovie(Integer movieId, MovieDto movieDto,MultipartFile file) throws IOException;
	
	public String deleteMovie(Integer movieId) throws IOException;
	
	public List<MovieDto> getAllMovies();

}
