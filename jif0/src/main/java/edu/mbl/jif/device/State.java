package edu.mbl.jif.device;

/**
 *
 * @author GBH
 */// For use in binding a list to a JComboBox
import java.util.ArrayList;
import java.util.List;

// ... needs equals and toString methods
import org.jdesktop.beansbinding.Converter;

public class State {

    int state;
    String description;
    // StateList for this type of State
    List<State> stateList = new ArrayList<State>();
//    static {
//        stateList = new ArrayList<State>();
    // load list of states for this type
//        stateList.add(new State(1, "red"));
//        stateList.add(new State(2, "blue"));
//        stateList.add(new State(3, "green"));
//    }
    public static void loadStateList() {
        
    }

    public List getStateList() {
        return stateList;
    }

    public void addToStateList(String stateName) {
        int currentNumberOfStates = stateList.size();
        stateList.add(new State(currentNumberOfStates + 1, stateName));
    }

    public State(int state, String description) {
        this.state = state;
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof State && ((State) other).state == state;
    }

    // ???
//    @Override
//    public int hashCode()
//      {
//        int hash = 3;
//        hash = 29 * hash + this.state;
//        hash = 29 * hash + (this.description != null ? this.description.hashCode() : 0);
//        return hash;
//      }
    @Override
    public String toString() {
        return getDescription();
    }
    // Converter 
    /// ??? 
    Converter<Integer, State> stateConverter = new Converter<Integer, State>() {

        public State convertForward(Integer value) {
            return new State(value, "");
        }

        public Integer convertReverse(State value) {
            return value.getState();
        }

    };

    public static void main(String[] args) {
//        State state = new State();
//        state.addToStateList("red");
//        state.addToStateList("green");
//        state.addToStateList("blue");
//        state.getStateList();
//        for (State s : state.getStateList())
//          {
//          }

    }

}