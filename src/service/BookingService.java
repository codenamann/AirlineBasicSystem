package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import model.Flight;
import util.CSVUtils;

public class BookingService {
    private List<Booking> bookings;
    private final String FILE_PATH = "data/bookings.csv";
    private FlightService flightService;
    private PassengerService passengerService;

    public BookingService(FlightService flightService, PassengerService passengerService) {
        this.flightService = flightService;
        this.passengerService = passengerService;
        bookings = new ArrayList<>();
        loadBookings();
    }

    private void loadBookings() {
        try {
            List<String[]> rows = CSVUtils.readAll(FILE_PATH);
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                if (row.length < 5)
                    continue;
                bookings.add(new Booking(
                        row[0], row[1], row[2], row[3], row[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    public Booking getBookingById(String id) {
        for (Booking b : bookings) {
            if (b.getBookingId().equals(id))
                return b;
        }
        return null;
    }

    public boolean createBooking(String flightId, String passengerId) {
        Flight flight = flightService.getFlightById(flightId);
        if (flight == null || flight.getSeatsLeft() <= 0) {
            return false;
        }

        // Auto-assign seat number: "Seat-" + (capacity - seatsLeft + 1)
        // Wait, if seatsLeft is 100 and capacity is 100, seat is 1.
        // If seatsLeft is 99, seat is 2. Correct.
        String seatNo = "Seat-" + (flight.getCapacity() - flight.getSeatsLeft() + 1);

        String bookingId = generateId();
        Booking booking = new Booking(bookingId, flightId, passengerId, seatNo, "CONFIRMED");
        bookings.add(booking);

        // Reduce seatsLeft
        flight.setSeatsLeft(flight.getSeatsLeft() - 1);
        flightService.updateFlight(flight);

        saveBookings();
        return true;
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking == null || "CANCELLED".equals(booking.getStatus())) {
            return false;
        }

        booking.setStatus("CANCELLED");

        // Increment seatsLeft
        Flight flight = flightService.getFlightById(booking.getFlightId());
        if (flight != null) {
            flight.setSeatsLeft(flight.getSeatsLeft() + 1);
            flightService.updateFlight(flight);
        }

        saveBookings();
        return true;
    }

    private void saveBookings() {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] { "booking_id", "flight_id", "passenger_id", "seat_no", "status" });
        for (Booking b : bookings) {
            rows.add(new String[] {
                    b.getBookingId(), b.getFlightId(), b.getPassengerId(),
                    b.getSeatNo(), b.getStatus()
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
        for (Booking b : bookings) {
            try {
                int idNum = Integer.parseInt(b.getBookingId().substring(1));
                if (idNum > maxId)
                    maxId = idNum;
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return String.format("B%03d", maxId + 1);
    }
}
