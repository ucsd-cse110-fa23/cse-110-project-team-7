import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class SaveRecipeTest {

    private Recipe recipe;
    private Recipe recipe2;
    private ArrayList<Recipe> recipeList;

    @BeforeEach
    public void setUp(){

       recipe = new Recipe();
       recipeList = new ArrayList<>();
       recipe.setTitle("[Egg and Rice]");
       recipe.setIngredients("Egg, Rice");
       recipe.setInstructions("Instructions: ");

       recipe2 = new Recipe();
       recipe2.setTitle("[Beef and Rice]");
       recipe2.setIngredients("Beef and rice");
       recipe2.setInstructions("Instructions");

    }

    
    @Test
    public void testSavingCorrect(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 
            assertEquals(recipeList.get(0), recipe2);

            // saveARecipe

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveCSV(){
        try{

            recipeList = saveRecipe.saveARecipe(recipeList, recipe); 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 


            saveRecipe.saveToCSV(recipeList);
            String path = "./recipes.csv";
            assertTrue(isCSVFileNotEmpty(path));
                
            
            // saveARecipe

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSavingExistingRecipe(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe); 
            assertEquals(recipeList.get(0), recipe);
            assertEquals(recipeList.size(), 1);

            // saveARecipe

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static boolean isCSVFileNotEmpty(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the first line of the file
            String line = reader.readLine();

            // Check if the first line is not null and not empty
            return line != null && !line.trim().isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Assuming an IOException means the file is empty or does not exist
        }
    }

}