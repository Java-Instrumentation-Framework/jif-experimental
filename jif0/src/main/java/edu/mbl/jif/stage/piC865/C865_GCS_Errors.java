package edu.mbl.jif.stage.piC865;

/**
 *
 * @author GBH
 */
public class C865_GCS_Errors {

// Communication errorcodes.
    public static final int COM_NO_ERROR = 0; // No error occurred during function call.

    public static final int COM_ERROR = -1; // Error during com operation (could not be specified).

    public static final int SEND_ERROR = -2; // Error while sending data.

    public static final int REC_ERROR = -3; // Error while receiving data.

    public static final int NOT_CONNECTED_ERROR = -4; // Not connected (no port with given ID open).

    public static final int COM_BUFFER_OVERFLOW = -5; // Buffer overflow.

    public static final int CONNECTION_FAILED = -6; // Error while opening port.

    public static final int COM_TIMEOUT = -7; // Timeout error.

    public static final int COM_MULTILINE_RESPONSE = -8; // There are more lines waiting in buffer.

    public static final int COM_INVALID_ID = -9; // There is no interface open with the given ID.

    public static final int COM_NOTIFY_EVENT_ERROR = -10; // The event for the notification could not be opened.

    public static final int COM_NOT_IMPLEMENTED = -11; // The function was not implemented (e.g. only RS-232 communication provides this feature and it was called for IEEE488).

    public static final int COM_ECHO_ERROR = -12; // Error while sending "echoed" data .

    public static final int COM_GPIB_EDVR = -13; // IEEE488: System error.

    public static final int COM_GPIB_ECIC = -14; // IEEE488: Function requires GPIB board to be CIC.

    public static final int COM_GPIB_ENOL = -15; // IEEE488: Write function detected no Listeners.

    public static final int COM_GPIB_EADR = -16; // IEEE488: Interface board not addressed correctly.

    public static final int COM_GPIB_EARG = -17; // IEEE488: Invalid argument to function call.

    public static final int COM_GPIB_ESAC = -18; // IEEE488: Function requires GPIB board to be SAC.

    public static final int COM_GPIB_EABO = -19; // IEEE488: I/O operation aborted.

    public static final int COM_GPIB_ENEB = -20; // IEEE488: Non-existent interface board.

    public static final int COM_GPIB_EDMA = -21; // IEEE488: Error performing DMA.

    public static final int COM_GPIB_EOIP = -22; // IEEE488: I/O operation started before previous operation completed.

    public static final int COM_GPIB_ECAP = -23; // IEEE488: No capability for intended operation.

    public static final int COM_GPIB_EFSO = -24; // IEEE488: File system operation error.

    public static final int COM_GPIB_EBUS = -25; // IEEE488: Command error during device call.

    public static final int COM_GPIB_ESTB = -26; // IEEE488: Serial poll status byte lost.

    public static final int COM_GPIB_ESRQ = -27; // IEEE488: SRQ remains asserted.

    public static final int COM_GPIB_ETAB = -28; // IEEE488: The return buffer is full.

    public static final int COM_GPIB_ELCK = -29; // IEEE488: Address or board is locked.

    public static final int COM_RS_INVALID_DATA_BITS = -30; // RS-232: The use of 5 data bits with 2 stop bits is an invalid combination, as is 6, 7, or 8 data bits with 1.5 stop bits.

    public static final int COM_ERROR_RS_SETTINGS = -31; // RS-232: Error when configuring the COM port.

    public static final int COM_INTERNAL_RESOURCES_ERROR = -32; // Error when dealing with internal system resources (events, threads, ...).

    public static final int COM_DLL_FUNC_ERROR = -33; // A DLL or one of the required functions could not be loaded.

    public static final int COM_MAX_ERROR = -1000;
// PI CONTROLLER errors
    public static final int PI_UNKNOWN_AXIS_IDENTIFIER = (COM_MAX_ERROR - 1); // Unknown axis identifier.

    public static final int PI_NR_NAV_OUT_OF_RANGE = (COM_MAX_ERROR - 2); // Number for \c NAV out of range - must be in [1,10000].

    public static final int PI_INVALID_SGA = (COM_MAX_ERROR - 3); // Invalid value for \c SGA - must be one of {1, 10, 100, 1000}.

