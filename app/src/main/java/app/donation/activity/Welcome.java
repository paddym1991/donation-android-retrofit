package app.donation.activity;

import app.donation.R;
import app.donation.activity.Login;
import app.donation.activity.Signup;
import app.donation.main.DonationApp;
import app.donation.model.Candidate;
import app.donation.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;


public class Welcome extends AppCompatActivity implements Callback<List<User>>
{
  private DonationApp app;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    app = (DonationApp) getApplication();
  }

  /**
   * issuing a request to the API for the list of all donors
   */
  @Override
  public void onResume()
  {
    super.onResume();
    app.currentUser = null;
    Call<List<User>> call1 = (Call<List<User>>) app.donationService.getAllUsers();
    call1.enqueue(this);

    Call<List<Candidate>> call2 = (Call<List<Candidate>>) app.donationService.getAllCandidates();
    call2.enqueue(new Callback<List<Candidate>>() {
      @Override
      public void onResponse(Call<List<Candidate>> call, Response<List<Candidate>> response) {
        app.candidates = response.body();
      }

      @Override
      public void onFailure(Call<List<Candidate>> call, Throwable t) {
        app.donationServiceAvailable = false;
        serviceUnavailableMessage();
      }
    });

  }

  /**
   * Successful response to API request
   * @param call
   * @param response
   */
  @Override
  public void onResponse(Call<List<User>> call, Response<List<User>> response)
  {
    serviceAvailableMessage();
    app.users = response.body();
    //Log.v("donation", app.users.toString());
    app.donationServiceAvailable = true;
  }

  /**
   * Error response to API request
   * @param call
   * @param t
   */
  @Override
  public void onFailure(Call<List<User>> call, Throwable t)
  {
    app.donationServiceAvailable = false;
    serviceUnavailableMessage();
  }

  public void loginPressed (View view)
  {
    if (app.donationServiceAvailable)
    {
      startActivity (new Intent(this, Login.class));
    }
    else
    {
      serviceUnavailableMessage();
    }
  }

  public void signupPressed (View view)
  {
    if (app.donationServiceAvailable)
    {
      startActivity (new Intent(this, Signup.class));
    }
    else
    {
      serviceUnavailableMessage();
    }
  }

  void serviceUnavailableMessage()
  {
    Toast toast = Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG);
    toast.show();
  }

  void serviceAvailableMessage()
  {
    Toast toast = Toast.makeText(this, "Donation Contacted Successfully", Toast.LENGTH_LONG);
    toast.show();
  }
}