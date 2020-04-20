package cordova.plugin.egeesappel;

// Cordova imports
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.LOG;

// JSON imports
import org.json.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;

//Android imports
import android.Manifest;
import android.os.Build;
import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

// DIEHL imports
import com.diehl.metering.izar.license.api.LicenseService;
import com.diehl.metering.izar.module.readout.text.impl.IzarLibraryVersion;
import com.diehl.metering.izar.module.config.basic.text.impl.MeterConfig;
import com.diehl.metering.izar.module.readout.text.impl.RadioInterpret;
import com.diehl.metering.izar.module.readout.text.impl.Receiver;
import com.diehl.metering.izar.module.readout.text.impl.Receiver.IReceiverCallback;

import com.diehl.metering.izar.module.readout.text.impl.ReceiverStatic;
import com.diehl.metering.izar.module.config.basic.text.impl.MeterConfigStatic;

public class EgeeSappel extends CordovaPlugin {

    private static final String lic = "020000167B010401C0000000000004031F202020202020202020202020202020204547454520202020207777772E656765652E667200000000000000009ADEC26B9DCA187F2B26B2679753063115F2A8FA0383BF34EEED47E57349ECD0D1D3F6A0223BD00B73937256F6BBEC20FFEFB56C8FF06778491112DC67C7552D56E10603BFCEC010115BBD04278FB8C5CE71DF49862CC8D64D758891F20A322E48734D270F294C7DD476515115C35C522F4306F74057C2835CFFECAECF3E43FD0713D2B9E0C6529AAFBC899C910D94A8499D80652EC973C030E02F461D70FB4FA81A9EA59D2AF902CD890507DB6639FC5B2F7AA36EEA9277C8EDC479B40294EC3E13C5A24BBAF7E28C8E880344FB37F36B6DE43B181CE4E099DF89BD02FDAF091939CEEDCA8FD0130BCAE18C02F1BB6EDC22D9495D1D0BA1510536B8A48F9570";

    private CallbackContext PUBLIC_CALLBACKS = null;
    private static final String TAG = "EGEESAPPEL";

    private static final String ACTION_GET_VERSION = "getVersion";

    //Fonstions de programmations
    private static final String ACTION_OPEN_CONFIG_CONNECTION = "openConfigurationConnection";
    private static final String ACTION_INITIALIZE_CONFIGURATION = "initializeConfiguration";
    private static final String ACTION_CLOSE_CONFIG_CONNECTION = "closeConfigConnection";
    private static final String ACTION_SET_CONFIGURATION = "setDeviceConfiguration";
    private static final String ACTION_READ_DEVICE_CONFIGURATION = "readDeviceConfiguration";

    //Fonctions de lecture
    private static final String ACTION_OPENCONNECTION = "openConnection";
    private static final String ACTION_POLLFRAMES = "pollFrames";
    private static final String ACTION_CLOSECONNECTION = "closeConnection";

    //Fonctions de lecture static
    private static final String ACTION_OPENCONNECTION_STATIC = "openConnectionStatic";
    private static final String ACTION_POLLFRAME_STATIC = "pollFrameStatic";
    private static final String ACTION_CLOSECONNECTION_STATIC = "closeConnectionStatic";
   
   

