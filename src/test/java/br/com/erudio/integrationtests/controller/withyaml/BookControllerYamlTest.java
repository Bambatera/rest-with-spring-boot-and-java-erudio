package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.configs.TestConfigs;
import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.BookVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper ymlMapper;

    private static BookVO books;

    @BeforeAll
    public static void setup() {
        ymlMapper = new YMLMapper();

        books = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException, JsonProcessingException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(credentials, ymlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, ymlMapper).getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/books/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var persistedBook = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(books, ymlMapper).when()
                .post().then().statusCode(200)
                .extract().body().as(BookVO.class, ymlMapper);

        books = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Barry Burd", persistedBook.getAuthor());
//        assertEquals(new Date(2022, 04, 22), persistedBook.getLaunchDate());
        assertEquals(18.00, persistedBook.getPrice());
        assertEquals("Java for Dummies", persistedBook.getTitle());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        books.setTitle("Java for Dummies, 8th Edition");

        var persistedBook = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(books, ymlMapper).when()
                .put().then().statusCode(200)
                .extract().body().as(BookVO.class, ymlMapper);

        books = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertEquals(books.getId(), persistedBook.getId());

        assertEquals("Barry Burd", persistedBook.getAuthor());
//        assertEquals(new Date(2022, 04, 22), persistedBook.getLaunchDate());
        assertEquals(18.00, persistedBook.getPrice());
        assertEquals("Java for Dummies, 8th Edition", persistedBook.getTitle());
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        mockBook();

        var persistedBook = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", books.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, ymlMapper);

        books = persistedBook;

        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getLaunchDate());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getTitle());

        assertEquals(books.getId(), persistedBook.getId());

        assertEquals("Barry Burd", persistedBook.getAuthor());
//        assertEquals(stringToDate("2022-04-22"), persistedBook.getLaunchDate());
        assertEquals(18.00, persistedBook.getPrice());
        assertEquals("Java for Dummies, 8th Edition", persistedBook.getTitle());
    }

    @Test
    @Order(4)
    public void testDelete() throws JsonMappingException, JsonProcessingException {
        given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", books.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO[].class, ymlMapper);

        List<BookVO> books = Arrays.asList(content);

        BookVO foundBookOne = books.get(0);

        assertNotNull(foundBookOne.getId());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getLaunchDate());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getTitle());

        assertEquals(1, foundBookOne.getId());

        assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
//        assertEquals(stringToDate("2017-11-29"), foundBookOne.getLaunchDate());
        assertEquals(49.00, foundBookOne.getPrice());
        assertEquals("Working effectively with legacy code", foundBookOne.getTitle());

        BookVO foundBookEight = books.get(8);

        assertNotNull(foundBookEight.getId());
        assertNotNull(foundBookEight.getAuthor());
        assertNotNull(foundBookEight.getLaunchDate());
        assertNotNull(foundBookEight.getPrice());
        assertNotNull(foundBookEight.getTitle());

        assertEquals(9, foundBookEight.getId());

        assertEquals("Brian Goetz e Tim Peierls", foundBookEight.getAuthor());
//        assertEquals(stringToDate("2017-11-07"), foundBookEight.getLaunchDate());
        assertEquals(80.00, foundBookEight.getPrice());
        assertEquals("Java Concurrency in Practice", foundBookEight.getTitle());
    }

    @Test
    @Order(6)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        var specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/books/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockBook() {
        books.setAuthor("Barry Burd");
        books.setLaunchDate(stringToDate("2022-04-22"));
        books.setPrice(18.00);
        books.setTitle("Java for Dummies");
    }

    private Date stringToDate(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(strDate);
        } catch (ParseException ex) {
            return null;
        }
    }
}
