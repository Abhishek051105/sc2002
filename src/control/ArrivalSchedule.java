package control;

import entity.Customer;
import entity.RegularCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ArrivalSchedule {
    private final Set<Integer> arrivalTurns;
    private int nextCustomerId = 1;

    public ArrivalSchedule(Set<Integer> arrivalTurns) {
        this.arrivalTurns = arrivalTurns;
    }

    /** Empty list if nobody arrives this turn. Creates new RegularCustomer(id, turn). */
    public List<Customer> customersArrivingAt(int turn) { 
        List<Customer> list = new ArrayList<>();
        if(arrivalTurns.contains(turn)){
            list.add(new RegularCustomer(nextCustomerId,turn));
            nextCustomerId++;
        }
        return list;
    }
}
