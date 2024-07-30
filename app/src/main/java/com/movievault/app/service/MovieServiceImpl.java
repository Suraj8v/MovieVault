package com.movievault.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename())))
		{
			throw new RuntimeException("File already exists, please enter another filename");
		}
		String uploadedFileName = fileService.uploadFile(path, file);	
		
		//2. set the value of field poster as filename
		movieDto.setPoster(uploadedFileName);
		
		//3.map the dto to movie object
		Movie movie = new  Movie(null, movieDto.getTitle(), movieDto.getDirector(), 
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
		
		//check the data in DB if it exists then fetch it
		Movie movie = movieRepo.findById(movieId).orElseThrow(()->new RuntimeException("Movie not found!"));
		
		//generate poster url
		String posterUrl =  baseUrl + "/file/" + movie.getPoster();
		
		//map the movieDto object and return it
		MovieDto response = new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(),
				movie.getStudio(), movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(),posterUrl);
		
		
		return response;
	}

	@Override
	public List<MovieDto> getAllMovies() {
		// 1.fetch all the data from DB
		List<Movie> movies =	movieRepo.findAll();
		
		List<MovieDto> moviesDto = new ArrayList<MovieDto>();
		
		//2.iterate through all movies to generate posterurl for each movie and map the movieDto object
		for(Movie movie : movies)
		{
			String posterUrl =  baseUrl + "/file/" + movie.getPoster();
			
			MovieDto response = new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(),
					movie.getStudio(), movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(),posterUrl);
			
			moviesDto.add(response);
		}
		
		return moviesDto ;
	}


	@Override
	public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
		//1. check if movie exists in our DB or not
		Movie mv = movieRepo.findById(movieId).orElseThrow(()->new RuntimeException("Movie not found!"));
		
		//2.if file is null do nothing
		//if the file is not null then delete the file which is associated with old record and replace it with updated new file
		
		String fileName = mv.getPoster();
		if(file!=null)
		{
			Files.deleteIfExists(Paths.get(path+File.separator+fileName));
			fileName = fileService.uploadFile(path, file);
		}
		
		//3. set movieDto's poster value
		movieDto.setPoster(fileName);
		
		//4. map it to movie object
		Movie movie = new Movie(mv.getMovieId(), movieDto.getTitle(), movieDto.getDirector(), 
				movieDto.getStudio(), movieDto.getMovieCast(),
				movieDto.getReleaseYear(),
				movieDto.getPoster());
		
		//5.save the movie object
		movieRepo.save(movie);
		
		//6.generate poster url
		String posterUrl = baseUrl+"/file/"+fileName;
		
		//7.map to moviedto and return it
		MovieDto response = new MovieDto(movie.getMovieId(), movie.getTitle(), movie.getDirector(),
				movie.getStudio(), movie.getMovieCast(), movie.getReleaseYear(), movie.getPoster(),posterUrl);;
		
		return response;
	}


	@Override
	public String deleteMovie(Integer movieId) throws IOException {
		//1. check if movie exists in Db
		
		Movie mv = movieRepo.findById(movieId).orElseThrow(()->new RuntimeException("Movie not found!"));

		//2. delete the file associated with it
		Files.deleteIfExists(Paths.get(path+File.separator+mv.getPoster()));
		
		//3. delete the movie
		movieRepo.delete(mv);
		return "Movie deleted with ID : "+movieId;
	}

}
