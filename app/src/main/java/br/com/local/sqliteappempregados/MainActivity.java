package br.com.local.sqliteappempregados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String NOME_BANCO_DE_DADOS = "dbTI97.db";

    TextView lblEmpregados;
    EditText txtNomeEmpregado, txtSalarioEmpregado;
    Spinner spnDepartamentos;

    Button btnAdicionaFuncionario;

    //Declarando a variavel que terá todos os comandos do SQLite
    SQLiteDatabase meuBancoDeDados;

    //Create Database, Table
    //Insert, Select, Update, Delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblEmpregados = findViewById(R.id.lblVisualizaFuncionario);
        txtNomeEmpregado = findViewById(R.id.txtNomeNovoFuncionario);
        txtSalarioEmpregado = findViewById(R.id.txtNovoSalarioFuncionario);
        spnDepartamentos = findViewById(R.id.spnDepartamentos);

        btnAdicionaFuncionario = findViewById(R.id.btnAdicionarfuncionario);

        //Irá pegar a ação de click nos dois componentes
        btnAdicionaFuncionario.setOnClickListener(this);
        lblEmpregados.setOnClickListener(this);

        //Criando banco de dados
        meuBancoDeDados = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        //Criar as tabelas para o banco de dados
        criarTabelaEmpregado();

    }

//Este método irá validar o nome e o salário
    //departamento não precisa de validação, pois é um spinner e não pode estar vazio

    private boolean verificarEntrada(String nome, String salario) {
        if (nome.isEmpty()) {
            txtNomeEmpregado.setError("Por favor entre com o nome");
            txtNomeEmpregado.requestFocus();
            return false;
        }

        if (salario.isEmpty() || Integer.parseInt(salario) <= 0) {
            txtSalarioEmpregado.setError("Por favor entre com o salário");
            txtSalarioEmpregado.requestFocus();
            return false;
        }
        return true;
    }

    //Neste método vamos fazer a operação para adicionar os funcionario
    private void adicionarEmpregado() {

        String nomeEmpr = txtNomeEmpregado.getText().toString().trim();
        String salarioEmpr = txtSalarioEmpregado.getText().toString().trim();
        String deptoEmpr = spnDepartamentos.getSelectedItem().toString();

        // obtendo o horário atual para data de inclusão
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dataEntrada = simpleDateFormat.format(calendar.getTime());

        //validando entrada
        if (verificarEntrada(nomeEmpr, salarioEmpr)) {


            String insertSQL = "INSERT INTO funcionarios (" +
                    "nome, " +
                    "departamento, " +
                    "dataEntrada," +
                    "salario)" +
                    "VALUES(?, ?, ?, ?);";

            // usando o mesmo método execsql para inserir valores
            // desta vez tem dois parâmetros
            // primeiro é a string sql e segundo são os parâmetros que devem ser vinculados à consulta

            meuBancoDeDados.execSQL(insertSQL, new String[]{nomeEmpr, deptoEmpr, dataEntrada, salarioEmpr});

            Toast.makeText(getApplicationContext(), "Funcionário adicionado com sucesso!!!", Toast.LENGTH_SHORT).show();

            limparCadastro();

        }

    }

    //Limpar os campos apos cadastro
    public void limparCadastro() {

        txtNomeEmpregado.setText("");
        txtSalarioEmpregado.setText("");
        txtNomeEmpregado.requestFocus();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdicionarfuncionario:
                adicionarEmpregado();
                break;
            case R.id.lblVisualizaFuncionario:
                startActivity(new Intent(getApplicationContext(), Funcionarios_Activity.class));
                break;
        }

    }
    // este método irá criar a tabela
    // como vamos chamar esse método toda vez que lançarmos o aplicativo
    // Eu adicionei IF NOT EXISTS ao SQL
    // então, só criará a tabela quando a tabela ainda não estiver criada


    private void criarTabelaEmpregado() {
        meuBancoDeDados.execSQL(
                "CREATE TABLE IF NOT EXISTS funcionarios (" +
                        "id integer PRIMARY KEY AUTOINCREMENT," +
                        "nome varchar(200) NOT NULL," +
                        "departamento varchar(200) NOT NULL," +
                        "dataEntrada datetime NOT NULL," +
                        "salario double NOT NULL );"
        );

    }

}