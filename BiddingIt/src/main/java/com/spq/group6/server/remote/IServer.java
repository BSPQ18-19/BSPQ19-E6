package com.spq.group6.server.remote;

import com.spq.group6.server.data.Administrator;
import com.spq.group6.server.data.Auction;
import com.spq.group6.server.data.Product;
import com.spq.group6.server.data.User;
import com.spq.group6.server.exceptions.AccountException;
import com.spq.group6.server.exceptions.AdministratorException;
import com.spq.group6.server.exceptions.AuctionException;
import com.spq.group6.server.utils.observer.remote.IRemoteObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Remote Façade Interface for BididngIt server.
 * <p>
 * This is the API that describes functionality offered by BiddingIt Server.
 * It is used on the User and Administrator applications.
 */
public interface IServer extends Remote {

    // Account API

    /**
     * Method for login and getting access to the system.
     * <p>
     * Checks if User credentials are valid. If credentials are valid,
     * returns the User object that matched login credentials.
     * This method receives a RemoteObserver as paremeter, which is used
     * by the User for receiving notifications.
     * Returned User object is used for later method calls, for identifying the caller
     * Client.
     *
     * @param username User's username.
     * @param password User's password.
     * @param observer User's observer responsible for handling events sent by server.
     * @return Logged in User.
     * @throws RemoteException  is raised in case of Error on RMI connection.
     * @throws AccountException is raised in case of invalid credentials.
     */
    User logIn(String username, String password, IRemoteObserver observer) throws RemoteException, AccountException;

    /**
     * Method for letting know the server a User has logged out.
     * <p>
     * When this method is called, the Server deletes User's RemoteObserver,
     * so it will not receive more notifications. The RemoteObserver must be
     * the same as the one passed when log-in or sign-in. This is the way for
     * telling Server that a User will not be using the system.
     *
     * @param observer User's observer that will not listen more for Events.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    void logOut(IRemoteObserver observer) throws RemoteException;

    /**
     * Method for creating a new User.
     * <p>
     * This is the method used for creating an Account on BiddingIt
     * server. When creating the account, stores all User basic deatils, creates a
     * User object, persists it and returns it. This method receives a RemoteObserver as paremeter, which is used
     * by the User for receiving notifications. This method is used by the User application.
     *
     * @param username User's username.
     * @param password User's password.
     * @param country  User's country. Useful for auctions search.
     * @param observer User's observer responsible for handling events sent by server.
     * @return Created User.
     * @throws RemoteException  is raised in case of Error on RMI connection.
     * @throws AccountException is raised in case of invalid credentials.
     */
    User signIn(String username, String password, String country, IRemoteObserver observer) throws RemoteException, AccountException;

    /**
     * Method for updating User's password or country.
     * <p>
     * Whenever a User wants to update any of its detail's aacount,
     * this method is called passing its new values. Even if only one
     * value has changed, it is mandatory to pass both, country and password.
     * Received Country and Password will be set to the passed User, and after
     * persisting it, returns the updated User.
     *
     * @param user     Old User that will be updated.
     * @param password New (or same old) password for updating User.
     * @param country  New (or same old) country for updating User.
     * @return Updated User.
     * @throws RemoteException  is raised in case of Error on RMI connection.
     * @throws AccountException is raised in case of invalid values for update.
     */
    User updateUser(User user, String country, String password) throws RemoteException, AccountException;

