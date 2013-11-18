

package binding.ObjectCompBinding;

import com.jgoodies.binding.beans.Model;

    public class CustomerBean extends Model {
        public static final String NAME_PROPERTY = "name";
        public static final String PREFERRED_PROPERTY = "preferred";

        private String name = "";
        private boolean preferred;

        public String getName() {
            return name;
        }

        public void setName(String newName) {
            String oldValue = name;
            name = newName;
            firePropertyChange(NAME_PROPERTY, oldValue, newName);
        }

        public boolean isPreferred() {
            return preferred;
        }

        public void setPreferred(boolean isPreferred) {
            boolean oldValue = preferred;
            preferred = isPreferred;
            firePropertyChange(PREFERRED_PROPERTY, oldValue, isPreferred);
        }

    }
