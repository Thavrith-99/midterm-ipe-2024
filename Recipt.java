import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeManager extends Application {

    private Map<String, List<String>> recipes = new HashMap<>();
    private ObservableList<String> recipeNames = FXCollections.observableArrayList();

    private ListView<String> recipeListView;
    private TextArea ingredientsTextArea;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Recipe List View
        recipeListView = new ListView<>();
        recipeListView.setItems(recipeNames);
        recipeListView.setOnMouseClicked(event -> displayIngredients());

        // Ingredients Text Area
        ingredientsTextArea = new TextArea();
        ingredientsTextArea.setEditable(false);

        // Button to add recipe
        Button addRecipeButton = new Button("Add Recipe");
        addRecipeButton.setOnAction(event -> showAddRecipeDialog());

        // Button to remove recipe
        Button removeRecipeButton = new Button("Remove Recipe");
        removeRecipeButton.setOnAction(event -> removeSelectedRecipe());

        // Top layout for buttons
        HBox buttonBox = new HBox(10, addRecipeButton, removeRecipeButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        root.setTop(buttonBox);
        root.setLeft(recipeListView);
        root.setCenter(ingredientsTextArea);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Recipe Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayIngredients() {
        String selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            List<String> ingredients = recipes.get(selectedRecipe);
            StringBuilder sb = new StringBuilder();
            for (String ingredient : ingredients) {
                sb.append(ingredient).append("\n");
            }
            ingredientsTextArea.setText(sb.toString());
        }
    }

    private void showAddRecipeDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Recipe");
        dialog.setHeaderText("Enter recipe name and ingredients:");

        TextField recipeNameField = new TextField();
        TextArea ingredientsArea = new TextArea();
        ingredientsArea.setPromptText("Enter ingredients separated by commas");

        dialog.getDialogPane().setContent(new VBox(10, recipeNameField, ingredientsArea));

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String recipeName = recipeNameField.getText().trim();
                String[] ingredients = ingredientsArea.getText().split(",");
                List<String> ingredientList = new ArrayList<>();
                for (String ingredient : ingredients) {
                    ingredientList.add(ingredient.trim());
                }
                recipes.put(recipeName, ingredientList);
                recipeNames.add(recipeName);
                return recipeName;
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void removeSelectedRecipe() {
        String selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();
        if (selectedRecipe != null) {
            recipes.remove(selectedRecipe);
            recipeNames.remove(selectedRecipe);
            ingredientsTextArea.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
