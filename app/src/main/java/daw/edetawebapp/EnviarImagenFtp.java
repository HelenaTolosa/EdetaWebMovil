package daw.edetawebapp;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTP;
import java.io.FileInputStream;

/**
 * Created by DAW on 13/03/2017.
 */

public class EnviarImagenFtp extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {

        //Para conectar mediante FTP importamos las librerias Commons.net de apache
        FTPClient cliente = new FTPClient();
        cliente.setControlEncoding("iso-8859-1");


        String sFTP = "ftp.fotosedetaweb.esy.es";
        String sUser = "u657439255.helena";
        String sPassword = "linares16";

        try{
            //conectamos
            cliente.connect(sFTP);

            //Iniciamos sesion
            boolean login = cliente.login(sUser, sPassword);
            FileInputStream fis=null;

            //comprobamos que hemos accedido correctamente
            if(login){
                //seleccionamos la carpeta
                cliente.changeWorkingDirectory("/galleries/ruinas");
                cliente.enterLocalPassiveMode();
                cliente.setFileType(FTP.BINARY_FILE_TYPE);
                cliente.setFileTransferMode(FTP.BINARY_FILE_TYPE);

                //recuperamos la foto desde la ruta que hemos pasado por parametros
                fis = new FileInputStream(params[0]);

                //Guardamos en nuestro Ftp pasando un Nombre y el archivo
                double n=Math.random()*10;


                if(cliente.storeFile(n+".jpg", fis)){
                    return "envio OK";
                }else{
                    return "Error al enviar";
                }
            }else{
                return "Error al loguear";
            }

        }catch (Exception e){
            return "Error"+e.toString();
        }


    }


}
