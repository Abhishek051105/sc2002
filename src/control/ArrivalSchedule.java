package control;

import entity.Customer;
import entity.RegularCustomer;
import entity.VIPCustomer;
import entity.CriticCustomer;
import entity.CustomerType;

import java.util.ArrayList;
import java.util.List;


public class ArrivalSchedule {
    private final List<Integer> arrivalTurns;
    private final List<CustomerType> arrivalTypes;
    private int nextCustomerId = 1;

   
    public ArrivalSchedule(List<Integer> arrivalTurns, List<CustomerType> arrivalTypes) {
        this.arrivalTurns = arrivalTurns;
        this.arrivalTypes = arrivalTypes;
    }

    
    public List<Customer> customersArrivingAt(int turn) {
        List<Customer> list = new ArrayList<>();
        for (int i = 0; i < arrivalTurns.size(); i++) {
            if (arrivalTurns.get(i) == turn) {
                list.add(createCustomer(arrivalTypes.get(i), nextCustomerId, turn));
                nextCustomerId++;
            }
        }
        return list;
    }

  
    private Customer createCustomer(CustomerType type, int id, int turn) {
        switch (type) {
            case VIP:
                return new VIPCustomer(id, turn);
            case CRITIC:
                return new CriticCustomer(id, turn);
            default:
                return new RegularCustomer(id, turn);
        }
    }
}