    public static final int PI_UNEXPECTED_RESPONSE = (COM_MAX_ERROR - 4); // Controller has sent unexpected response.

    public static final int PI_NO_MANUAL_PAD = (COM_MAX_ERROR - 5); // No manual control pad installed, calls to \c SMA and related commands are not allowed.

    public static final int PI_INVALID_MANUAL_PAD_KNOB = (COM_MAX_ERROR - 6); // Invalid number for manual control pad knob.

    public static final int PI_INVALID_MANUAL_PAD_AXIS = (COM_MAX_ERROR - 7); // Axis not currently controlled by a manual control pad.

    public static final int PI_CONTROLLER_BUSY = (COM_MAX_ERROR - 8); // Controller is busy with some lengthy operation (e.g. reference movement, fast scan algorithm).

    public static final int PI_THREAD_ERROR = (COM_MAX_ERROR - 9); // Internal error - could not start thread.

    public static final int PI_IN_MACRO_MODE = (COM_MAX_ERROR - 10); // Controller is (already) in macro mode - command not valid in macro mode.

    public static final int PI_NOT_IN_MACRO_MODE = (COM_MAX_ERROR - 11); // Controller not in macro mode - command not valid unless macro mode active.

    public static final int PI_MACRO_FILE_ERROR = (COM_MAX_ERROR - 12); // Could not open file to write macro or to read macro.

    public static final int PI_NO_MACRO_OR_EMPTY = (COM_MAX_ERROR - 13); // No macro with given name on controller or macro is empty.

    public static final int PI_MACRO_EDITOR_ERROR = (COM_MAX_ERROR - 14); // Internal error in macro editor.

    public static final int PI_INVALID_ARGUMENT = (COM_MAX_ERROR - 15); // One of the arguments given to the function is invalid (empty string, index out of range, ...).

    public static final int PI_AXIS_ALREADY_EXISTS = (COM_MAX_ERROR - 16); // Axis identifier is already in use for a connected stage.

    public static final int PI_INVALID_AXIS_IDENTIFIER = (COM_MAX_ERROR - 17); // Invalid axis identifier.

    public static final int PI_COM_ARRAY_ERROR = (COM_MAX_ERROR - 18); // Could not access array data in COM server.

    public static final int PI_COM_ARRAY_RANGE_ERROR = (COM_MAX_ERROR - 19); // Range of array does not fit the number of parameters.

    public static final int PI_INVALID_SPA_CMD_ID = (COM_MAX_ERROR - 20); // Command ID given to \c SPA or \c SPA? is not valid.

    public static final int PI_NR_AVG_OUT_OF_RANGE = (COM_MAX_ERROR - 21); // Number for \c AVG out of range - must be >0.

    public static final int PI_WAV_SAMPLES_OUT_OF_RANGE = (COM_MAX_ERROR - 22); // Number of samples given to \c WAV out of range.

    public static final int PI_WAV_FAILED = (COM_MAX_ERROR - 23); // Generation of wave failed.

    public static final int PI_MOTION_ERROR = (COM_MAX_ERROR - 24); // motion error while stage was moving.

    public static final int PI_RUNNING_MACRO = (COM_MAX_ERROR - 25); // Controller is (already) running a macro.

    public static final int PI_PZT_CONFIG_FAILED = (COM_MAX_ERROR - 26); // Configuration of PZT stage or amplifier failed.

    public static final int PI_PZT_CONFIG_INVALID_PARAMS = (COM_MAX_ERROR - 27); // Current settings are not valid for desired configuration.

    public static final int PI_UNKNOWN_CHANNEL_IDENTIFIER = (COM_MAX_ERROR - 28); // Unknown channel identifier.

    public static final int PI_WAVE_PARAM_FILE_ERROR = (COM_MAX_ERROR - 29); // Error while reading/writing to wave generator parameter file.

    public static final int PI_UNKNOWN_WAVE_MACRO = (COM_MAX_ERROR - 30); // Could not find description of wave form. Maybe WG.INI is missing?

    public static final int PI_WAVE_MACRO_FUNC_NOT_LOADED = (COM_MAX_ERROR - 31); // The function of WGMacro DLL was not found at startup.

    public static final int PI_USER_CANCELLED = (COM_MAX_ERROR - 32); // The user cancelled a dialog.

