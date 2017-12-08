package app.donation.activity;

import app.donation.R;
import app.donation.main.DonationApp;
import app.donation.model.Candidate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * The welcome activity can be simplified, as we are no longer downloading a full list of users
 */
public class Welcome extends AppCompatActivity
{
  private DonationApp app;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);

    app = (DonationApp) getApplication();
  }

  @Override
  public void onResume()
  {
    super.onResume();
    app.currentUser = null;;

    Call<List<Candidate>> call = (Call<List<Candidate>>) app.donationServiceOpen.getAllCandidates();
    call.enqueue(new Callback<List<Candidate>>() {
      @Override
      public void onResponse(Call<List<Candidate>> call, Response<List<Candidate>> response) {
        serviceAvailableMessage();
        app.candidates = response.body();
      }

      @Override
      public void onFailure(Call<List<Candidate>> call, Throwable t) {
        serviceUnavailableMessage();
      }
    });
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
    app.donationServiceAvailable = false;
    Toast toast = Toast.makeText(this, "Donation Service Unavailable. Try again later", Toast.LENGTH_LONG);
    toast.show();
  }

  void serviceAvailableMessage()
  {
    app.donationServiceAvailable = true;
    Toast toast = Toast.makeText(this, "Donation Contacted Successfully", Toast.LENGTH_LONG);
    toast.show();
  }
}