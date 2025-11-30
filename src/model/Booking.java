package model;

public class Booking {
    private String bookingId;
    private String flightId;
    private String passengerId;
    private String seatNo;
    private String status; // CONFIRMED or CANCELLED

    public Booking(String bookingId, String flightId, String passengerId, String seatNo, String status) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.passengerId = passengerId;
        this.seatNo = seatNo;
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", flightId='" + flightId + '\'' +
                ", passengerId='" + passengerId + '\'' +
                ", seatNo='" + seatNo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
