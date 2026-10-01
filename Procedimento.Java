public class Procedimento { 
    private String nome;
    private int duracaoEstimada;
    private double valor;
    private String complexidade;



public Procedimento(String nome, int duracaoEstimada, double valor, String complexidade) {
    this.nome = nome;
    this.duracaoEstimada = duracaoEstimada;
    this.valor = valor;
    this.complexidade = complexidade;
}

public String getNome() { return nome; }
public int getDuracaoEstimada() { return duracaoEstimada; }
public double getValor() { return valor; }
public String getComplexidade() { return complexidade; }

@Override
public String toString() {
    return nome + " | Duração: " + duracaoEstimada + " min | Valor: R$ "
            + String.format("%.2f", valor) + " | Complexidade: " + complexidade;
}
}