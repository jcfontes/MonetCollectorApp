package br.com.jcfontes.monetcollector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.jcfontes.monetcollector.R;
import br.com.jcfontes.monetcollector.configs.AppConfig;
import br.com.jcfontes.monetcollector.models.User;
import br.com.jcfontes.monetcollector.utils.FirebaseAuthUtils;
import br.com.jcfontes.monetcollector.utils.FirebaseDatabaseUtils;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView tvNome, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        User user = getIntent().getParcelableExtra("USER");
//
//        if (user == null) {
//            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//            finish();
//        }
//
//        View header = navigationView.getHeaderView(0);
//        tvNome = header.findViewById(R.id.tvName);
//        tvEmail = header.findViewById(R.id.tvEmail);
//
//        tvNome.setText(user.getName());
//        tvEmail.setText(user.getEmail());

        String userCode = FirebaseAuthUtils.getUUID();

        if (userCode != null) {
            DatabaseReference databaseReference = FirebaseDatabaseUtils.getInstance()
                    .getReference("user/" + userCode);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        User loggedUser = dataSnapshot.getValue(User.class);

                        if (loggedUser != null) {

                            View header = navigationView.getHeaderView(0);
                            tvNome = header.findViewById(R.id.tvName);
                            tvEmail = header.findViewById(R.id.tvEmail);

                            tvNome.setText(loggedUser.getName());
                            tvEmail.setText(loggedUser.getEmail());
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error: " + databaseError.getDetails());
                }
            });
        }


        String fragmentName = getIntent().getStringExtra(AppConfig.ARG_FRAGMENT) == null ?
                "" : getIntent().getStringExtra(AppConfig.ARG_FRAGMENT);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass;

            switch (fragmentName) {
                case AppConfig.KEY_FRAGMENT_CATEGORY:
                    fragmentClass = CategoryFragment.class;
                    break;
                default:
                    fragmentClass = HomeFragment.class;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_home, fragment).commit();
        }

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        showSelectedScreen(item);
        return true;
    }

    private void showSelectedScreen(MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_camera:
                fragment = new HomeFragment();
                getSupportActionBar().setTitle(getString(R.string.title_activity_home));
                break;

            case R.id.nav_gallery:
                fragment = new CategoryFragment();
                getSupportActionBar().setTitle(getString(R.string.category_title));
                break;

            case R.id.nav_share:
                startActivity(new Intent(this, AboutActivity.class));
                break;

            case R.id.nav_send:
                FirebaseAuthUtils.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_home, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
