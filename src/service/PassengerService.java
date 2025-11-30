package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Passenger;
import util.CSVUtils;

public class PassengerService {
    private List<Passenger> passengers;
    private final String FILE_PATH = "data/passengers.csv";

    public PassengerService() {
        passengers = new ArrayList<>();
        loadPassengers();
    }

    private void loadPassengers() {
        try {
            List<String[]> rows = CSVUtils.readAll(FILE_PATH);
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length < 5)
                    continue;
                passengers.add(new Passenger(
                        row[0], row[1], Integer.parseInt(row[2]), row[3], row[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Passenger> getAllPassengers() {
        return passengers;
    }

    public Passenger getPassengerById(String id) {
        for (Passenger p : passengers) {
            if (p.getPassengerId().equals(id))
                return p;
        }
        return null;
    }

    public boolean addPassenger(Passenger p) {
        String newId = generateId();
        p.setPassengerId(newId);
        passengers.add(p);
        savePassengers();
        return true;
    }

    public boolean updatePassenger(Passenger p) {
        for (int i = 0; i < passengers.size(); i++) {
            if (passengers.get(i).getPassengerId().equals(p.getPassengerId())) {
                passengers.set(i, p);
                savePassengers();
                return true;
            }
        }
        return false;
    }

    public boolean deletePassenger(String id) {
        boolean removed = passengers.removeIf(p -> p.getPassengerId().equals(id));
        if (removed) {
            savePassengers();
        }
        return removed;
    }

    private void savePassengers() {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "passenger_id", "name", "age", "gender", "passport" });
        for (Passenger p : passengers) {
            rows.add(new String[] {
                    p.getPassengerId(), p.getName(), String.valueOf(p.getAge()),
                    p.getGender(), p.getPassportNumber()
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
        for (Passenger p : passengers) {
            try {
                int idNum = Integer.parseInt(p.getPassengerId().substring(1));
                if (idNum > maxId)
                    maxId = idNum;
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return String.format("P%03d", maxId + 1);
    }
}
