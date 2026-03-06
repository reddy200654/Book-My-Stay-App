import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ================================================================
 * CLASS - RoomAllocationService
 * ================================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class is responsible for confirming
 * booking requests and assigning rooms.
 *
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 *
 * @version 6.0
 */
public class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRoomIds;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /** Initializes allocation tracking structures. */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * a unique room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        String inventoryRoomType = mapToInventoryRoomType(roomType);

        Integer available = inventory.getRoomAvailability().get(inventoryRoomType);
        if (available == null || available <= 0) {
            System.out.println("Booking failed for Guest: " + reservation.getGuestName()
                    + ", No rooms available for type: " + roomType);
            return;
        }

        String roomId = generateRoomId(roomType);

        allocatedRoomIds.add(roomId);
        assignedRoomsByType.computeIfAbsent(roomType, key -> new HashSet<>()).add(roomId);

        inventory.updateAvailability(inventoryRoomType, available - 1);

        System.out.println("Booking confirmed for Guest: " + reservation.getGuestName()
                + ", Room ID: " + roomId);
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        Set<String> roomTypeAssignments = assignedRoomsByType
                .computeIfAbsent(roomType, key -> new HashSet<>());

        int nextNumber = roomTypeAssignments.size() + 1;
        String roomId = roomType + "-" + nextNumber;

        while (allocatedRoomIds.contains(roomId)) {
            nextNumber++;
            roomId = roomType + "-" + nextNumber;
        }

        return roomId;
    }

    /** Maps reservation room labels to inventory keys. */
    private String mapToInventoryRoomType(String roomType) {
        if ("Single".equalsIgnoreCase(roomType) || "Single Room".equalsIgnoreCase(roomType)) {
            return "Single Room";
        }
        if ("Double".equalsIgnoreCase(roomType) || "Double Room".equalsIgnoreCase(roomType)) {
            return "Double Room";
        }
        if ("Suite".equalsIgnoreCase(roomType) || "Suite Room".equalsIgnoreCase(roomType)) {
            return "Suite Room";
        }
        return roomType;
    }
}