    /**
     * Method for creating a new Product.
     * <p>
     * This method is called whenever a User wants to add a Product to the system.
     * The Users may want to add a Product to the system, in order to create an Auction later.
     * As every Product is related to a User, it is necessary to pass the User as argument.
     * After creating a product, it appends it to User's product list, and returns the updated
     * User.
     *
     * @param user        Product's owner User.
     * @param name        Product's name.
     * @param description Product's description.
     * @return User with its new Product on 'ownedProducts' ArrayList.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    User createProduct(User user, String name, String description) throws RemoteException;

    /**
     * Method for updating Product's name or description.
     * <p>
     * Whenever a User wants to change details of one of its Products,
     * this method is called. When this method is executed, the Server
     * creates a updated passed Product object with received 'name' and
     * 'description' strings, it persists it, and it returns updated Product.
     * Even if only one value has changed, it is mandatory to pass both,
     * 'name' and 'description'.
     *
     * @param product     Product that will be updated.
     * @param name        Product's new (or same old) name for updating product.
     * @param description Product's new (or same old) description for updating product.
     * @return Updated Product.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    Product updateProduct(Product product, String name, String description) throws RemoteException;

    /**
     * Method for deleting a Product.
     * <p>
     * Whenever a User wants to delete a Product from the system,
     * this method is called. This may happen when a User buys a Product
     * in an Auction, and it is not interested on selling it, so it deletes it from
     * the system. This method receives product's User owner, it deletes the Product,
     * removes it from User's product list, and returns the updated User without the
     * deleted Product.
     *
     * @param user    Product's owner User.
     * @param product Product that will be deleted.
     * @return User without Product. Product is deleted from 'ownedProducts' ArrayList.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    User deleteProduct(User user, Product product) throws RemoteException;

    // Auctions API

    /**
     * Method for creating an Auction.
     * <p>
     * Whenever a User wants to create a public Auction for
     * selling a product, this method is called. A public Auction
     * accepts Bids from any User.
     * As every Auction is related to a User,
     * it is necessary to pass the User as argument. When this method is
     * executed, the Server creates an Auction, persists it, starts a Thread
     * for managing Auction's end (executing any exchange if there is), and returns
     * created Auction. The next time the User logs-in, it will not have the Auctioned
     * product. If the Auction ends without an exchange, the User will get back the Product,
     * otheriwse, it will receive Highest Bid money.
     *
     * @param owner        Auction creator or Product's owner.
     * @param product      Product that will be sold on Auction.
     * @param dayLimit     Timestamp when Auction will be closed.
     * @param initialPrice Initial proce for Product.
     * @return Created Auction.
     * @throws RemoteException  is raised in case of Error on RMI connection.
     * @throws AuctionException is raised in case of invalid values for Auction.
     */
    Auction createPublicAuction(User owner, Product product, Timestamp dayLimit, float initialPrice) throws RemoteException, AuctionException;

    /**
     * Method for creating a private Auction.
     * <p>
     * Whenever a User wants to create a private Auction for
     * selling a product, this method is called.
     * In contrast to a public Auction, private Auctions require a Password
     * in order to be able to bid Auction's Product.
     * When this method is executed, the Server creates an Auction, persists it, starts a Thread
     * for managing Auction's end (executing any exchange if there is), and returns
     * created Auction. The next time the User logs-in, it will not have the Auctioned
     * product. If the Auction ends without an exchange, the User will get back the Product,
     * otheriwse, it will receive Highest Bid money.
     *
     * @param owner        Auction creator or Product's owner.
     * @param product      Product that will be sold on Auction.
     * @param dayLimit     Timestamp when Auction will be closed.
     * @param initialPrice Initial proce for Product.
     * @param password     Password that the Auction will be protected with the Auction..
     *                     It needs to be different from null.
     * @return Created Auction.
     * @throws RemoteException  is raised in case of Error on RMI.
     * @throws AuctionException is raised in case of invalid values for Auction.
     */
    Auction createPrivateAuction(User owner, Product product, Timestamp dayLimit, float initialPrice, String password) throws RemoteException, AuctionException;

    /**
     * Method for bidding an Auction.
     * <p>
     * Whenever a User wants to Bid in an Auction,
     * this method is called.
     * As every Bid is related to a User and an Auction,
     * it is necessary to pass the User and the Auction as arguments.
     * Then, if the Bid is higher than the current bid (or in case of no bid,
     * the initial price), the Server creates a Bid, sets it as Auction's highest Bid,
     * and returns updated Auction. Also, a notification is sent to RemoteObservers
     * informing about a new Bid. Users that have Bidded on the Auction or the
     * one that created may react to it.
     * This method does not check if User has money enough, as it considers that
     * a User can loose or get money before the Auction ends. Instead,
     * the money is checked when the Auction finishes. If it has no enough money,
     * Auction ends without an exchange. Auctions do not contain a history with previous Bids.
     *
     * @param auction Auction will be bidded to.
     * @param user    Bid creator User.
     * @param amount  Bidded amount (money).
     * @return Auction with the new Bid.
     * @throws RemoteException  is raised in case of Error on RMI connection.
     * @throws AuctionException is raised in case of invalid Bid amount.
     */
    Auction bid(Auction auction, User user, float amount) throws RemoteException, AuctionException;

