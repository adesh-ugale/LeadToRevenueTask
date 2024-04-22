package com.tka.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.tka.entity.Products;
import com.tka.entity.Rating;
import jakarta.persistence.Query;

@Service
public class ProductService
{
		@Autowired
		SessionFactory factory;

//..........................Add Product......................
		
	    public Products addProduct(Products product)
	    {
	    	
	    	Session session=factory.openSession();
	    	
	    	Transaction tx = session.beginTransaction();
			
			session.persist(product);
				
			tx.commit();
	    	
	    	return product;
	    }
	    
	    
//..........................Get Product......................
	    
	    
	    public Products getProduct(Long productId)
	    {
	    	
	        Session session = factory.openSession();
	        Transaction tx = null;
	        try
	        {
	            tx = session.beginTransaction();
	            Products product = session.get(Products.class, productId);
	            if (product != null)
	            {
	                
	                return product;
	            }
	        }
	        catch (HibernateException e)
	        {
	            if (tx != null) tx.rollback();
	            e.printStackTrace();
	        }
	        finally
	        {
	            session.close();
	        }
			return null;
	    }
	    
	    
 //   ..................Search Product.......................
	    
	    
	    public List<Products> searchByName(String name)
	    {
	    	List<Products> products=null;
	        try (Session session = factory.openSession())
	        {
	    		products=(List<Products>) session.createQuery("from Products where name=:productName",Products.class).setParameter("productName",name).list();
	  
	    		return products;
	        }
	    }
	    
	    public List<Products> searchByCategory(String category)
	    {
	        try (Session session = factory.openSession())
	        {
	            Query query=session.createQuery("from Products e where :category MEMBER OF e.categories");
	            query.setParameter("category",category);
	            
	            List<Products> list=((org.hibernate.query.Query) query).list();

	            return list;
	        }
	    }
	    
	    
	    public List<Products> searchByAttributeKey(String attributeKey)
	    {
	        try (Session session = factory.openSession())
	        {
	            Query query = session.createQuery("select p from Products p join p.attributes a where key(a) = :attributeKey");
	            query.setParameter("attributeKey", attributeKey);
	            return query.getResultList();
	        }
	    }
	    
	    public List<Products> searchByAttributeValue(String attributeValue)
	    {
	        try (Session session = factory.openSession())
	        {
	        	  Query query = session.createQuery("select p from Products p join p.attributes a where value(a) = :attributeValue");
	    		
	        	  query.setParameter("attributeValue",attributeValue);
	        	  
	        	  return query.getResultList();
	        }
	    }
	    
	    
 //  .......................Delete Product.....................
	    
	    
	    public boolean deleteProduct(Long productId)
	    {
	    	
	        Session session = factory.openSession();
	        Transaction tx = null;
	        try
	        {
	            tx = session.beginTransaction();
	            Products product = session.get(Products.class, productId);
	            if (product != null) {
	                session.delete(product);
	                tx.commit();
	                return true;
	            }
	        }
	        catch (HibernateException e)
	        {
	            if (tx != null) tx.rollback();
	            e.printStackTrace();
	        }
	        finally
	        {
	            session.close();
	        }
			return false;
	    }
	    
	    
	    
//  .............................Update Product....................
	    
	    
	    public ResponseEntity<Products> updateProduct(long productId, Products updatedProduct) 
	   {
	        try (Session session = factory.openSession())
	        {
	            Transaction tx = session.beginTransaction();

	            Products existingProduct = session.get(Products.class, productId);
	            if (existingProduct == null)
	            {
	                tx.rollback();
	                return ResponseEntity.notFound().build();
	            }
	            // Update the existing product with the new details
	            existingProduct.setAttributes(updatedProduct.getAttributes());
	            existingProduct.setAvailability(updatedProduct.getAvailability());
	            existingProduct.setCategories(updatedProduct.getCategories());
	            existingProduct.setDescription(updatedProduct.getDescription());
	            existingProduct.setName(updatedProduct.getName());
	            existingProduct.setPrice(updatedProduct.getPrice());
	            existingProduct.setRatings(updatedProduct.getRatings());

	            session.update(existingProduct);
	            tx.commit();

	            return ResponseEntity.ok(existingProduct);
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	        }
	   }
	    
//................Provide an endpoint for rating a product, which should update the product's ratings array.................
	    
	    
	    public boolean rateProduct(Long productId, Rating rating)
	    {

	        try (Session session = factory.openSession())
	        {
	            Products products = session.get(Products.class,productId);
	            if (products != null)
	            {
	                products.getRatings().add(rating);
	               
	                session.update(products);
	                
	                // Commit the transaction
	                Transaction tx = session.beginTransaction();
	                tx.commit();
	                
	                return true; 
	            }
	            else
	            {
	                return false;
	            }
	        }
	    }
	    
//....................Implement pagination and sorting in the product list retrieval endpoint.........................
	    
	    
	    public List<Products> getProducts(int page, int size, String sortBy, String sortOrder)
	    {
	        try (Session session = factory.openSession())
	        {
	            String queryString = "FROM Products";
	            queryString += " ORDER BY " + sortBy + " " + sortOrder;
	            List<Products> products = session.createQuery(queryString, Products.class)
	                                             .setFirstResult(page * size)
	                                             .setMaxResults(size)
	                                             .getResultList();
	            return products;
	        }
	    }
	    
	    
	   
}
