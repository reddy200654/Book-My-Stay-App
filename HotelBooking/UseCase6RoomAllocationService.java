
public class UseCase6RoomAllocationService {


    public static void main(String[] args) {
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        System.out.println("Room Allocation Processing");

        while (bookingQueue.hasPendingRequests()) {
            Reservation reservation = bookingQueue.getNextRequest();
            allocationService.allocateRoom(reservation, inventory);
        }
    }
}
