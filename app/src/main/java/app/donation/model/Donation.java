package app.donation.model;

public class Donation
{
  public String _id;
  public int    amount;
  public String method;

  public Donation (int amount, String method)
  {
    this.amount = amount;
    this.method = method;
  }
}