package arc.hotelapp.rooms.rooms_fragment;

import org.json.JSONObject;

public class Room {


    private String roomId;
    private String roomNumber;

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    private String hotelId;
    private boolean isBooked;
    private String bookingId;
    private String roomType;
    private String capacity;
    private String lastModified;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Room(String roomId, String roomNumber, String hotelId, boolean isBooked, String bookingId, String roomType, String capacity) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.hotelId = hotelId;
        this.isBooked = isBooked;
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.capacity = capacity;
    }

    public static Room parse(JSONObject json) throws Exception
    {
        String roomNumber = json.getString("roomNumber");
        String hotelId = json.getString("hotelId");
        String roomId = json.getString("roomId");
        String bookingId = json.getString("bookingId");
        String roomType = json.getString("roomType");
        String capacity = json.getString("capacity");
        boolean booked = json.getBoolean("booked");
        Room room = new Room(roomId,roomNumber,hotelId,booked,bookingId,roomType,capacity);
        return room;
    }
}

