package com.example.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private Button btnEnviar;
    private Socket cliente;
    private ServerSocket server;

    private View datos;
    EditText servidor, usuario, mensaje;
    LinearLayout chat;

    String strServidor, strUsuario;
    private PrintWriter output;
    private BufferedReader input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datos = getLayoutInflater().inflate(R.layout.datos, null);
        chat = findViewById(R.id.chatText);
        mensaje = findViewById(R.id.editTextTextMultiLine);
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);

//        alert.setTitle("Datos").setView(datos).setPositiveButton("Entrar", (dialogInterface, i) -> {
//            servidor = datos.findViewById(R.id.edtServidor);
//            usuario = datos.findViewById(R.id.edtUsuario);

            strServidor = "localhost";
            strUsuario = "pepe";
        System.out.println("hola oscar");
            new Thread(new Servidor()).start();

//        }).setCancelable(false).show();

        btnEnviar = findViewById(R.id.button);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cliente = new Socket(strServidor,1500);
                            output = new PrintWriter(cliente.getOutputStream());
                            output = new PrintWriter(cliente.getOutputStream());
                            String mensajeEnviar=mensaje.getText().toString();
                            output.write(strUsuario+": "+mensajeEnviar);
                            output.flush();
                            cliente.close();

                            runOnUiThread(() -> {
                                TextView txtmensaje = new TextView(MainActivity.this);
                                txtmensaje.setText(mensajeEnviar);
                                txtmensaje.setTextSize(20);
                                txtmensaje.setGravity(Gravity.RIGHT);
                                chat.addView(txtmensaje);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();

                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al crear el servidor", Toast.LENGTH_LONG).show());

                        }
                    }
                }).start();
            }
        });
    }

    class Servidor implements Runnable {

        @Override
        public void run() {
            try {
                server = new ServerSocket(1500);
                System.out.println("hola sadafggf");
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al crear el servidor", Toast.LENGTH_LONG).show());
            }
            while (true) {
                try {

                    Socket cliente = server.accept();
                    input = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    String mensaje = input.readLine();

                    if (mensaje != null && !mensaje.isEmpty()) {
                        runOnUiThread(() -> {
                            TextView txtmensaje = new TextView(MainActivity.this);
                            txtmensaje.setText(mensaje);
                            txtmensaje.setTextSize(20);
                            txtmensaje.setGravity(Gravity.LEFT);
                            chat.addView(txtmensaje);
                        });
                    }
                    cliente.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}