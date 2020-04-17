package arc.hotelapp.bookings;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class Booking {

    @PrimaryKey
    @NonNull
    private String bookingId;
    private String visitorEmail;
    private String roomId;
    private String roomNumber;
    private String hotelName;
    private String no_of_guests;
    private String checkIn;
    private String checkOut;

    public Booking(@NonNull String bookingId, String visitorEmail, String roomId, String roomNumber, String hotelName, String no_of_guests, String checkIn, String checkOut) {
        this.bookingId = bookingId;
        this.visitorEmail = visitorEmail;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.hotelName = hotelName;
        this.no_of_guests = no_of_guests;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }


    public static Booking parse(JSONObject response) throws Exception{
        String bId = response.getString("bookingId");
        String hotelName = response.getString("hotelName");
        String roomNumber = response.getString("roomNumber");
        String roomId = response.getString("roomId");
        String no_of_guests = response.getString("no_of_guests");
        String checKIn = response.getString("checkIn").substring(0,10);
        String checkOut = response.getString("checkOut").substring(0,10);
        String email = response.getString("visitorEmail");
        Booking booking = new Booking(bId, email,roomId,roomNumber,hotelName,no_of_guests,checKIn,checkOut);

        return booking;
    }

    @NonNull
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(@NonNull String bookingId) {
        this.bookingId = bookingId;
    }

    public String getVisitorEmail() {
        return visitorEmail;
    }

    public void setVisitorEmail(String visitorEmail) {
        this.visitorEmail = visitorEmail;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getNo_of_guests() {
        return no_of_guests;
    }

    public void setNo_of_guests(String no_of_guests) {
        this.no_of_guests = no_of_guests;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}
