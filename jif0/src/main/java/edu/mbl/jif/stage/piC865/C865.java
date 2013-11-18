package edu.mbl.jif.stage.piC865;

// JNI Signature: Java_edu_mbl_jif_stage_piC865_C865
import java.io.File;
/////////////////////////////////////////////////////////////////////////////
// Program: C865-Control Java interface to DLL using JNI
// for PHYSIK INSTRUMENTE PI C865 controller DLL on Windows
///////////////////////////////////////////////////////////////////////////// 
public class C865 {
    // Load the C-code library (.DLL)
    public static boolean initialize() {
        try {
            System.out.println("java.library.path= " + System.getProperty("java.library.path"));
            String libLoadPath = //com.myjavatools.lib.Files.getcwd() + 
                    ".\\" + System.mapLibraryName(
                "c865");
            System.out.println("libLoadPath = " + libLoadPath);
            File f = new File(libLoadPath);
            if (f.exists()) {
                System.load(libLoadPath);  // explicit path
                System.out.println("C865.dll library loaded");
                return true;
            } else {
                System.err.println("Could not load C865.dll");
                return false;
            }
        // Load the JNI interface, C865.dll; 
        // Requires that C865_GCS_DLL, MC_C865, and stages.dat are also in the current directory.

        //System.loadLibrary("c865");  // assuming .dll is in java.library.path 
        //System.load("C:\\_DevEnv\\_projects\\Jif\\jif0\\c865.dll");  // explicit path

        } catch (Exception e) {
            System.out.println(
                "Could not load c865.dll\njava.library.path= " + System.getProperty(
                "java.library.path"));
            return false;
        }

    }
/////////////////////////////////////////////////////////////////////////////
    public static native int ConnectRS232(int nPortNr, int BaudRate);

    public static native boolean IsConnected(int iId);

    public static native void CloseConnection(int iId);

    public static native int GetError(int iId);

    public static native boolean SetErrorCheck(int iId, boolean bErrorCheck);

    public static native boolean TranslateError(int errNr, byte[] szBuffer, int maxlen);

    public static native int GetCurrentInterfaceId(int iBoardNumber);

    public static native boolean qERR(int iId, int pnError);

    public static native boolean qIDN(int iId, byte[] buffer, int maxlen); // <<<<<<<<<<<<

    public static native boolean qCST(int iId, byte[] szAxes, byte[] names, int maxlen);

    public static native boolean CST(int iId, byte[] szAxes, byte[] names);

    public static native boolean qSAI(int iId, byte[] axes, int maxlen);

    public static native boolean INI(int iId, byte[] szAxes);

