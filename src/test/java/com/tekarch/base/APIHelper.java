package com.tekarch.base;

import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpStatus;
import org.techArk.requestPOJO.AddDataRequest;
import org.techArk.requestPOJO.CreateNewRepoRequest;
import org.techArk.requestPOJO.DeleteDataRequest;
import org.techArk.requestPOJO.LoginRequest;
import org.techArk.requestPOJO.UpdateDataRequest;
import org.techArk.responsePOJO.LoginResponse;
import org.techArk.utils.EnvironmentDetails;
import org.testng.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APIHelper {
    RequestSpecification reqSpec;
    String token = "";

    public APIHelper() {
        RestAssured.baseURI = EnvironmentDetails.getProperty("baseURL");
        reqSpec = RestAssured.given();
       
    }

    public void login(String authToken) {
    	this.token = authToken;
    }

    public Response getData() {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            response = reqSpec.get("/getdata");
            response.then().log().headers();
        } catch (Exception e) {
            Assert.fail("Get data is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response getSingleRepository(String repositoryPath) {
        reqSpec = RestAssured.given();
        Response response = null;
        try {
            reqSpec.headers(getHeaders(false));            
            response = reqSpec.when().get(repositoryPath);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("getSingleRepository functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    public Response createNewRepo(CreateNewRepoRequest createNewRepoRequest) {
        reqSpec = RestAssured.given();
        Response response = null;
        try {
            log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(createNewRepoRequest));
            reqSpec.headers(getHeaders(false));
            reqSpec.body(new ObjectMapper().writeValueAsString(createNewRepoRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.when().post("/user/repos");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("createNewRepo functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    public Response updateRepo(CreateNewRepoRequest createNewRepoRequest) {
        reqSpec = RestAssured.given();
        Response response = null;
        try {
            log.info("Adding below data :: " + new ObjectMapper().writeValueAsString(createNewRepoRequest));
            reqSpec.headers(getHeaders(false));
            reqSpec.body(new ObjectMapper().writeValueAsString(createNewRepoRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.when().patch("/repos/SugunaChandran/Hello-World");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("updateRepo functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }
    
    public Response deleteRepo(String repoPath) {
        reqSpec = RestAssured.given();
        Response response = null;
        try {
            reqSpec.headers(getHeaders(false));
            response = reqSpec.when().delete(repoPath);
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("deleteRepo functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response putData(UpdateDataRequest updateDataRequest) {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(updateDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.when().put("/updateData");
            response.then().log().all();
        } catch (Exception e) {
            Assert.fail("Update data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public Response deleteData(DeleteDataRequest deleteDataRequest) {
        reqSpec = RestAssured.given();
        reqSpec.headers(getHeaders(false));
        Response response = null;
        try {
            reqSpec.body(new ObjectMapper().writeValueAsString(deleteDataRequest)); //Serializing addData Request POJO classes to byte stream
            response = reqSpec.when().delete("/deleteData");
            response.then().log().body();
        } catch (Exception e) {
            Assert.fail("Delete data functionality is failing due to :: " + e.getMessage());
        }
        return response;
    }

    public HashMap<String, String> getHeaders(boolean forLogin) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        if (!forLogin) {
            headers.put("Authorization", "Bearer " + token);
        }
        return headers;
    }

}