    public static final int PI_C844_ERROR = (COM_MAX_ERROR - 33); // Error forn The C844 Controller.

    public static final int PI_DLL_NOT_LOADED = (COM_MAX_ERROR - 34); // Error forn The C844 Controller.

    public static final int PI_PARAMETERFILE_PROTECTED = (COM_MAX_ERROR - 35); // Error forn The C844 Controller.

    public static final int PI_NO_PARAMETERFILE_OPEND = (COM_MAX_ERROR - 36); // Error forn The C844 Controller.

    public static final int PI_STAGE_DOSE_NOT_EXIST = (COM_MAX_ERROR - 37); // Error forn The C844 Controller.

    public static final int PI_PARAMETERFILE_ALLREADY_OPEND = (COM_MAX_ERROR - 38); // Error forn The C844 Controller.

    public static final int PI_PARAMETERFILE__OPEN_ERROR = (COM_MAX_ERROR - 39); // DLL neccessary to call function not loaded, or function not found in DLL.

    public static final int PI_INVALID_CONTROLLER_VERSION = (COM_MAX_ERROR - 40); // The Version of the connected controller is invalid.

    public static final int PI_PARAM_SET_ERROR = (COM_MAX_ERROR - 41); // parameter could not be set with SPA, parameter on controller undefined!

    public static final int PI_NUMBER_OF_POSSIBLE_WAVES_EXCEEDED = (COM_MAX_ERROR - 42); // The Number of the possible waves has exceeded.

    public static final int PI_NUMBER_OF_POSSIBLE_GENERATORS_EXCEEDED = (COM_MAX_ERROR - 43); // The Number of the possible waves generators has exceeded.

    public static final int PI_NO_WAVE_FOR_AXIS_DEFINED = (COM_MAX_ERROR - 44); // There is no wave for the given axis defind.

    public static final int PI_CANT_STOP_OR_START_WAV = (COM_MAX_ERROR - 45); // You can ; //t stop a wave of an axis if it; //s already stopped, or start it if it; //s already started.

    public static final int PI_REFERENCE_ERROR = (COM_MAX_ERROR - 46); // Not all axes could be referenced.

    public static final int PI_REQUIRED_WAVE_MACRO_NOT_FOUND = (COM_MAX_ERROR - 47); // Could not find parameter set, required by frequency relation.

    public static final int PI_INVALID_SPP_CMD_ID = (COM_MAX_ERROR - 48); // Command ID given to \c SPP or \c SPP? is not valid.

    public static final int PI_STAGENAME_ISNT_UNIQUE = (COM_MAX_ERROR - 49); // A stagename given to \c CST isn; //t unique.
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//   Do not forget do update TranslatePIError    !
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public static final int PI_CONTROLLER_MAX_ERR = (COM_MAX_ERROR - 50);
////////////////////////////////////////////////////////////////////////////
//                      Controller error codes                             //
/////////////////////////////////////////////////////////////////////////////
    public static final int PI_CNTR_NO_ERROR = 0; // No error.

    public static final int PI_CNTR_PARAM_SYNTAX = 1; // Parameter syntax error.

    public static final int PI_CNTR_UNKNOWN_COMMAND = 2; // Unknown command.

    public static final int PI_CNTR_MOVE_WITHOUT_INI = 5; // Attempt to move before \c INI or when servo is off*/

    public static final int PI_CNTR_INVALID_SGA_PARAM = 6; // Parameter for SGA not valid.

    public static final int PI_CNTR_POS_OUT_OF_LIMITS = 7; // Position out of limits.

    public static final int PI_CNTR_VEL_OUT_OF_LIMITS = 8; // Velocity out of limits.

    public static final int PI_CNTR_STOP = 10; // Controller was stopped.

    public static final int PI_CNTR_SST_OR_SCAN_RANGE = 11; // Parameter for SST or for one of the embedded scan algorithms out of range.

    public static final int PI_CNTR_INVALID_SCAN_AXES = 12; // Invalid axis combination for fast scan.

    public static final int PI_CNTR_INVALID_NAV_PARAM = 13; // Parameter for NAV out of range.

    public static final int PI_CNTR_INVALID_ANALOG_INPUT = 14; // Invalid analog channel.

    public static final int PI_CNTR_INVALID_AXIS_IDENTIFIER = 15; // Invalid axis identifier.

