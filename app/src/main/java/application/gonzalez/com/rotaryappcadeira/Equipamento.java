package application.gonzalez.com.rotaryappcadeira;

public class Equipamento {


    private int id;
    public String nome_equipamento, nome_paciente, nome_clube;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_equipamento() {
        return nome_equipamento;
    }

    public void setNome_equipamento(String nome_equipamento) {
        this.nome_equipamento = nome_equipamento;
    }

    public String getNome_paciente() {
        return nome_paciente;
    }

    public void setNome_paciente(String nome_paciente) {
        this.nome_paciente = nome_paciente;
    }

    public String getNome_clube() {
        return nome_clube;
    }

    public void setNome_clube(String nome_clube) {
        this.nome_clube = nome_clube;
    }

}
