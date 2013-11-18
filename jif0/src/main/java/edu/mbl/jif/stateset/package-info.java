/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.stateset;

/*
A Model controls and maintains a current state.
* 
Models are ‘JavaBeans’

* A SavedState is a sub-set of the properties of a Model.
- dynamically created Bean with properties that correspond to a defined sub-set of a Model’s properties
- can capture the current state of a Model’s properties
- can be modified using a PropertyEditor
- can be applied to a Model to restore it to the remembered state

* A SavedStateSet can be used as a ‘Mode’
- Contains one or more SavedStates, containing properties to apply to one or more Models
- Saved to and restored from an XML file

* StateSetManager
- Manages SavedStateSets
- Implemented for microscope imaging application, as a Mode Manager

 */