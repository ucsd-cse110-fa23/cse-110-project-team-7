import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sort {
    /*
     * Make four methods to sort the recipes:
     * Alphabetical A-Z
     * Alphabetical Z-A
     * Chronological Latest to Oldest
     * Chronological Oldest to Latest
     */
    
    private static class CustomRecipeComparator implements Comparator<Recipe> {
    
        public int compare(Recipe r1, Recipe r2) {
            return r1.getTitle().compareTo(r2.getTitle());
        }
    }

    private static class CustomRecipeComparatorZA implements Comparator<Recipe> {
    
        public int compare(Recipe r1, Recipe r2) {
            return r2.getTitle().compareTo(r1.getTitle());
        }
    }

    public static ArrayList<Recipe> alphabeticalAZSort(ArrayList<Recipe> recipes) {
        CustomRecipeComparator custom = new CustomRecipeComparator();
        Collections.sort(recipes, custom);
        //Collections.sort(recipes, Comparator.comparing(r -> r.getTitle(), Comparator.nullsLast(Comparator.naturalOrder())));

        return recipes;
    }
    public static ArrayList<Recipe> alphabeticalZASort(ArrayList<Recipe> recipes) {
        CustomRecipeComparatorZA custom = new CustomRecipeComparatorZA();
        Collections.sort(recipes, custom);
        return recipes;
    }
    /* 
    public static ArrayList<Recipe> newestToOldestSort(ArrayList<Recipe> recipes) {
        // CustomRecipeComparator custom = new CustomRecipeComparator();
        
        return recipes;
    }
    */
    public static ArrayList<Recipe> oldestToNewestSort(ArrayList<Recipe> recipes) {
        ArrayList<Recipe> reversedList = new ArrayList<>(recipes);
        Collections.reverse(reversedList);
        return reversedList;
    }
    
}
