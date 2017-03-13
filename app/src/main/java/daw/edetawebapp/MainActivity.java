package daw.edetawebapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    //declaramos objetos
    private Button foto;
    private Button enviar;
    private TextView text;

    //uri sirve para encapsular un recurso en este caso la foto
    private Uri output;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //seleccionamos el layout (XML)
        setContentView(R.layout.activity_main);

        //relacionamos objetos con los objetos del layout
        foto=(Button)findViewById(R.id.button);
        enviar=(Button)findViewById(R.id.button2);
        text=(TextView)findViewById(R.id.textView);

        //colocamos el escuchador del boton
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creamos un intent nuevo para abrir la camara de fotos.
                Intent intent = new Intent(
                        "android.media.action.IMAGE_CAPTURE");
                //creamos el File donde colocaremos la foto
                File fileFoto = null;

                try {
                    // llamamos al metodo para crear un archivo temporal que almacenara la imagen
                    fileFoto = createTemporaryFile("foto", ".jpg", MainActivity.this);
                    fileFoto.delete();


                } catch (Exception e) {

                    text.setText("error:"+e.toString());
                }

                output = Uri.fromFile(fileFoto);


                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);

                startActivityForResult(intent, 1040);
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //comunicamos que estamos enviando.
                text.setText("Enviando...");
                try {
                    //enviamos la imagen pasando la ruta a nuestra asynctask (es necesario hacerlo en un hilo aparte porque si no la app quedaria
                    // congelada) Al acceder a recursos de internet Etc hay un tiempo de espera que se previene haciendo Un Asynctask.
                    String res= new EnviarImagenFtp().execute(output.getPath()).get();
                    text.setText(res);


                } catch (Exception e) {
                    text.setText("Error:"+ e.toString());
                }

            }
        });


    }


    public static File createTemporaryFile(String part, String ext,Context myContext) throws Exception {
        File tempDir = myContext.getExternalCacheDir();
        tempDir = new File(tempDir.getAbsolutePath() + "/temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //si hemos relaizado la imagen correctamente
        if (requestCode == 1040 && resultCode == RESULT_OK) {
            text.setText("Imagen Ok");

        }else{
            text.setText("Imagen NO OK");

        }
    }

}
