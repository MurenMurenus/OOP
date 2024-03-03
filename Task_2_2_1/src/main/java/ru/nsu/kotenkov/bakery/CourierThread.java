package ru.nsu.kotenkov.bakery;

import ru.nsu.kotenkov.bakery.exceptions.CourierInterrupted;

public class CourierThread extends Thread {
    // TODO add myself
    public int id;
    private int capacity;
    private boolean ready = false;
    private int orderShippingDuration;
    private int speed;

    public CourierThread (int id, int capacity, int orderShippingDuration) {
        this.id = id;
        this.capacity = capacity;
        this.orderShippingDuration = orderShippingDuration;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setOrderShippingDuration(int orderShippingDuration) {
        this.orderShippingDuration = orderShippingDuration;
    }

    @Override
    public synchronized void run() {
        this.ready = false;
        try {
            Thread.sleep(this.orderShippingDuration / this.speed);
        } catch (InterruptedException e) {
            throw new CourierInterrupted("Baker " + this + " was interrupted while cooking.\n");
        }
        this.ready = true;
        this.orderShippingDuration = -1;
    }
}