    private Receiver IZARReceiver;
    private ReceiverStatic IZARReceiverStatic;
    private MeterConfigStatic IZARMeterConfigStatic;
    private final StringBuilder frames = new StringBuilder();
    private String connectionInfo = "Connection needs to be started";
   

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        Log.d(TAG, "Initialisation du plugin");
        
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {

        PUBLIC_CALLBACKS = callbackContext;

        if (ACTION_GET_VERSION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getVersion();
                }
            });
        } else  if (ACTION_OPENCONNECTION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    openConnection(args,callbackContext);
                }
            });
        } else if (ACTION_POLLFRAMES.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    pollFrames(callbackContext);
                }
            });
        } else if (ACTION_CLOSECONNECTION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    closeConnection(callbackContext);
                }
            });
        } else if (ACTION_OPENCONNECTION_STATIC.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    openConnectionStatic(args,callbackContext);
                }
            });
        } else if (ACTION_POLLFRAME_STATIC.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    pollFrameStatic(callbackContext);
                }
            });
        } else if (ACTION_CLOSECONNECTION_STATIC.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    closeConnectionStatic(callbackContext);
                }
            });
        } else if (ACTION_OPEN_CONFIG_CONNECTION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    openConfigurationConnection(args,callbackContext);
                }
            });
        } else if (ACTION_INITIALIZE_CONFIGURATION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    initializeConfiguration(args,callbackContext);
                }
            });
        } else if (ACTION_SET_CONFIGURATION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    setDeviceConfiguration(args,callbackContext);
                }
            });
        } else if (ACTION_READ_DEVICE_CONFIGURATION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    readDeviceConfiguration(callbackContext);
                }
            });
        } else if (ACTION_CLOSE_CONFIG_CONNECTION.equals(action)) {
            cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    closeConfigConnection(callbackContext);
                }
            });
        } else {
            return false;
        }

       

        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        return true;
    }

    //  transfert des messages vers javascript
    public void transmitToJs(JSONObject message) {
        try {
            if (PUBLIC_CALLBACKS == null) {
                return;
            }
            Log.d(TAG, "RETOUR: " + message);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, message);
            pluginResult.setKeepCallback(true);
            PUBLIC_CALLBACKS.sendPluginResult(pluginResult);
        } catch (Exception e) {
            Log.e(TAG, "transmitToJs: "+ e.toString());
        }
    }
   
    /**
     * Cette fonction retourne la version de IZAR@LIBRARY 
     *
     * @param callbackContext A Cordova callback context
     * @return un objet contenant la version exemple: {"message": "2.3.1"}.
     */
    private void getVersion() {
        try {
            String version = IzarLibraryVersion.INSTANCE.getVersion();
            JSONObject msg = new JSONObject();
            msg.put("message", version);
            transmitToJs(msg);

        } catch (JSONException jsonEx) {
            Log.e(TAG, "getVersion JSONException: "+ jsonEx.toString());
        } catch (Exception exc) {
            Log.e(TAG, "getVersion: "+ exc.toString());
        } 
    }

    /**
     * Cette fonction permet de tester la validité de la licence
     * @return vrai si valide ou faux si non.
     */
    private boolean isLicenseValid() {
        try {
            return LicenseService.getInstance().read(lic).validate("www.egee.fr");
        } catch (Exception e) {
            Log.d(TAG, "Erreur validation licence : " + e.toString());
            return false;
        }

    }
    
    /**
     * Cette fonction permet d'initialiser la connexion Bluetooth au OPTO en vue d'une configuration 
     * @param param  args tableau des paramétre contenant 'macAddress', 'passworr' et leurs valeurs respectives
     * @param callbackContext A Cordova callback context
     * @return un objet message qui contient le status de la connexion.
     */
    private void openConfigurationConnection(JSONArray args, CallbackContext callback) {
        if (args != null) {
            try {

                JSONObject params = args.getJSONObject(0);
                String param1 = params.getString("macAddressOpto");
                String param2 = params.getString("passwordOpto");

                if (param1 == null || param1 == "") {
                    JSONObject msgParam1 = new JSONObject();
                    msgParam1.put("message", "La valeur de l'adresse MAC du OPTO est null.");
                    transmitToJs(msgParam1);
                }

                if (param2 == null || param2 == "") {
                    param2 = "PASSWORD\t0000";
                }

                if (!isLicenseValid()) {
                    JSONObject msgLicence = new JSONObject();
                    msgLicence.put("message", "Licence de la librairie invalide");
                    transmitToJs(msgLicence);
                } else {
                    String paramString = param1 + "\t" + param2;
                    String connectionInfo = this.IZARMeterConfigStatic.create(paramString);
                    JSONObject msg = new JSONObject();
                    msg.put("message", connectionInfo);
                    transmitToJs(msg);
                }

            } catch (JSONException jsonEx) {
                Log.e(TAG, "openConfigurationConnection JSONException: " + jsonEx.toString());
                callback.error("Erreur openConfigurationConnection: " + jsonEx.toString());
            } catch (Exception exc) {
                Log.e(TAG, "openConfigurationConnection: " + exc.toString());
                callback.error("Erreur openConfigurationConnection: " + exc.toString());
            }
        } else {
            callback.error("La liste des paramétres est vide");
        }
    }
    
    /**
     * Cette fonction retourne les informations de l'entête du module
     * 
     * @param paramString     paramètre et valeur séparer par le caractère de tabulation
     * Liste paramétre possible (MASS_CONFIG, PASSWORD, TIMINGS, RESET_ALARMS, SYNC_TIME, AUTO_RELEASE, SERIAL_OPTOH_EAD)
     * Exemple: PASSWORD\t0000\nRESET_ALARMS\tTRUE
     * @param callbackContext A Cordova callback context
     * @return une chaine contenant le status, le nom du module et le N° de série
     */
    private void initializeConfiguration(JSONArray args, CallbackContext callback) {

        try {

            JSONObject params = args.getJSONObject(0);
            String paramString = params.getString("paramString");

            if (paramString == null || paramString == "") {
                JSONObject msgParam1 = new JSONObject();
                msgParam1.put("message", "La valeur du paramétre est null.");
                transmitToJs(msgParam1);
            }

            if (!isLicenseValid()) {
                JSONObject msgLicence = new JSONObject();
                msgLicence.put("message", "Licence de la librairie invalide");
                transmitToJs(msgLicence);
            } else {

                String resultInit = this.IZARMeterConfigStatic.initialize(paramString);

                if (resultInit != null && resultInit != "") {
                    JSONObject resultInitFormat = formatResponse(resultInit);
                    transmitToJs(resultInitFormat);
                }
            }

        } catch (JSONException jsonEx) {
            Log.e(TAG, "initializeConfiguration JSONException: " + jsonEx.toString());
            callback.error("Erreur initializeConfiguration: " + jsonEx.toString());
        } catch (Exception ex) {
            Log.e(TAG, "initializeConfiguration: " + ex.toString());
            callback.error("Erreur initializeConfiguration: " + ex.toString());
        }
    }
    
    /**
     * Cette fonction retourne les informations mémorisées dans le module
     * 
     * @param callbackContext A Cordova callback context
     * @return les informations de la configuration actuelle du module
     *         not.
     */
    private void readDeviceConfiguration(CallbackContext callback) {

        try {
            String readResult = this.IZARMeterConfigStatic.readConfiguration();
            JSONObject msg = new JSONObject();
            msg.put("configuration", readResult);
            transmitToJs(msg);
        } catch (JSONException jsonEx) {
            Log.e(TAG, "readDeviceConfiguration JSONException: " + jsonEx.toString());
            callback.error("Erreur readDeviceConfiguration: " + jsonEx.toString());
        } catch (Exception ex) {
            Log.e(TAG, "readDeviceConfiguration: " + ex.toString());
            callback.error("Erreur readDeviceConfiguration: " + ex.toString());
        }
    }

    /**
     * Cette fonction permet de fermer la connexion Bluetooth au OPTO 
     *
     * @param callbackContext A Cordova callback context
     * @return message de déconnexion.
     */
    private void closeConfigConnection(CallbackContext callback) {
        try {
            String resultDisconnect = this.IZARMeterConfigStatic.disconnect();
            JSONObject msgClose = new JSONObject();
            msgClose.put("message", resultDisconnect);
            transmitToJs(msgClose);

        } catch (JSONException jsonEx) {
            Log.e(TAG, "closeConnection JSONException: " + jsonEx.toString());
            callback.error("Erreur closeConnection: " + jsonEx.toString());
        } catch (Exception exc) {
            Log.e(TAG, "closeConnection: " + exc.toString());
            callback.error("Erreur closeConnection: " + exc.toString());
        }
    }

    /**
     * Cette fonction permet de modifier les informations du module
     * 
     * @param paramString Chaîne contenant les informations à modifier
     * séparer par le caractère de tabulation
     * @param callbackContext A Cordova callback context
     * @return une chaine contenant le résultat de la modification
     */
    private void setDeviceConfiguration(JSONArray args, CallbackContext callback) {

        try {

            JSONObject params = args.getJSONObject(0);
            String paramString = params.getString("paramString");
           

            if (paramString == null || paramString == "") {
                JSONObject msgParam1 = new JSONObject();
                msgParam1.put("message", "La valeur du paramétre est null.");
                transmitToJs(msgParam1);
            }

            if (!isLicenseValid()) {
                JSONObject msgLicence = new JSONObject();
                msgLicence.put("message", "Licence de la librairie invalide");
                transmitToJs(msgLicence);
            } else {

                String resultSetConfig = this.IZARMeterConfigStatic.setConfiguration(paramString);

                String resultWrite = this.IZARMeterConfigStatic.writeConfiguration();

                if (resultWrite == "") {
                    JSONObject msgWrite = new JSONObject();
                    msgWrite.put("message", "Configuration effectuée avec succés");
                    transmitToJs(msgWrite);
                } else {
                    JSONObject msgSet = new JSONObject();
                    msgSet.put("message", resultSetConfig);
                    transmitToJs(msgSet);
                }
                
            }

        } catch (JSONException jsonEx) {
            Log.e(TAG, "setDeviceConfiguration JSONException: " + jsonEx.toString());
            callback.error("Erreur setDeviceConfiguration: " + jsonEx.toString());
        } catch (Exception ex) {
            Log.e(TAG, "setDeviceConfiguration: " + ex.toString());
            callback.error("Erreur setDeviceConfiguration: " + ex.toString());
        }
    }


    /**
     * Cette fonction permet d'initialiser la connexion Bluetooth au PRT 
     * @param param  args tableau des paramétre contenant 'macAddress' et sa valeur
     * @param callbackContext A Cordova callback context
     * @return un objet message qui contient le status de la connexion.
     */
    private void openConnectionStatic(JSONArray args, CallbackContext callback) {
        if (args != null) {
            try {
               
                JSONObject params = args.getJSONObject(0);
                String param1 = params.getString("macAddress");
                if (param1 == null || param1 == "") {
                    JSONObject msgParam1 = new JSONObject();
                    msgParam1.put("message", "La valeur de l'adresse MAC du PRT est null.");
                    transmitToJs(msgParam1);
                }

                if (!isLicenseValid()) {
                    JSONObject msgLicence = new JSONObject();
                    msgLicence.put("message", "Licence de la librairie invalide");
                    transmitToJs(msgLicence);
                } else {
                  
                    String connectionInfo = this.IZARReceiverStatic.getStatus();
                    if ( !"Connection enabled".equals(connectionInfo)) {
                        this.IZARReceiverStatic.start(param1);
                        connectionInfo = this.IZARReceiverStatic.getStatus();
                        JSONObject msg = new JSONObject();
                        msg.put("message", connectionInfo);
                        transmitToJs(msg);
                    } else {
                        JSONObject msg = new JSONObject();
                        msg.put("message", connectionInfo);
                        transmitToJs(msg);
                    }
                }

            } catch (JSONException jsonEx) {
                Log.e(TAG, "openConnectionStatic JSONException: " + jsonEx.toString());
                callback.error("Erreur openConnection: " + jsonEx.toString());
            } catch (Exception exc) {
                Log.e(TAG, "openConnection: " + exc.toString());
                callback.error("Erreur openConnection: " + exc.toString());
            }
        } else {
            callback.error("La liste des paramétres est vide");
        }
    }
    
    /**
     * Cette fonction permet de récuperer la liste des télégrammes  
     * @param callbackContext A Cordova callback context
     * @return un tableau d'object télégrammes ou un object message.
     */
    private void pollFrameStatic(CallbackContext callback) {

        try {
        
            if (!isLicenseValid()) {
                JSONObject msgLicence = new JSONObject();
                msgLicence.put("message", "Licence de la librairie invalide");
                transmitToJs(msgLicence);
            } else {
               
                String connectionInfo = this.IZARReceiverStatic.getStatus();
                if ("Connection enabled".equals(connectionInfo)) {
                    String mframes = this.IZARReceiverStatic.pollFrames();
                    if (mframes != null && mframes != "") {
                        List<String> mesFrames = this.getFrames(mframes);
                        JSONArray arrayJSON = new JSONArray();
                        //Interprétation des frames reçues.
                        for (String fr : mesFrames) {

                            String frameInterpret = RadioInterpret.INSTANCE.interpret(fr);
                            JSONObject msgFormat = formatResponse(frameInterpret.replace("VOLUME[1]", "VOLUME_1").replace("TIMEPOINT[1]", "TIMEPOINT_1"));
                            arrayJSON.put(msgFormat);
                        }

                        JSONObject msgRetour = new JSONObject();
                        msgRetour.put("telegrams", arrayJSON);
                        transmitToJs(msgRetour);
                    }
                    
                } else {
                    JSONObject msg = new JSONObject();
                    msg.put("message", connectionInfo);
                    transmitToJs(msg);
                }
            }

        } catch (JSONException jsonEx) {
            Log.e(TAG, "pollFrameStatic JSONException: " + jsonEx.toString());
            callback.error("Erreur pollFrameStatic: " + jsonEx.toString());
        } catch (Exception exc) {
            Log.e(TAG, "pollFrameStatic: " + exc.toString());
            callback.error("Erreur pollFrameStatic: " + exc.toString());
        }
       
    }

     /**
     * Cette fonction permet de fermer la connexion Bluetooth au PRT 
     *
     * @param callbackContext A Cordova callback context
     * @return true si OK ou false si KO.
     */
    private void closeConnectionStatic(CallbackContext callback) {
        try {
            String connectionInfo = this.IZARReceiverStatic.getStatus();
            if ("Connection enabled".equals(connectionInfo)) {
                this.IZARReceiverStatic.stop();
                JSONObject msgClose = new JSONObject();
                msgClose.put("message", "Connexion au PRT fermée");
                transmitToJs(msgClose);
            } else {
                JSONObject msg = new JSONObject();
                msg.put("message", connectionInfo);
                transmitToJs(msg);
            }

       } catch (JSONException jsonEx) {
            Log.e(TAG, "closeConnectionStatic JSONException: "+ jsonEx.toString());
            callback.error("Erreur closeConnectionStatic: " + jsonEx.toString()); 
        } catch (Exception exc) {
            Log.e(TAG, "closeConnectionStatic: " + exc.toString());
            callback.error("Erreur closeConnectionStatic: " + exc.toString());
        } 
    }

     /**
     * Cette fonction permet de lire et interpreter les telegrammes
     * 
     * @param param           adresse mac PRT
     * @param callbackContext A Cordova callback context
     * @return true if frame interpretation success, fail will be called if not.
     */
     private void openConnection(JSONArray args, CallbackContext callback) {
         if (args != null) {

             try {

                 JSONObject params = args.getJSONObject(0);
                 String param1 = params.getString("macAddress");

                 if (param1 == null || param1 == "") {
                     JSONObject msgParam = new JSONObject();
                     msgParam.put("message", "L'adresse MAC du PRT ne peut pas être null.");
                     transmitToJs(msgParam);
                 }

                 if (!isLicenseValid()) {
                    JSONObject msgLicence = new JSONObject();
                    msgLicence.put("message", "Licence de la librairie invalide");
                    transmitToJs(msgLicence);
                } else {
                    if (IZARReceiver == null) {
                       
                        IZARReceiver = Receiver.start(param1, IZARReceiverCallback);

                        if (IZARReceiver != null) {
                            String myConnectionInfo = getStatus();
                            JSONObject msg = new JSONObject();
                            msg.put("message", myConnectionInfo);
                            transmitToJs(msg);
                        }
                    } else {
                        String myConnectionInfo = getStatus();
                        JSONObject msg = new JSONObject();
                        msg.put("message", myConnectionInfo);
                        transmitToJs(msg);
                    }
                 } 
                
             } catch (JSONException jsonEx) {
                callback.error("Erreur read JSONException: " + jsonEx.toString());
             } catch (Exception ex) {
                 callback.error("Erreur read:"+ex.getMessage());
             }
         } else {
             callback.error("La liste des paramétres est vide");
         }

     }
    
     private final  IReceiverCallback IZARReceiverCallback = new IReceiverCallback()
     {
        @Override
         public final  void onFrame(String deviceUid, String frame) {
            // try {
                 Log.d("RECEIVER", "device: " + deviceUid + " frame: " + frame);
                 
                 synchronized (frames) {
                    frames.append(deviceUid).append('\t').append(frame).append('\n');
                    return;
                  }

                 // interpretation d'une frame reçu
                //  if (deviceUid != "" && frame != "") {

                //      String resultInterpret = RadioInterpret.INSTANCE.interpret(frame);

                //      JSONObject msg = formatResponse(resultInterpret.replace("VOLUME[1]", "VOLUME_1").replace("TIMEPOINT[1]", "TIMEPOINT_1"));
                //      transmitToJs(msg);
                //  }
                
            //  } catch (JSONException exjson) {
            //      Log.e("RECEIVER", "JSON Exception caught: "+exjson);
            //  }
            
         }
       
       public final  void onKeepAlive() {}
       public final  void onError(Exception paramAnonymousException) {}
       public final  void onConnectionClosed() {}
   };

   private String pollFrame()
   {
       String str;
       synchronized (frames) {
           str = frames.toString();
           frames.setLength(0);
       }
       return str;
   }

   private String getStatus()
    {
        if (IZARReceiver == null) {
            if (connectionInfo == null) {
                connectionInfo = "Connection needs to be started";
            }
        }
        else if (frames.length() == 0) {
            connectionInfo = "The receiver is not enabled";
        } else {
            connectionInfo = "Connection enabled";
        }
        
        return connectionInfo;
    }
    
   /**
     * Cette fonction permet de récuperer la liste des télégrammes  
     * @param callbackContext A Cordova callback context
     * @return un tableau d'object télégrammes ou un object message.
     */
   private void pollFrames(CallbackContext callback) {

       try {

           if (!isLicenseValid()) {
               JSONObject msgLicence = new JSONObject();
               msgLicence.put("message", "Licence de la librairie invalide");
               transmitToJs(msgLicence);
           } else {

               String myConnectionInfo = getStatus();
               if ("Connection enabled".equals(myConnectionInfo)) {
                   String mframes = pollFrame();
                   if (mframes != null && mframes != "") {
                       List<String> mesFrames = this.getFrames(mframes);
                       JSONArray arrayJSON = new JSONArray();
                       //Interprétation des frames reçues.
                       for (String fr : mesFrames) {

                           String frameInterpret = RadioInterpret.INSTANCE.interpret(fr);
                           JSONObject msgFormat = formatResponse(frameInterpret.replace("VOLUME[1]", "VOLUME_1")
                                   .replace("TIMEPOINT[1]", "TIMEPOINT_1"));
                           arrayJSON.put(msgFormat);
                       }

                       JSONObject msgRetour = new JSONObject();
                       msgRetour.put("telegrams", arrayJSON);
                       transmitToJs(msgRetour);
                   }

               } else {
                   JSONObject msg = new JSONObject();
                   msg.put("message", myConnectionInfo);
                   transmitToJs(msg);
               }
           }

       } catch (JSONException jsonEx) {
           Log.e(TAG, "pollFrames JSONException: " + jsonEx.toString());
           callback.error("Erreur pollFrames: " + jsonEx.toString());
       } catch (Exception exc) {
           Log.e(TAG, "pollFrames: " + exc.toString());
           callback.error("Erreur pollFrames: " + exc.toString());
       }

   }
    
    /**
     * Cette fonction permet de fermer la connexion Bluetooth au PRT 
     *
     * @param callbackContext A Cordova callback context
     * @return true si OK ou false si KO.
     */
    private void closeConnection(CallbackContext callback) {
        try {
           
            if (IZARReceiver != null) {
                IZARReceiver.stop();
                IZARReceiver = null;
                connectionInfo = null;
                JSONObject msgClose = new JSONObject();
                msgClose.put("message", "Connexion au PRT fermée");
                transmitToJs(msgClose);
            } else {
                JSONObject msg = new JSONObject();
                msg.put("message", "Aucune connexion active");
                transmitToJs(msg);
            }

       } catch (JSONException jsonEx) {
            Log.e(TAG, "closeConnection JSONException: "+ jsonEx.toString());
            callback.error("Erreur closeConnection: " + jsonEx.toString()); 
        } catch (Exception exc) {
            Log.e(TAG, "closeConnection: " + exc.toString());
            callback.error("Erreur closeConnection: " + exc.toString());
        } 
    }

   /**
     * Cette fonction permet d'extraire le frame à interpreter de la chaîne deviceUid +'\t'+frame+'\n'
     * 
     * @param chaineFrame     concaténation du deviceUid et du frame
     * @return liste de frame à interpreter.
     */
   private List<String> getFrames(String chaineFrame) throws JSONException {
        List<String> frame = new ArrayList<String>();
     
           if (chaineFrame != null && chaineFrame != "") {

                List<String> myframes = new ArrayList<String>();

                String[] lines = chaineFrame.split("\\s*\\r?\\n\\s*");
               
               for (String line : lines) {
                   myframes.add(line.replaceAll("\t", " "));
               }
               
               for (int i = 0; i < myframes.size(); i++) {
                   String[] lignes = myframes.get(i).split(" ", 2);
                   frame.add(lignes[1]);
               }

           }
           return frame;
   }
    
    /**
     * Cette fonction permet de transformer la response en object
     * 
     * @param result     chaine de frame
     * @return un JSONObject frame.
     */
   private final  JSONObject formatResponse(String result) throws JSONException {
       JSONObject msg = new JSONObject();
      
           if (result != null && result != "") {

               ArrayList<String> myframes = new ArrayList<String>();

               String[] lines = result.split("\\s*\\r?\\n\\s*");
               for (String line : lines) {
                   myframes.add(line.replaceAll("\t", " "));
               }
              
               for (String fr : myframes) {
                   String[] lignes = fr.split(" ", 2);
                   msg.put(lignes[0], lignes[1]);
               }

           }
           return msg;
  
   }
   
}
