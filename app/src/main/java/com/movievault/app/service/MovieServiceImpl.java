package com.movievault.app.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movievault.app.dto.MovieDto;
import com.movievault.app.entities.Movie;
import com.movievault.app.repo.MovieRepo;
import org.springframework.beans.factory.annotation.*;

@Service
public class MovieServiceImpl implements MovieService {
	
	@Autowired
	private MovieRepo movieRepo;
	
	@Autowired
	private FileServiceImpl fileService;
	
	@Value("${project.poster}")
	private String path;

	@Value("${base.url}")
	private String baseUrl;

	@Override
	public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
		
		//1.upload the file
		String uploadedFileName = fileService.uploadFile(path, file);	
		
		//2. set the value of field poster as filename
		movieDto.setPoster(uploadedFileName);
		
		//3.map the dto to movie object
		Movie movie = new  Movie(movieDto.getMovieId(), movieDto.getTitle(), movieDto.getDirector(), 
				movieDto.getStudio(), movieDto.getMovieCast(),
				movieDto.getReleaseYear(),
				movieDto.getPoster());
		
		//4. save movie
		Movie savedMovie = movieRepo.save(movie);
		
		//5 generate the poster url
		String posterUrl = baseUrl + "/file/" + uploadedFileName;
		
		//6. map movie object to moviedto object and return it
		MovieDto response = new MovieDto(savedMovie.getMovieId(), savedMovie.getTitle(), savedMovie.getDirector(),
				savedMovie.getStudio(), savedMovie.getMovieCast(), savedMovie.getReleaseYear(), savedMovie.getPoster(),posterUrl);
		
		return response;
		
	}

	@Override
	public MovieDto getMovie(Integer movieId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MovieDto> getAllMovies() {
		// TODO Auto-generated method stub
		return null;
	}

}
