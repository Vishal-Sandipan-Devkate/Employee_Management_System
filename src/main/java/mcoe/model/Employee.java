package mcoe.model;

import java.time.LocalDateTime;

public record Employee(
        long id,
        String name,
        String email,
        String department,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
