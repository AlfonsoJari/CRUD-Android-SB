package com.example.crud_android_sb;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;


public class MainActivity extends AppCompatActivity {
    private EditText clave;

    private EditText direccion;

    private EditText nombre;

    private JSONArray respArrayJSON;

    private JSONObject respJSON;

    private TableLayout tableLayout;

    private EditText telefono;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.clave = findViewById(R.id.clave);
        this.nombre = findViewById(R.id.nombre);
        this.direccion = findViewById(R.id.direccion);
        this.telefono = findViewById(R.id.telefono);
        this.tableLayout = findViewById(R.id.tableLayout);
        limpiarTabla();
    }

    public void actualizar(View view) {
        ConsumEmpleados consumEmpleados = new ConsumEmpleados();
        consumEmpleados.execute("update", this.clave.getText().toString(), this.nombre.getText().toString(), this.direccion.getText().toString(), this.telefono.getText().toString());
        quitarContenido();
    }

    public void anadir(View view) {
        ConsumEmpleados consumEmpleados = new ConsumEmpleados();
        consumEmpleados.execute("new", this.nombre.getText().toString(), this.direccion.getText().toString(), this.telefono.getText().toString());
        quitarContenido();
    }

    public void cambioDep(View view) {
        startActivity(new Intent(getBaseContext(), DepView.class));
        finish();
    }

    public void consultaId(View view) {
        limpiarTabla();
        ConsumEmpleados consumEmpleados = new ConsumEmpleados();
        consumEmpleados.execute("search", this.clave.getText().toString());
        quitarContenido();
    }

    public void consultarTodos(View view) {
        limpiarTabla();
        ConsumEmpleados consumEmpleados = new ConsumEmpleados();
        System.out.println("Compro");
        consumEmpleados.execute("todos");
        quitarContenido();
    }

    public void eliminar(View view) {
        ConsumEmpleados consumEmpleados = new ConsumEmpleados();
        consumEmpleados.execute("delete", this.clave.getText().toString());
        quitarContenido();
    }

    TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

    public void limpiarTabla() {
        this.tableLayout.removeAllViews();
        TableRow tableRow = new TableRow(getBaseContext());
        tableRow.setLayoutParams(layoutFila);
        TextView colum1 = new TextView(this);
        colum1.setText("Clave");
        colum1.setGravity(Gravity.CENTER);
        colum1.setBackgroundColor(Color.BLACK);
        colum1.setTextColor(Color.WHITE);
        colum1.setWidth(350);
        colum1.setPadding(10, 10, 10, 10);
        colum1.setLayoutParams(layoutFila);
        tableRow.addView(colum1);

        TextView colum2 = new TextView(this);
        colum2.setText("Nombre");
        colum2.setGravity(Gravity.CENTER);
        colum2.setBackgroundColor(Color.BLACK);
        colum2.setTextColor(Color.WHITE);
        colum2.setPadding(10, 10, 10, 10);
        colum2.setWidth(350);
        colum2.setLayoutParams(layoutFila);
        tableRow.addView(colum2);

        TextView colum3 = new TextView(this);
        colum3.setText("Direccion");
        colum3.setGravity(Gravity.CENTER);
        colum3.setBackgroundColor(Color.BLACK);
        colum3.setTextColor(Color.WHITE);
        colum3.setPadding(10, 10, 10, 10);
        colum3.setWidth(350);
        colum3.setLayoutParams(layoutFila);
        tableRow.addView(colum3);

        TextView colum4 = new TextView(this);
        colum4.setText("Telefono");
        colum4.setGravity(Gravity.CENTER);
        colum4.setBackgroundColor(Color.BLACK);
        colum4.setTextColor(Color.WHITE);
        colum4.setPadding(10, 10, 10, 10);
        colum4.setWidth(350);
        colum4.setLayoutParams(layoutFila);
        tableRow.addView(colum4);

        this.tableLayout.addView(tableRow);
    }

    public void mostrarId(JSONObject paramJSONObject) {
        String[] arrayOfString1;
        try {
            String str2 = paramJSONObject.getString("id");
            String str3 = paramJSONObject.getString("nombre");
            String str4 = paramJSONObject.getString("direccion");
            String str1 = paramJSONObject.getString("telefono");
            arrayOfString1 = new String[]{str2, str3, str4, str1};
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
            arrayOfString1 = new String[0];
        }
        TableRow tableRow = new TableRow(getBaseContext());
        for (int i = 0; i < 4; i++) {
            TextView textView = new TextView(getBaseContext());
            textView.setText(arrayOfString1[i]);
            textView.setTextColor(-16777216);
            tableRow.addView(textView);
        }
        this.tableLayout.addView(tableRow);
    }

    public void mostrarTodos() {
        for (int i = 0; i < this.respArrayJSON.length(); i++) {
            try {
                mostrarId(this.respArrayJSON.getJSONObject(i));
            } catch (JSONException jSONException) {
                jSONException.printStackTrace();
            }
        }
    }

    public void quitarContenido() {
        this.clave.setText("");
        this.nombre.setText("");
        this.direccion.setText("");
        this.telefono.setText("");
    }

    public class ConsumEmpleados extends AsyncTask<String, Integer, Integer> {

        public Integer doInBackground(String... param1VarArgs) {
            System.out.println(param1VarArgs[0]);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            Integer integer1 = Integer.valueOf(10);
            ;
            if (param1VarArgs[0].equals("new")) {
                HttpPost httpPost = new HttpPost("http://34.71.4.51:5026/api/empleados");
                httpPost.setHeader("content-type", "application/json");
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("nombre", param1VarArgs[1]);
                    jSONObject.put("direccion", param1VarArgs[2]);
                    jSONObject.put("telefono", param1VarArgs[3]);
                    System.out.println(jSONObject.toString());
                    httpPost.setEntity(new StringEntity(jSONObject.toString()));
                    httpclient.execute(httpPost);
                    integer1 = Integer.valueOf(1);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            if (param1VarArgs[0].equals("update")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("http://34.71.4.51:5026/api/empleados/");
                stringBuilder.append(param1VarArgs[1]);
                HttpPut httpPut = new HttpPut(stringBuilder.toString());
                httpPut.setHeader("content-type", "application/json");
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("id", param1VarArgs[1]);
                    jSONObject.put("nombre", param1VarArgs[2]);
                    jSONObject.put("direccion", param1VarArgs[3]);
                    jSONObject.put("telefono", param1VarArgs[4]);
                    httpPut.setEntity(new StringEntity(jSONObject.toString()));
                    httpclient.execute(httpPut);
                    integer1 = Integer.valueOf(2);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            if (param1VarArgs[0].equals("delete")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("http://34.71.4.51:5026/api/empleados/");
                stringBuilder.append(param1VarArgs[1]);
                try {
                    HttpDelete httpDelete = new HttpDelete(stringBuilder.toString());
                    httpclient.execute(httpDelete);
                    integer1 = Integer.valueOf(3);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            if (param1VarArgs[0].equals("search")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("http://34.71.4.51:5026/api/empleados/");
                stringBuilder.append(param1VarArgs[1]);
                HttpGet httpGet = new HttpGet(stringBuilder.toString());
                httpGet.setHeader("content-type", "application/json");
                try {
                    String str = EntityUtils.toString(httpclient.execute(httpGet).getEntity());
                    integer1 = Integer.valueOf(4);
                    MainActivity.this.respJSON = new JSONObject(str);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            if (param1VarArgs[0].equals("todos")) {
                HttpGet httpGet = new HttpGet("http://34.71.4.51:5026/api/empleados");
                System.out.println("Se guardo URI");
                httpGet.setHeader("content-type", "application/json");
                try {
                    String str = EntityUtils.toString(httpclient.execute(httpGet).getEntity());
                    MainActivity.this.respArrayJSON = new JSONArray(str);
                    integer1 = Integer.valueOf(5);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            return integer1;
        }

        public void onPostExecute(Integer param1Integer) {
            System.out.println("se inicia postexec");
            System.out.println(param1Integer);
            int i = param1Integer;
            if (i == 1) {
                Toast.makeText(MainActivity.this, "Insercion exitosa", Toast.LENGTH_SHORT).show();
            }
            if (i == 2) {
                Toast.makeText(MainActivity.this, "Actualizacion exitosa", Toast.LENGTH_SHORT).show();
            }
            if (i == 3) {
                Toast.makeText(MainActivity.this, "Eliminacion exitosa", Toast.LENGTH_SHORT).show();
            }
            if (i == 4) {
                Toast.makeText(MainActivity.this, "Consulta exitosa", Toast.LENGTH_SHORT).show();
                MainActivity.this.mostrarId(MainActivity.this.respJSON);
            }
            if (i == 5) {
                Toast.makeText(MainActivity.this, "Consulta exitosa", Toast.LENGTH_SHORT).show();
                MainActivity.this.mostrarTodos();
            }
            if (i == 10) {
                Toast.makeText(MainActivity.this, "Operacion fallida", Toast.LENGTH_SHORT).show();
            }
        }

    }

}