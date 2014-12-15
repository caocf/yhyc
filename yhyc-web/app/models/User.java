package models;

import java.util.List;

import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.modules.mongodb.jackson.MongoDB;

public class User {

	@Id
	@ObjectId
	public String id;

	@Constraints.Required
	@Formats.NonEmpty
	public String email;

	@Constraints.Required
	public String name;

	@Constraints.Required
	public String password;

	public String tel;
	
	public boolean admin;

	// -- Queries

	private static JacksonDBCollection<User, String> coll = MongoDB
			.getCollection("users", User.class, String.class);

	/**
	 * Retrieve all users.
	 */
	public static List<User> findAll() {
		return User.coll.find().toArray();
	}
	
	public static User findById(String id){
		return User.coll.findOneById(id);
	}

	/**
	 * Retrieve a User from email.
	 */
	public static User findByEmail(String email) {
		return User.coll.findOne(DBQuery.is("email", email));
	}

	public static void save(User user) {
		User.coll.save(user);
	}
	
	/**
	 * Authenticate a User.
	 */
	public static User authenticate(String email, String password) {
		return User.coll.findOne(DBQuery.is("email", email).is("password",
				password));
	}

	// --

	public String toString() {
		return "User(" + email + ")";
	}

}
