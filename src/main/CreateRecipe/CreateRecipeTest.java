package main.CreateRecipe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;
import main.ListView.Recipe;
import main.CreateRecipe.*;


public class CreateRecipeTest {

    private Recipe recipe = new Recipe();

    @BeforeClass
    public static void setUp() throws Exception {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {
            // Optional initialization code, if needed
        });
    }

    @Test
    public void testValidMealType() {
        try{
            AppFrame appFrame = new AppFrame();

            // Test with valid meal types
            assertTrue(appFrame.checkMealType("breakfast"));
            assertTrue(appFrame.checkMealType("lunch"));
            assertTrue(appFrame.checkMealType("dinner"));

            assertTrue(appFrame.checkMealType("Breakfast"));
            assertTrue(appFrame.checkMealType("Lunch"));
            assertTrue(appFrame.checkMealType("Dinner"));


            assertTrue(appFrame.checkMealType("Breakfast."));
            assertTrue(appFrame.checkMealType("Lunch."));
            assertTrue(appFrame.checkMealType("Dinner."));

            assertTrue(appFrame.checkMealType("BreAkfAst."));
            assertTrue(appFrame.checkMealType("LUNCH!"));
            assertTrue(appFrame.checkMealType("DinNER?"));
        }
        catch(Exception e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidMealType() {
        try{
            AppFrame appFrame = new AppFrame();

            // Test with invalid meal types
            assertFalse(appFrame.checkMealType("break"));
            assertFalse(appFrame.checkMealType("test"));
            assertFalse(appFrame.checkMealType("mmmm"));
        }
        catch(Exception e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }


    
}
