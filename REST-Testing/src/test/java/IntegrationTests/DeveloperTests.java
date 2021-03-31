package IntegrationTests;

import models.Developer;
import java.io.IOException;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;


public class DeveloperTests {

    Developer developer;
    Developer developerToDelete;
    String baseURL = "https://tech-services-1000201953.uc.r.appspot.com/";

    @BeforeSuite
    public void setup(){
        developer = new Developer("Shivani","Senguttuvan","Java",2015);//update me
        
        developerToDelete = new Developer("toDelete","toDelete","toDelete",1);
        
        
    }

    @Test
    public void postDeveloper_postsDeveloper() throws IOException {
        // create a client
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(developer)
                .when()
                .post(baseURL+"developer")
                .then()
                .extract().response();

        developer = response.getBody().as(Developer.class);
        assertEquals(response.statusCode(),200);
        assertEquals(developer.getFirstName(),"Shivani");//update me
        assertNotNull(developer.getId());

    }
    
    @Test
    public void getAllDevelopers_returnsAnArrayOfDevelopers() throws IOException {
        // create a client
        Response response =  given()
                .when()
                .get(baseURL+"developers")
                .then()
                .extract().response();

        Developer[] arrayOfDevelopers = response.getBody().as(Developer[].class);
        assertEquals(response.statusCode(),200);
        
        assertNotNull(arrayOfDevelopers);
        assertEquals(arrayOfDevelopers.getClass().isArray(), true);
        for(int i = 0; i < arrayOfDevelopers.length; i++) {
        	assertNotNull(arrayOfDevelopers[i].getId());
        }

    }
    
    @Test
    public void getDeveloperById_returnsExpectedDEveloper() throws IOException {
        // create a client

        Response response =  given()
                .when()
                .get(baseURL+"developer/6064a29639e6f0000a54da4b")
                .then()
                .extract().response();

        developer = response.getBody().as(Developer.class);
        assertEquals(response.statusCode(),200);
        assertEquals(developer.getFirstName(),"Shivani");//update me
        assertEquals(developer.getFavoriteLanguage(), "Java");

    }
    
    @Test
    public void putEndpoint_updatesADeveloper() throws IOException {
        // create a client
    	
    	developer.setYearStarted(2020);
    	
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(developer)
                .when()
                .put(baseURL+"developer")
                .then()
                .extract().response();

        developer = response.getBody().as(Developer.class);
        assertEquals(response.statusCode(),200);
        assertEquals(developer.getFirstName(),"Shivani");//update me
        assertEquals(developer.getYearStarted(), 2020);
        
    }
    
    @Test
    public void deleteEndpoint_deletesTheDeveloper() throws IOException {
        // create a client
    	    	
        Response response =  given()
                .header("Content-type", "application/json")
                .and()
                .body(developerToDelete)
                .when()
                .post(baseURL+"developer")
                .then()
                .extract().response();
        
        developerToDelete = response.getBody().as(Developer.class);
        String id = developerToDelete.getId();
    	    	
        Response responseForDeletion =  given()
                .when()
                .delete(baseURL+"developer/" + id)
                .then()
                .extract().response();

        assertEquals(responseForDeletion.statusCode(),200);
        
        Response verifyDeletion =  given()
                .when()
                .get(baseURL+"developer/" + id)
                .then()
                .extract().response();
        
        assertEquals(verifyDeletion.getBody().asString(), "");
        
        
        

    }
    
 
    
    
    

}
