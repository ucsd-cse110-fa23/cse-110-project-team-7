
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class CreateRecipeTest {

    private Recipe recipe = new Recipe();

    @Test
    public void testValidMealType() {
        try{

            // Test with valid meal types
            assertTrue(recipe.checkMealType("breakfast"));
            assertTrue(recipe.checkMealType("lunch"));
            assertTrue(recipe.checkMealType("dinner"));

            assertTrue(recipe.checkMealType("Breakfast"));
            assertTrue(recipe.checkMealType("Lunch"));
            assertTrue(recipe.checkMealType("Dinner"));


            assertTrue(recipe.checkMealType("Breakfast."));
            assertTrue(recipe.checkMealType("Lunch."));
            assertTrue(recipe.checkMealType("Dinner."));

            assertTrue(recipe.checkMealType("BreAkfAst."));
            assertTrue(recipe.checkMealType("LUNCH!"));
            assertTrue(recipe.checkMealType("DinNER?"));
        }
        catch(Exception e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidMealType() {
        try{
            // Test with invalid meal types
            assertFalse(recipe.checkMealType("break"));
            assertFalse(recipe.checkMealType("test"));
            assertFalse(recipe.checkMealType("mmmm"));
        }
        catch(Exception e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }

    @Test 
    public void testSetTitle(){
        String input = "[Curried Egg Sandwich]" + "\n" +"Ingredients: 2 eggs " + "\n" + "Instructions: " + "\n" + "Steps";
        Recipe recipe = new Recipe(); 
        recipe.setInstructions(input); 
        recipe.setMealType("breakfast");
        recipe.setTitle(input);
        String instructions = "\nIngredients: 2 eggs " + "\n" + "Instructions: " + "\n" + "Steps";
        assertEquals("Curried Egg Sandwich - Breakfast", recipe.getTitle());
        assertEquals(instructions, recipe.getInstructions());

    }


    
}
