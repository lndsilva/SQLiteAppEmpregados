package br.com.local.sqliteappempregados;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Funcionarios_Activity extends AppCompatActivity {

    List<Empregados> empregadosList;
    EmpregadoAdapter empregadoAdapter;
    SQLiteDatabase meuBancoDeDados;
    ListView listViewEmpregados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funcionarios_layout);

        listViewEmpregados = findViewById(R.id.listarFuncionariosView);

        empregadosList = new ArrayList<>();

        meuBancoDeDados = openOrCreateDatabase(MainActivity.NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        visualizarEmpregadosDatabase();
    }

    //Executar um select na tabela do banco de dados
    private void visualizarEmpregadosDatabase() {

        Cursor cursorEmpregados = meuBancoDeDados.rawQuery("SELECT * FROM funcionarios", null);

        if (cursorEmpregados.moveToFirst()) {
            do {
                empregadosList.add(new Empregados(
                        cursorEmpregados.getInt(0),
                        cursorEmpregados.getString(1),
                        cursorEmpregados.getString(2),
                        cursorEmpregados.getString(3),
                        cursorEmpregados.getDouble(4)
                ));
            } while (cursorEmpregados.moveToNext());
        }
        cursorEmpregados.close();

        //Verificar o layout
        empregadoAdapter = new EmpregadoAdapter(this, R.layout.lista_view_funcionarios, empregadosList, meuBancoDeDados);

        //carregando o adaptador na listView
        listViewEmpregados.setAdapter(empregadoAdapter);
    }
}