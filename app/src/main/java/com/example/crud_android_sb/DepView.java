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

public class DepView extends AppCompatActivity {
    private EditText clave;

    private EditText nombre;

    private JSONArray respArrayJSON;

    private JSONObject respJSON;

    private TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dep_view);
        this.clave = findViewById(R.id.claveDep);
        this.nombre = findViewById(R.id.nombreDep);
        this.tableLayout = findViewById(R.id.tableLayout2);
        limpiarTabla();
    }

    public void actualizar(View view) {
        ConsumDepartamentos ConsumDepartamentos = new ConsumDepartamentos();
        ConsumDepartamentos.execute("update", this.clave.getText().toString(), this.nombre.getText().toString());
        quitarContenido();
    }

    public void anadir(View view) {
        ConsumDepartamentos ConsumDepartamentos = new ConsumDepartamentos();
        ConsumDepartamentos.execute("new", this.nombre.getText().toString());
        quitarContenido();
    }

    public void cambioEmp(View view) {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    public void consultaId(View view) {
        limpiarTabla();
        ConsumDepartamentos ConsumDepartamentos = new ConsumDepartamentos();
        ConsumDepartamentos.execute("search", this.clave.getText().toString());
        quitarContenido();
    }

    public void consultarTodos(View view) {
        limpiarTabla();
        ConsumDepartamentos ConsumDepartamentos = new ConsumDepartamentos();
        ConsumDepartamentos.execute("todos");
        quitarContenido();
    }

    public void eliminar(View view) {
        ConsumDepartamentos ConsumDepartamentos = new ConsumDepartamentos();
        ConsumDepartamentos.execute("delete", this.clave.getText().toString());
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
        colum1.setWidth(700);
        colum1.setPadding(10, 10, 10, 10);
        colum1.setLayoutParams(layoutFila);
        tableRow.addView(colum1);

        TextView colum2 = new TextView(this);
        colum2.setText("Nombre");
        colum2.setGravity(Gravity.CENTER);
        colum2.setBackgroundColor(Color.BLACK);
        colum2.setTextColor(Color.WHITE);
        colum2.setPadding(10, 10, 10, 10);
        colum2.setWidth(700);
        colum2.setLayoutParams(layoutFila);
        tableRow.addView(colum2);

        this.tableLayout.addView(tableRow);
    }

    public void mostrarId(JSONObject paramJSONObject) {
        String[] arrayOfString1;
        try {
            String str2 = paramJSONObject.getString("id");
            String str3 = paramJSONObject.getString("nombre");
            arrayOfString1 = new String[]{str2, str3};
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
            arrayOfString1 = new String[0];
        }
        TableRow tableRow = new TableRow(getBaseContext());
        for (int i = 0; i < 2; i++) {
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
    }

    public class ConsumDepartamentos extends AsyncTask<String, Integer, Integer> {

        public Integer doInBackground(String... param1VarArgs) {
            System.out.println(param1VarArgs[0]);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            Integer integer1 = Integer.valueOf(10);
            ;
            if (param1VarArgs[0].equals("new")) {
                HttpPost httpPost = new HttpPost("http://34.71.4.51:5026/api/departamentos");
                httpPost.setHeader("content-type", "application/json");
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("nombre", param1VarArgs[1]);
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
                stringBuilder.append("http://34.71.4.51:5026/api/departamentos/");
                stringBuilder.append(param1VarArgs[1]);
                HttpPut httpPut = new HttpPut(stringBuilder.toString());
                httpPut.setHeader("content-type", "application/json");
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("id", param1VarArgs[1]);
                    jSONObject.put("nombre", param1VarArgs[2]);
                    httpPut.setEntity(new StringEntity(jSONObject.toString()));
                    httpclient.execute(httpPut);
                    integer1 = Integer.valueOf(2);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            if (param1VarArgs[0].equals("delete")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("http://34.71.4.51:5026/api/departamentos/");
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
                stringBuilder.append("http://34.71.4.51:5026/api/departamentos/");
                stringBuilder.append(param1VarArgs[1]);
                HttpGet httpGet = new HttpGet(stringBuilder.toString());
                httpGet.setHeader("content-type", "application/json");
                try {
                    String str = EntityUtils.toString(httpclient.execute(httpGet).getEntity());
                    integer1 = Integer.valueOf(4);
                    DepView.this.respJSON = new JSONObject(str);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            if (param1VarArgs[0].equals("todos")) {
                HttpGet httpGet = new HttpGet("http://34.71.4.51:5026/api/departamentos");
                httpGet.setHeader("content-type", "application/json");
                try {
                    String str = EntityUtils.toString(httpclient.execute(httpGet).getEntity());
                    DepView.this.respArrayJSON = new JSONArray(str);
                    integer1 = Integer.valueOf(5);
                } catch (Exception exception) {
                    integer1 = Integer.valueOf(10);
                }
            }
            return integer1;
        }

        public void onPostExecute(Integer param1Integer) {
            System.out.println(param1Integer);
            int i = param1Integer;
            if (i == 1) {
                Toast.makeText(DepView.this, "Insercion exitosa", Toast.LENGTH_SHORT).show();
            }
            if (i == 2) {
                Toast.makeText(DepView.this, "Actualizacion exitosa", Toast.LENGTH_SHORT).show();
            }
            if (i == 3) {
                Toast.makeText(DepView.this, "Eliminacion exitosa", Toast.LENGTH_SHORT).show();
            }
            if (i == 4) {
                Toast.makeText(DepView.this, "Consulta exitosa", Toast.LENGTH_SHORT).show();
                DepView.this.mostrarId(DepView.this.respJSON);
            }
            if (i == 5) {
                Toast.makeText(DepView.this, "Consulta exitosa", Toast.LENGTH_SHORT).show();
                DepView.this.mostrarTodos();
            }
            if (i == 10) {
                Toast.makeText(DepView.this, "Operacion fallida", Toast.LENGTH_SHORT).show();
            }
        }

    }
    
}