    /**
     * Method for searching auctions by Auction owner's country.
     * <p>
     * Whenever a User wants to browse Auctions and search them by Country,
     * this method is called.
     * The result does not include Auctions created by the User,
     * nor Auctions already closed. That is why the User needs to be passed as an argument,
     * in order to exclude Auctions created by the User when the result is returned.
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions.
     * @param country   Country that Auction's owner is from.
     * @return Auctions matching owner's country.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    ArrayList<Auction> searchAuctionByCountry(User requester, String country) throws RemoteException;

    /**
     * Method for searching auctions by Auction product's name.
     * <p>
     * Whenever a User wants to browse Auctions and search them by Product name,
     * this method is called.
     * The result does not include Auctions created by the User,
     * nor Auction already closed. That is why the User needs to be passed as an argument,
     * n order to exclude Auctions created by the User when the result is returned.
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions.
     * @param name      Name that Auction's product have.
     * @return Auctions matching product's name.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    ArrayList<Auction> searchAuctionByProductName(User requester, String name) throws RemoteException;

    /**
     * Method for getting all Auctions, without needing to search by a condition.
     * <p>
     * Whenever a User wants to browse Auctions and is not interested
     * in a specific Auction, this method is called.
     * The result does not include Auctions created by the User,
     * nor Auction already closed. That is why the User needs to be passed as an argument,
     * n order to exclude Auctions created by the User when the result is returned.
     *
     * @param requester User that requests Auctions. Needed for not sending User's Auctions.
     * @return All open auctions.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    ArrayList<Auction> getAllAuctions(User requester) throws RemoteException;

    // Administrator API

    /**
     * Method for login an Administrator User.
     * <p>
     * Checks if Administrator credentials are valid,
     * and returns an Administrator object that matched login credentials.
     *
     * @param username Administrator's username.
     * @param password Administrator's password.
     * @return Logged in Administrator.
     * @throws RemoteException        is raised in case of Error on RMI connection.
     * @throws AdministratorException is raised in case of invalid credentials.
     */
    Administrator adminLogIn(String username, String password) throws RemoteException, AdministratorException;

    /**
     * Method for deleting an Auction.
     * <p>
     * This method is used by Administrators in order to delete an Auction
     * from the system. When this method is called, a notification is sent to
     * all RemoteObservers informing about a deleted Auction. And the ones observing
     * the Auction, or the User owner, may react to it.
     *
     * @param auction Auction that will be deleted.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    void deleteAuction(Auction auction) throws RemoteException;

    /**
     * Method for deleting a User.
     * <p>
     * This method is used by Administrators in order to delete a User
     * from the system. A notification is sent to all RemoteObservers
     * and the Client that is identified by this User may react to it.
     * This triggers too the removal of Products, Auctions and Bids that
     * the User is owner of. Then, more notification are sent to
     * all RemoteObservers informing about deleted objects.
     *
     * @param user User that will be deleted.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    void deleteUser(User user) throws RemoteException;

    /**
     * Method for getting all Users.
     * <p>
     * This method is used by Administrators in order to retrieve all
     * Users that have an account on the system. Therefore, this method
     * must be called only from Administrator application.
     *
     * @return All Users that have an account on BiddingIt.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    ArrayList<User> getAllUsers() throws RemoteException;

    /**
     * Method for getting specific User's Auctions.
     * <p>
     * This method is used by Administrators in order to retrieve all open Auctions,
     * created by a specific User. This may be used for latter deleting a retrieved
     * Auction.
     *
     * @param user Auctions owner User.
     * @return All opened Acutions the User is owner of.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    ArrayList<Auction> getAuctionByUser(User user) throws RemoteException;

    /**
     * Method for getting all Auctions.
     * <p>
     * This method is used by Administrators in order to retrieve all
     * opened Auctions on the system. This may be used for latter deleting a retrieved
     * Auction.
     *
     * @return All opened Auctions on the system.
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    ArrayList<Auction> getAllAuctionsAdmin() throws RemoteException;

    /**
     * Method for starting Auctions' Countdown threads.
     * <p>
     * This is only used when testing. It is needed when an Auction
     * have been created manually on the database after initializing the Server.
     * This method does not need to be called, if it is created calling 'createPublicAcution'
     * or 'createPrivateAuction'.
     *
     * @throws RemoteException is raised in case of Error on RMI connection.
     */
    void startUncheckedAuctions() throws RemoteException;

    /**
     * Method for persisting an Adminsitrator on the system.
     * <p>
     * This method is used for persisting an Administrator object. It is equivalent
     * to User 'sign-in', but it receives an already created Administror object,
     * instead of creating it when calling this method.
     * It is used when there is no possibility of connecting directly to the
     * database, and creating manually an Adminsitrator. This method should not be called,
     * just in rare occasions.
     * It is encouraged to create Administrators directly through the database.
     *
     * @param admin Administrator that will be persisted on the system.
     * @return Persisted Administrator.
     * @throws RemoteException        is raised in case of Error on RMI connection.
     * @throws AdministratorException is raised in case of Error while persisting the Administrator.
     */
    public Administrator createAdministrator(Administrator admin) throws RemoteException, AdministratorException;
}
