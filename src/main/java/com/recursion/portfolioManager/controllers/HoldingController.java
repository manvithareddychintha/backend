package com.recursion.portfolioManager.controllers;

import com.recursion.portfolioManager.DTO.HoldingRequest;
import com.recursion.portfolioManager.DTO.HoldingWithPriceDTO;
import com.recursion.portfolioManager.models.Holdings;
import com.recursion.portfolioManager.repositories.HoldingsRepository;
import com.recursion.portfolioManager.services.HoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/holdings")
public class HoldingController {

    @Autowired
    private HoldingsService holdingService;
    @Autowired
    private HoldingsRepository holdingsRepository;

    @PostMapping
    public ResponseEntity<Object> addHolding(@RequestBody HoldingRequest request) {
        try {
            Holdings savedHolding = holdingService.saveHolding(request);
            return new ResponseEntity<>(savedHolding, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving holding", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Holdings>> getHoldings(){
        List<Holdings> holdingsList=holdingService.getAll();
        return ResponseEntity.ok(holdingsList);
    }


    @GetMapping("/latest")
    public ResponseEntity<Object> getHoldingsWithLatestPrice() {
        try {
            List<HoldingWithPriceDTO> holdingsWithLatestPrice = holdingService.findHoldingsWithLatestPriceAndProfit();
            return ResponseEntity.ok(holdingsWithLatestPrice);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching holdings with latest price", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PutMapping("/{id}")
//    public ResponseEntity updateStocks(@PathVariable Long id,@RequestBody HoldingRequest holdings)
//    {
//        holdingService.update(id,holdings);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteStocks(@PathVariable Long id)
//    {
//        holdingService.deleteById(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity fetchById(@PathVariable Long id)
//    {
//        return holdingService.fetchById(id);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStocks(@PathVariable Long id, @RequestBody HoldingRequest holdings) {
        try {
            // Attempt to update the holding
            Holdings updatedHolding = holdingService.update(id, holdings);

            // If the holding does not exist
            if (updatedHolding == null) {
                return new ResponseEntity<>("Holding with id " + id + " not found", HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok(updatedHolding);  // Return the updated holding
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating holding", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStocks(@PathVariable Long id) {
        try {
            // Attempt to delete the holding
            boolean deleted = holdingService.deleteById(id);

            // If the holding does not exist
            if (!deleted) {
                return new ResponseEntity<>("Holding with id " + id + " not found", HttpStatus.NOT_FOUND);
            }

            return ResponseEntity.ok("Holding with id " + id + " deleted successfully");  // Return success message
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting holding", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchById(@PathVariable Long id) {
        try {
            // Attempt to fetch the holding
            Holdings holding;
            Optional<Holdings> op        = holdingService.fetchById(id);

            // If the holding is not found
            try{
                return ResponseEntity.ok(op.get());  // Return the holding data
            }catch(Exception e){

                return new ResponseEntity<>("Holding with id " + id + " not found", HttpStatus.NOT_FOUND);
            }


        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching holding", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
