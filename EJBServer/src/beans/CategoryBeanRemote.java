package beans;

import java.util.List;

import javax.ejb.Remote;

import models.Category;

@Remote
public interface CategoryBeanRemote {
	
		public List<Category> getCategories();
}
