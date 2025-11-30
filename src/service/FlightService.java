package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Flight;
import util.CSVUtils;

public class FlightService {
    private List<Flight> flights;
    private final String FILE_PATH = "data/flights.csv";

    public FlightService() {
        flights = new ArrayList<>();
        loadFlights();
    }

    private void loadFlights() {
        try {
            List<String[]> rows = CSVUtils.readAll(FILE_PATH);
            // Skip header
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length < 7)
                    continue;
                flights.add(new Flight(
                        row[0], row[1], row[2], row[3], row[4],
                        Integer.parseInt(row[5]), Integer.parseInt(row[6])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Flight> getAllFlights() {
        return flights;
    }

    public Flight getFlightById(String id) {
        for (Flight f : flights) {
            if (f.getFlightId().equals(id))
                return f;
        }
        return null;
    }

    public boolean addFlight(Flight f) {
        // Generate ID if not present or just assume it's passed?
        // Requirements say "Generate auto-increment IDs: F001, F002..."
        // So we should generate it here.

        String newId = generateId();
        f.setFlightId(newId);

        flights.add(f);
        saveFlights();
        return true;
    }

    public boolean updateFlight(Flight updated) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlightId().equals(updated.getFlightId())) {
                flights.set(i, updated);
                saveFlights();
                return true;
            }
        }
        return false;
    }

    public boolean deleteFlight(String id) {
        boolean removed = flights.removeIf(f -> f.getFlightId().equals(id));
        if (removed) {
            saveFlights();
        }
        return removed;
    }

    private void saveFlights() {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "flight_id", "origin", "destination", "departure", "arrival", "capacity",
                "seats_left" });
        for (Flight f : flights) {
            rows.add(new String[] {
                    f.getFlightId(), f.getOrigin(), f.getDestination(),
                    f.getDeparture(), f.getArrival(),
                    String.valueOf(f.getCapacity()), String.valueOf(f.getSeatsLeft())
            });
        }
        try {
            CSVUtils.writeAll(FILE_PATH, rows);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateId() {
        int maxId = 0;
        for (Flight f : flights) {
            try {
                int idNum = Integer.parseInt(f.getFlightId().substring(1));
                if (idNum > maxId)
                    maxId = idNum;
            } catch (NumberFormatException e) {
                // Ignore invalid IDs
            }
        }
        return String.format("F%03d", maxId + 1);
    }
}
