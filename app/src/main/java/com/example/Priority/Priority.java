package com.example.Priority;

public enum Priority {
    LOW(3),
    MEDIUM(2),
    HIGH(1);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getIntValue() {
        return priority;
    }

    public Priority getPriorityValue() {
        switch (priority) {
            case 1:
                return Priority.HIGH;

            case 2:
                return Priority.MEDIUM;

            case 3:
                return Priority.LOW;
        }
        return null;
    }
}
