package app.donation.main;

import java.util.List;

import app.donation.model.Token;
import app.donation.model.Candidate;
import app.donation.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Open interface
 */
public interface DonationServiceOpen
{
    @GET("/api/candidates")
    Call<List<Candidate>> getAllCandidates();

    @POST("/api/users")
    Call<User> createUser(@Body User User);

    @POST("/api/users/authenticate")
    Call<Token> authenticate(@Body User user);
}