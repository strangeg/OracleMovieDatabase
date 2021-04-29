package CIT304ProjectConnection;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class Application {
	
	static Scanner user_choice = new Scanner(System.in);
	static String first_name;
	static String last_name;
	static int memb_id;
	public static void main(String[] args) throws SQLException {

		try {
			boolean keep_going = true;
			
			//Start of UI, simple menu that allows user input
			while (keep_going == true) {
				userMenu();
				String user_str = user_choice.nextLine();
				int user = Integer.parseInt(user_str);
				
				if (user == 1){
					
					//checks to make sure its actually admin logging in
					boolean admin_check = checkAdmin();
					if (admin_check) {
						System.out.println("good login");
						//loops through admin menu until 7 is pressed
						boolean admin_keep_going = true;
						
						while (admin_keep_going) {
							
							adminMenu();
							String admin_input_str = user_choice.nextLine();
							int admin_input = Integer.parseInt(admin_input_str);
							
							if (admin_input == 1) {
								addMovie();
							}
							if (admin_input == 2) {
								addMember();
							}
							if (admin_input == 3) {
								searchUpdateMovie();
							}
							if (admin_input == 4) {
								searchDeleteMovie();
							}
							if (admin_input == 5) {
								searchUpdateMember();
							}
							if (admin_input == 6) {
								searchDeleteMember();
							}
							if (admin_input == 7) {
								admin_keep_going = false;
							}
						}
					}
					else {
						keep_going = false;
					}
				}
				if (user == 2){
					//checks to make sure its right user
					boolean check_user = checkUser();
					
					if (check_user) {
						overdueRent();
						
						//loops through member menu
						boolean member_keep_going = true;
						while (member_keep_going) {
							memberMenu();
							String member_input_str = user_choice.nextLine();
							int member_input = Integer.parseInt(member_input_str);
							
							if (member_input == 1) {
								searchMovie();
							}
							if (member_input == 2) {
								rentMovie();
							}
							if (member_input == 3) {
								returnMovie();
							}
							if (member_input == 4) {
								member_keep_going = false;
							}
						}
					}
					else {
						keep_going = false;
					}
				}
				if (user == 3){
					keep_going = false;
				}
			}
			
			user_choice.close();
			
		} finally {
			DBConnection.getInstance().getConnection().close();
		}

	}

	private static void callFunction() {

		DBConnection dbConnection = DBConnection.getInstance();
		CallableStatement pstmt = null;

		String state = "VA";
		int val = 100;
		try {
			pstmt = dbConnection.getConnection().prepareCall("{call tax_cost_sp(?,?,?,?)}");
			pstmt.setString(1, state);
			pstmt.setInt(2, val);

			pstmt.registerOutParameter(3, Types.DOUBLE);
			pstmt.registerOutParameter(4, Types.DOUBLE);
			pstmt.execute();

			double tax = pstmt.getDouble(3);
			double rate = pstmt.getDouble(4);
			System.out.print(tax + rate);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	private static void insertStudentData() {

		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;

		try {
			ps = dbConnection.getConnection().prepareStatement("insert into student VALUES (?, ?, ?,?) ");
			int i = 1;
			ps.setString(i++, "S05");
			ps.setString(i++, "SM");
			ps.setString(i++, "222");
			ps.setString(i++, "SM");

			int r = ps.executeUpdate();
			System.out.println("Number of inserts : " + r);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void getStudentData() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;

		try {
			ps = dbConnection.getConnection().prepareStatement("select * from student");
			//ps.setString(1, "%" + name + "%");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("studentname"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void getStudentDataById(String id) {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;

		try {
			ps = dbConnection.getConnection().prepareStatement("select * from student where STUDENTID = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("studentName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void addMovie() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;
		
		try {
			ps = dbConnection.getConnection().prepareStatement("insert into mm_movie VALUES (movie_seq.nextval, ?, ?, ?,?) ");
			System.out.print("Enter movie name: ");
			String movie_name = user_choice.nextLine();
			System.out.print("Enter move type: ");
			String movie_type = user_choice.nextLine();
			System.out.print("Enter price: ");
			String movie_price = user_choice.nextLine();
			System.out.print("Enter quantity");
			String movie_quan = user_choice.nextLine();
			
			int i = 1;
			ps.setString(i++, movie_name);
			ps.setString(i++, movie_type);
			ps.setString(i++, movie_price);
			ps.setString(i++, movie_quan);
			
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void addMovieType() {
		
	}
	
	private static void addMember() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;
		
		try {
			ps = dbConnection.getConnection().prepareStatement("insert into mm_member VALUES (member_seq.nextval, ?, ?, ?, ?, ?, ?, ?)");
			System.out.print("Enter last name");
			String member_last = user_choice.nextLine();
			System.out.print("Enter first name");
			String member_first = user_choice.nextLine();
			System.out.print("Enter license number");
			String member_license = user_choice.nextLine();
			System.out.print("Enter license state");
			String member_state = user_choice.nextLine();
			System.out.print("Enter credit card");
			String member_card = user_choice.nextLine();
			System.out.print("Suspension (Y/N)");
			String member_susp = user_choice.nextLine();
			System.out.print("Mailing list (Y/N)");
			String member_list = user_choice.nextLine();
			
			int i = 1;
			ps.setString(i++, member_last);
			ps.setString(i++, member_first);
			ps.setString(i++, member_license);
			ps.setString(i++, member_state);
			ps.setString(i++, member_card);
			ps.setString(i++, member_susp);
			ps.setString(i++, member_list);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static void addPaymentType() {
		
	}
	private static void searchMovie() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;
		
		try {
			ps = dbConnection.getConnection().prepareStatement("select * from mm_movie");
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("movie_title"));
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	private static void searchUpdateMovie() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;
		
		try {
			ps = dbConnection.getConnection().prepareStatement("select * from mm_movie");
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i < columnCount + 1; i++) {
				rsmd.getColumnName(i);
			}
			
			while (rs.next()) {
				System.out.println(rs.getString("movie_title"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	private static void searchDeleteMovie() {
		
	}
	private static void searchUpdateMember() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;
		
		try {
			ps = dbConnection.getConnection().prepareStatement("select * from mm_member");
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("first"));
				System.out.println(rs.getString("last"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	private static void searchDeleteMember() {
		
	}
	private static void rentMovie() {
		DBConnection dbConnection = DBConnection.getInstance();
		CallableStatement pstmt = null;

		String state = "VA";
		int val = 100;
		try {
			pstmt = dbConnection.getConnection().prepareCall("{call tax_cost_sp(?,?,?,?)}");
			pstmt.setString(1, state);
			pstmt.setInt(2, val);

			pstmt.registerOutParameter(3, Types.DOUBLE);
			pstmt.registerOutParameter(4, Types.DOUBLE);
			pstmt.execute();

			double tax = pstmt.getDouble(3);
			double rate = pstmt.getDouble(4);
			System.out.print(tax + rate);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private static void returnMovie() {
		DBConnection dbConnection = DBConnection.getInstance();
		PreparedStatement ps;
		try {
			ps = dbConnection.getConnection().prepareStatement("select * from mm_member");
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				System.out.println(rs.getString("first"));
				System.out.println(rs.getString("last"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	private static void overdueRent() {
		DBConnection dbConnection = DBConnection.getInstance();
		CallableStatement pstmt = null;
		try {
			pstmt = dbConnection.getConnection().prepareCall("{call check_overdue_sp(?,?)}");
			pstmt.setInt(1, memb_id);

			pstmt.registerOutParameter(2, Types.VARCHAR);
			pstmt.execute();
			
			String overdue = pstmt.getString(2);
			
			if (overdue.equals("true")) {
				System.out.println("You have overdue movies");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	//checks to make sure admin is actually admin
	private static boolean checkAdmin() {
		
		System.out.println("Enter admin username");
		String admin_name = user_choice.nextLine();
		System.out.println("Enter admin password");
		String admin_pass_str = user_choice.nextLine();
		int admin_pass = Integer.parseInt(admin_pass_str);
		
		if (admin_name.equals("admin") && admin_pass == 1234) {
			return true;
		}
		else {
			System.out.println("Wrong login");
			return false;
		}
	}
	//checks to make sure user is actual user
	//also used for queries later so user can rent movie
	private static boolean checkUser() {
		
		System.out.println("Enter your first name");
		first_name = user_choice.nextLine();
		System.out.println("Enter your last name");
		last_name = user_choice.nextLine();
		
		DBConnection dbConnection = DBConnection.getInstance();
		CallableStatement pstmt = null;
		try {
			pstmt = dbConnection.getConnection().prepareCall("{call check_user_sp(?,?,?,?)}");
			pstmt.setString(1, first_name);
			pstmt.setString(2, last_name);

			pstmt.registerOutParameter(3, Types.VARCHAR);
			pstmt.registerOutParameter(4, Types.INTEGER);
			pstmt.execute();

			String user_check = pstmt.getString(3);
			memb_id = pstmt.getInt(4);
			
			if (user_check.equals("true")){
				return true;
			}
			else {
				System.out.println("Wrong login, check capitalization");
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private static void userMenu()
	{
		System.out.println("1) Admin");
		System.out.println("2) Member");
		System.out.println("3) Exit");
		System.out.print("Enter a Number: ");
	}
	private static void adminMenu()
	{
		System.out.println("1) Add new movie");
		System.out.println("2) Add new member");
		System.out.println("3) Search and update movie");
		System.out.println("4) Search and delete movie");
		System.out.println("5) Search and update member");
		System.out.println("6) Search and delete member");
		System.out.println("7) Back to user menu");
		System.out.print("Enter a Number: ");
	}
	private static void memberMenu()
	{
		System.out.println("1) Search movie");
		System.out.println("2) Rent movie");
		System.out.println("3) Return movie");
		System.out.println("4) Back to user menu");
		System.out.println("Enter a Number: ");
	}
}
