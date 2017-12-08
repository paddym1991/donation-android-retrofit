package app.donation.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import app.donation.model.Token;
import app.donation.model.Candidate;
import app.donation.model.User;
import app.donation.model.Donation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DonationApp class in restructured now to use the new authenticate route - encapsulated in the validUser and onResponse
 */
public class DonationApp extends Application implements Callback<Token>
{
  public DonationServiceOpen donationServiceOpen;
  public DonationService     donationService;

  public boolean         donationServiceAvailable = false;
  public String          service_url  = "http://10.0.2.2:4000";   // Standard Emulator IP Address

  public final int       target       = 10000;
  public int             totalDonated = 0;

  public User             currentUser;
  public List <Donation>  donations    = new ArrayList<Donation>();
  public List <Candidate> candidates   = new ArrayList<Candidate>();

  public boolean newDonation(Donation donation)
  {
    boolean targetAchieved = totalDonated > target;
    if (!targetAchieved)
    {
      donations.add(donation);
      totalDonated += donation.amount;
    }
    else
    {
      Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
      toast.show();
    }
    return targetAchieved;
  }

  @Override
  public void onCreate()
  {
    super.onCreate();
    donationServiceOpen = RetrofitServiceFactory.createService(DonationServiceOpen.class);
    Log.v("Donation", "Donation App Started");
  }

  public boolean validUser (String email, String password)
  {
    User user = new User ("", "", email, password);
    donationServiceOpen.authenticate(user);
    Call<Token> call = (Call<Token>) donationServiceOpen.authenticate (user);
    call.enqueue(this);
    return true;
  }

  //creating the closed route proxy using the token we have just received (if we had the correct credentials)
  @Override
  public void onResponse(Call<Token> call, Response<Token> response) {
    Token auth = response.body();
    currentUser = auth.user;
    donationService =  RetrofitServiceFactory.createService(DonationService.class, auth.token);
    Log.v("Donation", "Authenticated " + currentUser.firstName + ' ' + currentUser.lastName);
  }

  @Override
  public void onFailure(Call<Token> call, Throwable t) {
    Toast toast = Toast.makeText(this, "Unable to authenticate with Donation Service", Toast.LENGTH_SHORT);
    toast.show();
    Log.v("Donation", "Failed to Authenticated!");
  }
}