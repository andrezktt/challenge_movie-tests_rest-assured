package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ScoreControllerRA {

	private String validUsername;
	private String validPassword;
	private String validToken;

	private String invalidToken;

	private Map<String, Object> saveScoreInstance;

	@BeforeEach
	public void setUp() throws Exception {
		validUsername = "alex@gmail.com";
		validPassword = "123456";
		validToken = TokenUtil.obtainAccessToken(validUsername, validPassword);

		invalidToken = validToken + "000";

		saveScoreInstance = new HashMap<>();
		saveScoreInstance.put("movieId", 1);
		saveScoreInstance.put("score", 4);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		saveScoreInstance.put("movieId", 1000L);
		JSONObject newScore = new JSONObject(saveScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + validToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		saveScoreInstance.put("movieId", "");
		JSONObject newScore = new JSONObject(saveScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + validToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		saveScoreInstance.put("score", -5);
		JSONObject newScore = new JSONObject(saveScoreInstance);

		given()
				.header("Content-type", "application/json")
				.header("Authorization", "Bearer " + validToken)
				.body(newScore)
				.contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.when()
				.put("/scores")
				.then()
				.statusCode(422);
	}
}
