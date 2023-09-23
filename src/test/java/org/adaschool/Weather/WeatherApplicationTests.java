package org.adaschool.Weather;

import org.adaschool.Weather.controller.WeatherReportController;
import org.adaschool.Weather.data.WeatherApiResponse;
import org.adaschool.Weather.data.WeatherReport;
import org.adaschool.Weather.service.WeatherReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
class WeatherApplicationTests {

	@Autowired
	private WeatherReportController weatherReportController;

	@MockBean
	private WeatherReportService weatherReportService;


	private WeatherReport weatherReport;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = standaloneSetup(weatherReportController).build();
		weatherReport = new WeatherReport();
	}

	@Test
	public void getWeather() throws Exception {
		Double latitude = 44.34;
		Double longitude = 10.99;
		weatherReport.setHumidity(95.0);
		weatherReport.setTemperature(32.0);

		when(weatherReportService.getWeatherReport(latitude, longitude))
				.thenReturn(weatherReport);

		mockMvc.perform(get("/v1/api/weather-report")
				.param("latitude", latitude.toString())
				.param("longitude", longitude.toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.humidity", is(95.0)))
				.andExpect(jsonPath("$.temperature", is(32.0)));

		verify(weatherReportService, times(1)).getWeatherReport(latitude,longitude);
	}



}
