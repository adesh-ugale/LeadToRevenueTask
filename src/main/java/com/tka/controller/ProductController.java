package com.tka.controller;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tka.entity.Products;
import com.tka.entity.Rating;
import com.tka.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController
{
	@Autowired
    private ProductService productService;
	
	@Autowired
	SessionFactory factory;
	

	//..........................Add Product......................
	
//	Json String----------
//	{
//		  "name": "Product Name",
//		  "description": "Best Product",
//		  "price": 110,
//		  "categories": ["Category1", "Category2"],
//		  "attributes":{
//		    "size": "1kg",
//		    "color": "red",
//		    "brand":"Parle"
//		  },
//		  "availability": {
//		    "inStock": true,
//		    "quantity": 11
//		  },
//		  "ratings": [
//		    {
//		      "rating": 5,
//		      "comment": "Great product!"
//		    },
//		    {
//		      "rating": 4,
//		      "comment": "Good product."
//		    }
//		  ]
//	}
	
    @PostMapping("/addProducts")
    public Products addProduct(@RequestBody Products products) {
        return productService.addProduct(products);
    }
    
  //..........................Get Product......................
    
    @GetMapping("/getProducts/{productId}")
    public Products getProduct(@PathVariable long productId) {
        return productService.getProduct(productId);
    }
    
    
  //   ..................Search Product.......................
    

    @GetMapping("/searchName/{productName}")
    public List<Products> searchProductName(@PathVariable String productName)
    {
    	 return productService.searchByName(productName);
    }
    
    @GetMapping("/searchCategory/{category}")
    public List<Products> searchProductCategory(@PathVariable String category)
    {
    	 return productService.searchByCategory(category);
    }
    
    @GetMapping("/searchAttributeKey/{attributeKey}")
    public List<Products> searchProductAttributeKey(@PathVariable String attributeKey)
    {
    	 return productService.searchByAttributeKey(attributeKey);
    }
    
    @GetMapping("/searchAttributeValue/{attributeValue}")
    public List<Products> searchProductAttributeValue(@PathVariable String attributeValue)
    {
    	 return productService.searchByAttributeValue(attributeValue);
    }
    
  //  .......................Delete Product.....................
    
    @DeleteMapping("/deleteProduct/{productId}")
    public boolean deleteProducts(@PathVariable long productId)
    {
    	return productService.deleteProduct(productId);
    }
    
  //  .............................Update Product....................

    
    @PutMapping("/updateProducts/{productId}")
    public ResponseEntity<Products> updateProduct(@PathVariable Long productId, @RequestBody Products updatedProduct) 
   {
    	return productService.updateProduct(productId, updatedProduct);
   }
    
    
//................Provide an endpoint for rating a product, which should update the product's ratings array.................
    
    
    @PostMapping("rate/{productId}")
    public boolean rateProduct(@PathVariable Long productId,@RequestBody Rating rating)
    {
    	return productService.rateProduct(productId, rating);
    }
    
    
// ...................Implement pagination and sorting in the product list retrieval endpoint...................
   
    
    @GetMapping("/pagination")
    public List<Products> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        return productService.getProducts(page, size, sortBy, sortOrder);
    }

         
}


