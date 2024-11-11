package org.techArk.tests.CRUD;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.techArk.listeners.TestEventListenersUtility;
import org.techArk.requestPOJO.CreateNewRepoRequest;
import org.techArk.responsePOJO.CreateDuplicateRepoResponse;
import org.techArk.responsePOJO.CreateNewRepoResponse;
import org.techArk.responsePOJO.GetSingleRepositoryResponse;
import org.techArk.utils.EnvironmentDetails;
import org.techArk.utils.JsonSchemaValidate;
import org.techArk.utils.TestDataUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;
import com.tekarch.base.APIHelper;
import com.tekarch.base.BaseTest;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
@Listeners(TestEventListenersUtility.class)
		
public class ValidateAddAndDeleteData_Functionality extends BaseTest {
    String userId, accountNo, departmentNo, salary, pincode;
    APIHelper apiHelper;
    private Faker faker;
    String dataId = "";

    @BeforeClass
    public void beforeClass() {
        faker = new Faker();
        apiHelper = new APIHelper();
        apiHelper.login(EnvironmentDetails.getProperty("authToken"));        
    }

    @Test(priority = 0, description = "validate Get a single repository functionality")
    public void validateGetSingleRepositoryFunctionality() {        
        Response response = apiHelper.getSingleRepository("/repos/SugunaChandran/SalesForceTestngPOM");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "GetSingleRepository functionality is not working as expected.");
        Assert.assertEquals(response.as(GetSingleRepositoryResponse.class).getFullName(), TestDataUtils.getProperty("repositoryFullName"), "The value of status key is not as expected in response ");
        Assert.assertEquals(response.as(GetSingleRepositoryResponse.class).getDefaultBranch(), TestDataUtils.getProperty("defaultBranch"), "The value of default branch key is not as expected in response ");
        Assert.assertEquals(response.contentType(), TestDataUtils.getProperty("contentType"), "The value of content type key is not as expected in response ");
        JsonSchemaValidate.validateSchema(response.asPrettyString(), "GetSingleRepositoryResponseSchema.json");

    }
    
    @Test(priority = 0, description = "validate Get single Non Existing repository functionality")
    public void validateGetNonExistingSingleRepositoryFunctionality() {        
        Response response = apiHelper.getSingleRepository("/repos/SugunaChandran/SalesForceTestngPOM1");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "GetSingleNonExistingRepository functionality is not working as expected.");
        Assert.assertEquals(response.as(GetSingleRepositoryResponse.class).getMessage(), TestDataUtils.getProperty("message"), "The value of message key is not as expected in response ");
        JsonSchemaValidate.validateSchema(response.asPrettyString(), "GetSingleRepositoryResponseSchema.json");
    }
    
    @Test(priority = 0, description = "validate Get All repositories functionality")
    public void validateGetAllRepositoriesFunctionality() {        
        Response response = apiHelper.getSingleRepository("/user/repos");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "GetSingleNonExistingRepository functionality is not working as expected.");
        Assert.assertEquals(response.contentType(), TestDataUtils.getProperty("contentType"), "The value of content type key is not as expected in response ");
        List<GetSingleRepositoryResponse> getAllRepositoriesResponse = response.getBody().as(new TypeRef<List<GetSingleRepositoryResponse>>() {
        });
        Assert.assertEquals(getAllRepositoriesResponse.size(), 2, "GetAllRepositoriesFunctionality functionality is not working as expected.");
        
        List<String> publicRepos = getAllRepositoriesResponse.stream()
        .filter(entry -> "public".equalsIgnoreCase(entry.getVisibility()))
        .map(GetSingleRepositoryResponse :: getFullName).collect(Collectors.toList());
        
        Assert.assertEquals(publicRepos.size(), 2, "GetAllRepositoriesFunctionality functionality is not working as expected.");
        
        //JsonSchemaValidate.validateSchema(response.asPrettyString(), "GetAllRepositoriesResponseSchema.json");
    }
    
    @Test(priority = 0, description = "validate Create New Repo functionality")
    public void validateCreateNewRepoFunctionality() {        
        CreateNewRepoRequest createNewRepoRequest = CreateNewRepoRequest.builder().name(TestDataUtils.getProperty("createNewRepoName")).description(TestDataUtils.getProperty("createNewRepoDescription")).homepage(TestDataUtils.getProperty("createNewRepoHomePage")).build();
        Response response = apiHelper.createNewRepo(createNewRepoRequest);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "CreateNewRepo functionality is not working as expected.");
        Assert.assertEquals(response.as(CreateNewRepoResponse.class).getName(), TestDataUtils.getProperty("createNewRepoName"), "The value of name key is not as expected in response ");
        Assert.assertEquals(response.as(CreateNewRepoResponse.class).getOwner().getLogin(), TestDataUtils.getProperty("gitHubLogin"), "The value of Login key is not as expected in response ");
        Assert.assertEquals(response.as(CreateNewRepoResponse.class).getOwner().getType(), TestDataUtils.getProperty("gitHubRepoOwnerType"), "The value of type key is not as expected in response ");
        JsonSchemaValidate.validateSchema(response.asPrettyString(), "CreateNewRepositoryResponseSchema.json");

    }
    
    @Test(priority = 0, description = "validate Create New Repo functionality")
    public void validateCreateDuplicateRepoFunctionality() {        
        CreateNewRepoRequest createNewRepoRequest = CreateNewRepoRequest.builder().name(TestDataUtils.getProperty("createNewRepoName")).description(TestDataUtils.getProperty("createNewRepoDescription")).homepage(TestDataUtils.getProperty("createNewRepoHomePage")).build();
        Response response = apiHelper.createNewRepo(createNewRepoRequest);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY, "CreateDuplicateRepo functionality is not working as expected.");
        Assert.assertEquals(response.as(CreateDuplicateRepoResponse.class).getErrors().get(0).getMessage(), TestDataUtils.getProperty("duplicateRepoError"), "The value of duplicateRepoError key is not as expected in response ");
        //JsonSchemaValidate.validateSchema(response.asPrettyString(), "CreateNewRepositoryResponseSchema.json");

    }
    
    @Test(priority = 0, description = "validate Update Repo functionality")
    public void validateUpdateRepoFunctionality() {        
        CreateNewRepoRequest createNewRepoRequest = CreateNewRepoRequest.builder().name(TestDataUtils.getProperty("updateNewRepoName")).description(TestDataUtils.getProperty("updateRepoDescription")).homepage(TestDataUtils.getProperty("createNewRepoHomePage")).build();
        Response response = apiHelper.updateRepo(createNewRepoRequest);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "UpdateRepo functionality is not working as expected.");
        Assert.assertEquals(response.as(CreateNewRepoResponse.class).getName(), TestDataUtils.getProperty("updateNewRepoName"), "The value of duplicateRepoError key is not as expected in response ");
        //JsonSchemaValidate.validateSchema(response.asPrettyString(), "CreateNewRepositoryResponseSchema.json");

    }
    
    @Test(priority = 0, description = "validate Delete Repo functionality")
    public void validateDeleteRepoFunctionality() {        
        Response response = apiHelper.deleteRepo(TestDataUtils.getProperty("deleteRepoPath"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT, "DeleteRepo functionality is not working as expected.");
        //Assert.assertEquals(response.as(CreateNewRepoResponse.class).getName(), TestDataUtils.getProperty("updateNewRepoName"), "The value of duplicateRepoError key is not as expected in response ");
        //JsonSchemaValidate.validateSchema(response.asPrettyString(), "CreateNewRepositoryResponseSchema.json");

    }
    
    @Test(priority = 0, description = "validate Delete Non Repo functionality")
    public void validateDeleteNonRepoFunctionality() {        
        Response response = apiHelper.deleteRepo(TestDataUtils.getProperty("deleteNonRepoPath"));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_NOT_FOUND, "DeleteNonRepo functionality is not working as expected.");
        //Assert.assertEquals(response.as(CreateNewRepoResponse.class).getName(), TestDataUtils.getProperty("updateNewRepoName"), "The value of duplicateRepoError key is not as expected in response ");
        //JsonSchemaValidate.validateSchema(response.asPrettyString(), "CreateNewRepositoryResponseSchema.json");

    }

}
