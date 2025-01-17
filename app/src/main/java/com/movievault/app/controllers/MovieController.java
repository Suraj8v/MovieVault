package com.movievault.app.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movievault.app.dto.MovieDto;
import com.movievault.app.service.MovieServiceImpl;

@RestController
@RequestMapping("/api/movie")
public class MovieController {
	
	@Autowired
	private MovieServiceImpl movieService;
	
	@PostMapping("/add-movie")
	public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException
	{
		MovieDto dto = convertToMovieDTO(movieDto);
		
		return new ResponseEntity<>(movieService.addMovie(dto, file),HttpStatus.CREATED);
		
	}
	
	private MovieDto convertToMovieDTO(String movieDto) throws JsonMappingException, JsonProcessingException
	{
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(movieDto, MovieDto.class);
	}
	
	@GetMapping("/{movieId}")
	public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId)
	{
		return ResponseEntity.ok(movieService.getMovie(movieId));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<MovieDto>> getAllMoviesHandler()
	{
		System.out.println(movieService.getAllMovies());
		return ResponseEntity.ok(movieService.getAllMovies());
	}
	
	@PutMapping("/update/{movieId}")
	public ResponseEntity<MovieDto> updateMovieHandler(@PathVariable Integer movieId, @RequestPart MultipartFile file, 
			@RequestPart String movieDto) throws IOException
	{
		if(file.isEmpty()) {
			file=null;
		}
		MovieDto dto = convertToMovieDTO(movieDto);
		return new ResponseEntity<>(movieService.updateMovie(movieId, dto, file),HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{movieId}")
	public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException
	{
		return  ResponseEntity.ok(movieService.deleteMovie(movieId));
	}
}