    public static native boolean qVEL(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean VEL(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean MOV(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean REF(int iId, byte[] szAxes);

    public static native boolean RON(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean qRON(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean qREF(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean qLIM(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean IsReferenceOK(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean IsReferencing(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean GetRefResult(int iId, byte[] szAxes, int pnResult);

    public static native boolean CLR(int ID, byte[] szAxes);

    public static native boolean qMOV(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean MVR(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean IsMoving(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean qONT(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean DFF(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean qDFF(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean DFH(int iId, byte[] szAxes);

    public static native boolean qDFH(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean GOH(int iId, byte[] szAxes);

    public static native boolean qPOS(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean POS(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean HLT(int iId, byte[] szAxes);

    public static native boolean STP(int iId);

    public static native boolean qVST(int iId, byte[] buffer, int maxlen);

    public static native boolean qTVI(int iId, byte[] axes, int maxlen);

    public static native boolean SAI(int iId, String szOldAxes, String szNewAxes);

    public static native boolean SVO(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean qSVO(int iId, byte[] szAxes, boolean[] pbValarray);

    public static native boolean SMO(int iId, byte[] szAxes, int pnValarray);

    public static native boolean qSMO(int iId, byte[] szAxes, int pnValarray);

    public static native boolean SPA(int iId, byte[] szAxes, int iCmdarray, double[] dValarray,
                                     byte[] szStageNames);

    public static native boolean qSPA(int iId, byte[] szAxes, int iCmdarray, double[] dValarray,
                                      byte[] szStageNames, int iMaxNameSize);

    public static native boolean GetInputChannelNames(int iId, byte[] szBuffer, int maxlen);

    public static native boolean GetOutputChannelNames(int iId, byte[] szBuffer, int maxlen);

    public static native boolean DIOREPEAT(int iId, int n, int period);

    public static native boolean DIO(int iId, String szChannels, boolean[] pbValarray);

    public static native boolean DIOOFF(int iId);

    public static native boolean DIOON(int iId);

    public static native boolean qDIO(int iId, String szChannels, boolean[] pbValarray);

    public static native boolean qTIO(int iId, int pINr, int pONr);

    public static native boolean qTAV(int iId, int nChannel, double pdValue);

    public static native boolean STE(int iId, char cAxis, double dOffset, int iDelay);

    public static native boolean qSTE(int iId, char cAxis, int iOffset, int nrValues,
                                      double[] pdValarray);

    public static native boolean qHLP(int ID, byte[] buffer, int maxlen);

    public static native boolean C865Commandset(int iId, String szCommand, byte[] szAwnser,
                                                int iMaxSize);

    public static native boolean GcsCommandset(int iId, String szCommand);

    public static native boolean GcsGetAnswer(int ID, byte[] szAnswer, int bufsize);

    public static native boolean GcsGetAnswerSize(int ID, int iAnswerSize);

    public static native boolean SetQMC(int iId, byte bCmd, byte bAxis, int Param);

    public static native boolean GetQMC(int iId, byte bCmd, byte bAxis, int pResult);

    public static native boolean SetQMCA(int iId, byte bCmd, byte bAxis, short Param1, int lParam2);

    public static native boolean GetQMCA(int iId, byte bCmd, byte bAxis, short lParam, int pResult);

    public static native boolean MNL(int iId, byte[] szAxes);

    public static native boolean MPL(int iId, byte[] szAxes);

    public static native boolean REF(int iId, char[] szAxes);

    public static native boolean qTMN(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean qTMX(int iId, byte[] szAxes, double[] pdValarray);

    public static native boolean AddStage(int iId, byte[] szAxes);

    public static native boolean RemoveStage(int iId, byte[] szStageName);

    public static native boolean OpenUserStagesEditDialog(int iId);

    public static native boolean OpenPiStagesEditDialog(int iId);

}


/*
 *  * C Declarations from C865_GCS_DLL.h
 * 
/////////////////////////////////////////////////////////////////////////////
// DLL initialization and comm functions
int	C865_FUNC_DECL	C865_ConnectRS232(const int nPortNr, const int BaudRate);
BOOL	C865_FUNC_DECL	C865_IsConnected(const int iId);
void	C865_FUNC_DECL	C865_CloseConnection(const int iId);
int	C865_FUNC_DECL	C865_GetError(const int iId);
BOOL	C865_FUNC_DECL	C865_SetErrorCheck(const int iId, BOOL bErrorCheck);
BOOL	C865_FUNC_DECL	C865_TranslateError(int errNr, char* szBuffer, const int maxlen);
int	C865_FUNC_DECL	C865_GetCurrentInterfaceId(const int iBoardNumber);


/////////////////////////////////////////////////////////////////////////////
// general
BOOL C865_FUNC_DECL C865_qERR(const int iId, int* pnError);
BOOL C865_FUNC_DECL C865_qIDN(const int iId, char* buffer, const int maxlen);
BOOL C865_FUNC_DECL C865_INI(const int iId, char* const szAxes);
BOOL C865_FUNC_DECL C865_CLR(const int ID, char* const szAxes);

BOOL C865_FUNC_DECL C865_MOV(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_qMOV(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_MVR(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_IsMoving(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qONT(const int iId, char* const szAxes, BOOL* pbValarray);

BOOL C865_FUNC_DECL C865_DFF(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_qDFF(const int iId, char* const szAxes, double* pdValarray);

BOOL C865_FUNC_DECL C865_DFH(const int iId, char* const szAxes);
BOOL C865_FUNC_DECL C865_qDFH(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_GOH(const int iId, char* const szAxes);

BOOL C865_FUNC_DECL C865_qPOS(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_POS(const int iId, char* const szAxes, double* pdValarray);

BOOL C865_FUNC_DECL C865_HLT(const int iId, char* const szAxes);
BOOL C865_FUNC_DECL C865_STP(const int iId);

BOOL C865_FUNC_DECL C865_qCST(const int iId, char* const szAxes, char* names, const int maxlen);
BOOL C865_FUNC_DECL C865_CST(const int iId, char* const szAxes, char* names);
BOOL C865_FUNC_DECL C865_qVST(const int iId, char* buffer, const int maxlen);
BOOL C865_FUNC_DECL C865_qTVI(const int iId, char* axes, const int maxlen);
BOOL C865_FUNC_DECL C865_SAI(const int iId, char* const szOldAxes, char* const szNewAxes);
BOOL C865_FUNC_DECL C865_qSAI(const int iId, char* axes, const int maxlen);
BOOL C865_FUNC_DECL C865_qSAI_ALL(const int iId, char* axes, const int maxlen);

BOOL C865_FUNC_DECL C865_SVO(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qSVO(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_SMO(const int iId, char* const szAxes, int* pnValarray);
BOOL C865_FUNC_DECL C865_qSMO(const int iId, char* const szAxes, int* pnValarray);

BOOL C865_FUNC_DECL C865_VEL(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_qVEL(const int iId, char* const szAxes, double* pdValarray);

BOOL C865_FUNC_DECL C865_SPA(const int iId, char* const szAxes, int* iCmdarray, double* dValarray, char* szStageNames);
BOOL C865_FUNC_DECL C865_qSPA(const int iId, char* const szAxes, int* iCmdarray, double* dValarray, char* szStageNames, int iMaxNameSize);

BOOL C865_FUNC_DECL C865_GetInputChannelNames(const int iId, char* szBuffer, const int maxlen);
BOOL C865_FUNC_DECL C865_GetOutputChannelNames(const int iId, char* szBuffer, const int maxlen);
BOOL C865_FUNC_DECL C865_DIO(const int iId, char* const szChannels, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qDIO(const int iId, char* const szChannels, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qTIO(const int iId, int* pINr, int* pONr);
BOOL C865_FUNC_DECL C865_qTAV(const int iId, int nChannel, double* pdValue);

BOOL C865_FUNC_DECL C865_STE(const int iId, char const  cAxis, double dOffset, int iDelay);
BOOL C865_FUNC_DECL C865_qSTE(const int iId, const char cAxis, const int iOffset, const int nrValues, double* pdValarray);

BOOL C865_FUNC_DECL C865_qHLP(const int ID, char* buffer, const int maxlen);

/////////////////////////////////////////////////////////////////////////////
// String commands
BOOL C865_FUNC_DECL C865_C865Commandset(const int iId, char* const szCommand, char* szAwnser, int iMaxSize);
BOOL C865_FUNC_DECL C865_GcsCommandset(const int iId, char* const szCommand);
BOOL C865_FUNC_DECL C865_GcsGetAnswer(const int ID, char* szAnswer, const int bufsize);
BOOL C865_FUNC_DECL C865_GcsGetAnswerSize(const int ID, int* iAnswerSize);



/////////////////////////////////////////////////////////////////////////////
// QMC commands.
BOOL C865_FUNC_DECL C865_SetQMC(const int iId, BYTE bCmd, BYTE bAxis, int Param);
BOOL C865_FUNC_DECL C865_GetQMC(const int iId, BYTE bCmd, BYTE bAxis, int* pResult);
BOOL C865_FUNC_DECL C865_SetQMCA(const int iId, BYTE bCmd, BYTE bAxis, WORD Param1, int lParam2);
BOOL C865_FUNC_DECL C865_GetQMCA(const int iId, BYTE bCmd, BYTE bAxis, WORD lParam, int* pResult);



/////////////////////////////////////////////////////////////////////////////
// limits
BOOL C865_FUNC_DECL C865_MNL(const int iId,  char* const szAxes);
BOOL C865_FUNC_DECL C865_MPL(const int iId,  char* const szAxes);
BOOL C865_FUNC_DECL C865_REF(const int iId, char* const szAxes);
BOOL C865_FUNC_DECL C865_qREF(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qLIM(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_IsReferencing(const int iId, char* const szAxes, BOOL* pbIsReferencing);
BOOL C865_FUNC_DECL C865_GetRefResult(const int iId, char* const szAxes, int* pnResult);
BOOL C865_FUNC_DECL C865_IsReferenceOK(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qTMN(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_qTMX(const int iId, char* const szAxes, double* pdValarray);
BOOL C865_FUNC_DECL C865_RON(const int iId, char* const szAxes, BOOL* pbValarray);
BOOL C865_FUNC_DECL C865_qRON(const int iId, char* const szAxes, BOOL* pbValarray);


/////////////////////////////////////////////////////////////////////////////
// Spezial
BOOL	C865_FUNC_DECL	C865_AddStage(const int iId, char* const szAxes);
BOOL	C865_FUNC_DECL	C865_RemoveStage(const int iId, char* szStageName);
BOOL	C865_FUNC_DECL C865_OpenUserStagesEditDialog(const int iId);
BOOL	C865_FUNC_DECL C865_OpenPiStagesEditDialog(const int iId);
 */