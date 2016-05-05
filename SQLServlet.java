

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SQLServlet
 */
@WebServlet("/SQLServlet")
public class SQLServlet extends HttpServlet {
	//DB instance variables
	private static final long serialVersionUID = 1L;
	public static final String USER = "root";
	public static final String PASS = "Singapore1";
	public static final String CONN_STRING = 
			"jdbc:mysql://localhost/mydatabase";
	
	// HTML and Servlet instance variables
	private String username;
	private String password;
	private String login; 
	PrintWriter out = null; 
	
    public SQLServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
    	username = request.getParameter("username");
    	password = request.getParameter("password");
    	//register = request.getParameter("register");
    	login = request.getParameter("login");
    	if(check(username, password, response)){
    		if(login != null)
				processLogin(request, response);
			else
				processRegister(request, response);

    	}
		
	}// end of doGet()
	
//B
		//three cases to check
private boolean check(String username, String password,
		HttpServletResponse response) throws IOException{
	// username and password are null.
	if( (username == null) && (password == null)){
		//System.out.println("USR=NULL and PWRD=NULL");
		userAndPassNull(response);
		return false;
		// username is not null but it is an empty string.
	}else if ((username != null)&& (username.equals(""))){
		//System.out.println("username is not null but it is an empty string.");
		userPassIsEmpty(response);
		return false;
		// password is not null but it is an empty string.
	}else if((password != null)&& (password.equals(""))){
		//System.out.println("password is not null but it is an empty string.");
		userPassIsEmpty(response);
		return false;
	}else{
		//You've passed the check!;
		//System.out.println("You've Passed the CHECK");
		return true; 
	}

}// end of check()

	//C
  private void processRegister(HttpServletRequest request, HttpServletResponse response)
		  throws IOException{
  
  // connect to DB 
  try{ 
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection conn = DriverManager.getConnection(CONN_STRING, USER, PASS);
		Statement stmt = conn.createStatement();
		 // select the result set from mytable
		ResultSet rs = stmt.executeQuery("Select username FROM mytable WHERE username = \'" 
		 + username + "\'");
		// move Resultset Cursor forward 1 row
		if( rs.next()){
			//double check result set for a match
			if(rs.getString(1).equals(username)){
			// Tell user you have already registered.
			System.out.println(" Your've Already registered");
			System.out.println("Please log in to access the site");
			haveRegistered(response);
			}
		}else{
			// user needs to register
			//use the DB connection to insert new username and password
			stmt.execute("CREATE TABLE IF NOT EXISTS mytable(username VARCHAR(20), password VARCHAR(20))");
			String sql = "INSERT INTO mytable VALUES(\'" + username  + "\' ,\'" + password + "\')";
			stmt.execute(sql);
			justRegistered(response);
		}
  }catch(SQLException e){
	  System.out.println("SQL ERROR: " + e.getMessage());
	  System.out.println("SQL ERROR CODE: " + e.getErrorCode());
	  
  }catch(Exception e){
	  System.err.println("ERROR: " + e.getMessage());
  }

	
}//end processRegister
  
//E
  private void processLogin(HttpServletRequest request, HttpServletResponse response) 
		  throws IOException{
	  // connect to DB 
	  
	  try{ 
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection(CONN_STRING, USER, PASS);
			Statement stmt = conn.createStatement();
			 // select the result set from mytable
			ResultSet rs = stmt.executeQuery("Select username, password FROM mytable WHERE username = \'" 
					 + username + "\' AND password=\'" + password + "\'");
			// move ResultSet rs forward 1 row
			if( rs.next()){
				//double check username and password
				// in case something went wrong with DB
				if(( rs.getString(1).equals(username)) && (rs.getString(2).equals(password)) )
				//System.out.println("Welcome to the site");
				welcomePage(response);
				else
				// username or password INCORRECT or User needs to register
				//System.out.println("User or Password Incorrect, Please try again or register");
				userPassIncorrect(response);
			}else{
				//result set returned false - Must register
				//System.out.println("User or Password Incorrect, Please try again or register");
				//System.out.println("Result set returned false - Must register");
				mustRegister(response);
			}
	  }catch(SQLException e){
		  System.out.println("SQL ERROR: " + e.getMessage());
		  System.out.println("SQL ERROR CODE: " + e.getErrorCode());
		  e.printStackTrace();
		  
	  }catch(Exception e){
		  System.out.println("ERROR: " + e.getMessage());
	  }
	
  }// end processLogin
  
  // Output responses
  private void welcomePage(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>SQL Servlet</title></head>");
	  out.println("<body>");
	  out.println("<h1>Welcome to the SQL Servlet Site</h1>");
	  out.println("</body></html>");
  }//end welcome page
  
  private void userPassIsEmpty(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>SQL Servlet</title></head>");
	  out.println("<body>");
	  out.println("<h1>User or Password is empty and are required fields."
			  + "Try again</h1>");
	  out.println("<h3><a href='http://localhost:8080/Project6/index.html'>" 
	  + "Back to Registration/Login</a></h3>");
	  out.println("</body></html>");
  }
  
  private void userPassIncorrect(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>SQL Servlet</title></head>");
	  out.println("<body>");
	  out.println("<h1>User or Password Incorrect Please try again."
			  + "Please try again</h1>");
	  out.println("<h3><a href='http://localhost:8080/Project6/index.html'>" 
	  + "Back to Registration/Login</a></h3>");
	  out.println("</body></html>");
  }
  
  private void haveRegistered(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>Cookie Test</title></head>");
	  out.println("<body>");
	  out.println("<h2>You've already registered. Please login</h2>");
	  out.println("<h3><a href='http://localhost:8080/Project6/index.html' >Back to Resister/Login</a></h3>");
	  out.println("</body></html>");
  }
  
  private void justRegistered(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>SQL Servlet</title></head>");
	  out.println("<body>");
	  out.println("<h2>we did not find you in our DB so we went ahead "
	  		+ "and registered your username and password. Please login</h2>");
	  out.println("<h3><a href='http://localhost:8080/Project6/index.html' >Back to Registration/Login</a></h3>");
	  out.println("</body></html>");
  }
  
  private void mustRegister(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>SQL Servlet</title></head>");
	  out.println("<body>");
	  out.println("<h2>we did not find you in our DB. You must register then login</h2>");
	  out.println("<h3><a href='http://localhost:8080/Project6/index.html' >Back to Registration/Login</a></h3>");
	  out.println("</body></html>");
  }
  
  private void userAndPassNull(HttpServletResponse response)throws IOException{
	  response.setContentType("text/html");
	  out = response.getWriter();
	  out.println("<html><head>");
	  out.println("<style>h1,h2,h3{ text-align:center;}</style>");
	  out.println("<title>SQL Servlet</title></head>");
	  out.println("<body>");
	  out.println("<h1>User and Password are required fields</h1>");
	  out.println("<h3><a href='http://localhost:8080/Project6/index.html'>" 
	  + "Back to Registration</a></h3>");
	  out.println("</body></html>");
  }

}// end of SQLServlet
