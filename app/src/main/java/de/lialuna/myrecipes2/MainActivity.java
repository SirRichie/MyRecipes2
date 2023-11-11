package de.lialuna.myrecipes2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.MalformedURLException;
import java.net.URL;

import de.lialuna.myrecipes2.databinding.ActivityMainBinding;
import de.lialuna.myrecipes2.entity.Recipe;
import de.lialuna.myrecipes2.ui.edit.EditRecipeFragmentArgs;
import de.lialuna.myrecipes2.util.recipeparsers.ParseRecipeHelper;
import de.lialuna.myrecipes2.util.recipeparsers.Result;
import de.lialuna.myrecipes2.viewmodel.RecipeListViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private FirebaseAuth mAuth;

    protected void authenticate() {
        mAuth = FirebaseAuth.getInstance();
        // sign in with a fixed user to allow for securing Firestore
        signIn("recipes@lialuna.dnshome.de", "thisisnotreallyasecret");
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithEmail:success " + user.getUid() + " | " + user.getDisplayName());
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        authenticate();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        try {
            dispatchIntent(getIntent());
        } catch (MalformedURLException e) {
            showErrorSnackbar(binding.getRoot(), e);
        }
    }

    private void dispatchIntent(Intent intent) throws MalformedURLException {
        if (intent.getAction().equals(Intent.ACTION_SEND)) {
            importRecipe(new URL(intent.getStringExtra(Intent.EXTRA_TEXT)));
        }
    }

    private void importRecipe(URL url) {
        ParseRecipeHelper.downloadAndParseAsync(url, recipeResult -> {
            if (recipeResult instanceof Result.Success) {
                // Happy path
                RecipeListViewModel recipeListViewModel = new ViewModelProvider(this).get(RecipeListViewModel.class);
                int newRecipeIndex = recipeListViewModel.addRecipe(((Result.Success<Recipe>) recipeResult).data);
                // TODO use index to start edit fragment
                EditRecipeFragmentArgs args = new EditRecipeFragmentArgs.Builder(newRecipeIndex).build();
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                        .navigate(R.id.EditRecipeFragment, args.toBundle());
            } else {
                Result.Error<Recipe> error = (Result.Error<Recipe>) recipeResult;
                showErrorSnackbar(binding.getRoot(), error.exception);
            }

        });
    }

    private void showErrorSnackbar(View view, Exception e) {
        Snackbar.make(view, "Import Error " + e.getMessage(), Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}