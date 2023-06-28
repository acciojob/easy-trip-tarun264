package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AirportRepository {

    HashMap<String, City> CityDB= new HashMap<>();
    HashMap<Integer, Passenger> PassengerDB= new HashMap<>();
    HashMap<Integer, Flight> FlightDB= new HashMap<>();
    HashMap<String,Airport> AirportDB= new HashMap<>();
    HashMap<Integer, List<Integer>> FlightToPassengerDB= new HashMap<>();


    public void add(Airport airport) {
        String name=airport.getAirportName();
        AirportDB.put(name,airport);
    }

    public String getLargest() {
        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName

        int maxi= Integer.MIN_VALUE;
        String s="";

        for (Airport airport: AirportDB.values()) {
            if (airport.getNoOfTerminals() > maxi) {
                maxi = airport.getNoOfTerminals();
                s = airport.getAirportName();
            } else if (airport.getNoOfTerminals() == maxi) {
                if (airport.getAirportName().compareTo(s) < 0) { // Lexicographically smallest airportName
                    s = airport.getAirportName();
                }
            }
        }
        return s;
    }

    public String addFlight(Flight flight) {
        int flightID=flight.getFlightId();
        FlightDB.put(flightID,flight);
        return "SUCCESS";
    }


    public Double getShortestDuration(City fromCity, City toCity) {
        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.

        double ans=Double.MAX_VALUE;

        for(Flight flights:FlightDB.values()) {
            if (flights.getFromCity().equals(fromCity) && flights.getToCity().equals(toCity)) {
                if (ans > flights.getDuration()) ans = flights.getDuration();

            }
        }
        if(ans==Double.MAX_VALUE) {
            return Double.valueOf(-1);
        }
        return ans;

    }

    public String addPassenger(Passenger passenger) {
        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        int passengerID= passenger.getPassengerId();
        PassengerDB.put(passengerID,passenger);
        return "SUCCESS";
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight
        int count = 0;
        Airport airport = new Airport();
        for (Flight flight : FlightDB.values()) {
            if (flight.getFlightDate().equals(date) && airport.getAirportName().equals(airportName)) {
                if (flight.getFromCity().equals(airport.getCity()) && flight.getToCity().equals(airport.getCity())) {
                    // number of passenger in having the flight
                    int flightID = flight.getFlightId();
                    // number of passengers travelling to that flight id
                    count += FlightToPassengerDB.get(flightID).size();
                }
            }
        }
        return count;
    }


    public int calculateFare(Integer flightId) {
        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price

        int bookedFlight=0;
        if(!FlightToPassengerDB.containsKey(flightId)){
            return 0;
        }
           int bookedPassenger=FlightToPassengerDB.get(flightId).size();
        return 3000+bookedPassenger*50;
        }


    public String bookTicket(Integer flightId, Integer passengerId) {
        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS

        //if no passenger has booked flight and its less than max capacity
        if(!FlightToPassengerDB.containsKey(flightId)){
            return "FAILURE";
        }
        if (FlightToPassengerDB.get(flightId) != null && FlightToPassengerDB.get(flightId).size() <= FlightDB.get(flightId).getMaxCapacity()) {
            List<Integer> passengers = new ArrayList<>(FlightToPassengerDB.get(flightId));
            // if the passenger is already added
            if (passengers.contains(passengerId)) {
                return "FAILURE";
            }
            passengers.add(passengerId);
            FlightToPassengerDB.put(flightId, passengers);
            return "SUCCESS";
            //if the passengers list is empty
        } else if (FlightToPassengerDB.get(flightId) == null) {
            List<Integer> passengersList = new ArrayList<>(FlightToPassengerDB.get(flightId));
            // if the passenger is already added
            if (passengersList.contains(passengerId)) {
                return "FAILURE";
            }
            passengersList.add(passengerId);
            FlightToPassengerDB.put(flightId, passengersList);
            return "SUCCESS";
        }

    return "FAILURE";

        }


    public String cancelTicket(Integer flightId, Integer passengerId) {
        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
                // then return a "FAILURE" message
                // Otherwise return a "SUCCESS" message
                // and also cancel the ticket that passenger had booked earlier on the given flightId


        List<Integer> passengers= new ArrayList<>(FlightToPassengerDB.get(flightId));

        if(!FlightToPassengerDB.containsKey(flightId)){
            return "FAILURE";
        }

        for(List<Integer> passengersList: FlightToPassengerDB.values()){

        if (passengersList.contains(passengerId) && FlightToPassengerDB.keySet().equals(flightId)) {
            passengersList.remove(passengerId);
            PassengerDB.remove(passengerId);
            return "SUCCESS";
        }
    }
        return "FAILURE";

}

    public int getCountOfBooking(Integer passengerId) {
        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        int count=0;
       for(Integer flight: FlightToPassengerDB.keySet()) {
           List<Integer> passengers = FlightToPassengerDB.get(flight);
           for (Integer passenger : passengers) {
               if (passengers.contains(passengerId)) {
                   count++;
               }
           }
       }
       return count;
}


    public String getAirportName(Integer flightId) {
        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName

        String name = "";
        City city = FlightDB.get(flightId).getFromCity();
        if(FlightDB.containsKey(flightId)) {
            for (Airport airport : AirportDB.values()) {
                if (airport.getCity().equals(city)) {
                    return name = airport.getAirportName();
                }
            }
        }
        return null;
    }


    public int calculateRevenue(Integer flightId) {
        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        int noOfPassengers = FlightToPassengerDB.get(flightId).size();
        int variableFare = (noOfPassengers*(noOfPassengers+1))*25;
        int fixedFare = 3000*noOfPassengers;
        int totalFare = variableFare + fixedFare;

        return totalFare;


    }
}





