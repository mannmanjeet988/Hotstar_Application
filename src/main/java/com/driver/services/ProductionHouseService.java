package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){

        ProductionHouse productionHouse = new ProductionHouse();
        productionHouse.setName(productionHouseEntryDto.getName());

       List<WebSeries> webSeriesList =  productionHouse.getWebSeriesList();
       productionHouse.setWebSeriesList(webSeriesList);

        productionHouseRepository.save(productionHouse);
//        double sum = 0;
//        for(WebSeries webseries: webSeriesList){
//            double rating = webseries.getRating();
//             sum += rating;
//        }
//          double average = sum / webSeriesList.size();
//        return (int) average;
        return 0;
       // return  null;
    }



}
