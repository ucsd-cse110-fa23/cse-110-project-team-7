import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Updates.pull;
import com.sun.net.httpserver.HttpExchange;

import org.mockito.ArgumentMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

public class IntegrationTest {

    private Recipe recipe;
    private Recipe recipe2;
    private ArrayList<Recipe> recipeList;
    @Mock
    private MongoDatabase mockDatabase;

    @Mock
    private MongoCollection<Document> mockCollection;

    @Mock
    private FindIterable<Document> mockFindIterable;

    @Mock
    private MongoCursor<Document> mockCursor;

    @InjectMocks
    private saveAccount saveAccount;

    @Mock
    private saveAccount saveAccount2;

    @InjectMocks
    private URLHandler urlHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);
        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any()))
                .thenReturn(mockFindIterable);
        saveAccount = new saveAccount(mockDatabase, mockCollection);

        urlHandler = new URLHandler(new HashMap<>(), saveAccount);

    }

    @Test
    public void MS1_SystemBasedTest() {
        // You open the app and click on the create recipe button. You start to input
        // your meal type preference. (Covered in iteration 1 – User Story 1, 3)
        recipe = new Recipe();
        recipe2 = new Recipe();
        recipe.setMealType("Breakfast");
        // You realized you said the wrong meal type so you click the back button on the
        // page and it takes you back to the List view page (User Story 7)
        // You repeat the process of clicking the Create Recipe button on the List view
        // page and it opens the Create Recipe page and press the start button to
        // rerecording desired meal type and stop button to finish inputting. Meal type
        // will appear on screen. (Covered in iteration 1 – User Story 3)
        recipe.setMealType("Lunch");
        // After ingredients are listed, the window switches over to a detailed view
        // where the entire recipe is displayed. On this page you can either edit, save,
        // delete, or back out. (Covered in iteration 1 – User Story 2)
        String input = "[Curried Egg Sandwich]" + "\n" + "Ingredients: 2 eggs " + "\n" + "Instructions: " + "\n"
                + "Steps";
        recipe.setIngredients("Egg");
        recipe.setTitle(input);
        recipe.setInstructions(input);
        // Click the save button, the detailed instruction page closes and the list page
        // is displayed. The new recipe will be saved on the top of the list page. (User
        // Story 4)
        recipe2.setTitle("[Beef and Rice]");
        recipe2.setIngredients("Beef and rice");
        recipe2.setInstructions("Instructions");
        recipeList = new ArrayList<>();
        try {
            recipeList = saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2);
            assertEquals(recipeList.get(0), recipe2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Now you can scroll down the list of recipes on the list page, then you can
        // see your recipes in chronological order. (User Story 8)
        assertEquals(recipeList.get(0), recipe2);
        assertEquals(recipeList.get(1), recipe);
        // You click that saved recipe to edit instructions in that recipe, then you
        // will see detailed pages on your screen. (Covered in iteration 1 – User Story
        // 2)
        // When clicking the edit button, you will have a blinking text cursor displayed
        // which indicates the location you are modifying. (User Story 5)
        // After changes, you find it not satisfying so you press the back button. The
        // changes are not saved and you are back to the list page. (User Story 7)
        // Then you repeat the steps and click on the saved recipe again to see the
        // detailed view. (Covered in iteration 1 – User Story 2)

        // Then you click the edit button and edit the page (User Story 5)
        recipe.setInstructions("Step 10000000000 Ricky loves eggs");
        // After making changes, you save it and now the changes appear in the recipe.
        // (User Story 4)
        try {
            recipeList = saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(1).getInstructions(), "Step 10000000000 Ricky loves eggs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // After following the recipe you found out that it is not good so you decided
        // to find that recipe and press the delete button. Then the recipe is removed
        // from the list and its detailed view page will also terminate itself, sending
        // you back to the list view page. (User Story 6)
        DeleteRecipe.deleteTargetRecipe(recipeList, recipe2);
        assertEquals(recipeList.size(), 1);
        assertEquals(recipeList.get(0), recipe);

    }

    @Test
    public void MS2_Iteration1_SystemBasedTest() throws IOException {

        // Start the app and input username and password. You can click on the sign up
        // button to create an account (User Story 3).
        String username = "testUsername";
        String password = "testPassword";
        List<Document> mockRecipes = Arrays.asList();
        when(mockCollection.insertOne(any())).thenReturn(null);

        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any()))
                .thenReturn(mockFindIterable);

        boolean result = saveAccount.generateNewAccount(username, password);

        assertTrue(result);

        when(mockCollection.find(any(Bson.class), ArgumentMatchers.<Class<Document>>any()))
                .thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(new Document());

        assertTrue(saveAccount.accountExist(username));

        // You will see the Recipe list view and it should be an empty list because it
        // is a new account (User Story 3).
        Document mockUser = new Document("_id", username).append("recipes", mockRecipes);
        when(mockCollection.find(eq("_id", username))).thenReturn(Mockito.mock(FindIterable.class));
        when(mockCollection.find(eq("_id", username)).first()).thenReturn(mockUser);
        ArrayList<Recipe> emptyList = saveAccount.readDatabase(username);
        assertEquals(0, emptyList.size());

        // You can create a new recipe by clicking on the create recipe button and input
        // meal type and ingredients then create the recipe.
        // The recipe will have the meal type on the title (MS1 User Story 3 and User
        // Story 2) .
        mockRecipes = Arrays.asList(new Document("Title", "Recipe1").append("Ingredients", "Ingredients1")
                .append("Instructions", "Instructions1")
                .append("Image", "Image1").append("Meal Type", "Meal Type1"));
        mockUser = new Document("_id", username).append("recipes", mockRecipes);
        when(mockCollection.find(eq("_id", username))).thenReturn(Mockito.mock(FindIterable.class));
        when(mockCollection.find(eq("_id", username)).first()).thenReturn(mockUser);
        ArrayList<Recipe> result2 = saveAccount.readDatabase(username);

        // Assert
        assertEquals(1, result2.size());

        Recipe recipe1 = result2.get(0);
        assertEquals("Recipe1", recipe1.getTitle());
        assertEquals("Ingredients1", recipe1.getIngredients());
        assertEquals("Instructions1", recipe1.getInstructions());
        assertEquals("Image1", recipe1.getImageUrl());
        assertEquals("Meal Type1", recipe1.getMealType());

        // Then you can save the recipe and it will be saved on the recipe list. (User
        // Story 4)
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);

        when(mockFindIterable.first()).thenReturn(new Document("_id", username).append("recipes", mockRecipes));
        Recipe recipe = saveAccount.findRecipe(username, "Recipe1");

        assertNotNull(result, "Expected a non-null result");
        assertEquals("Recipe1", recipe.getTitle());

        // You can create another recipe and repeat the same steps to save it on the
        // recipe list.
        // Your recipes will automatically be sorted by the newest recipe created first
        // (User Story 5).
        Recipe r1 = new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type1");
        ArrayList<Recipe> recipes = saveRecipe.saveARecipe(new ArrayList<>(), r1);
        Recipe r2 = new Recipe("Recipe2", "Ingredients2", "Instructions2", "Image2", "Meal Type2");
        saveRecipe.saveARecipe(recipes, r2);

        assertEquals(recipes.get(0), r2);

        // Then you can sort your recipes by clicking the sort dropdown and choose how
        // you would want to sort your recipes. You can click on alphabetically and the
        // list will sort alphabetically (User Story 5).
        recipes = Sort.alphabeticalAZSort(recipes);
        ArrayList<Recipe> expected1 = new ArrayList<Recipe>();
        expected1.add(r1);
        expected1.add(r2);
        assertEquals(recipes, expected1);

        recipes = Sort.alphabeticalZASort(recipes);
        ArrayList<Recipe> expected2 = new ArrayList<Recipe>();
        expected2.add(r2);
        expected2.add(r1);
        assertEquals(recipes, expected2);

        recipes = Sort.oldestToNewestSort(recipes);
        ArrayList<Recipe> expected3 = new ArrayList<Recipe>();
        expected3.add(r1);
        expected3.add(r2);
        assertEquals(recipes, expected3);
        // Then you can filter your recipes by clicking on the filter dropdown and
        // clicking on a desired meal type to filter on. You can click on breakfast and
        // only the recipes that are of breakfast meal type can be shown (User Story 6).
        ArrayList<Recipe> breakfast = Filter.filterRecipes(recipes, "Breakfast");
        ArrayList<Recipe> expected4 = new ArrayList<Recipe>();
        assertEquals(breakfast, expected4);
    }

    @Test
    public void MS2_Iteration2_SystemBasedTest() throws IOException {
        // Start the app and you will be automatically logged in (User Story 11)

        String username = "testUser";
        String recipeTitle = "Recipe1";

        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        // You can create a new recipe by clicking on the create recipe button and input
        // meal type and ingredients then create the recipe. When you create the recipe,
        // a image of the meal should show up (User Story 7)
        List<Document> mockRecipes = Arrays.asList(
                new Document("Title", "Recipe1").append("Ingredients", "Ingredients1").append("Instructions",
                        "Instructions1"),
                new Document("Title", "Recipe2").append("Ingredients", "Ingredients2").append("Instructions",
                        "Instructions2"));
        when(mockFindIterable.first()).thenReturn(new Document("_id", username).append("recipes", mockRecipes));

        URI uri = URI.create("/recipe?=" + username + "_" + recipeTitle);
        HttpExchange httpExchange = mock(HttpExchange.class);
        when(httpExchange.getRequestMethod()).thenReturn("GET");
        when(httpExchange.getRequestURI()).thenReturn(uri);

        Recipe mockRecipe = new Recipe("Recipe1", "Ingredients1", "Instructions1", "Image1", "Meal Type 1");
        when(saveAccount2.findRecipe(username, recipeTitle)).thenReturn(mockRecipe);

        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);

        urlHandler.handle(httpExchange);
        String expectedResponse = "<html><body><h1>Recipe1</h1><img src=\"null\"> <br>" +
                "<p>Inputted Ingredients: Ingredients1</p> <br>" +
                "<p>Instructions1</p></body></html>";

        assertEquals(expectedResponse, responseOutputStream.toString());
        // When you click on the refresh button, a new recipe will be created with the
        // same inputs and will be displayed. (User Story 8)
        // You can click the share button and a url to the recipe will pop up that you
        // can copy to share with someone else. (User story 10)
        // When you encounter a server error then there will be a page that pops up
        // saying that we have a server error and you will exit the app (User story 12)
        // Then you can try to open the app on a different device and the application
        // should be able to be logged in with your username and password (User story 9)
    }

}
