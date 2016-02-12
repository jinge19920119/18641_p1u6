package jdbc;

import java.io.*;
import java.sql.*;

import model.Automobile;

public class JdbcBuild {
	Connection connection= null;
	Statement statement= null;
	ResultSet resultSet=null;
	/*
	 * driver name for this database, location of the database, username and password for it
	 */
	static final String jdbcDriver = "com.mysql.jdbc.Driver";
	static final String dbUrl= "jdbc:mysql://localhost";
	static final String user= "root";
	static final String passwd= "";
	
	/*
	 * constructor
	 */
	public JdbcBuild() {
		
	}
	/*
	 * build methods, initialization of the database
	 */
	public void buildConnect() {
		try {
			Class.forName(jdbcDriver);
			connection= DriverManager.getConnection(dbUrl, user, passwd);
			statement= connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * create method, that read from a txt file and read the MySQL language and write to the database.
	 */
	public void createDB() {
		try {
			BufferedReader br= new BufferedReader(new FileReader("db_schema.txt"));
			String line= null;
			StringBuilder sb= new StringBuilder();
			while ((line=br.readLine())!=null){
				sb.append(line);
				if(sb.length()>0 && sb.charAt(sb.length()-1)==';'){//see if it is the end of one line of command
					statement.executeUpdate(sb.toString());
					sb= new StringBuilder();
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * addAuto method. parse the Automobile object and add them to three tables.
	 */
	public void addAuto(Automobile auto) {
		String name= null;
		float price= 0;
		int autoid=0;
		String sql= null;
		try {
			String autoName= auto.getMake()+" "+auto.getModel();
			price= auto.getBasePrice();
			sql= "insert into automobile (name, base_price) values('"+autoName+"',"+price+")";//MySQL command
			statement.executeUpdate(sql);//add to automobile table
			
			sql= "select id from automobile where name ='"+ autoName+"';";
			ResultSet rs= statement.executeQuery(sql);//use ResultSet object to receive 
			while(rs.next()){
				autoid= rs.getInt("id");//get auto_id
			}
			for(int i=0;i<auto.getOptionSetNum();i++){
				name= auto.getOptionSetName(i);
				sql= "insert into optionset (name, auto_id) values('"+ name+"',"+ autoid+")";
				statement.executeUpdate(sql);//add to optionset table
				int opsid=0;
				sql= "select id from optionset where name ='"+name+"';";
				ResultSet rrs= statement.executeQuery(sql);
				while(rrs.next()){
					opsid= rrs.getInt("id");//get optionset id
				}
				for(int j=0;j<auto.getOptionNum(i);j++){
					name= auto.getOptionName(i, j);
					price= auto.getOptionPrice(i, j);
					sql= "insert into options (name, price, option_id) values('"+ name+"',"+price+","+opsid+")";
					statement.executeUpdate(sql);//add to options table
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * update method. select the objects and then update.
	 */
	public void updateAutoName(String oldName, String newName) {
		
		try {
			String sql= "update automobile set name = '"+newName +"'where name= '"+oldName+"';";
			statement.executeUpdate(sql);//update directly as it can be accessed directly from automobile table
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateOptionSetName(String modelName, String optionSetName,
			String newName){
		try {
			int autoid=0;
			String sql= "select id from automobile where name ='"+ modelName+"';";
			ResultSet rs= statement.executeQuery(sql);
			while(rs.next()){
				autoid= rs.getInt("id");//get auto_id
			}
			sql= "update optionset set name = '"+newName +"' where name= '"+optionSetName+"' and auto_id= "+autoid+";";
			statement.executeUpdate(sql);//update it in optionset table using name and auto_id(fk)
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateOptionPrice(String modelName, String optionSetName,
			String optionName, float newPrice){
		try {
			int autoid=0;
			String sql= "select id from automobile where name ='"+ modelName+"';";
			ResultSet rs= statement.executeQuery(sql);
			while(rs.next()){
				autoid= rs.getInt("id");//get auto_id
			}
			int opsid=0;
			sql= "select id from optionset where name ='"+ optionSetName+"' and auto_id= "+autoid+";";
			rs= statement.executeQuery(sql);
			while(rs.next()){
				opsid= rs.getInt("id");//get option_id
			}
			sql= "update options set price = "+newPrice +" where name= '"+optionName+"' and option_id= "+opsid+";";
			statement.executeUpdate(sql);//update it with name and option_id
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * delete methods. select fisrt and then delete.
	 */
	public void deleteAuto(String modelName){
		
		try {
			String sql= "delete from automobile where name= '"+modelName+"';";
			statement.executeUpdate(sql);//delete direcly as it can be accessed in automobile
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteOptionSet(String modelName, String optionSetName){
		
		try {
			int autoid=0;
			String sql= "select id from automobile where name ='"+ modelName+"';";
			ResultSet rs;
			rs = statement.executeQuery(sql);
			while(rs.next()){
				autoid= rs.getInt("id");//get auto_id
			}
			sql= "delete from optionset where name= '"+optionSetName+"' and auto_id= "+autoid;
			statement.executeUpdate(sql);//delete it using name and auto_id
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void deleteOption(String modelName, String optionSetName, String option){
		try {
			int autoid=0;
			String sql= "select id from automobile where name ='"+ modelName+"';";
			ResultSet rs;
			rs = statement.executeQuery(sql);
			while(rs.next()){
				autoid= rs.getInt("id");//get auto_id
			}
			int opsid=0;
			sql= "select id from optionset where name ='"+ optionSetName+"' and auto_id= "+autoid+";";
			rs= statement.executeQuery(sql);
			while(rs.next()){
				opsid= rs.getInt("id");//get option_id
			}
			sql= "delete from options where name= '"+option+"' and option_id= "+opsid;
			statement.executeUpdate(sql);//delete it using name and option_id
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	/*
	 * print function.
	 */
	public void print() {
		String sql= null;
		ResultSet rs;
		try {
			sql= "select * from automobile";
			rs = statement.executeQuery(sql);
			System.out.println("**************************automobile*********************************");
			while(rs.next()){//get all the values in automobile table
				System.out.printf("%-20s", rs.getString(1));//make it tidy and east to look
				System.out.printf("%-35s", rs.getString(2));
				System.out.printf("%-35s", rs.getString(3));
				System.out.println();
			}
			sql= "select * from optionset";
			rs= statement.executeQuery(sql);
			System.out.println("*****************************optionset********************************");
			while(rs.next()){//get all the values in optionset table
				System.out.printf("%-20s", rs.getString(1));
				System.out.printf("%-35s", rs.getString(2));
				System.out.printf("%-35s", rs.getString(3));
				System.out.println();
			}
			sql= "select * from options";
			rs= statement.executeQuery(sql);
			System.out.println("***************************options******************************");
			while(rs.next()){//get all the values in options table
				
				System.out.printf("%-20s", rs.getString(1));
				System.out.printf("%-35s", rs.getString(2));
				System.out.printf("%-35s", rs.getString(3));
				System.out.printf("%-35s", rs.getString(4));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
