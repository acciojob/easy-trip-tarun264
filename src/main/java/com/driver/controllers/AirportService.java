package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AirportService {
    AirportRepository airportRepository= new AirportRepository();

       public void add(Airport airport) {
        airportRepository.add(airport);
    }

    public String getLargest() {
        return airportRepository.getLargest();
    }

    public Double getShortestDuration(City fromCity, City toCity) {
        return airportRepository.getShortestDuration(fromCity,toCity);
    }

    public String addFlight(Flight flight) {
           return airportRepository.addFlight(flight);
    }

    public String addPassenger(Passenger passenger) {
          return airportRepository.addPassenger(passenger);
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
           return airportRepository.getNumberOfPeopleOn(date,airportName);
    }


    public int calculateFare(Integer flightId) {
           return airportRepository.calculateFare(flightId);
    }

    public String bookTicket(Integer flightId, Integer passengerId) {
           return airportRepository.bookTicket(flightId,passengerId);
    }

    public String cancelTicket(Integer flightId, Integer passengerId) {
           return airportRepository.cancelTicket(flightId,passengerId);
    }

    public int getCountOfBooking(Integer passengerId) {
           return airportRepository.getCountOfBooking(passengerId);
    }

    public String getAirportName(Integer flightId) {
           return airportRepository.getAirportName(flightId);
    }

    public int calculateRevenue(Integer flightId) {
           return airportRepository.calculateRevenue(flightId);
    }
}
