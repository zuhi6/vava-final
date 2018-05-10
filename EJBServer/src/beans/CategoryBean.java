package beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import models.Category;

/**
 * Session Bean implementation class Category
 */
@Stateless
public class CategoryBean implements CategoryBeanRemote {
	
	/***
	 * @return vsetky kategorie ktore sa nachadzaju v databaze
	 */
	@Override
	public List<Category> getCategories() {
		List<Category> categories = new ArrayList<Category>();
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/datasources/ds");
			Connection conn = ds.getConnection();
			Statement stm = conn.createStatement();
			stm.executeQuery("SELECT * FROM category");
			ResultSet rs = stm.getResultSet();
			while(rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt("id"));
				category.setName(rs.getString("name"));
				categories.add(category);
				
			}
			
		}catch(Exception e) {
			
		}
		return categories;
	}

	
   
}