    public static final int PI_CNTR_INVALID_STAGE_NAME = 16; // Unknown stage name.

    public static final int PI_CNTR_PARAM_OUT_OF_RANGE = 17; // Parameter out of range.

    public static final int PI_CNTR_INVALID_MACRO_NAME = 18; // Invalid macro name.

    public static final int PI_CNTR_MACRO_RECORD = 19; // Error while recording macro.

    public static final int PI_CNTR_MACRO_NOT_FOUND = 20; // Macro not found.

    public static final int PI_CNTR_AXIS_HAS_NO_BRAKE = 21; // Axis has no brake.

    public static final int PI_CNTR_DOUBLE_AXIS = 22; // Axis identifier given more than once.

    public static final int PI_CNTR_INVALID_AXIS = 23; // Invalid axis.

    public static final int PI_CNTR_PARAM_NR = 24; // Incorrect number of parameters.

    public static final int PI_CNTR_INVALID_REAL_NR = 25; // Invalid floating point number.

    public static final int PI_CNTR_MISSING_PARAM = 26; // Missing parameter.

    public static final int PI_CNTR_SOFT_LIMIT_OUT_OF_RANGE = 27; // Soft limit out of range.

    public static final int PI_CNTR_NO_MANUAL_PAD = 28; // No manual pad connected.

    public static final int PI_CNTR_NO_JUMP = 29; // PI_CNTR_NO_JUMP.

    public static final int PI_CNTR_INVALID_JUMP = 30; // PI_CNTR_INVALID_JUMP.

    public static final int PI_CNTR_RESERVED_31 = 31; // PI internal error code 31.

    public static final int PI_CNTR_RESERVED_32 = 32; // PI internal error code 32

    public static final int PI_CNTR_NO_RELAY_CARD = 33; // No relay card installed.

    public static final int PI_CNTR_RESERVED_34 = 34; // PI internal error code 34.

    public static final int PI_CNTR_NO_DIGITAL_INPUT = 35; // No digital input installed.

    public static final int PI_CNTR_NO_DIGITAL_OUTPUT = 36; // No digital output installed.

    public static final int PI_CNTR_NO_AXIS = 200; // No stage connected.

    public static final int PI_CNTR_NO_AXIS_PARAM_FILE = 201; // PI_CNTR_NO_AXIS_PARAM_FILE.

    public static final int PI_CNTR_INVALID_AXIS_PARAM_FILE = 202; // PI_CNTR_INVALID_AXIS_PARAM_FILE.

    public static final int PI_CNTR_NO_AXIS_PARAM_BACKUP = 203; // PI_CNTR_NO_AXIS_PARAM_BACKUP.

    public static final int PI_CNTR_SENDING_BUFFER_OVERFLOW = 301; // PI_CNTR_SENDING_BUFFER_OVERFLOW.

    public static final int PI_CNTR_VOLTAGE_OUT_OF_LIMITS = 302; // Voltage out of limits.

    public static final int PI_CNTR_VOLTAGE_SET_WHEN_SERVO_OFF = 303; // Attempt to set voltage when servo on.

    public static final int PI_CNTR_RECEIVING_BUFFER_OVERFLOW = 304; // PI_CNTR_RECEIVING_BUFFER_OVERFLOW.

    public static final int PI_CNTR_EEPROM_ERROR = 305; // Error while reading/writing EEPROM.

    public static final int PI_CNTR_I2C_ERROR = 306; // Error on I2C bus.

    public static final int PI_CNTR_RECEIVING_TIMEOUT = 307; // Timeout while receiving command.

    public static final int PI_CNTR_TOO_MANY_NESTED_MACROS = 1000; //Too many nested macros.

    public static final int PI_CNTR_MACRO_ALREADY_DEFINED = 1001; // Macro already defined.

    public static final int PI_CNTR_NO_MACRO_RECORDING = 1002; // No macro recording.

    public static final int PI_CNTR_INVALID_MAC_PARAM = 1003; // Invalid parameter for \c MAC*/.

    public static final int PI_CNTR_RESERVED_1004 = 1004; // IPI internal error code 31.

    public static final int PI_CNTR_ALREDY_HAS_SERIAL_NUMBER = 2000; // Controller already has a serial number.

}
